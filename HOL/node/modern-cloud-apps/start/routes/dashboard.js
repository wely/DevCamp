var express = require('express');
var router = express.Router();
var request = require('request');

/* GET dashboard. */
router.get('/', function (req, res) {

    // Query the API for incident data
    getData().then(function (incidents) {

        // Render view
        res.render('dashboard', {
            title: 'Outage Dashboard',
            incidents: incidents
        });

    });

});

module.exports = router;

function getData() {

    return new Promise(function (resolve, reject) {

        // Define URL to use for the API
        var apiUrl = `https://${process.env.INCIDENT_API_URL}/incidents`;

        // Make a GET request with the Request libary
        request(apiUrl, { json: true }, function (error, results, body) {

            resolve(body);

        });

    });

}