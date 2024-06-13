INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, password) VALUES ('john_doe', 'password123');
INSERT INTO users (username, password) VALUES ('jane_smith', 'password123');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);

INSERT INTO workouts (date, user_id) VALUES ('2024-06-01', 1);
INSERT INTO workouts (date, user_id) VALUES ('2024-06-02', 1);

INSERT INTO exercises (name, sets, reps, weight, workout_id) VALUES ('Bench Press', 3, 10, 100.0, 1);
INSERT INTO exercises (name, sets, reps, weight, workout_id) VALUES ('Squat', 4, 8, 150.0, 1);
INSERT INTO exercises (name, sets, reps, weight, workout_id) VALUES ('Deadlift', 5, 5, 180.0, 2);