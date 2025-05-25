import express from 'express';
import router from './Router/Router.js';
import dotenv from 'dotenv';

const app = express();
const PORT = 3000;

//test that server is listening before controllers
// app.get('/', (req, res) => {
//   res.send('✅ Express server is running!');
// });

app.use('/', router);

app.listen(PORT, () => {
  console.log(`🚀 Server listening at http://localhost:${PORT}`);
});