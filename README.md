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
- [Scheduling](#scheduling)
- [Mapping](#mapping)
- [Testing](#testing)
- [Front-end Design](#front-end-design)
- [Usage](#usage)
- [Contact](#contact)

## Introduction
PowerLog was developed as part of the [Spring Advanced Course @ SoftUni](https://softuni.bg/trainings/4532/spring-advanced-june-2024). Replace the URL with the correct link to the course page if needed. Here's how you . It provide a comprehensive strength training tracking system.

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
- **Security:** Spring Security, JWT authentication with refresh tokens
- **Other Libraries:** MapStruct, Lombok

## Installation
### Prerequisites
- JDK 17
- Gradle
- MySQL

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/hyuseinlesho/power-log.git
2. Set up the database and update the environment variables with your own data.
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

## Scheduling

- Scheduler to send a daily contact summary email to the admin with new contacts created.

![daily-contact-summary-email-example](https://github.com/user-attachments/assets/9795e442-af6a-4877-a38e-010461f1beec)

## Mapping

- Use MapStruct for DTO conversions.

## Testing

- Unit tests for service layer.
- Integration tests for REST controllers.
- Achieved 60% line coverage.

## Front-end Design

- Description of the front-end design.
- Use of Thymeleaf templates and Bootstrap for styling.

## Usage

Here is a demonstration video of PowerLog application:

- [PowerLog demo](https://youtu.be/bhbnsqqFx3Y)

### * Note that this video is from 2024/07/28

## Contact

- Author: Hyusein Lesho
- [Email](mailto:hl.dev.acc@gmail.com)
