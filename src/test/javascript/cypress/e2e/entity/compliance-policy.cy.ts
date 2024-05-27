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

describe('CompliancePolicy e2e test', () => {
  const compliancePolicyPageUrl = '/compliance-policy';
  const compliancePolicyPageUrlPattern = new RegExp('/compliance-policy(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compliancePolicySample = { name: 'lid optimistically till', rules: 'incidentally awkwardly fooey' };

  let compliancePolicy;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/compliance-policies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compliance-policies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compliance-policies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compliancePolicy) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/compliance-policies/${compliancePolicy.id}`,
      }).then(() => {
        compliancePolicy = undefined;
      });
    }
  });

  it('CompliancePolicies menu should load CompliancePolicies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compliance-policy');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CompliancePolicy').should('exist');
    cy.url().should('match', compliancePolicyPageUrlPattern);
  });

  describe('CompliancePolicy page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(compliancePolicyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CompliancePolicy page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compliance-policy/new$'));
        cy.getEntityCreateUpdateHeading('CompliancePolicy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compliancePolicyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/compliance-policies',
          body: compliancePolicySample,
        }).then(({ body }) => {
          compliancePolicy = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/compliance-policies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [compliancePolicy],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(compliancePolicyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CompliancePolicy page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compliancePolicy');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compliancePolicyPageUrlPattern);
      });

      it('edit button click should load edit CompliancePolicy page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompliancePolicy');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compliancePolicyPageUrlPattern);
      });

      it('edit button click should load edit CompliancePolicy page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CompliancePolicy');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compliancePolicyPageUrlPattern);
      });

      it('last delete button click should delete instance of CompliancePolicy', () => {
        cy.intercept('GET', '/api/compliance-policies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('compliancePolicy').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compliancePolicyPageUrlPattern);

        compliancePolicy = undefined;
      });
    });
  });

  describe('new CompliancePolicy page', () => {
    beforeEach(() => {
      cy.visit(`${compliancePolicyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CompliancePolicy');
    });

    it('should create an instance of CompliancePolicy', () => {
      cy.get(`[data-cy="name"]`).type('furthermore secret');
      cy.get(`[data-cy="name"]`).should('have.value', 'furthermore secret');

      cy.get(`[data-cy="description"]`).type('agonizing within than');
      cy.get(`[data-cy="description"]`).should('have.value', 'agonizing within than');

      cy.get(`[data-cy="rules"]`).type('verbally ultimately zowie');
      cy.get(`[data-cy="rules"]`).should('have.value', 'verbally ultimately zowie');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        compliancePolicy = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', compliancePolicyPageUrlPattern);
    });
  });
});
