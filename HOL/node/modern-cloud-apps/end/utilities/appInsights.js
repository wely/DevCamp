var appInsights = require("applicationinsights");

module.exports.setup = function (app) {

    if (process.env.APPINSIGHTS_INSTRUMENTATIONKEY) {

        console.log('Setting up Application Insights');

        // Configure server side App Insights SDK
        appInsights.setup().start();

        // Create route to return the AI Instrumentation Key
        // Used by the client side libary to instantiate AI
        app.get('/api/ai', function (req, res) {
            res.json({ key: process.env.APPINSIGHTS_INSTRUMENTATIONKEY });
        });

    }

};