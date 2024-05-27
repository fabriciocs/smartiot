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

describe('FeedbackResponse e2e test', () => {
  const feedbackResponsePageUrl = '/feedback-response';
  const feedbackResponsePageUrlPattern = new RegExp('/feedback-response(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const feedbackResponseSample = { responseData: 'unlike adventurously burn', submittedAt: '2024-05-20T12:02:32.353Z' };

  let feedbackResponse;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/feedback-responses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/feedback-responses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/feedback-responses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (feedbackResponse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/feedback-responses/${feedbackResponse.id}`,
      }).then(() => {
        feedbackResponse = undefined;
      });
    }
  });

  it('FeedbackResponses menu should load FeedbackResponses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback-response');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FeedbackResponse').should('exist');
    cy.url().should('match', feedbackResponsePageUrlPattern);
  });

  describe('FeedbackResponse page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(feedbackResponsePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FeedbackResponse page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feedback-response/new$'));
        cy.getEntityCreateUpdateHeading('FeedbackResponse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackResponsePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/feedback-responses',
          body: feedbackResponseSample,
        }).then(({ body }) => {
          feedbackResponse = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/feedback-responses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/feedback-responses?page=0&size=20>; rel="last",<http://localhost/api/feedback-responses?page=0&size=20>; rel="first"',
              },
              body: [feedbackResponse],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(feedbackResponsePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FeedbackResponse page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feedbackResponse');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackResponsePageUrlPattern);
      });

      it('edit button click should load edit FeedbackResponse page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeedbackResponse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackResponsePageUrlPattern);
      });

      it('edit button click should load edit FeedbackResponse page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeedbackResponse');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackResponsePageUrlPattern);
      });

      it('last delete button click should delete instance of FeedbackResponse', () => {
        cy.intercept('GET', '/api/feedback-responses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('feedbackResponse').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackResponsePageUrlPattern);

        feedbackResponse = undefined;
      });
    });
  });

  describe('new FeedbackResponse page', () => {
    beforeEach(() => {
      cy.visit(`${feedbackResponsePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FeedbackResponse');
    });

    it('should create an instance of FeedbackResponse', () => {
      cy.get(`[data-cy="responseData"]`).type('upwardly');
      cy.get(`[data-cy="responseData"]`).should('have.value', 'upwardly');

      cy.get(`[data-cy="submittedAt"]`).type('2024-05-20T01:29');
      cy.get(`[data-cy="submittedAt"]`).blur();
      cy.get(`[data-cy="submittedAt"]`).should('have.value', '2024-05-20T01:29');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        feedbackResponse = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', feedbackResponsePageUrlPattern);
    });
  });
});
