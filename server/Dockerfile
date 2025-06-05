# Using the latest lightweight official Node.js image
FROM node:24-alpine

# Setting the working directory inside the container
WORKDIR /app

# Copying package.json and package-lock.json
COPY package*.json ./

# Installing Node.js dependencies
RUN npm install

# Copying the rest of the backend code into the container
COPY . .

# Exposing the port the Express server listens on
EXPOSE 3001

# Command to start the Express server
CMD ["node", "index.js"]