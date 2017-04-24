var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('index', { title: 'Express' });
});

module.exports = router;

router.get('/sign_up', function(req, res) {
    res.render('sign_up', { title: 'Sign Up' });
});