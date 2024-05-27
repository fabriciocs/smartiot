import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Analytics e2e test', () => {
  const analyticsPageUrl = '/analytics';
  const analyticsPageUrlPattern = new RegExp('/analytics(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const analyticsSample = { type: 'lout', data: 'lest', createdAt: '2024-05-20T10:57:32.952Z' };

  let analytics;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/analytics+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/analytics').as('postEntityRequest');
    cy.intercept('DELETE', '/api/analytics/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (analytics) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/analytics/${analytics.id}`,
      }).then(() => {
        analytics = undefined;
      });
    }
  });

  it('Analytics menu should load Analytics page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('analytics');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Analytics').should('exist');
    cy.url().should('match', analyticsPageUrlPattern);
  });

  describe('Analytics page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(analyticsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Analytics page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/analytics/new$'));
        cy.getEntityCreateUpdateHeading('Analytics');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', analyticsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/analytics',
          body: analyticsSample,
        }).then(({ body }) => {
          analytics = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/analytics+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [analytics],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(analyticsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Analytics page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('analytics');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', analyticsPageUrlPattern);
      });

      it('edit button click should load edit Analytics page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Analytics');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', analyticsPageUrlPattern);
      });

      it('edit button click should load edit Analytics page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Analytics');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', analyticsPageUrlPattern);
      });

      it('last delete button click should delete instance of Analytics', () => {
        cy.intercept('GET', '/api/analytics/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('analytics').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', analyticsPageUrlPattern);

        analytics = undefined;
      });
    });
  });

  describe('new Analytics page', () => {
    beforeEach(() => {
      cy.visit(`${analyticsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Analytics');
    });

    it('should create an instance of Analytics', () => {
      cy.get(`[data-cy="type"]`).type('whoa unless sorrowful');
      cy.get(`[data-cy="type"]`).should('have.value', 'whoa unless sorrowful');

      cy.get(`[data-cy="data"]`).type('tax awkwardly');
      cy.get(`[data-cy="data"]`).should('have.value', 'tax awkwardly');

      cy.get(`[data-cy="createdAt"]`).type('2024-05-20T17:22');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-05-20T17:22');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        analytics = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', analyticsPageUrlPattern);
    });
  });
});
