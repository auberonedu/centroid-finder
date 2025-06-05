# 1. Using the latest Node.js for the builder
FROM node:24-alpine AS builder

# 2. Setting the working directory inside the container
WORKDIR /app

# 3. Installing git and cloning the front-end repo
RUN apk add --no-cache git
RUN git clone https://github.com/f3liz/centroid-finder-frontend.git .

# 4. Installing dependencies and building the Next.js app
RUN npm install
RUN npm run build

# 5. Completing the Runtime phase using Node.js
FROM node:24-alpine

WORKDIR /app

# 6. Copying the build output from the builder stage
COPY --from=builder /app ./

# 7. Exposing the default Next.js port
EXPOSE 3000

# 8. Starting the Next.js server
CMD ["npm", "start"]