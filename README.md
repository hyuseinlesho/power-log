# PowerLog
PowerLog is a Spring MVC web application designed to help strength training enthusiasts track their workouts, monitor progress, and manage their training schedules.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Security](#security)
- [Validation and Error Handling](#validation-and-error-handling)
- [Internationalization (i18n)](#internationalization-i18n)
- [Contact Management](#contact-management)
- [Events and Scheduling](#events-and-scheduling)
- [Apache Kafka](#apache-kafka)
- [Mapping](#mapping)
- [Testing](#testing)
- [Front-end Design](#front-end-design)
- [Usage](#usage)
- [Contact](#contact)

## Introduction
PowerLog was developed as part of the [Spring Advanced Course @ SoftUni](https://softuni.bg/trainings/4532/spring-advanced-june-2024). It provide a comprehensive strength training tracking system.

## Features
- **Home, About, and Contact pages:** Accessible to unauthenticated users.
- **Authentication:** Login and register options.
- **Workout Management:**
  - Create, update, and delete workouts.
  - Use routines as templates for new workouts.
  - Create custom exercises.
  - View exercise progress with graphs using Chart.js.
- **Tools:**
  - Log daily weight and view progress graphs.
  - Store and view progress photos.
- **Account Management:**
  - View and edit profile information (email, password).
- **Admin Dashboard:**
  - View all users.
  - Access to app interface settings and usage statistics.
  - Role-based access control with user and admin roles.

## Technologies Used
- **Backend:** Spring Framework, Spring Boot
- **Frontend:** JavaScript, jQuery, AJAX, HTML, Bootstrap, Thymeleaf, Thymeleaf Layout Dialect, Chart.js, DataTables
- **Database:** MySQL, Hibernate (JPA provider)
- **Messaging** Apache Kafka
- **Storage Cloud** Cloudinary
- **Security:** Spring Security, JWT authentication with refresh tokens
- **Other Libraries:** MapStruct, Lombok

## Installation
### Prerequisites
- JDK 17+
- Gradle
- MySQL

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/hyuseinlesho/power-log.git
2. Set up environment variables.
   ```bash
   -DB_USERNAME=
   -DB_PASSWORD=
   -SECRET_KEY=
   -ACCESS_TOKEN_EXPIRATION=
   -REFRESH_TOKEN_EXPIRATION=
   -REMEMBER_ME_REFRESH_TOKEN_EXPIRATION=
   -ACCESS_TOKEN_COOKIE_MAX_AGE=
   -REFRESH_TOKEN_COOKIE_MAX_AGE=
   -REMEMBER_ME_REFRESH_TOKEN_COOKIE_MAX_AGE=
   -MAIL_USERNAME=
   -MAIL_PASSWORD=
   -ADMIN_EMAIL=
   -CLOUDINARY_CLOUD_NAME=
   -CLOUDINARY_API_KEY=
   -CLOUDINARY_API_SECRET=
3. (Optional) Set up used Cloudinary for storing progress pictures, install and run Apache Kafka server for messages from ContactService or uncomment this property in `application.yaml` file
   ![disable-kafka-auto-configuration-powerlog](https://github.com/user-attachments/assets/5e2dbc3e-ce16-49e5-8921-af3e4cbfc2bc)
4. Build and run the application:
   ```bash
   .\gradlew clean build
   .\gradlew bootRun

## API Endpoints

### Authentication Controller

- `GET /auth/login`
- `POST /auth/login`
- `GET /auth/register`
- `POST /auth/register`
- `POST /auth/logout`

### Contact Controller

- `GET /contact`
- `POST /contact`

### Exercise Controller

- `GET /exercises`
- `GET /exercises/graph`

### Home Controller

- `GET /`
- `GET /about`
- `GET /home`

### Progress Photo Controller

- `GET /progress-photos`
- `GET /progress-photos/upload`

### Routine Controller

- `GET /workouts/routines`
- `GET /workouts/routines/create`
- `POST /workouts/routines/create`
- `POST /workouts/routines/{id}/delete`
- `GET /workouts/routines/{id}/details`
- `GET /workouts/routines/{id}/edit`
- `POST /workouts/routines/{id}/edit`

### User Controller

- `GET /users/profile`

  ### Weight Log Controller

- `GET /weight-logs`
- `POST /weight-logs/graph`

### Workout Controller

- `GET /workouts/create`
- `POST /workouts/create`
- `GET /workouts/history`
- `GET /workouts/history/search`
- `POST /workouts/{id}/delete`
- `GET /workouts/{id}/details`
- `GET /workouts/{id}/edit`
- `POST /workouts/{id}/edit`
 
#### Other is used for AJAX requests

### Exercise Log REST Controller

- `GET /api/exercise-logs`

### Exercise REST Controller

- `POST /api/exercies/create`
- `PUT /api/exercies/{id}`
- `DELETE /api/exercies/{id}`

### User Profile REST Controller

- `POST /users/profile/change-email`
- `POST /users/profile/change-password`

### Weight Log REST Controller

- `GET /api/weight-logs`
- `POST /api/weight-logs/create`
- `PUT /api/weight-logs/{id}`
- `DELETE /api/weight-logs/{id}`

## Database Schema

- The database schema for the PowerLog application is designed to support the functionalities required for tracking strength training workouts, user authentication, and role-based access control. Below is a detailed description of each table and its relationships.

### ER Diagram

![er-diagram](https://github.com/user-attachments/assets/ddd8c227-f690-4b6f-8636-357a24ee10b7)

- This schema ensures that each user can have multiple roles, workouts, exercises, routines, weight logs, and progress photos, providing a comprehensive structure for the application's functionality.

## Security

- Custom JWT authentication with refresh tokens based on [this tutorial](https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac).
- Role-based authentication with user and admin roles.
- Security features including CSRF protection and password hashing.

## Validation and Error Handling

- Client-side and server-side validation.

### Examples of validation messages.
![contact-validations](https://github.com/user-attachments/assets/ae4010c5-c258-4eee-bb70-1b8fd713c38a)
![login-validation](https://github.com/user-attachments/assets/72818760-b42e-4fcb-8681-8e496bb96daf)
![client-side-validation](https://github.com/user-attachments/assets/0bad0045-271b-4b8c-b6e1-06741db41d55)

## Internationalization (i18n)

- Supported languages: English, Bulgarian, and German.

![change-language-dropdown](https://github.com/user-attachments/assets/f09155c6-495c-4087-a402-a1582209c837)
![home-en](https://github.com/user-attachments/assets/2379010f-a7f5-4264-b5d2-4b087410b499)
![home-bg](https://github.com/user-attachments/assets/ebfe21cc-55b0-458c-a743-862730e214c3)

## Contact Management

Utilizes a separate REST service, [ContactService](https://github.com/hyuseinlesho/contact-service), for saving new contacts and fetching contacts based on date.

## Events and Scheduling

- Event which send welcome email after a new user registers.

![welcome-email-example](https://github.com/user-attachments/assets/34d54c13-33e8-4928-bbee-a892b75a42bb)

- Event which send notification email after users create now contact.
- Consume new messages from ContactService producer through Kafka

![new-contact-notification-email](https://github.com/user-attachments/assets/11bc120a-8151-446e-8e8a-a2e69fcb3205)

- Scheduler to send a weekly contact summary email to the admin with new contacts created.

![weekly-contact-summary-email](https://github.com/user-attachments/assets/4b01523a-4916-4f22-aaa2-bea26ae91b9d)

## Apache Kafka

### Setup and Configuration

- Ensure Apache Kafka is installed and running to test it locally.

### ContactConsumer

- Consumes messages from the Kafka topic `contact-topic` and ContactService as producer.
- Parses the consumed message and processes the contact data.
- Configured consumer settings, including bootstrap servers, key and value deserializers, and group ID in `application.yaml` file.

### Process Contact

- Processes the contact data after consumption from Kafka.
- Sends notification email to the admin when a new contact is created.

### Note

- Ensure that Kafka server is installed and running when testing the integration.

## Mapping

- Use MapStruct for DTO conversions.

## Testing

- Unit tests for service layer.
- Integration tests for REST controllers.
- Achieved 60% line coverage.

## Front-end Design

The PowerLog application uses modern web technologies to ensure a responsive and user-friendly interface. Key technologies include:

### Thymeleaf Templates with Layout Dialect

- **Thymeleaf**: Used as the template engine to generate dynamic HTML content on the server side.
- **Layout Dialect**: Provides tools to create reusable layouts, components, and fragments, ensuring consistency across the application.
- **Security Integration**: Manages user authentication and authorization within templates using Spring Security.

### Bootstrap for Styling

- **Bootstrap 5**: Utilized for responsive CSS styling and layout design, offering a library of pre-designed components like forms, buttons, and navigation bars.
- **Modals**: Used for creating dialog boxes and forms for user interactions without navigating away from the current page.

### JavaScript and AJAX for Interactivity

- **JavaScript**: Enhances user interactions and provides dynamic functionality on the client side, including form validation and event handling.
- **AJAX**: Enables asynchronous communication with the server, allowing data to be sent and received without reloading the page, thus improving user experience.

### Chart.js for Data Visualization

- **Chart.js**: Utilized for creating responsive and interactive charts, helping to visualize workout progress and other data in a clear and engaging manner.

### DataTables for Tabular Data

- **DataTables**: Provides advanced interaction controls for HTML tables, such as sorting, filtering, and pagination, making it easier to manage and analyze tabular data.

## Usage

Here is a demonstration video of PowerLog application:

- [PowerLog demo](https://youtu.be/bhbnsqqFx3Y)

### * Note that this video is from 2024/07/28

## Contact

- Author: Hyusein Lesho
- [Email](mailto:hl.dev.acc@gmail.com)
