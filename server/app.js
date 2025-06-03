import express from 'express';
import router from './routes/serverApi.js'; 
import path from 'path';
import cors from 'cors';

const app = express();
const PORT = 3001;

app.use(cors());

app.use('/videos', express.static(path.resolve('../videos')));
app.use('/results', express.static(path.resolve('../results')));
app.use('/', router);

app.listen(PORT, () => console.log(`Listening on http://localhost:${PORT}`));
