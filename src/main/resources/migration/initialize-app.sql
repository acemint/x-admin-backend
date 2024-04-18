INSERT INTO xa_clinic(id, name, code, subscription_valid_from, subscription_valid_to)
VALUES (GEN_RANDOM_UUID(), 'x-admin-app', CONCAT('CLC-', nextval('clinic_sequence')), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '100' YEAR);

INSERT INTO xa_member(id, clinic_username, code, first_name, last_name, email_address, "password", "role", status, clinic_id)
VALUES (GEN_RANDOM_UUID(), 'steven.kristian@developer', CONCAT('MBR-', nextval('member_sequence')), 'Steven', 'Kristian', 'steven@gmail.com', null, 'ROLE_DEVELOPER', 'ACTIVE', (SELECT id FROM xa_clinic WHERE name = 'x-admin-app'))