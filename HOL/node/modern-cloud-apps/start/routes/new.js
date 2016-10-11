var fs = require('fs');
var express = require('express');
var router = express.Router();

/* GET new outage */
router.get('/', function (req, res) {
    res.render('new', {
        title: 'Report an Outage'
    });
});

// POST new outage
router.post('/', function (req, res) {
    res.render('new', {
        title: 'Report an Outage'
    });
});

module.exports = router;