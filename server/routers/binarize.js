import express from "express";
import { binarizeThumbnail } from "../controllers/controller.js";

const router = express.Router();

router.get("/binarize-thumbnail", binarizeThumbnail);

export default router;
