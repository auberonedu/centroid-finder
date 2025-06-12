// import controller from '../controllers/videoController.js';
// import fs from 'fs/promises';
// import { existsSync } from 'fs';
// import { spawn } from 'child_process';
// import { v4 as uuidv4 } from 'uuid';
// import * as ffmpegHelpers from '../Utils/ffmpegHelpers.js';

// jest.mock('fs/promises');
// jest.mock('fs', () => ({
//   existsSync: jest.fn(),
// }));
// jest.mock('uuid', () => ({ v4: jest.fn() }));
// jest.mock('child_process', () => ({
//   spawn: jest.fn(),
// }));
// jest.mock('../Utils/ffmpegHelpers.js', () => ({
//   generateThumbnail: jest.fn(),
// }));

// describe('Video Controller Handlers', () => {
//   let req, res;

//   beforeEach(() => {
//     req = { body: {}, params: {}, app: { locals: { JAR_PATH: '/mock/path.jar' } } };
//     res = {
//       status: jest.fn().mockReturnThis(),
//       json: jest.fn(),
//     };
//     uuidv4.mockReturnValue('mock-uuid');
//     jest.clearAllMocks();
//   });

//   describe('getVideos', () => {
//     it('should return 429 if already scanning', async () => {
//       // simulate lock is true
//       controller.__isScanning = true;
//       await controller.getVideos(req, res);
//       expect(res.status).toHaveBeenCalledWith(429);
//     });

//     it('should return video metadata', async () => {
//       fs.readdir.mockResolvedValue(['test.mp4']);
//       fs.mkdir.mockResolvedValue();
//       fs.stat.mockResolvedValue({ birthtime: new Date(), mtime: new Date() });
//       fs.readFile.mockResolvedValue('{}');
//       ffmpegHelpers.generateThumbnail.mockResolvedValue('thumb.jpg');

//       await controller.getVideos(req, res);

//       expect(res.json).toHaveBeenCalledWith({
//         videos: expect.any(Array),
//       });
//     });
//   });

//   describe('getVideoByFilename', () => {
//     it('should return 400 for invalid UUID', async () => {
//       req.params.videoID = 'invalid';
//       await controller.getVideoByFilename(req, res);
//       expect(res.status).toHaveBeenCalledWith(400);
//     });

//     it('should return 404 for missing video file', async () => {
//       req.params.videoID = 'mock-uuid';
//       fs.readFile.mockResolvedValue(JSON.stringify({ "video.mp4": { id: 'mock-uuid', thumbnail: 't.jpg' } }));
//       existsSync.mockReturnValue(false);

//       await controller.getVideoByFilename(req, res);
//       expect(res.status).toHaveBeenCalledWith(404);
//     });
//   });

//   describe('videoProcessing', () => {
//     it('should return 400 if required fields are missing', async () => {
//       req.body = {}; // nothing
//       await controller.videoProcessing(req, res);
//       expect(res.status).toHaveBeenCalledWith(400);
//     });

//     it('should respond with job ID and start process', async () => {
//       req.body = {
//         filename: 'test.mp4',
//         color: { r: 255, g: 255, b: 255 },
//         threshold: 10,
//         interval: '5s',
//       };
//       const mockProc = {
//         stdout: { on: jest.fn() },
//         stderr: { on: jest.fn() },
//         on: jest.fn((event, cb) => {
//           if (event === 'close') cb(0); // simulate success
//         }),
//       };
//       spawn.mockReturnValue(mockProc);
//       fs.mkdir.mockResolvedValue();

//       await controller.videoProcessing(req, res);
//       expect(res.json).toHaveBeenCalledWith({
//         jobId: 'mock-uuid',
//         message: 'Video processing started.',
//       });
//     });
//   });

//   describe('getCompletedCSVs', () => {
//     it('should return list of CSV files', async () => {
//       fs.readdir.mockResolvedValue(['file.csv', 'ignore.txt']);
//       await controller.getCompletedCSVs(req, res);
//       expect(res.json).toHaveBeenCalledWith({ csvFiles: ['file.csv'] });
//     });

//     it('should handle directory read error', async () => {
//       fs.readdir.mockRejectedValue(new Error('fail'));
//       await controller.getCompletedCSVs(req, res);
//       expect(res.status).toHaveBeenCalledWith(500);
//     });
//   });

//   describe('getStatus', () => {
//     it('should return 400 for missing job ID', async () => {
//       req.params.jobId = 'status'; // invalid
//       await controller.getStatus(req, res);
//       expect(res.status).toHaveBeenCalledWith(400);
//     });

//     it('should return 404 for unknown job ID', async () => {
//       req.params.jobId = 'mock-uuid';
//       await controller.getStatus(req, res);
//       expect(res.status).toHaveBeenCalledWith(404);
//     });

//     it('should return job status if exists', async () => {
//       const jobInfo = { status: 'completed', output: 'done', error: '' };
//       controller.__jobStatusMap = new Map([[ 'mock-uuid', jobInfo ]]);
//       req.params.jobId = 'mock-uuid';

//       // temporarily inject map
//       const oldMap = controller.jobStatusMap;
//       controller.jobStatusMap = controller.__jobStatusMap;

//       await controller.getStatus(req, res);
//       expect(res.json).toHaveBeenCalledWith({
//         jobId: 'mock-uuid',
//         status: 'completed',
//         output: 'done',
//         error: '',
//       });

//       controller.jobStatusMap = oldMap; // restore
//     });
//   });
// });

import { describe, it, expect, vi } from 'vitest';
import { getMockReq, getMockRes } from 'vitest-mock-express';
import { videoProcessing } from './path/to/your/controller';
import { spawn } from 'child_process';

// Mock the spawn function to prevent actual execution
vi.mock('child_process', () => ({
  spawn: vi.fn(() => ({
    stdout: { on: vi.fn() },
    stderr: { on: vi.fn() },
    on: vi.fn((event, callback) => {
      if (event === 'close') callback(0); // Simulate successful completion
    }),
  })),
}));

describe('videoProcessing Controller', () => {
  it('should start video processing and return a job ID', async () => {
    const { req, res } = getMockRes();
    req.body = {
      filename: 'test.mp4',
      color: { r: 255, g: 0, b: 0 },
      threshold: 0.5,
      interval: '1s',
    };

    await videoProcessing(req, res);

    expect(res.json).toHaveBeenCalledWith(
      expect.objectContaining({
        jobId: expect.any(String),
        message: 'Video processing started.',
      })
    );
  });

  it('should handle missing required fields', async () => {
    const { req, res } = getMockRes();
    req.body = {}; // Missing required fields

    await videoProcessing(req, res);

    expect(res.status).toHaveBeenCalledWith(400);
    expect(res.json).toHaveBeenCalledWith(
      expect.objectContaining({
        error: 'Missing required fields',
      })
    );
  });
});