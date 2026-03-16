CREATE TABLE IF NOT EXISTS roles (
    id          BIGSERIAL PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id       BIGINT NOT NULL REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS clients (
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(100) NOT NULL,
    surname              VARCHAR(100) NOT NULL,
    birth_date           DATE NOT NULL,
    phone                VARCHAR(11) NOT NULL UNIQUE,
    email                VARCHAR(255) NOT NULL UNIQUE,
    driver_license       VARCHAR(50) NOT NULL UNIQUE,
    license_expiry_date  DATE NOT NULL,
    user_id              BIGINT NOT NULL UNIQUE REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS car_categories (
    id            BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description   TEXT NOT NULL,
    base_cost     NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS car_colors (
    id    BIGSERIAL PRIMARY KEY,
    color VARCHAR(50) NOT NULL UNIQUE,
    hex   VARCHAR(7) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS car_statuses (
    id     BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS cars (
    id            BIGSERIAL PRIMARY KEY,
    brand         VARCHAR(50) NOT NULL,
    model         VARCHAR(50) NOT NULL,
    year          INTEGER NOT NULL,
    category_id   BIGINT NOT NULL REFERENCES car_categories(id),
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    color_id      BIGINT NOT NULL REFERENCES car_colors(id),
    daily_cost    NUMERIC(10, 2) NOT NULL,
    car_status_id BIGINT NOT NULL REFERENCES car_statuses(id)
);

CREATE TABLE IF NOT EXISTS rental_statuses (
    id     BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rentals (
    id               BIGSERIAL PRIMARY KEY,
    client_id        BIGINT NOT NULL REFERENCES clients(id),
    car_id           BIGINT NOT NULL REFERENCES cars(id),
    start_date       TIMESTAMP NOT NULL DEFAULT NOW(),
    end_date         TIMESTAMP NOT NULL,
    total_amount     NUMERIC(10, 2) NOT NULL,
    rental_status_id BIGINT NOT NULL REFERENCES rental_statuses(id)
);

CREATE TABLE IF NOT EXISTS violation_types (
    id           BIGSERIAL PRIMARY KEY,
    type_name    VARCHAR(100) NOT NULL,
    default_fine NUMERIC(10, 2) NOT NULL,
    description  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS violations (
    id                BIGSERIAL PRIMARY KEY,
    rental_id         BIGINT NOT NULL REFERENCES rentals(id),
    violation_type_id BIGINT NOT NULL REFERENCES violation_types(id),
    description       TEXT NOT NULL,
    fine_amount       NUMERIC(10, 2) NOT NULL,
    violation_date    TIMESTAMP NOT NULL,
    is_paid           BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO roles (role_name) VALUES ('user'), ('admin')
ON CONFLICT (role_name) DO NOTHING;

INSERT INTO car_statuses (status) VALUES ('Свободна'), ('В аренде'), ('На обслуживании')
ON CONFLICT (status) DO NOTHING;

INSERT INTO rental_statuses (status) VALUES ('Активна'), ('Завершена'), ('Отменена')
ON CONFLICT (status) DO NOTHING;
