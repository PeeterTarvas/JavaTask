\c dwh;

CREATE SCHEMA IF NOT EXISTS helmes;

CREATE TABLE helmes.sector (
    sector_id integer PRIMARY KEY,
    sector_name varchar(255) NOT NULL,
    sector_parent_id integer
);

CREATE TABLE helmes.company (
    company_name varchar(255) PRIMARY KEY,
    company_sector_id integer NOT NULL,
    company_terms boolean NOT NULL,
    FOREIGN KEY (company_sector_id) REFERENCES helmes.sector(sector_id)
);