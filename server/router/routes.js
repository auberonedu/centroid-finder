import express from 'express';

const router = express.Router();

router.get("/home", (req, res) => {
    res.send('Salamander API server is running!');
});

export default router;