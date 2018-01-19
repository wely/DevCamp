var appInsights = require("applicationinsights");
var client = new appInsights.TelemetryClient();

module.exports.setup = function (app) {

    console.log('App Insights Key Found. Starting AI');

    // Check if an instrumentation key is configured
    if (process.env.APPINSIGHTS_INSTRUMENTATIONKEY) {

        // Setup the Application Insights client
        // .setup() can be called without an instrumentation key
        // when an environment variable is set
        appInsights.setup().start();

    }

}

module.exports.customEvent = function (userid, tenantid) {

    // Use SDK Client to pass custom event    
    client.trackEvent({ name: "profileview", properties: { userid: userid, tenantid: tenantid }});

}