var express = require('express');
var router = express.Router();
var request = require('request');

// Setup Redis Client
var redis = require("redis");
var client = redis.createClient(process.env.REDISCACHE_SSLPORT, process.env.REDISCACHE_HOSTNAME, { auth_pass: process.env.REDISCACHE_PRIMARY_KEY, tls: { servername: process.env.REDISCACHE_HOSTNAME } });

/* GET dashboard. */
router.get('/', function (req, res) {

    getIncidents().then(function (incidents) {

        // Render view
        res.render('dashboard', {
            title: 'Outage Dashboard',
            incidents: incidents
        });

    });

});

module.exports = router;

function getIncidents() {

    return new Promise(function (resolve, reject) {

        // Check cache for incidentData key
        client.get('incidentData', function (error, reply) {

            if (reply) {
                // Cached key exists
                console.log('Cached key found');

                // Parse results
                var incidents;
                if (reply === 'undefined') {
                    // No results, return null
                    incidents = null;
                }
                else {
                    incidents = JSON.parse(reply);
                }

                // Resolve Promise with incident data
                resolve(incidents);

            }
            else {
                // Cached key does not exist
                console.log('Cached key not found');

                // Define URL to use for the API
                var apiUrl = `${process.env.INCIDENT_API_URL}/incidents`;

                // Make a GET request with the Request libary
                request(apiUrl, { json: true }, function (error, results, body) {

                    // Store results in cache
                    client.set("incidentData", JSON.stringify(body), 'EX', 60, function (error, reply) {
                        console.log('Stored results in cache');
                    });

                    // Resolve Promise with incident data
                    resolve(body);

                });

            }

        });

    });

}