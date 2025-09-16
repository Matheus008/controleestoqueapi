CREATE TABLE cliente_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cpf_ou_cnpj VARCHAR(255),
  nome_cliente VARCHAR(255),
  tipo_cliente VARCHAR(20) CHECK (tipo_cliente IN ('FISICA', 'JURIDICA'))
);

CREATE TABLE config_estoque_tb (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  estoque_maximo INT,
  estoque_minimo INT,
  status SMALLINT,
  produto_id BIGINT,
  CONSTRAINT UK_config_produto UNIQUE (produto_id)
);

CREATE TABLE config_estoque_tb_seq (
  next_val BIGINT
);

CREATE TABLE fornecedor_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cpf_ou_cnpj VARCHAR(255),
  nome_fornecedor VARCHAR(255),
  tipo_fornecedor VARCHAR(100)
);

CREATE TABLE movimentacao_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  data_hora TIMESTAMP,
  descricao VARCHAR(255),
  quantidade INT,
  tipo_de_movimentacao VARCHAR(20) CHECK (tipo_de_movimentacao IN ('ENTRADA','SAIDA')),
  produto_id INT,
  usuario_id INT
);

CREATE TABLE produtos_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  descricao VARCHAR(255),
  nome VARCHAR(255),
  preco DOUBLE PRECISION,
  quantidade INT,
  valor_total DOUBLE PRECISION,
  fornecedor_id INT
);

CREATE TABLE usuario_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE,
  nome VARCHAR(255),
  senha VARCHAR(255),
  nivel_do_usuario VARCHAR(100) NOT NULL,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vendas_tb (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  data_venda TIMESTAMP,
  quantidade INT,
  valor_total_vendido DOUBLE PRECISION,
  cliente_id INT,
  movimentacao_id INT UNIQUE,
  produto_id INT,
  usuario_id INT
);

-- Foreign keys
ALTER TABLE movimentacao_tb
  ADD CONSTRAINT fk_mov_produto FOREIGN KEY (produto_id) REFERENCES produtos_tb(id);
ALTER TABLE movimentacao_tb
  ADD CONSTRAINT fk_mov_usuario FOREIGN KEY (usuario_id) REFERENCES usuario_tb(id);

ALTER TABLE produtos_tb
  ADD CONSTRAINT fk_prod_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor_tb(id);

ALTER TABLE vendas_tb
  ADD CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES cliente_tb(id);
ALTER TABLE vendas_tb
  ADD CONSTRAINT fk_venda_mov FOREIGN KEY (movimentacao_id) REFERENCES movimentacao_tb(id);
ALTER TABLE vendas_tb
  ADD CONSTRAINT fk_venda_prod FOREIGN KEY (produto_id) REFERENCES produtos_tb(id);
ALTER TABLE vendas_tb
  ADD CONSTRAINT fk_venda_usuario FOREIGN KEY (usuario_id) REFERENCES usuario_tb(id);
