--liquibase formatted sql

--changeset bynikolai: create tables for different type of users

CREATE TABLE "user"
(
    id              BIGSERIAL primary key ,
    id_chat         BIGINT,
    login           TEXT,
    contact_details TEXT
);

