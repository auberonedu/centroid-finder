import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const JOBS_DIR = path.resolve(__dirname, '..', '..', 'jobs');

// Ensure the jobs directory exists
fs.mkdirSync(JOBS_DIR, { recursive: true });

export function writeJobFile(jobId, data) {
  const jobFilePath = path.join(JOBS_DIR, `${jobId}.json`);
  fs.writeFileSync(jobFilePath, JSON.stringify(data, null, 2));
  return jobFilePath;
}

export function readJobFile(jobId) {
  const jobFilePath = path.join(JOBS_DIR, `${jobId}.json`);
  if (!fs.existsSync(jobFilePath)) return null;
  return JSON.parse(fs.readFileSync(jobFilePath, 'utf-8'));
}

export function updateJobStatus(jobId, statusData) {
  const currentData = readJobFile(jobId);
  if (!currentData) return false;

  const updatedData = {
    ...currentData,
    ...statusData
  };

  writeJobFile(jobId, updatedData);
  return true;
}

export function jobFilePath(jobId) {
  return path.join(JOBS_DIR, `${jobId}.json`);
}
