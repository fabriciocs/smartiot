import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SysUserDetailComponent } from './sys-user-detail.component';

describe('SysUser Management Detail Component', () => {
  let comp: SysUserDetailComponent;
  let fixture: ComponentFixture<SysUserDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SysUserDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SysUserDetailComponent,
              resolve: { sysUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SysUserDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SysUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sysUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SysUserDetailComponent);

      // THEN
      expect(instance.sysUser()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
