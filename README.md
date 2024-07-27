# PowerLog
PowerLog is a Spring MVC web application designed to help strength training enthusiasts track their workouts, monitor progress, and manage their training schedules.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Security](#security)
- [Validation and Error Handling](#validation-and-error-handling)
- [Internationalization (i18n)](#internationalization-i18n)
- [Scheduling](#scheduling)
- [Mapping](#mapping)
- [Testing](#testing)
- [Front-end Design](#front-end-design)
- [Source Control](#source-control)
- [Online Project Defense](#online-project-defense)
- [Contact](#contact)

## Introduction
PowerLog was developed as part of the Spring Advanced Course @ SoftUni. It provide a comprehensive strength training tracking system.

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
  - View and edit profile information (username, email, password).
- **Admin Dashboard:**
  - View all users.
  - Access to app interface settings and usage statistics.
  - Role-based access control with user and admin roles.

## Technologies Used
- **Backend:** Spring Framework, Spring Boot
- **Frontend:** JavaScript, jQuery, AJAX, HTML, Bootstrap, Thymeleaf, Thymeleaf Layout Dialect, Chart.js, DataTables
- **Database:** MySQL, Hibernate (JPA provider)
- **Security:** Spring Security, JWT authentication with refresh tokens
- **Other Libraries:** MapStruct, Lombok

## Installation
### Prerequisites
- JDK 11+
- Gradle
- MySQL

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/hyuseinlesho/power-log.git
2. Set up the database and update the environtment variables with your own data.
3. Build and run the application:
   ```bash
   gradle clean build
   gradle bootRun

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
 
#### Other is for AJAX requests

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

- Description of the database schema.
- ER diagram (if available).

## Security

- Custom JWT authentication with refresh tokens based on [this tutorial](https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac).
- Role-based authentication with user and admin roles.
- Security features including CSRF protection and password hashing.

## Validation and Error Handling

- Client-side and server-side validation.
- Examples of validation messages.
- Error handling strategy.

## Internationalization (i18n)

- Supported languages: English, Bulgarian, and German.
- Instructions on how to switch languages.

## Scheduling

- Scheduler to send a daily contact summary email to the admin with new contacts created.

## Mapping

- Usage of MapStruct for DTO conversions.

## Testing

- Unit tests for service layer.
- Integration tests for REST controllers.
- Achieved 60% line coverage.

## Front-end Design

- Description of the front-end design.
- Use of Thymeleaf templates and Bootstrap for styling.

## Usage

- Instructions on how to use the application.
- Provide screenshots or GIFs of key pages and functionalities.

## Contact

- Author: Hyusein Lesho
- [Email](mailto:hl.dev.acc@gmail.com)
