-- Excluir tabelas existentes (se existirem)
DROP TABLE IF EXISTS itens_venda;
DROP TABLE IF EXISTS vendas;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS clientes;

CREATE TABLE clientes (
    codigo UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    limite_credito DOUBLE PRECISION,
    fechamento_fatura DATE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE produtos (
    codigo UUID PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE vendas (
    codigo UUID PRIMARY KEY,
    cliente_codigo UUID,
    data_venda TIMESTAMP NOT NULL,
    valor_total DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (cliente_codigo) REFERENCES clientes(codigo)
);

CREATE TABLE itens_venda (
    codigo UUID PRIMARY KEY,
    produto_codigo UUID,
    quantidade INTEGER NOT NULL,
    venda_codigo UUID,
    FOREIGN KEY (produto_codigo) REFERENCES produtos(codigo),
    FOREIGN KEY (venda_codigo) REFERENCES vendas(codigo)
);