describe("Preview Page", () => {
  const testFilename = "ensantina.mp4";

  it("loads the preview page and shows canvases", () => {
    cy.visit(`/preview?filename=${testFilename}`);

    cy.contains("Video Preview Page").should("be.visible");
    cy.get("canvas").should("have.length", 2);
  });

  it("should allow color selection", () => {
    cy.visit(`/preview?filename=${testFilename}`);
    cy.contains("Target Color Selection").click();
    cy.get("canvas").first().click(100, 100);

  });

  it("should handle a successful video process submission", () => {
    cy.intercept("POST", "http://localhost:3001/process", {
      statusCode: 200,
      body: { jobId: "abc123" },
    }).as("processVideo");

    cy.visit(`/preview?filename=${testFilename}`);
    cy.contains("Target Color Selection").click();
    cy.get("canvas").first().click(100, 100);
    cy.contains("Process Video With These Settings").click();

    cy.wait("@processVideo");
    cy.url().should("include", "/results/abc123");
  });

  
});