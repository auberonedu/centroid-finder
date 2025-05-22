import express from 'express';
import router from './router/router.js';
import dotenv from 'dotenv';

dotenv.config({ path: '../.env' });

const app = express();
const PORT = 3000;

app.use('/', router);

app.listen(PORT, () => console.log(`Listening on http://localhost:${PORT}`));