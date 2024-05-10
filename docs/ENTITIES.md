Para fornecer um detalhamento das entidades gerenciadas pelo sistema SmartIoT, vamos explorar as principais entidades, descrevendo seus campos e funcionalidades em um contexto de monitoramento e gestão de dispositivos IoT. As entidades que discutiremos incluem `Cliente`, `Sensor`, `DadoSensor`, `Configuração de Alerta`, e `Autoridade`.

### Detalhamento das Entidades do Sistema SmartIoT

#### 1. **Cliente**

- **ID:** Identificador único para cada cliente no sistema.
- **Nome:** Nome do cliente ou da organização.
- **Email:** Endereço de email para contato.
- **Sensores:** Lista de sensores associados ao cliente. Cada cliente pode ter vários sensores registrados sob sua gestão.

#### 2. **Sensor**

- **ID:** Identificador único do sensor dentro do sistema.
- **Nome:** Nome descritivo do sensor, facilitando sua identificação.
- **Tipo:** Tipo do sensor, como temperatura, umidade, pressão, etc.
- **Configuração:** Configurações específicas aplicáveis ao sensor, como intervalos de medição.
- **Configuração de Alertas:** Conjunto de regras de alerta associadas ao sensor para monitoramento de condições específicas.
- **Dados dos Sensores:** Dados históricos gerados pelo sensor.
- **Cliente:** Referência ao cliente ao qual o sensor está associado.

#### 3. **DadoSensor**

- **ID:** Identificador único do dado coletado.
- **Dados:** Os dados brutos coletados pelo sensor, como valores de temperatura, umidade, etc.
- **Timestamp:** Carimbo de data/hora de quando os dados foram coletados.
- **Sensor:** Referência ao sensor que coletou os dados.

#### 4. **Configuração de Alerta**

- **ID:** Identificador único da configuração de alerta.
- **Limite:** Valor limite que, se excedido, aciona o alerta.
- **Email:** Email para onde as notificações de alerta devem ser enviadas.
- **Sensor:** Sensor ao qual a configuração de alerta está aplicada.

#### 5. **Autoridade (Authority)**

- **ID:** Identificador único da autoridade.
- **Nome:** Nome da autoridade, que define o nível de acesso ou grupo de controle dentro do sistema, como 'admin', 'user', etc.

### Funcionalidades Associadas às Entidades

- **Gestão de Clientes:** Permite registrar e manter o perfil de clientes que utilizam o sistema, facilitando a administração de suas contas e dispositivos associados.
- **Monitoramento de Sensores:** Oferece uma visão detalhada do funcionamento dos sensores e seu desempenho em tempo real, permitindo ajustes nas configurações conforme necessário.
- **Análise de Dados:** Os dados coletados pelos sensores são analisados para fornecer insights operacionais e ajudar na tomada de decisões baseada em dados concretos.
- **Gerenciamento de Alertas:** Configurações proativas que permitem aos usuários definir parâmetros específicos para o acionamento de alertas, garantindo ação rápida em situações críticas.
- **Controle de Acesso:** Definição de níveis de acesso e autorizações para diversos usuários e grupos dentro do sistema, garantindo a segurança e a integridade dos dados.

Cada entidade e campo dentro do SmartIoT é projetado para facilitar a gestão eficiente de uma rede complexa de dispositivos IoT, maximizando a utilidade e minimizando os riscos operacionais.
