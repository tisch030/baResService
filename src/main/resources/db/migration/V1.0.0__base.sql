create table credential
(
    id                     varchar(36)  not null primary key,
    locked                 bit          not null,
    password               varchar(255) null,
    username               varchar(255) null,
    password_expired       bit          null,
    scheduled_locking_date date         null,
    unique (username)
);

create table person
(
    id            varchar(36)  not null primary key,
    credential_id varchar(36)  not null,
    name          varchar(36)  null,
    activities    varchar(255) not null,
    modules       varchar(255) not null,
    foreign key (credential_id) references credential (id),
    unique (credential_id)
);

create table identity_provider
(
    id                                 varchar(36)  not null primary key,
    name                               varchar(255) not null,
    unique_identifier_attribute_at_idp varchar(255) not null,
    enabled                            bit          not null,
    identity_provider_type             varchar(255) not null,
    position                           int          not null,
    button_label                       varchar(255) not null,
    unique (position),
    unique (name)
);

create table credential_identity_provider_correlation
(
    id                   varchar(36)  not null primary key,
    correlation_value    varchar(255) not null,
    credential_id        varchar(36)  not null,
    identity_provider_id varchar(36)  not null,
    foreign key (credential_id) references credential (id),
    foreign key (identity_provider_id) references identity_provider (id)
);


create table open_id_connect_settings
(
    id                   varchar(36)  not null primary key,
    identity_provider_id varchar(36)  not null,
    client_id            varchar(255) not null,
    client_secret        varchar(255) not null,
    scopes               varchar(255) null,
    use_discovery        bit          not null,
    issuer_url           varchar(255) null,
    authorization_url    varchar(255) null,
    jwks_url             varchar(255) null,
    token_url            varchar(255) null,
    user_info_url        varchar(255) null,
    unique (identity_provider_id),
    foreign key (identity_provider_id) references identity_provider (id)
);



create table saml_settings
(
    id                   varchar(36)  not null primary key,
    issuer_url           varchar(255) not null,
    identity_provider_id varchar(36)  not null,
    stork_qaa_level      varchar(255) null,
    unique (identity_provider_id),
    foreign key (identity_provider_id) references identity_provider (id)
);



create table saml_service_provider_information
(
    id                            varchar(36)   not null primary key,
    entity_id                     varchar(255)  not null,
    idp_initiated_logout_endpoint varchar(255)  null,
    private_key                   varchar(5000) null,
    x509certificate               varchar(5000) null
);



create table jwt_key_store
(
    id          varchar(36)   not null primary key,
    not_after   datetime      not null,
    not_before  datetime      not null,
    private_key varchar(5000) not null,
    public_key  varchar(5000) not null
);



create table local_authentication_settings
(
    id                                                    varchar(36) not null primary key,
    enabled                                               bit         not null,
    max_failed_attempts_per_username_and_ip_in_one_minute int         not null
);



create table template_settings
(
    id                      varchar(36)  not null primary key,
    contact_person_mail     varchar(255) null,
    contact_person_name     varchar(255) null,
    university_name         varchar(255) null,
    university_display_name varchar(255) null,
    university_url          varchar(255) null
);



create table authentication_log_settings
(
    id                                         varchar(36) not null primary key,
    anonymize_ip_addresses_that_are_older_than bigint      null,
    delete_log_entries                         bit         not null
);


create table authentication_log
(
    id                       varchar(36)  not null primary key,
    authentication_operation varchar(255) null,
    ip_address               varchar(255) null,
    ip_address_anonymized    bit          not null,
    username                 varchar(255) null,
    person_id                varchar(255) null,
    created_at               datetime     not null default now()
);