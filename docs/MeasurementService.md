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
