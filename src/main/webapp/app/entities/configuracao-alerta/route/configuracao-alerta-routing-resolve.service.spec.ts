import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { ConfiguracaoAlertaService } from '../service/configuracao-alerta.service';

import configuracaoAlertaResolve from './configuracao-alerta-routing-resolve.service';

describe('ConfiguracaoAlerta routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ConfiguracaoAlertaService;
  let resultConfiguracaoAlerta: IConfiguracaoAlerta | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ConfiguracaoAlertaService);
    resultConfiguracaoAlerta = undefined;
  });

  describe('resolve', () => {
    it('should return IConfiguracaoAlerta returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        configuracaoAlertaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultConfiguracaoAlerta = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultConfiguracaoAlerta).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        configuracaoAlertaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultConfiguracaoAlerta = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultConfiguracaoAlerta).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IConfiguracaoAlerta>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        configuracaoAlertaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultConfiguracaoAlerta = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultConfiguracaoAlerta).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
