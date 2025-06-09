FROM node:20-slim

# Set working directory
WORKDIR /app

# Copy only what's needed
COPY package*.json ./

# Install deps first (Docker layer caching!)
RUN npm install

# Copy remaining files
COPY . .

# Expose port
EXPOSE 3000

# Run app
CMD ["npm", "run", "dev"]
