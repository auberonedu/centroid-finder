describe('Video List Page (real backend)', () => {
  beforeEach(() => {
    cy.intercept('GET', 'http://localhost:3001/videos').as('getVideos');
    cy.visit('/videos');
  });


  it('loads and displays a list of videos', () => {
    cy.wait('@getVideos');
    cy.get('[data-testid="video-item"]').should('have.length.at.most', 10);
  });

  it('disables Previous on first page', () => {
    cy.wait('@getVideos');
    cy.get('button').contains('Previous').should('be.disabled');
  });

  it('disables Next on last page', () => {
    cy.wait('@getVideos');
    function clickNextUntilDisabled() {
      cy.get('button').contains('Next').then(($btn) => {
        if (!$btn.is(':disabled')) {
          cy.wrap($btn).click();
          clickNextUntilDisabled();
        }
      });
    }
    clickNextUntilDisabled();
    cy.get('button').contains('Next').should('be.disabled');
  });
});