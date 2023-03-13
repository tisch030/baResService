-- password is always test
INSERT IGNORE INTO credential(id, locked, password, username, password_expired, scheduled_locking_date)
VALUES ('c80f5551-e45b-46b1-b858-1dcc501a22b5', 0, '$2a$12$Eaci8vuMW0tLnsFFg0HaJuDxDok6h41uyaRsaK2tFUEWGhJsk2olq',
        'timur.schulz', 0, '2024-12-14'),
       ('a0fe7104-dcc5-4555-ad8c-a068d5a2b44b', 0, '$2a$12$Eaci8vuMW0tLnsFFg0HaJuDxDok6h41uyaRsaK2tFUEWGhJsk2olq',
        'max.mustermann', 0, '2024-12-14')
ON DUPLICATE KEY UPDATE id = VALUES(id);

INSERT IGNORE INTO person(id, credential_id, name, activities, modules)
VALUES ('c175b4cc-3ca9-43d1-8c56-755e9f256652', 'c80f5551-e45b-46b1-b858-1dcc501a22b5', 'Timur Schulz',
        'Vorlesung Datenstrukturen, Vorlesung BWL', 'BWL, Datenstrukturen'),
       ('02eb1947-048d-4440-8b58-fb9d255991ac', 'a0fe7104-dcc5-4555-ad8c-a068d5a2b44b', 'Max Mustermann',
        'Vorlesung Informatik, Vorlesung Analysis', 'Angewandte Informatik, Analysis')
ON DUPLICATE KEY UPDATE id = VALUES(id);

INSERT INTO local_authentication_settings (id, enabled, max_failed_attempts_per_username_and_ip_in_one_minute)
VALUES ('cfa189c6-4c5c-4ed0-8826-9042d36a683d', true, 5)
ON DUPLICATE KEY UPDATE id = VALUES(id);


INSERT INTO template_settings (id, contact_person_mail, contact_person_name, university_name, university_display_name,
                               university_url)
VALUES ('ff62994e-1acd-4c6d-8de6-330dfed8e88a', 'testMail@mail.com',
        'testContactPerson name', 'testName uni', 'testName uni display', 'http://www.testUni.com')
ON DUPLICATE KEY UPDATE id = VALUES(id);

INSERT INTO authentication_log_settings (id, anonymize_ip_addresses_that_are_older_than, delete_log_entries)
VALUES ('d2734fab-47ee-4247-afa7-e9256aa6b510', 5000, 1)
ON DUPLICATE KEY UPDATE id = VALUES(id);

INSERT INTO identity_provider (id, name, unique_identifier_attribute_at_idp, enabled, identity_provider_type, position,
                               button_label)
VALUES ('4927c96d-faed-4843-b4b2-5ad0223a3a20', 'companyxoidc', 'bPK2', 1, 'OPENID_CONNECT', 3, 'KeyCloak OIDC'),
       ('9646513b-5c4a-4af3-8afc-94b2e7779b78', 'bundid', 'urn:oid:1.3.6.1.4.1.25484.494450.3', 1, 'SAML', 1,
        'BundID Testumgebung'),
       ('f6a293fb-909a-4953-88af-f21ab2bfa001', 'companyxsaml', 'bPK2', 1, 'SAML', 2, 'KeyCloak SAML')
ON DUPLICATE KEY UPDATE id = VALUES(id);

INSERT INTO open_id_connect_settings (id, identity_provider_id, client_id, client_secret, scopes, use_discovery,
                                      issuer_url, authorization_url, jwks_url, token_url, user_info_url)
VALUES ('22cbf162-b3d6-45c4-aeac-ed953038d8b1', '4927c96d-faed-4843-b4b2-5ad0223a3a20',
        'companyxoidc', 'RaurwG0u56rWMWV3rF9aPE19RYGyTwBB', 'openid', 1,
        'http://127.0.0.1:8080/realms/master', null, null, null, null)
ON DUPLICATE KEY UPDATE id = VALUES(id);



INSERT INTO saml_service_provider_information (id, entity_id, idp_initiated_logout_endpoint)
VALUES ('c2996f48-9a12-4039-9537-57d931900c48', 'http://127.0.0.1:9500/.well-known/saml-metadata/',
        'http://127.0.0.1:9500/api/auth/logout')
ON DUPLICATE KEY UPDATE id = VALUES(id);


INSERT INTO saml_settings (id, issuer_url, identity_provider_id, stork_qaa_level)
VALUES ('bf097f9d-fc48-4d4c-8b3f-839986adeb36', 'http://127.0.0.1:8080/realms/master/protocol/saml/descriptor',
        'f6a293fb-909a-4953-88af-f21ab2bfa001', null),
       ('da28a92c-61c8-4c7d-b18e-5fac6a38d4a7', 'https://int.id.bund.de/idp',
        '9646513b-5c4a-4af3-8afc-94b2e7779b78', null)
ON DUPLICATE KEY UPDATE id = VALUES(id);

