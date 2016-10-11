var appInsights = require("applicationinsights");

module.exports.setup = function (app) {

    if (process.env.APPINSIGHTS_INSTRUMENTATIONKEY) {

        console.log('Setting up Application Insights');

        // Configure server side App Insights SDK
        appInsights.setup().start();

    }

};