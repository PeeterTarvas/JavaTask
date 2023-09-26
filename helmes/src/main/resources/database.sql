CREATE SCHEMA IF NOT EXISTS helmes;

CREATE TABLE helmes.sector (
    sector_id integer PRIMARY KEY,
    sector_name varchar(255) NOT NULL,
    sector_parent_id integer
);

CREATE TABLE helmes.company (
    company_id BIGSERIAL PRIMARY KEY,
    company_name varchar(255),
    company_sector_id integer NOT NULL,
    company_terms boolean NOT NULL,
    FOREIGN KEY (company_sector_id) REFERENCES helmes.sector(sector_id)  ON UPDATE CASCADE,
    CONSTRAINT AK_company_name UNIQUE (company_name)

);

CREATE TABLE helmes.user (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT AK_user_name UNIQUE (username)
);

CREATE TABLE helmes.user_company_reference (
    reference_id BIGSERIAL  PRIMARY KEY,
    user_id BIGINT,
    company_id BIGINT,
    FOREIGN KEY (company_id) REFERENCES helmes.sector(sector_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES helmes.user(user_id)  ON UPDATE CASCADE ON DELETE CASCADE
)