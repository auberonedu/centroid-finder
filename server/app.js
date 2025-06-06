import express from 'express';
import router from './routes/serverApi.js'; 
import path from 'path';
import cors from 'cors';
import dotenv from 'dotenv';
import { fileURLToPath } from 'url';

dotenv.config();

// Ensure consistent path resolution
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const ROOT_DIR = path.resolve(__dirname, '..');

const app = express();
const PORT = 3000;

app.use(cors());

app.use('/videos', express.static(path.resolve(ROOT_DIR, process.env.VIDEO_DIR)));
app.use('/results', express.static(path.resolve(ROOT_DIR, process.env.RESULTS_DIR)));

app.use('/', router);

app.listen(PORT, () => console.log(`Listening on http://localhost:${PORT}`));
