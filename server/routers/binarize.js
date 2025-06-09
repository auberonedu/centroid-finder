import express from "express";
import { binarize } from "../controllers/controller.js";

const router = express.Router();

router.get("/binarize", binarize);  // only "/binarize", not full path

export default router;