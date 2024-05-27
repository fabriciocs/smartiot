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

describe('Sensor e2e test', () => {
  const sensorPageUrl = '/sensor';
  const sensorPageUrlPattern = new RegExp('/sensor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sensorSample = { nome: 'major-league', tipo: 'TEMPERATURE' };

  let sensor;
  let cliente;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/clientes',
      body: { nome: 'gah', email: 'B+*I-@1]W.+VD' },
    }).then(({ body }) => {
      cliente = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sensors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sensors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sensors/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/configuracao-alertas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/clientes', {
      statusCode: 200,
      body: [cliente],
    });

    cy.intercept('GET', '/api/dado-sensors', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (cliente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/clientes/${cliente.id}`,
      }).then(() => {
        cliente = undefined;
      });
    }
  });

  it('Sensors menu should load Sensors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sensor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Sensor').should('exist');
    cy.url().should('match', sensorPageUrlPattern);
  });

  describe('Sensor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sensorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Sensor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sensor/new$'));
        cy.getEntityCreateUpdateHeading('Sensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sensorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sensors',
          body: {
            ...sensorSample,
            cliente: cliente,
          },
        }).then(({ body }) => {
          sensor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sensors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sensors?page=0&size=20>; rel="last",<http://localhost/api/sensors?page=0&size=20>; rel="first"',
              },
              body: [sensor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sensorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Sensor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sensor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sensorPageUrlPattern);
      });

      it('edit button click should load edit Sensor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Sensor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sensorPageUrlPattern);
      });

      it('edit button click should load edit Sensor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Sensor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sensorPageUrlPattern);
      });

      it('last delete button click should delete instance of Sensor', () => {
        cy.intercept('GET', '/api/sensors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('sensor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sensorPageUrlPattern);

        sensor = undefined;
      });
    });
  });

  describe('new Sensor page', () => {
    beforeEach(() => {
      cy.visit(`${sensorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Sensor');
    });

    it('should create an instance of Sensor', () => {
      cy.get(`[data-cy="nome"]`).type('than hilarious');
      cy.get(`[data-cy="nome"]`).should('have.value', 'than hilarious');

      cy.get(`[data-cy="tipo"]`).select('TEMPERATURE');

      cy.get(`[data-cy="configuracao"]`).type('regarding');
      cy.get(`[data-cy="configuracao"]`).should('have.value', 'regarding');

      cy.get(`[data-cy="cliente"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sensor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sensorPageUrlPattern);
    });
  });
});
