var express = require('express');
var router = express.Router();
var validator = require('../public/javascripts/validator');
var debug = require('debug')('signin:index');
var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');
var url = 'mongodb://localhost:27017/myproject';

var extraErr = '';
var users = {};

MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Connected correctly to server");
  users = db.collection('users');
  var userManager = require('../models/userManager')(db, users);
  //  成功连接数据库
  //userManager.removeAllUser();
  userManager.listAllUser();

  router.get('/', function(req, res, next) {
    debug(req.query);
    if (!isEmpty(req.query)) {
      if (typeof req.session.user == 'undefined') {
        extraErr = '尚未注册';
        res.redirect('/signin');
      } else {
        if (req.query['username'] == req.session.user['username']) {
          res.redirect('/detail');
          //  成功跳转
        } else {
          extraErr = '不能访问别人信息';
          res.redirect('/detail');
        }
      }
    } else {
      if (typeof req.session.user != 'undefined') {
        res.redirect('/detail');
      } else {
        res.redirect('/signin');
      }
    }
  });

  router.get('/signin', function(req, res, next) {
    res.render('signin', { title: '用户登录', extraError: extraErr});
    extraErr = '';
  });
  router.post('/signin', function(req, res, next) {
    userManager.findUser(req.body.username, req.body.password)
      .then(function (user) {
        req.session.user = user;
        res.redirect('/detail');
      })
      .catch(function () {
        res.render('signin', { title: '用户登录', error: '错误的用户名或者密码'});
      });
  });

  router.get('/regist', function(req, res, next) {
    res.render('index', { title: '用户注册'});
  });
  router.post('/regist', function(req, res, next) {
    var user = req.body;
    userManager.checkUser(user)
      .then(userManager.createUser)
      .then(function () {
        req.session.user = user;
        res.redirect('/detail');
      })
      .catch(function (error) {
        console.log(error);
        res.render('index', { title: '用户注册', user: user, error: error });
      });
  });

  router.get('/signout', function (req, res, next) {
    delete req.session.user;
    console.log(req.session.user);
    res.redirect('/signin');
  });

  router.all('*', function (req, res, next) {
    if (req.session.user) {
      next();
    } else {
      extraErr = '尚未登录';
      res.redirect('/signin');
    }
  });

  router.get('/detail', function(req, res, next) {
    res.render('detail', { title: '用户详情', user: req.session.user, extraError: extraErr});
    extraErr = '';
  });

});

module.exports = router;

function checkUser(user) {
  var errorMessages = [];
  for(var key in user) {
    if (!validator.isFieldValid(key, user[key])) errorMessages.push(validator.form[key].errorMessage);
    if (!validator.isAttrValueUnique(users, user, key)) errorMessages.push(
      "key: " + key + " is not unique by value: " + user[key]
    );
  }
  if (errorMessages.length > 0) throw new Error(errorMessages.join('<br />'));
}

function isEmpty(obj) {
  var flag = true;
  for (var key in obj) {
    flag = false;
  }
  return flag;
}