CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    source_account_id UUID REFERENCES accounts(id),
    destination_account_id UUID REFERENCES accounts(id),
    amount DECIMAL(19, 4) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);