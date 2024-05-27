import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cliente">
        <Translate contentKey="global.menu.entities.cliente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sensor">
        <Translate contentKey="global.menu.entities.sensor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/configuracao-alerta">
        <Translate contentKey="global.menu.entities.configuracaoAlerta" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dado-sensor">
        <Translate contentKey="global.menu.entities.dadoSensor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sys-user">
        <Translate contentKey="global.menu.entities.sysUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sys-role">
        <Translate contentKey="global.menu.entities.sysRole" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/profile">
        <Translate contentKey="global.menu.entities.profile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/feedback-form">
        <Translate contentKey="global.menu.entities.feedbackForm" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/feedback-response">
        <Translate contentKey="global.menu.entities.feedbackResponse" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification-settings">
        <Translate contentKey="global.menu.entities.notificationSettings" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report">
        <Translate contentKey="global.menu.entities.report" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/analytics">
        <Translate contentKey="global.menu.entities.analytics" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/integration-config">
        <Translate contentKey="global.menu.entities.integrationConfig" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/external-system">
        <Translate contentKey="global.menu.entities.externalSystem" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/audit-log">
        <Translate contentKey="global.menu.entities.auditLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/compliance-policy">
        <Translate contentKey="global.menu.entities.compliancePolicy" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
