const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const express = require('express');
const router = express.Router();
const processController = require('../controllers/processController');

router.post('/:filename', processController.startJob);
router.get('/:jobId/status', processController.getJobStatus);

module.exports = router;