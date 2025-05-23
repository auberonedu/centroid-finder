require('dotenv').config();
const express = require('express');
const app = express();
const videoRoutes = require('./routes/videoRoutes');
const processRoutes = require('./routes/processRoutes');

app.use(express.json());

app.use('/api/videos', videoRoutes);
app.use('/process', processRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));