import express from 'express';
import dotenv from 'dotenv';
import router from './routers/router.js';
import cors from 'cors';

// read in env congif environment variables
dotenv.config({
    path: "./config.env"
})

// create server
const app = express();
const port = 3000;

// Allow requests from any origin
app.use(cors());

// mount routers
app.use("/", router)

// listen
app.listen(port, () => console.log(`Server started on http://localhost:${port}`));

