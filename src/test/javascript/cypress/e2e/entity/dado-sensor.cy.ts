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

describe('DadoSensor e2e test', () => {
  const dadoSensorPageUrl = '/dado-sensor';
  const dadoSensorPageUrlPattern = new RegExp('/dado-sensor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const dadoSensorSample = {"dados":"following loathsome"};

  let dadoSensor;
  // let sensor;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sensors',
      body: {"nome":"absolute what ouch","tipo":"HUMIDITY","configuracao":"oof rehearse bestow"},
    }).then(({ body }) => {
      sensor = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/dado-sensors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dado-sensors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dado-sensors/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/sensors', {
      statusCode: 200,
      body: [sensor],
    });

  });
   */

  afterEach(() => {
    if (dadoSensor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dado-sensors/${dadoSensor.id}`,
      }).then(() => {
        dadoSensor = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (sensor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sensors/${sensor.id}`,
      }).then(() => {
        sensor = undefined;
      });
    }
  });
   */

  it('DadoSensors menu should load DadoSensors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dado-sensor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DadoSensor').should('exist');
    cy.url().should('match', dadoSensorPageUrlPattern);
  });

  describe('DadoSensor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dadoSensorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DadoSensor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dado-sensor/new$'));
        cy.getEntityCreateUpdateHeading('DadoSensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dadoSensorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dado-sensors',
          body: {
            ...dadoSensorSample,
            sensor: sensor,
          },
        }).then(({ body }) => {
          dadoSensor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dado-sensors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/dado-sensors?page=0&size=20>; rel="last",<http://localhost/api/dado-sensors?page=0&size=20>; rel="first"',
              },
              body: [dadoSensor],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dadoSensorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(dadoSensorPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DadoSensor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dadoSensor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dadoSensorPageUrlPattern);
      });

      it('edit button click should load edit DadoSensor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DadoSensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dadoSensorPageUrlPattern);
      });

      it('edit button click should load edit DadoSensor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DadoSensor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dadoSensorPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DadoSensor', () => {
        cy.intercept('GET', '/api/dado-sensors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('dadoSensor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dadoSensorPageUrlPattern);

        dadoSensor = undefined;
      });
    });
  });

  describe('new DadoSensor page', () => {
    beforeEach(() => {
      cy.visit(`${dadoSensorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DadoSensor');
    });

    it.skip('should create an instance of DadoSensor', () => {
      cy.get(`[data-cy="dados"]`).type('step-grandfather');
      cy.get(`[data-cy="dados"]`).should('have.value', 'step-grandfather');

      cy.get(`[data-cy="timestamp"]`).type('2024-05-09T19:06');
      cy.get(`[data-cy="timestamp"]`).blur();
      cy.get(`[data-cy="timestamp"]`).should('have.value', '2024-05-09T19:06');

      cy.get(`[data-cy="sensor"]`).select([0]);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dadoSensor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', dadoSensorPageUrlPattern);
    });
  });
});
