var appInsights = require("applicationinsights");

module.exports.setup = function () {

    if (process.env.APPINSIGHTS_INSTRUMENTATIONKEY) {
        appInsights.setup().start();
    }
    
};