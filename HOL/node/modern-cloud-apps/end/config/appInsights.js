var appInsights = require("applicationinsights");

module.exports.setup = function () {
    appInsights.setup().start();
};