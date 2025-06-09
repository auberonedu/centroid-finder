describe("VideoChooserPage", () => {
  const mockVideos = [
    { name: "sample1.mp4", duration: 12.3 },
    { name: "sample2.mp4", duration: 45.1 },
  ];

  beforeEach(() => {
    // Stub the /videos API request
    cy.intercept("GET", "http://localhost:3001/videos", {
      statusCode: 200,
      body: { videos: mockVideos },
    }).as("getVideos");
  });

    it("displays loading and then a list of videos", () => {
        cy.visit("/videos"); // adjust if your route is different (e.g., /videos)

        cy.contains("Video Chooser Page").should("be.visible");
        cy.contains("Please select the video you want to process").should(
        "be.visible"
        );

        // Wait for the stubbed request and check that list renders
        cy.wait("@getVideos");

        // Check both video names are rendered
        mockVideos.forEach((video) => {
        cy.contains(video.name).should("be.visible");
        cy.contains(`Duration: ${Math.round(video.duration)}s`).should(
            "be.visible"
        );
        });
    });

    it("navigates to the preview page when a video is clicked", () => {
        cy.visit("/videos");

        cy.wait("@getVideos");

        cy.contains(mockVideos[0].name)
        .closest("a")
        .should(
            "have.attr",
            "href",
            `/preview?filename=${encodeURIComponent(mockVideos[0].name)}`
        )
        .click();

        // The new page should show "Video Preview Page" if your Preview page is integrated
        cy.url().should("include", "/preview");
    });
    });