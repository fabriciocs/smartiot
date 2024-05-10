import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConfiguracaoAlertaComponent } from './list/configuracao-alerta.component';
import { ConfiguracaoAlertaDetailComponent } from './detail/configuracao-alerta-detail.component';
import { ConfiguracaoAlertaUpdateComponent } from './update/configuracao-alerta-update.component';
import ConfiguracaoAlertaResolve from './route/configuracao-alerta-routing-resolve.service';

const configuracaoAlertaRoute: Routes = [
  {
    path: '',
    component: ConfiguracaoAlertaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfiguracaoAlertaDetailComponent,
    resolve: {
      configuracaoAlerta: ConfiguracaoAlertaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfiguracaoAlertaUpdateComponent,
    resolve: {
      configuracaoAlerta: ConfiguracaoAlertaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfiguracaoAlertaUpdateComponent,
    resolve: {
      configuracaoAlerta: ConfiguracaoAlertaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default configuracaoAlertaRoute;
