# SPEC-004: AlertService

## Background

The AlertService is designed to manage the creation, tracking, and notification of alerts within the Water Resource Management System. It handles various types of alerts, ensuring efficient and timely notifications to users about important events and conditions in the system. The service integrates seamlessly with other system components such as NotificationService.

## Requirements

### Business Requirements

1. **Alert Management**
   - Create and manage different types of alerts.
   - Provide descriptions and details for each alert.
   - Track the creation date and status of alerts.

### Technical Requirements

1. **Scalability**

   - Handle a large number of alerts efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with NotificationService.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "AlertService" {
[AlertController] --> [AlertService]
[AlertService] --> [AlertRepository]
[AlertService] --> [NotificationService]
}
@enduml
\```

**Database Schema:**

- **Alert Table:**
  - `id`: Long (Primary Key)
  - `alertType`: String
  - `description`: String
  - `createdDate`: ZonedDateTime

### API Endpoints

- **Alert Endpoints:**
  - `POST /alerts` - Create a new alert.
  - `GET /alerts/{id}` - Retrieve alert by ID.
  - `PUT /alerts/{id}` - Update alert details.
  - `DELETE /alerts/{id}` - Delete alert.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `Alert`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with NotificationService.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

# SPEC-009: ApiGateway

## Background

The ApiGateway is designed to manage routing, security, and load balancing for the microservices within the Water Resource Management System. It serves as a single entry point for clients, providing centralized authentication and authorization, routing requests to appropriate microservices, and ensuring efficient communication between services.

## Requirements

### Business Requirements

1. **Routing**

   - Route incoming requests to appropriate microservices.
   - Handle dynamic routing based on service discovery.

2. **Security**

   - Implement centralized authentication and authorization using JWT.
   - Ensure secure communication between clients and microservices.

3. **Load Balancing**
   - Distribute incoming requests evenly across microservice instances.
   - Ensure high availability and fault tolerance.

### Technical Requirements

1. **Scalability**

   - Handle a large volume of requests efficiently.

2. **Integration**
   - Integrate seamlessly with all microservices.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, Spring Cloud Gateway, JWT for authentication, Gradle for build.

**Frameworks:** Spring Security, Spring Cloud Netflix (Eureka for service discovery), Spring Cloud Gateway.

**Component Diagram:**
\```plantuml
@startuml
package "ApiGateway" {
[ApiGatewayController] --> [AuthenticationService]
[ApiGatewayController] --> [RoutingService]
[ApiGatewayController] --> [LoadBalancingService]
[ApiGatewayController] --> [ConsumerService]
[ApiGatewayController] --> [EnrollmentService]
[ApiGatewayController] --> [MeasurementService]
[ApiGatewayController] --> [AlertService]
[ApiGatewayController] --> [ReportingService]
[ApiGatewayController] --> [NotificationService]
[ApiGatewayController] --> [ManualEntryService]
[ApiGatewayController] --> [DataAggregationService]
}
@enduml
\```

### API Endpoints

- **Authentication Endpoints:**

  - `POST /authenticate` - Authenticate a user and return a JWT.

- **Routing Endpoints:**
  - Dynamic routing to all other microservices based on the request.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the security and routing settings.

2. **Develop Gateway Logic:**

   - Implement routing, authentication, and load balancing logic.

3. **Integrate Microservices:**

   - Ensure integration with all other microservices.

4. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Gateway Logic Development:** Implement routing, authentication, and load balancing logic.
3. **Microservice Integration:** Integrate with other microservices.
4. **Testing:** Unit, integration, and end-to-end testing.
5. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**

   - Measure response times, throughput, and resource utilization.

2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

# SmartIoT

This application was generated using JHipster 8.4.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v8.4.0](https://www.jhipster.tech/documentation-archive/v8.4.0).

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if omitted) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js](https://nodejs.org/): We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.config.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.config.ts](src/main/webapp/app/app.config.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import 'leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.config.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the SmartIoT application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker compose -f src/main/docker/jhipster-control-center.yml up
```

## Testing

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

### Spring Boot tests

