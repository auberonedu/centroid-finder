import express from 'express';

// create server
const app = express();
const port = 3003;

// mount routers

// listen
app.listen(port, () => console.log(`Server started on http://localhost:${port}`));

