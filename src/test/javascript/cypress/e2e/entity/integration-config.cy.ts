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

describe('IntegrationConfig e2e test', () => {
  const integrationConfigPageUrl = '/integration-config';
  const integrationConfigPageUrlPattern = new RegExp('/integration-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const integrationConfigSample = { serviceName: 'rustle likewise hire', configData: 'goose', createdAt: '2024-05-20T02:28:23.140Z' };

  let integrationConfig;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/integration-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/integration-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/integration-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (integrationConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/integration-configs/${integrationConfig.id}`,
      }).then(() => {
        integrationConfig = undefined;
      });
    }
  });

  it('IntegrationConfigs menu should load IntegrationConfigs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('integration-config');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IntegrationConfig').should('exist');
    cy.url().should('match', integrationConfigPageUrlPattern);
  });

  describe('IntegrationConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(integrationConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IntegrationConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/integration-config/new$'));
        cy.getEntityCreateUpdateHeading('IntegrationConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', integrationConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/integration-configs',
          body: integrationConfigSample,
        }).then(({ body }) => {
          integrationConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/integration-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [integrationConfig],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(integrationConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IntegrationConfig page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('integrationConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', integrationConfigPageUrlPattern);
      });

      it('edit button click should load edit IntegrationConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IntegrationConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', integrationConfigPageUrlPattern);
      });

      it('edit button click should load edit IntegrationConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IntegrationConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', integrationConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of IntegrationConfig', () => {
        cy.intercept('GET', '/api/integration-configs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('integrationConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', integrationConfigPageUrlPattern);

        integrationConfig = undefined;
      });
    });
  });

  describe('new IntegrationConfig page', () => {
    beforeEach(() => {
      cy.visit(`${integrationConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IntegrationConfig');
    });

    it('should create an instance of IntegrationConfig', () => {
      cy.get(`[data-cy="serviceName"]`).type('gravy and scientific');
      cy.get(`[data-cy="serviceName"]`).should('have.value', 'gravy and scientific');

      cy.get(`[data-cy="configData"]`).type('personal woot surprised');
      cy.get(`[data-cy="configData"]`).should('have.value', 'personal woot surprised');

      cy.get(`[data-cy="createdAt"]`).type('2024-05-20T05:37');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-05-20T05:37');

      cy.get(`[data-cy="updatedAt"]`).type('2024-05-20T15:52');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-05-20T15:52');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        integrationConfig = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', integrationConfigPageUrlPattern);
    });
  });
});
