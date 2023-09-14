\c dwh;

CREATE SCHEMA IF NOT EXISTS helmes;

CREATE TABLE helmes.sector (
    sector_id integer,
    sector_name varchar(255) NOT NULL,
    sector_parent_id integer,
    CONSTRAINT PK_sector_id REFERENCES helmes.sector(sector_id)
);

CREATE TABLE helmes.company (
    company_id BIGINT,
    company_name varchar(255),
    company_sector_id integer NOT NULL,
    company_terms boolean NOT NULL,
    CONSTRAINT PK_company_id PRIMARY KEY helmes.company(company_id)
        ON UPDATE cascade,
    FOREIGN KEY (company_sector_id) REFERENCES helmes.sector(sector_id)
);

CREATE TABLE helmes.user (
    user_id BIGINT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT PK_user_id PRIMARY KEY helmes.user(user_id),
    CONSTRAINT AK_user_id UNIQUE helmes.user(username)
);

CREATE TABLE helmes.user_company_reference (
    reference_id BIGINT,
    user_id BIGINT,
    company_id BIGINT,
    CONSTRAINT PK_reference_id PRIMARY KEY helmes.user_company_reference(reference_id),
    FOREIGN KEY (company_id) REFERENCES helmes.sector(sector_id),
    FOREIGN KEY (user_id) REFERENCES helmes.user(user_id)
)