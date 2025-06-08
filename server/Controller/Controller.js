import fs from 'fs/promises';
import { existsSync } from 'fs';
import path from 'path';

import { fileURLToPath } from 'url';
import dotenv from 'dotenv';
import { v4 as uuidv4 } from 'uuid';
import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstaller from '@ffmpeg-installer/ffmpeg';

ffmpeg.setFfmpegPath(ffmpegInstaller.path);

// Required in ES modules to get __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
dotenv.config({ path: path.resolve(__dirname, '../.env') })

// Go up one directory from /server/Controller to /project/videos
const indexFilePath = path.resolve(__dirname, '../videoIndex.json')
const videosDir = path.resolve(__dirname, process.env.VIDEO_DIR);
const thumbnailsDir = path.resolve(__dirname, '../thumbnails');
const resultsDir = path.resolve(__dirname, '../results');

const loadIndex = async () => {
    try {
        const data = await fs.readFile(indexFilePath, 'utf8');
        return JSON.parse(data);
    } catch {
        return {};
    }
};

const saveIndex = async (index) => {
    await fs.writeFile(indexFilePath, JSON.stringify(index, null, 2));
};

console.log("Resolved videosDir:", videosDir);


const getVideoDuration = (filePath) => {
    return new Promise((resolve, reject) => {
        ffmpeg.ffprobe(filePath, (err, metadata) => {
            if (err) {
                console.error('ffprobe error:', err.message);
                return resolve(null); // Don't crash the app — just skip metadata
            }

            try {
                const seconds = metadata?.format?.duration;
                resolve(seconds ?? null);
            } catch (parseErr) {
                console.error('Error parsing metadata:', parseErr.message);
                resolve(null);
            }
        });
    });
};

const generateThumbnail = async (inputPath, outputPath) => {
  await new Promise((resolve, reject) => {
    ffmpeg(inputPath)
      .screenshots({
        count: 1,
        folder: path.dirname(outputPath),
        filename: path.basename(outputPath),
        timemarks: ['0'] // Capture first frame
      })
      .on('end', resolve)
      .on('error', reject);
  });
};
const getVideos = async (req, res) => {
  try {
    const index = await loadIndex();
    const files = await fs.readdir(videosDir);
    const videoFiles = files.filter(file =>
      ['.mp4', '.mov', '.avi'].includes(path.extname(file).toLowerCase())
    );

    await fs.mkdir(thumbnailsDir, { recursive: true });

    for (const file of videoFiles) {
      // Assign UUID if missing
      if (!index[file]) {
        index[file] = {
          id: uuidv4(),
          thumbnail: `thumbnails/placeholder.jpg` // temp value
        };
      }

      const videoId = index[file].id;
      const thumbnailPath = path.join(thumbnailsDir, `${videoId}.jpg`);
      const thumbnailRelPath = `thumbnails/${videoId}.jpg`;

      // Generate thumbnail if not already present
      if (!existsSync(thumbnailPath)) {
        const fullVideoPath = path.join(videosDir, file);
        try {
          await generateThumbnail(fullVideoPath, thumbnailPath);
          index[file].thumbnail = thumbnailRelPath;
        } catch (err) {
          console.error(`Failed to generate thumbnail for ${file}:`, err);
          index[file].thumbnail = null;
        }
      } else {
        index[file].thumbnail = thumbnailRelPath;
      }
    }

    // Save updated index
    await saveIndex(index);

    // Return metadata
    const metadataList = await Promise.all(videoFiles.map(async (file) => {
      const fullPath = path.join(videosDir, file);
      const stats = await fs.stat(fullPath);
      const duration = await getVideoDuration(fullPath);
      const { id, thumbnail } = index[file];

      return {
        id,
        name: file,
        duration,
        createdAt: stats.birthtime,
        modifiedAt: stats.mtime,
        thumbnail
      };
    }));

    res.json({ videos: metadataList });

  } catch (err) {
    console.error('Error in getVideos:', err);
    res.status(500).json({ error: 'Failed to load video metadata' });
  }
};

const getVideoById = async (req, res) => {
  try {
    const { id } = req.params;
    const index = await loadIndex();

    // Debugging: log the received ID
    console.log('Looking for video with ID:', id);

    // Find matching filename for this ID
    const entry = Object.entries(index).find(([filename, meta]) => {
      console.log(`Checking: ${filename} -> ${meta.id}`);
      return meta.id === id;
    });

    if (!entry) {
      console.warn('No video found for ID:', id);
      return res.status(404).json({ error: 'Video not found' });
    }

    const [filename, metadata] = entry;
    const fullPath = path.join(videosDir, filename);

    // Confirm the video file actually exists
    if (!existsSync(fullPath)) {
      return res.status(404).json({ error: 'Video file missing from disk' });
    }

    const stats = await fs.stat(fullPath);
    const duration = await getVideoDuration(fullPath);

    res.json({
      id: metadata.id,
      name: filename,
      duration,
      createdAt: stats.birthtime,
      modifiedAt: stats.mtime,
      thumbnail: metadata.thumbnail
    });

  } catch (err) {
    console.error('Error in getVideoById:', err);
    res.status(500).json({ error: 'Failed to retrieve video metadata' });
  }
};

const videoProcessing = async (req, res) => {

  const jobId = uuidv4();
  const outputCsvPath = path.join(resultsDir, `${jobId}.csv`);
  
  const { videoPath, targetColorHex, threshold, frameInterval } = req.body;

  if (!videoPath || !targetColorHex || !threshold || !outputCsvPath) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  jobStatusMap.set(jobId, { status: 'processing', output: '', error: '' });

  const args = [
    '-jar',
    path.resolve(__dirname, '../videoprocessor.jar'),
    videoPath,
    targetColorHex,
    threshold.toString(),
    outputCsvPath,
    frameInterval ? frameInterval.toString() : '1'
  ];

  const java = spawn('java', args);

  let output = '';
  let error = '';

  java.stdout.on('data', (data) => {
    output += data.toString();
  });

  java.stderr.on('data', (data) => {
    error += data.toString();
  });

  java.on('close', (code) => {
    if (code === 0) {
      jobStatusMap.set(jobId, {
        status: 'completed',
        output,
        error: ''
      });
    } else {
      jobStatusMap.set(jobId, {
        status: 'failed',
        output,
        error
      });
    }
  });

  // Respond immediately
  res.json({
    jobId,
    message: 'Video processing started.'
  });
};

const getCompletedCSVs = async (req, res) => {
  try {
    const files = await fs.promises.readdir(resultsDir);

    const csvFiles = files.filter((file) => file.endsWith('.csv'));

    res.json({
      csvFiles
    });
  } catch (err) {
    console.error('Error reading results directory:', err);
    res.status(500).json({ error: 'Unable to list CSV files' });
  }
};

const getStatus = (req, res) => {
  const { jobId } = req.params;

  if (!jobId) {
    return res.status(400).json({ error: 'Missing jobId in request' });
  }

  const jobInfo = jobStatusMap.get(jobId);

  if (!jobInfo) {
    return res.status(404).json({ error: 'Job ID not found' });
  }

  res.json({
    jobId,
    status: jobInfo.status,
    output: jobInfo.output,
    error: jobInfo.error
  });
};


export default {
    getVideos,
    getVideoById,
    videoProcessing,
    getCompletedCSVs,
    getStatus
}