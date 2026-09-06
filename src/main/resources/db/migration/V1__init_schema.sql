CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    kyc_status VARCHAR(50) DEFAULT 'PENDING'
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    alias_cvu VARCHAR(100) UNIQUE NOT NULL,
    currency VARCHAR(3) NOT NULL,
    version BIGINT DEFAULT 0
);