import fs from 'fs/promises';
import path from 'path';
import ffmpeg from 'fluent-ffmpeg';

const getVideos = async (req, res) => {
  try {
    const videoDir = path.join(process.cwd(), 'videos');
    const files = await fs.readdir(videoDir);

    const videoURLs = files.map(file => `${file}`);
    res.json(videoURLs);
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({ error: 'Failed to read video directory' });
  }
};

const getThumbnail = (req, res) => {
  try {

    const videoDir = path.join(process.cwd(), 'videos');
    const thumbnailId = req.params.filename;
    const videoPath = path.join(videoDir, thumbnailId);

    res.setHeader('Content-Type', 'image/jpeg');

    ffmpeg(videoPath)
      .inputOptions(['-ss 0'])        // jump to start
      .outputOptions(['-frames:v 1']) // capture 1 frame
      .format('mjpeg')                // output format as JPEG
      .pipe(res, { end: true });      // stream directly to response
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({
      error: "Error generating thumbnail"
    });
  };

}

export default {
  getVideos,
  getThumbnail
};
