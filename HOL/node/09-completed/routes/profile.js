var express = require('express');
var router = express.Router();
var request = require('request');

var authUtility = require('../utilities/auth');

/* GET home page. */
router.get('/', authUtility.ensureAuthenticated, function (req, res) {

    // Query Graph API
    var options = {
        json: true,
        headers: {
            authorization: 'Bearer ' + req.user.token
        }
    };

    request('https://graph.microsoft.com/v1.0/me', options, function (error, results, body) {

        res.render('profile', {
            title: 'Profile',
            user: req.user,
            attributes: body
        });

    });

});

module.exports = router;