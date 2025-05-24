import router from './Router/Router.js';
import express from 'express';
import dotenv from 'dotenv';

const express = require('express');
const app = express();
const PORT = 3000;


app.use('/', router);

app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});