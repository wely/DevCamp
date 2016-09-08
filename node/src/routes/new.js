var express = require('express');
var router = express.Router();

/* GET new outage */
router.get('/', function(req, res) {
    res.render('new', { title: 'Report an Outage' });
});

module.exports = router;