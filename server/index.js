import express from 'express';
import cors from 'cors';                 
import router from './Router/Router.js';
import dotenv from 'dotenv';

dotenv.config();

const app = express();                   

app.use(cors());                         
app.use('/', router);

const PORT = 3001;
console.log("Resolved videosDir:", process.env.VIDEO_DIR); 

app.listen(PORT, () => {
  console.log(`Server listening at http://localhost:${PORT}`);
});