To launch your application's tests, run:

```
./mvnw verify
```

## Others

### Code quality using Sonar

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off forced authentication redirect for UI in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar -Dsonar.login=admin -Dsonar.password=admin
```

Additionally, Instead of passing `sonar.password` and `sonar.login` as CLI arguments, these parameters can be configured from [sonar-project.properties](sonar-project.properties) as shown below:

```
sonar.login=admin
sonar.password=admin
```

For more information, refer to the [Code quality page][].

### Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 8.4.0 archive]: https://www.jhipster.tech/documentation-archive/v8.4.0
[Using JHipster in development]: https://www.jhipster.tech/documentation-archive/v8.4.0/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/documentation-archive/v8.4.0/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/documentation-archive/v8.4.0/production/
[Running tests page]: https://www.jhipster.tech/documentation-archive/v8.4.0/running-tests/
[Code quality page]: https://www.jhipster.tech/documentation-archive/v8.4.0/code-quality/
[Setting up Continuous Integration]: https://www.jhipster.tech/documentation-archive/v8.4.0/setting-up-ci/
[Node.js]: https://nodejs.org/
[NPM]: https://www.npmjs.com/
[Webpack]: https://webpack.github.io/
[BrowserSync]: https://www.browsersync.io/
[Jest]: https://facebook.github.io/jest/
[Leaflet]: https://leafletjs.com/
[DefinitelyTyped]: https://definitelytyped.org/
[Angular CLI]: https://cli.angular.io/

# SPEC-008: DataAggregationService

## Background

The DataAggregationService is designed to handle the consolidation and aggregation of data from various sources within the Water Resource Management System. This ensures comprehensive and accurate data analysis and reporting, integrating seamlessly with other system components such as ReportingService.

## Requirements

### Business Requirements

1. **Data Aggregation**
   - Consolidate data from multiple sources.
   - Provide dynamic and configurable data aggregation.
   - Ensure data accuracy and consistency.

### Technical Requirements

1. **Scalability**

   - Handle large volumes of data efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with ReportingService and other relevant services.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "DataAggregationService" {
[DataAggregationController] --> [DataAggregationService]
[DataAggregationService] --> [DataAggregationRepository]
[DataAggregationService] --> [ReportingService]
}
@enduml
\```

**Database Schema:**

- **AggregatedData Table:**
  - `id`: Long (Primary Key)
  - `dataType`: String
  - `aggregatedValue`: Double
  - `aggregationDate`: ZonedDateTime

### API Endpoints

- **AggregatedData Endpoints:**
  - `POST /aggregated-data` - Create a new aggregated data entry.
  - `GET /aggregated-data/{id}` - Retrieve aggregated data by ID.
  - `PUT /aggregated-data/{id}` - Update aggregated data details.
  - `DELETE /aggregated-data/{id}` - Delete aggregated data.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `AggregatedData`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with ReportingService and other relevant services.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

# SPEC-002: EnrollmentService

## Background

The EnrollmentService is designed to handle the enrollment processes within the Water Resource Management System. It manages the lifecycle of consumer enrollments, including registration dates and statuses. The service ensures efficient, scalable, and secure management of enrollment data, integrating seamlessly with other system components such as ConsumerService.

## Requirements

### Business Requirements

1. **Enrollment Management**
   - Register consumer enrollments with complete details.
   - Update and delete enrollment information.
   - Track enrollment statuses and history.

### Technical Requirements

1. **Scalability**

   - Handle a large number of enrollment records efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with ConsumerService.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "EnrollmentService" {
[EnrollmentController] --> [EnrollmentService]
[EnrollmentService] --> [EnrollmentRepository]
[EnrollmentService] --> [ConsumerService]
}
@enduml
\```

**Database Schema:**

- **Enrollment Table:**
  - `id`: Long (Primary Key)
  - `registrationDate`: LocalDate
  - `status`: String

### API Endpoints

- **Enrollment Endpoints:**
  - `POST /enrollments` - Create a new enrollment.
  - `GET /enrollments/{id}` - Retrieve enrollment by ID.
  - `PUT /enrollments/{id}` - Update enrollment details.
  - `DELETE /enrollments/{id}` - Delete enrollment.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `Enrollment`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with ConsumerService.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.
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

# SPEC-007: ManualEntryService

## Background

The ManualEntryService is designed to allow manual entry of data within the Water Resource Management System. This ensures data reliability and provides a fallback mechanism for data collection. The service integrates seamlessly with other system components such as MeasurementService.

## Requirements

### Business Requirements

1. **Manual Data Entry**
   - Allow manual data entry for various types of data.
   - Validate and process manually entered data.
   - Ensure data consistency with other services.

### Technical Requirements

1. **Scalability**

   - Handle a large volume of manual entries efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with MeasurementService and other relevant services.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "ManualEntryService" {
[ManualEntryController] --> [ManualEntryService]
[ManualEntryService] --> [ManualEntryRepository]
[ManualEntryService] --> [MeasurementService]
}
@enduml
\```

**Database Schema:**

- **ManualEntry Table:**
  - `id`: Long (Primary Key)
  - `entryType`: String
  - `entryData`: String
  - `entryDate`: ZonedDateTime

### API Endpoints

- **ManualEntry Endpoints:**
  - `POST /manual-entries` - Create a new manual entry.
  - `GET /manual-entries/{id}` - Retrieve manual entry by ID.
  - `PUT /manual-entries/{id}` - Update manual entry details.
  - `DELETE /manual-entries/{id}` - Delete manual entry.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `ManualEntry`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with MeasurementService and other relevant services.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

# SPEC-003: MeasurementService

## Background

The MeasurementService is designed to handle the recording and management of water usage measurements within the Water Resource Management System. It manages the lifecycle of measurement data, including the collection of data from meters, transmitters, concentrators, and repeaters. The service ensures efficient, scalable, and secure management of measurement data, integrating seamlessly with other system components such as ConsumerService.

## Requirements

### Business Requirements

1. **Measurement Management**
   - Record water usage with timestamp, amount, and unit.
   - Manage details of water meters.
   - Handle data transmission details from meters.
   - Manage devices that collect data from multiple transmitters.
   - Manage devices that extend the range of transmitters.

### Technical Requirements

1. **Scalability**

   - Handle a large number of measurement records efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with ConsumerService.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "MeasurementService" {
[MeasurementController] --> [MeasurementService]
[MeasurementService] --> [MeasurementRepository]
[MeasurementService] --> [MeterService]
[MeasurementService] --> [TransmitterService]
[MeasurementService] --> [ConcentratorService]
[MeasurementService] --> [RepeaterService]
}
@enduml
\```

**Database Schema:**

- **Measurement Table:**

  - `id`: Long (Primary Key)
  - `timestamp`: ZonedDateTime
  - `waterUsage`: Double
  - `unit`: String

- **Meter Table:**

  - `id`: Long (Primary Key)
  - `serialNumber`: String
  - `location`: String

- **Transmitter Table:**

  - `id`: Long (Primary Key)
  - `serialNumber`: String
  - `frequency`: Integer

- **Concentrator Table:**

  - `id`: Long (Primary Key)
  - `serialNumber`: String
  - `capacity`: Integer

- **Repeater Table:**
  - `id`: Long (Primary Key)
  - `serialNumber`: String
  - `range`: Integer

### API Endpoints

- **Measurement Endpoints:**

  - `POST /measurements` - Create a new measurement.
  - `GET /measurements/{id}` - Retrieve measurement by ID.
  - `PUT /measurements/{id}` - Update measurement details.
  - `DELETE /measurements/{id}` - Delete measurement.

- **Meter Endpoints:**

  - `POST /meters` - Create a new meter.
  - `GET /meters/{id}` - Retrieve meter by ID.
  - `PUT /meters/{id}` - Update meter details.
  - `DELETE /meters/{id}` - Delete meter.

- **Transmitter Endpoints:**

  - `POST /transmitters` - Create a new transmitter.
  - `GET /transmitters/{id}` - Retrieve transmitter by ID.
  - `PUT /transmitters/{id}` - Update transmitter details.
  - `DELETE /transmitters/{id}` - Delete transmitter.

- **Concentrator Endpoints:**

  - `POST /concentrators` - Create a new concentrator.
  - `GET /concentrators/{id}` - Retrieve concentrator by ID.
  - `PUT /concentrators/{id}` - Update concentrator details.
  - `DELETE /concentrators/{id}` - Delete concentrator.

- **Repeater Endpoints:**
  - `POST /repeaters` - Create a new repeater.
  - `GET /repeaters/{id}` - Retrieve repeater by ID.
  - `PUT /repeaters/{id}` - Update repeater details.
  - `DELETE /repeaters/{id}` - Delete repeater.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `Measurement`, `Meter`, `Transmitter`, `Concentrator`, and `Repeater`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with ConsumerService and other relevant services.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

# SPEC-006: NotificationService

## Background

The NotificationService is designed to handle the management and delivery of notifications within the Water Resource Management System. It ensures timely and efficient communication of alerts and other important messages to users, integrating seamlessly with other system components such as AlertService.

## Requirements

### Business Requirements

1. **Notification Management**
   - Create and manage notifications.
   - Deliver notifications via email, SMS, or other channels.
   - Track the status and history of notifications.

### Technical Requirements

1. **Scalability**

   - Handle a large volume of notifications efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with AlertService.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "NotificationService" {
[NotificationController] --> [NotificationService]
[NotificationService] --> [NotificationRepository]
[NotificationService] --> [AlertService]
}
@enduml
\```

**Database Schema:**

- **Notification Table:**
  - `id`: Long (Primary Key)
  - `notificationType`: String
  - `message`: String
  - `sentDate`: ZonedDateTime

### API Endpoints

- **Notification Endpoints:**
  - `POST /notifications` - Create a new notification.
  - `GET /notifications/{id}` - Retrieve notification by ID.
  - `PUT /notifications/{id}` - Update notification details.
  - `DELETE /notifications/{id}` - Delete notification.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `Notification`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with AlertService.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.

### Detalhamento do Sistema SmartIoT

O SmartIoT é uma plataforma robusta de gerenciamento de dispositivos da Internet das Coisas (IoT), desenvolvida para oferecer soluções integradas em tempo real para a monitoração e controle de sensores e atuadores em diversos setores. Utilizando tecnologia de ponta, o SmartIoT proporciona uma interface intuitiva que permite aos usuários configurar, monitorar e gerenciar dispositivos IoT com eficiência e segurança.

#### **Funcionalidades Principais:**

1. **Monitoramento em Tempo Real:**

   - O sistema oferece uma visualização ao vivo do status e dos dados coletados por sensores distribuídos em várias localizações. Isso inclui medições de temperatura, pressão, umidade, e outros dados relevantes para operações críticas.

2. **Gestão de Alertas e Notificações:**

   - Configurações personalizáveis de alertas permitem aos usuários definir limiares específicos para a ativação de notificações. Esses alertas ajudam na prevenção de falhas ou na identificação precoce de condições anormais.

3. **Análise e Relatórios Avançados:**

   - O sistema gera análises detalhadas e relatórios baseados nos dados coletados, facilitando a tomada de decisão baseada em informações precisas e históricos de desempenho.

4. **Controle de Acesso e Segurança:**

   - Com uma forte ênfase na segurança, o SmartIoT oferece controles de acesso detalhados e camadas de segurança para proteger a comunicação entre dispositivos e a plataforma.

5. **Integração com Outros Sistemas:**
   - A plataforma pode ser facilmente integrada com outros sistemas empresariais como ERP, CRM e sistemas de gestão de recursos, ampliando as funcionalidades e melhorando o fluxo de trabalho.

#### **Vantagens:**

- **Eficiência Operacional:**
  - Aumento da eficiência operacional através de automação e precisão nos dados coletados.
- **Redução de Custos:**

  - Diminuição de custos operacionais através da otimização de processos e manutenção preditiva.

- **Facilidade de Uso:**

  - Interface amigável e intuitiva que simplifica a configuração e o gerenciamento dos dispositivos IoT.

- **Escalabilidade:**
  - Capacidade de escalar facilmente à medida que novos dispositivos são adicionados, sem degradação na performance.

#### **Aplicações Potenciais:**

- **Indústria 4.0:**

  - Monitoramento e controle de máquinas industriais, garantindo operações ininterruptas e seguras.

- **Gestão Urbana:**

  - Aplicativos para cidades inteligentes, incluindo iluminação pública automatizada, monitoramento de tráfego e gestão de recursos energéticos.

- **Ambientes Residenciais:**
  - Soluções para casas inteligentes, como controle climático automatizado, sistemas de segurança e gerenciamento de energia.

O SmartIoT está posicionado para ser um pivô na transformação digital em diversos setores, oferecendo uma plataforma confiável e inovadora para o futuro da tecnologia IoT. Através de sua utilização, empresas e consumidores podem esperar uma melhoria significativa na qualidade e na eficiência de suas operações diárias.

# SPEC-005: ReportingService

## Background

The ReportingService is designed to handle the generation and management of reports within the Water Resource Management System. It provides real-time and historical data analysis, customizable reports, and dashboards to support decision-making processes. The service ensures efficient and scalable data aggregation and reporting, integrating seamlessly with other system components such as MeasurementService and AlertService.

## Requirements

### Business Requirements

1. **Data Analysis and Reporting**
   - Provide real-time and historical data analysis.
   - Generate customizable reports and dashboards.
   - Aggregate data from various sources.

### Technical Requirements

1. **Scalability**

   - Handle large volumes of data efficiently.

2. **Security**

   - Ensure data privacy and protection.
   - Implement JWT-based authentication.

3. **Integration**
   - Seamlessly integrate with MeasurementService, AlertService, and other relevant services.
   - Ensure robust API design for inter-service communication.

## Method

### Architecture Design

#### Microservice Architecture

**Technology Stack:** Java, Spring Boot, MySQL, JWT for authentication, Gradle for build.

**Frameworks:** Spring Data JPA, Spring Security, Spring Cloud.

**Component Diagram:**
\```plantuml
@startuml
package "ReportingService" {
[ReportingController] --> [ReportingService]
[ReportingService] --> [ReportingRepository]
[ReportingService] --> [MeasurementService]
[ReportingService] --> [AlertService]
}
@enduml
\```

**Database Schema:**

- **Report Table:**
  - `id`: Long (Primary Key)
  - `reportName`: String
  - `reportData`: String

### API Endpoints

- **Report Endpoints:**
  - `POST /reports` - Create a new report.
  - `GET /reports/{id}` - Retrieve report by ID.
  - `PUT /reports/{id}` - Update report details.
  - `DELETE /reports/{id}` - Delete report.

## Implementation

1. **Setup Project:**

   - Use JHipster to generate the base project structure.
   - Configure the database and security settings.

2. **Develop Entities and Repositories:**

   - Define JPA entities and repositories for `Report`.

3. **Implement Services:**

   - Implement business logic in service classes.

4. **Develop Controllers:**

   - Implement REST controllers for handling HTTP requests.

5. **Integration:**

   - Ensure integration with MeasurementService, AlertService, and other relevant services.

6. **Testing:**
   - Write unit and integration tests.
   - Perform end-to-end testing.

## Milestones

1. **Initial Setup:** Project structure and initial configuration.
2. **Entity Development:** Define entities and repositories.
3. **Service Implementation:** Business logic and services.
4. **Controller Development:** REST endpoints and controllers.
5. **Integration:** Integrate with other microservices.
6. **Testing:** Unit, integration, and end-to-end testing.
7. **Deployment:** Deploy the service in the production environment.

## Gathering Results

1. **Performance Metrics:**
   - Measure response times, throughput, and resource utilization.
2. **Functionality:**

   - Verify all endpoints and business logic meet requirements.

3. **Security:**

   - Conduct security audits and penetration testing.

4. **Scalability:**
   - Test the service under load to ensure it scales as expected.
