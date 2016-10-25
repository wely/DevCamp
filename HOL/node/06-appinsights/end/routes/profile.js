var express = require('express');
var router = express.Router();
var request = require('request');
var authUtility = require('../utilities/auth');
var appInsightsUtility = require('../utilities/appInsights');

/* GET profile page. */
router.get('/', authUtility.ensureAuthenticated, function (req, res) {

    // Record User Details with Custom Event
    // Generates the tenant ID and a user ID to send to AppInsights
    appInsightsUtility.customEvent(req.user._json.preferred_username, req.user._json.tid);

    // Create options object configuring the HTTP call
    var options = {
        url: 'https://graph.microsoft.com/v1.0/me',
        method: 'GET',
        json: true,
        headers: {
            authorization: 'Bearer ' + req.user.token
        }
    };

    // Query Graph API
    request(options, function (error, results, body) {

        // Render page with returned attributes
        res.render('profile', {
            title: 'Profile',
            user: req.user,
            attributes: body
        });

    });

});

module.exports = router;