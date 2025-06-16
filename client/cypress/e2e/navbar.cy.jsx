describe('Navigation Bar', () => {
  beforeEach(() => {
    cy.visit('/videos'); // Your app’s home route
  });

  it('should display the logo', () => {
    cy.get('img[alt="Logo"]').should('be.visible');
  });

  it('should display navigation buttons', () => {
    cy.contains('Video List').should('be.visible');
    cy.contains('All Results').should('be.visible');
  });

  it('should navigate to /videos when "Video List" is clicked', () => {
    cy.contains('Video List').click();
    cy.url().should('include', '/videos');
  });

  it('should navigate to /results when "All Results" is clicked', () => {
    cy.contains('All Results').click();
    cy.url().should('include', '/results');
  });
});