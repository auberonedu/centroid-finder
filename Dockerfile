# -------- Base Image --------
FROM node:20-slim

# Install Java and ffmpeg
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk ffmpeg && \
    apt-get clean

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Install dependencies
RUN npm install
RUN npm --prefix server install
RUN npm --prefix frontend install

# Set environment variables
ENV VIDEO_PATH=/videos
ENV RESULT_PATH=/results

# Expose ports for both frontend and backend
EXPOSE 3000
EXPOSE 3001

# Start both frontend and backend
CMD ["npm", "run", "dev"]
