## 🛠️ Development Plan

This plan outlines the structured steps for building the **Salamander Tracker** React frontend. It covers setup, feature implementation, and testing using Cypress.

---

### Project Presentation
-https://docs.google.com/presentation/d/1vQXuqPnlKrJEo4V9by8IB3ZFG53N0bQGygcrkF2fh7U/edit?usp=sharing

### Component Strucutre
-https://lucid.app/lucidchart/4743f22d-9630-4fd7-88ac-e31fabf7fb97/edit?viewport_loc=-677%2C-29%2C2992%2C1466%2C0_0&invitationId=inv_d51f7d63-5ba9-4026-a7e8-081cc175dbb1

### 1. 🏗️ Setup

- Initialize the Next.js app
- Install necessary dependencies:
  - cypress
  - Material UI
  - NextJS
- Organize project structure:
  - Create a `components/` folder
  - Remove default Vite boilerplate and styles

---

### 2. 🧭 Routing & Layout

- Set up NextJS with the following routes:
  - `/videos` – Video list page
  - `/preview/:filename` – Video preview and settings page
- Build a basic layout (optional header or nav)

---

### 3. 📼 Video Chooser Page

- Display a list of mock videos from `mock/videos.js` Or from API 
- Enable navigation to the preview page on click

---

### 4. 🔍 Video Preview Page

- Show:
  - Original thumbnail image
  - Binarized thumbnail with centroid
- Include interactive controls:
  - Threshold slider
  - Target color picker
- Add functionality:
  - “Process Video” button
  - Job status display
  - Download link to result CSV

---

### 5. 🖼️ Image Logic

- Convert the thumbnail image to binary using:
  - Selected color
  - Threshold value
- Detect the largest connected region
- Calculate and render the centroid on the binarized image
- Reimplement your Java logic in JavaScript

---

### 6. 🔄 Processing Jobs

- Implement job submission:
  - `POST /process/{filename}?targetColor=<hex>&threshold=<int>`
- Track job status via polling:
  - `GET /process/{jobId}/status`
- Display download link when job is complete

---

### 7. 🧪 Testing (Cypress)

- Use Cypress for end-to-end testing

#### `/videos` – Video Chooser Page

- Renders list of videos from mock data
- Navigates to preview page on selection

#### `/preview/:filename` – Video Preview Page

- Loads original and binarized thumbnails
- Updates binarized image on slider/color input
- Displays centroid marker
- Submits processing job correctly
- Tracks job status and displays CSV download

---

### 8. 🎨 Final Touches & Styling

- Apply consistent design with Material UI
- Add favicon, custom title, and UI polish
- Final code cleanup and deployment prep





Progress: 

Video Chooser	Lists mock video filenames and allows navigation to preview page
Thumbnail Preview	Renders original and binarized image from mock data
Live Tuning	Includes working threshold slider and color picker
✅ Process Button	Sends a mock POST request and simulates job start
✅ Job Status + CSV Link	Tracks mock job status and shows downloadable link when “complete”
API Successfully Calls Data 	 At least one feature now pulls real data from the backend using fetch 
✅ Basic Tests       	 Includes at least one working test using mock data (component or interaction)  
✅ Styling with Material UI 
