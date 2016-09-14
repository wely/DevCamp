var express = require('express');
var router = express.Router();

/* GET dashboard. */
router.get('/', function(req, res) {
    res.render('dashboard', { title: 'Outage Dashboard' });
});

module.exports = router;