#!/bin/bash

echo "Welcome to Salamander Tracker 🦎"
echo "--------------------------------"
echo "Please enter the FULL path to your input videos folder (e.g. /Users/yourname/videos):"
read VIDEO_DIRECTORY

echo "Please enter the FULL path to your output results folder (e.g. /Users/yourname/results):"
read RESULTS_DIRECTORY

# Confirm entries
echo ""
echo "🛠 Using:"
echo "   Videos  → $VIDEO_DIRECTORY"
echo "   Results → $RESULTS_DIRECTORY"
echo ""

# Start BACKEND (Express + Java, port 3001)
docker run \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/oakes777/salamander:latest &

# Start FRONTEND (Next.js, port 3000)
docker run \
  -p 3000:3000 \
  ghcr.io/oakes777/salamander-client:latest

