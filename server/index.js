import express from "express";
import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";
import route from "./routes/route.js";

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT;

app.use(express.json());

app.use("/", route);
app.use("/results", express.static(path.resolve(process.env.OUTPUT_DIR)));

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});