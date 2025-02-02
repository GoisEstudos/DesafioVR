-- Tabela clientes
CREATE TABLE clientes (
    codigo UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    limite_credito DOUBLE,
    fechamento_fatura DATE,
    status VARCHAR(50) NOT NULL
);

-- Tabela produtos
CREATE TABLE produtos (
    codigo UUID PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Tabela vendas
CREATE TABLE vendas (
    codigo UUID PRIMARY KEY,
    cliente_codigo UUID,
    data_venda TIMESTAMP NOT NULL,
    valor_total DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (cliente_codigo) REFERENCES clientes(codigo)
);

-- Tabela itens_venda
CREATE TABLE itens_venda (
    codigo UUID PRIMARY KEY,
    produto_codigo UUID,
    quantidade INTEGER NOT NULL,
    venda_codigo UUID,
    FOREIGN KEY (produto_codigo) REFERENCES produtos(codigo),
    FOREIGN KEY (venda_codigo) REFERENCES vendas(codigo)
);

-- Inserir clientes
INSERT INTO clientes (codigo, nome, limite_credito, fechamento_fatura, status)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'João Silva', 10000.00, '2023-10-31', 'Ativo'),
    ('223e4567-e89b-12d3-a456-426614174001', 'Maria Souza', 1500.00, '2023-11-15', 'Ativo');

-- Inserir produtos
INSERT INTO produtos (codigo, descricao, preco, status)
VALUES
    ('323e4567-e89b-12d3-a456-426614174002', 'Notebook', 3500.00, 'Ativo'),
    ('423e4567-e89b-12d3-a456-426614174003', 'Smartphone', 2000.00, 'Ativo'),
    ('523e4567-e89b-12d3-a456-426614174005', 'Monitor', 1200.00, 'Ativo'),
    ('623e4567-e89b-12d3-a456-426614174006', 'Teclado Mecânico', 450.00, 'Ativo'),
    ('723e4567-e89b-12d3-a456-426614174007', 'Mouse Gamer', 320.00, 'Ativo'),
    ('823e4567-e89b-12d3-a456-426614174008', 'Headset', 500.00, 'Ativo'),
    ('923e4567-e89b-12d3-a456-426614174009', 'Cadeira Gamer', 1500.00, 'Ativo');

-- Inserir vendas
INSERT INTO vendas (codigo, cliente_codigo, data_venda, valor_total, status)
VALUES
    ('523e4567-e89b-12d3-a456-426614174004', '123e4567-e89b-12d3-a456-426614174000', '2023-10-25 10:30:00', 7500.00, 'Ativo'),
    ('a23e4567-e89b-12d3-a456-426614174010', '123e4567-e89b-12d3-a456-426614174000', '2023-11-02 16:00:00', 9470.00, 'Ativo');

-- Inserir itens de venda
INSERT INTO itens_venda (codigo, produto_codigo, quantidade, venda_codigo)
VALUES
    ('623e4567-e89b-12d3-a456-426614174005', '323e4567-e89b-12d3-a456-426614174002', 1, '523e4567-e89b-12d3-a456-426614174004'),
    ('723e4567-e89b-12d3-a456-426614174006', '423e4567-e89b-12d3-a456-426614174003', 2, '523e4567-e89b-12d3-a456-426614174004'),
    ('b23e4567-e89b-12d3-a456-426614174011', '323e4567-e89b-12d3-a456-426614174002', 1, 'a23e4567-e89b-12d3-a456-426614174010'),
    ('c23e4567-e89b-12d3-a456-426614174012', '423e4567-e89b-12d3-a456-426614174003', 2, 'a23e4567-e89b-12d3-a456-426614174010'),
    ('d23e4567-e89b-12d3-a456-426614174013', '523e4567-e89b-12d3-a456-426614174005', 1, 'a23e4567-e89b-12d3-a456-426614174010'),
    ('e23e4567-e89b-12d3-a456-426614174014', '623e4567-e89b-12d3-a456-426614174006', 1, 'a23e4567-e89b-12d3-a456-426614174010'),
    ('f23e4567-e89b-12d3-a456-426614174015', '723e4567-e89b-12d3-a456-426614174007', 1, 'a23e4567-e89b-12d3-a456-426614174010');