import express from 'express';
import router from "./routes/routers.js"
import cors from 'cors';

const app = express();

app.use(cors());

app.use(express.json()); // read in JSON payloads in request body

app.use("/", router);

app.listen(3000, console.log("Listening on http://localhost:3000"));