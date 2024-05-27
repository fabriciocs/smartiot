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

describe('SysRole e2e test', () => {
  const sysRolePageUrl = '/sys-role';
  const sysRolePageUrlPattern = new RegExp('/sys-role(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sysRoleSample = { roleName: 'ack notice' };

  let sysRole;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sys-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sys-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sys-roles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sysRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sys-roles/${sysRole.id}`,
      }).then(() => {
        sysRole = undefined;
      });
    }
  });

  it('SysRoles menu should load SysRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sys-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SysRole').should('exist');
    cy.url().should('match', sysRolePageUrlPattern);
  });

  describe('SysRole page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sysRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SysRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sys-role/new$'));
        cy.getEntityCreateUpdateHeading('SysRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysRolePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sys-roles',
          body: sysRoleSample,
        }).then(({ body }) => {
          sysRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sys-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [sysRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sysRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SysRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sysRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysRolePageUrlPattern);
      });

      it('edit button click should load edit SysRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysRolePageUrlPattern);
      });

      it('edit button click should load edit SysRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysRolePageUrlPattern);
      });

      it('last delete button click should delete instance of SysRole', () => {
        cy.intercept('GET', '/api/sys-roles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('sysRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysRolePageUrlPattern);

        sysRole = undefined;
      });
    });
  });

  describe('new SysRole page', () => {
    beforeEach(() => {
      cy.visit(`${sysRolePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SysRole');
    });

    it('should create an instance of SysRole', () => {
      cy.get(`[data-cy="roleName"]`).type('salami city while');
      cy.get(`[data-cy="roleName"]`).should('have.value', 'salami city while');

      cy.get(`[data-cy="description"]`).type('an without ick');
      cy.get(`[data-cy="description"]`).should('have.value', 'an without ick');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sysRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sysRolePageUrlPattern);
    });
  });
});
