-- SCHEMAS
CREATE SCHEMA IF NOT EXISTS transactions;

-- SEQUENCES
create sequence if not exists transactions.seq_account_id start 1 increment 1;
create sequence if not exists transactions.seq_operation_type_id start 1 increment 1;
create sequence if not exists transactions.seq_transaction_id start 1 increment 1;

-- TABLES
create table if not exists transactions.account
(
    id                  int8         not null,
    version             int8         not null,
    created_at          timestamp    not null,
    last_modified_at    timestamp    not null,
    document_number     int8         not null,
    primary key (id),
    constraint un_document_number unique (document_number)
);

create table if not exists transactions.operation_type
(
    id                  int4        not null,
    description         varchar(50) not null,
    primary key (id)
);

create table if not exists transactions.transaction
(
    id                  int8            not null,
    version             int8            not null,
    created_at          timestamp       not null,
    last_modified_at    timestamp       not null,
    account_id          int8            not null,
    operation_type_id   int4            not null,
    amount              numeric(19, 2)  not null,
    event_date          timestamp       not null,
    primary key (id),
    constraint account_fkey foreign key (account_id) references transactions.account (id),
    constraint operation_type_fkey foreign key (operation_type_id) references transactions.operation_type (id)
);