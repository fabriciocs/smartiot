import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Cliente',
    route: '/cliente',
    translationKey: 'global.menu.entities.cliente',
  },
  {
    name: 'Sensor',
    route: '/sensor',
    translationKey: 'global.menu.entities.sensor',
  },
  {
    name: 'ConfiguracaoAlerta',
    route: '/configuracao-alerta',
    translationKey: 'global.menu.entities.configuracaoAlerta',
  },
  {
    name: 'DadoSensor',
    route: '/dado-sensor',
    translationKey: 'global.menu.entities.dadoSensor',
  },
];
