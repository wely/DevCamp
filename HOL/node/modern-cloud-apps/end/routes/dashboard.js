var express = require('express');
var router = express.Router();
var request = require('request');

// Setup Redis
var redis = require("redis");
var client = redis.createClient(6380, process.env.REDIS_SERVER, { auth_pass: process.env.REDIS_KEY, tls: { servername: process.env.REDIS_SERVER } });


/* GET dashboard. */
router.get('/', function (req, res) {

    // Check cache for incidentData key
    client.get('incidentData', function (error, reply) {

        if (reply) {
            // Cached key exists

            console.log('Found key, sooo tappin dat cache');

            // Render view
            res.render('dashboard', {
                title: 'Outage Dashboard',
                incidents: JSON.parse(reply)
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
                    incidents: data
                });

            });

        }

    });

});

module.exports = router;

function getData() {

    return new Promise(function (resolve, reject) {

        // Define URL to use for the API
        var apiUrl = 'http://incidentapi4w5agyt32vajs.azurewebsites.net/incidents';

        // Make a GET request with the Request libary
        request(apiUrl, { json: true }, function (error, results, body) {

            resolve(body);

        });

    });

}