--creo cuentas de prueba para testear
INSERT INTO users (id, email, password_hash, kyc_status)
VALUES ('11111111-1111-1111-1111-111111111111', 'facundo@email.com', 'hash123', 'APPROVED');
INSERT INTO users (id, email, password_hash, kyc_status)
VALUES ('22222222-2222-2222-2222-222222222222', 'agucho@email.com', 'hash456', 'APPROVED');

--lleno de datos, la primera tiene saldo inicial de 5000 y la segunda tiene saldo 0
INSERT INTO accounts (id, user_id, alias_cvu, currency, balance, version)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'facu.wallet', 'ARS', 5000.00, 0);
INSERT INTO accounts (id, user_id, alias_cvu, currency, balance, version)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 'agucho.wallet', 'ARS', 0.00, 0);