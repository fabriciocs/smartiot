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

describe('NotificationSettings e2e test', () => {
  const notificationSettingsPageUrl = '/notification-settings';
  const notificationSettingsPageUrlPattern = new RegExp('/notification-settings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const notificationSettingsSample = { preferences: 'pish condense' };

  let notificationSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/notification-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/notification-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/notification-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (notificationSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/notification-settings/${notificationSettings.id}`,
      }).then(() => {
        notificationSettings = undefined;
      });
    }
  });

  it('NotificationSettings menu should load NotificationSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification-settings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NotificationSettings').should('exist');
    cy.url().should('match', notificationSettingsPageUrlPattern);
  });

  describe('NotificationSettings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(notificationSettingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NotificationSettings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/notification-settings/new$'));
        cy.getEntityCreateUpdateHeading('NotificationSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationSettingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/notification-settings',
          body: notificationSettingsSample,
        }).then(({ body }) => {
          notificationSettings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/notification-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [notificationSettings],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(notificationSettingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NotificationSettings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('notificationSettings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationSettingsPageUrlPattern);
      });

      it('edit button click should load edit NotificationSettings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotificationSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationSettingsPageUrlPattern);
      });

      it('edit button click should load edit NotificationSettings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotificationSettings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationSettingsPageUrlPattern);
      });

      it('last delete button click should delete instance of NotificationSettings', () => {
        cy.intercept('GET', '/api/notification-settings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('notificationSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationSettingsPageUrlPattern);

        notificationSettings = undefined;
      });
    });
  });

  describe('new NotificationSettings page', () => {
    beforeEach(() => {
      cy.visit(`${notificationSettingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('NotificationSettings');
    });

    it('should create an instance of NotificationSettings', () => {
      cy.get(`[data-cy="preferences"]`).type('unnatural boo excluding');
      cy.get(`[data-cy="preferences"]`).should('have.value', 'unnatural boo excluding');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        notificationSettings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', notificationSettingsPageUrlPattern);
    });
  });
});
