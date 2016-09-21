var express = require('express');
var router = express.Router();
var request = require('request');

/* GET dashboard. */
router.get('/', function (req, res) {

    // Define URL to use for the API
    var apiUrl = 'http://incidentapi4w5agyt32vajs.azurewebsites.net/incidents';

    // Make a GET request with the Request libary
    request(apiUrl, { json: true }, function (error, results, body) {

        // Render view
        res.render('dashboard', {
            title: 'Outage Dashboard',
            incidents: body
        });

    });

});

module.exports = router;