import express from 'express';
import router from './routes/serverApi.js'; 
import path from 'path';

const app = express();
const PORT = 3000;

app.use('/videos', express.static(path.resolve('../videos')));
app.use('/results', express.static(path.resolve('../results')));
app.use('/', router);

app.listen(PORT, () => console.log(`Listening on http://localhost:${PORT}`));
