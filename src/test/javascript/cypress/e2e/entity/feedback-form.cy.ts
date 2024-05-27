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

describe('FeedbackForm e2e test', () => {
  const feedbackFormPageUrl = '/feedback-form';
  const feedbackFormPageUrlPattern = new RegExp('/feedback-form(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const feedbackFormSample = { title: 'hurricane', status: 'beautify minus', createdAt: '2024-05-20T21:33:38.239Z' };

  let feedbackForm;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/feedback-forms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/feedback-forms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/feedback-forms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (feedbackForm) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/feedback-forms/${feedbackForm.id}`,
      }).then(() => {
        feedbackForm = undefined;
      });
    }
  });

  it('FeedbackForms menu should load FeedbackForms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback-form');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FeedbackForm').should('exist');
    cy.url().should('match', feedbackFormPageUrlPattern);
  });

  describe('FeedbackForm page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(feedbackFormPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FeedbackForm page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feedback-form/new$'));
        cy.getEntityCreateUpdateHeading('FeedbackForm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackFormPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/feedback-forms',
          body: feedbackFormSample,
        }).then(({ body }) => {
          feedbackForm = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/feedback-forms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/feedback-forms?page=0&size=20>; rel="last",<http://localhost/api/feedback-forms?page=0&size=20>; rel="first"',
              },
              body: [feedbackForm],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(feedbackFormPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FeedbackForm page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feedbackForm');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackFormPageUrlPattern);
      });

      it('edit button click should load edit FeedbackForm page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeedbackForm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackFormPageUrlPattern);
      });

      it('edit button click should load edit FeedbackForm page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeedbackForm');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackFormPageUrlPattern);
      });

      it('last delete button click should delete instance of FeedbackForm', () => {
        cy.intercept('GET', '/api/feedback-forms/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('feedbackForm').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackFormPageUrlPattern);

        feedbackForm = undefined;
      });
    });
  });

  describe('new FeedbackForm page', () => {
    beforeEach(() => {
      cy.visit(`${feedbackFormPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FeedbackForm');
    });

    it('should create an instance of FeedbackForm', () => {
      cy.get(`[data-cy="title"]`).type('tightly lacerate');
      cy.get(`[data-cy="title"]`).should('have.value', 'tightly lacerate');

      cy.get(`[data-cy="description"]`).type('wag');
      cy.get(`[data-cy="description"]`).should('have.value', 'wag');

      cy.get(`[data-cy="status"]`).type('consequence');
      cy.get(`[data-cy="status"]`).should('have.value', 'consequence');

      cy.get(`[data-cy="createdAt"]`).type('2024-05-20T00:36');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-05-20T00:36');

      cy.get(`[data-cy="updatedAt"]`).type('2024-05-19T22:21');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-05-19T22:21');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        feedbackForm = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', feedbackFormPageUrlPattern);
    });
  });
});
