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

describe('SysUser e2e test', () => {
  const sysUserPageUrl = '/sys-user';
  const sysUserPageUrlPattern = new RegExp('/sys-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sysUserSample = {
    name: 'frantically foray truly',
    email: 'Toy_Block@gmail.com',
    passwordHash: 'downtown aw bashfully',
    createdAt: '2024-05-20T13:42:49.158Z',
  };

  let sysUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sys-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sys-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sys-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sysUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sys-users/${sysUser.id}`,
      }).then(() => {
        sysUser = undefined;
      });
    }
  });

  it('SysUsers menu should load SysUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sys-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SysUser').should('exist');
    cy.url().should('match', sysUserPageUrlPattern);
  });

  describe('SysUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sysUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SysUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sys-user/new$'));
        cy.getEntityCreateUpdateHeading('SysUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sys-users',
          body: sysUserSample,
        }).then(({ body }) => {
          sysUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sys-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sys-users?page=0&size=20>; rel="last",<http://localhost/api/sys-users?page=0&size=20>; rel="first"',
              },
              body: [sysUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sysUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SysUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sysUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysUserPageUrlPattern);
      });

      it('edit button click should load edit SysUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysUserPageUrlPattern);
      });

      it('edit button click should load edit SysUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysUserPageUrlPattern);
      });

      it('last delete button click should delete instance of SysUser', () => {
        cy.intercept('GET', '/api/sys-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('sysUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', sysUserPageUrlPattern);

        sysUser = undefined;
      });
    });
  });

  describe('new SysUser page', () => {
    beforeEach(() => {
      cy.visit(`${sysUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SysUser');
    });

    it('should create an instance of SysUser', () => {
      cy.get(`[data-cy="name"]`).type('continually');
      cy.get(`[data-cy="name"]`).should('have.value', 'continually');

      cy.get(`[data-cy="email"]`).type('Keanu.Klein@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Keanu.Klein@hotmail.com');

      cy.get(`[data-cy="passwordHash"]`).type('absolute limply');
      cy.get(`[data-cy="passwordHash"]`).should('have.value', 'absolute limply');

      cy.get(`[data-cy="createdAt"]`).type('2024-05-20T13:03');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-05-20T13:03');

      cy.get(`[data-cy="updatedAt"]`).type('2024-05-20T03:00');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-05-20T03:00');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        sysUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', sysUserPageUrlPattern);
    });
  });
});
