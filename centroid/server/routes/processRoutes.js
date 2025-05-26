const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const express = require('express');
const router = express.Router();

// import the process controller
const processController = require('../controllers/processController');

// routes for starting a job and checking its status
router.post('/:filename', processController.startJob);
router.get('/:jobId/status', processController.getJobStatus);

module.exports = router;