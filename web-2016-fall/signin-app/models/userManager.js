var bcrypt = require('bcrypt-as-promised');
var validator = require('../public/javascripts/validator');
var debug = require('debug')('signin:userManager');
var _ = require('lodash');

module.exports = function(db, users) {
  //var users = db.collection('users');
  return {
    findUser: function(username, password) {
      return users.findOne({ username: username }).then(function (user) {
        return user ? bcrypt.compare(password, user.password).then(function () {
          return user;
        }) : Promise.reject('user does not exist');
      });
    },
    createUser: function(user) {
      return bcrypt.hash(user.password, 10).then(function(hash) {
        user.password = hash;
        user.repeatPassword = hash;
        return users.insertOne(user);
      });
    },
    checkUser: function(user) {
      var mes = '';
      var formatError = validator.findFormatErrors(user);
      return new Promise(function (resolve, reject) {
        formatError ? reject(formatError) : resolve(user);
      }).then(function() {
        return users.findOne(getQuery(user)).then(function(existedUser) {
          if (existedUser != null) {
            for (var _key in user) {
              if (_key != 'password' && _key != 'repeatPassword') {
                if (user[_key] == existedUser[_key]) {
                  mes += _key;
                  mes += ' ';
                }
              }
            }
          }
          return existedUser ? Promise.reject(mes + 'is not unique') : Promise.resolve(user);
        })
      })
    },
    //confirmUser: function(username, password) {
    //  debug('enter confireuser');
    //  users.find({ username: username }).toArray(function (err, docs) {
    //    if (docs.length == 0) {
    //      return Promise.reject('错误的用户名或者密码');
    //    } else {
    //      return bcrypt.compare(password, docs[password])
    //        .then(function () {
    //          return Promise.resolve(docs);
    //        })
    //        .catch(function () {
    //          return Promise.reject('错误的用户名或者密码');
    //        });
    //    }
    //  });
    //},
    listAllUser: function () {
      users.find({}).toArray(function(err, docs) {
        console.log("Found the following records");
        console.dir(docs);
      });
    },
    removeAllUser: function () {
      users.drop();
    }
  }
};

function getQuery(user) {
  return {
    $or: _(user).omit('password').omit('repeatPassword').pairs().map(pairToObject).value()
  }
}
function pairToObject(pair) {
  var obj = {};
  obj[pair[0]] = pair[1];
  return obj;
}