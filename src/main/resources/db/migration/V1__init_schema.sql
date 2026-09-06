CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    estado_kyc VARCHAR(50) DEFAULT 'PENDIENTE'
);

CREATE TABLE cuentas (
    id UUID PRIMARY KEY,
    usuario_id UUID REFERENCES usuarios(id),
    cvu_alias VARCHAR(100) UNIQUE NOT NULL,
    moneda VARCHAR(3) NOT NULL,
    version BIGINT DEFAULT 0
);