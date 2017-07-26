var express = require('express');
var router = express.Router();
var request = require('request');

// Setup Redis
var redis = require("redis");
var client = redis.createClient(process.env.REDISCACHE_SSLPORT, process.env.REDISCACHE_HOSTNAME, { auth_pass: process.env.REDISCACHE_PRIMARY_KEY, tls: { servername: process.env.REDISCACHE_HOSTNAME } });

/* GET dashboard. */
router.get('/', function (req, res) {

    // Check cache for incidentData key
    client.get('incidentData', function (error, reply) {

        if (reply) {
            // Cached key exists

            // Parse results
            var incidents;
            if (reply === 'undefined') {
                // No results, return null
                incidents = null;
            }
            else {
                incidents = JSON.parse(reply);
            }

            // Render view
            res.render('dashboard', {
                title: 'Outage Dashboard',
                incidents: incidents,
                user: req.user
            });

        }
        else {
            // Cached key does not exist

            // Query the API for incident data
            getData().then(function (data) {

                // Store results in cache
                client.set("incidentData", JSON.stringify(data), 'EX', 60, function (error, reply) {
                    console.log('Stored results in cache');
                });

                // Render view
                res.render('dashboard', {
                    title: 'Outage Dashboard',
                    incidents: data,
                    user: req.user
                });

            });

        }

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