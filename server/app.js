import dotenv from 'dotenv';
dotenv.config();

import express from 'express';
import router from './routes/serverApi.js'; // assuming this uses env vars

const app = express();
const PORT = process.env.PORT || 3000;

app.use('/', router);

app.listen(PORT, () => console.log(`Listening on http://localhost:${PORT}`));
