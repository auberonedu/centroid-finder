import path from 'path';
import fs from 'fs';

const startVideoProcessingJob = (req, res) => {
    // /process/:filename
    const { fileName } = req.params;
    // ?targetColor=<hex>&threshold=<int>
    const { targetColor, threshold } = req.query;

    if (!targetColor || !threshold) {
        return res.status(400).json({ error: "Missing targetColor or threshold query parameter"});
    }


};

const getProcessingJobStatus = (req, res) => {

};

export default { startVideoProcessingJob, getProcessingJobStatus };