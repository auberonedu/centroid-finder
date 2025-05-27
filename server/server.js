import dotenv from 'dotenv';
import express from 'express';
import router from './router/routes.js';

dotenv.config({ path: '../.env' });

const app = express();
const PORT = 3000;

app.use(express.static('./public'));

app.use("/", router);

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});