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

describe('ConfiguracaoAlerta e2e test', () => {
  const configuracaoAlertaPageUrl = '/configuracao-alerta';
  const configuracaoAlertaPageUrlPattern = new RegExp('/configuracao-alerta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const configuracaoAlertaSample = {"email":"`]@pLx*3.<e<5jf"};

  let configuracaoAlerta;
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
      body: {"nome":"notwithstanding corny lag","tipo":"HUMIDITY","configuracao":"coverall vigilant"},
    }).then(({ body }) => {
      sensor = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/configuracao-alertas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/configuracao-alertas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/configuracao-alertas/*').as('deleteEntityRequest');
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
    if (configuracaoAlerta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/configuracao-alertas/${configuracaoAlerta.id}`,
      }).then(() => {
        configuracaoAlerta = undefined;
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

  it('ConfiguracaoAlertas menu should load ConfiguracaoAlertas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('configuracao-alerta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ConfiguracaoAlerta').should('exist');
    cy.url().should('match', configuracaoAlertaPageUrlPattern);
  });

  describe('ConfiguracaoAlerta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(configuracaoAlertaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ConfiguracaoAlerta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/configuracao-alerta/new$'));
        cy.getEntityCreateUpdateHeading('ConfiguracaoAlerta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', configuracaoAlertaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/configuracao-alertas',
          body: {
            ...configuracaoAlertaSample,
            sensor: sensor,
          },
        }).then(({ body }) => {
          configuracaoAlerta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/configuracao-alertas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/configuracao-alertas?page=0&size=20>; rel="last",<http://localhost/api/configuracao-alertas?page=0&size=20>; rel="first"',
              },
              body: [configuracaoAlerta],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(configuracaoAlertaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(configuracaoAlertaPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ConfiguracaoAlerta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('configuracaoAlerta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', configuracaoAlertaPageUrlPattern);
      });

      it('edit button click should load edit ConfiguracaoAlerta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ConfiguracaoAlerta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', configuracaoAlertaPageUrlPattern);
      });

      it('edit button click should load edit ConfiguracaoAlerta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ConfiguracaoAlerta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', configuracaoAlertaPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ConfiguracaoAlerta', () => {
        cy.intercept('GET', '/api/configuracao-alertas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('configuracaoAlerta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', configuracaoAlertaPageUrlPattern);

        configuracaoAlerta = undefined;
      });
    });
  });

  describe('new ConfiguracaoAlerta page', () => {
    beforeEach(() => {
      cy.visit(`${configuracaoAlertaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ConfiguracaoAlerta');
    });

    it.skip('should create an instance of ConfiguracaoAlerta', () => {
      cy.get(`[data-cy="limite"]`).type('32380.6');
      cy.get(`[data-cy="limite"]`).should('have.value', '32380.6');

      cy.get(`[data-cy="email"]`).type('6@blYNA.,z,c');
      cy.get(`[data-cy="email"]`).should('have.value', '6@blYNA.,z,c');

      cy.get(`[data-cy="sensor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        configuracaoAlerta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', configuracaoAlertaPageUrlPattern);
    });
  });
});
