-- 1) Usuários
INSERT INTO usuario_tb (email, nome, senha, nivel_do_usuario, criado_em) VALUES
('teste@teste.com', 'admin', '$2a$10$Sphs0R/paZmEJEYd9tmSZuBTXZkxQnj50naZWKkmFUphnb4aXtTV2', 'ADMIN', '2025-08-22 12:47:45'),
('gerente@gerente.com', 'gerente', '$2a$10$8MW58wq22MBK1iCqFPq4kudk2F7UIyrurinomRmpoLNK4jO0yuvJ.', 'GERENTE', '2025-08-23 18:41:22'),
('usuario@usuario.com', 'usuario', '$2a$10$RMXMMdrKm1bXu/VWgX9QhuCll1yLpzj6muVTxgCZ7.yXNrEqlPaRa', 'USUARIO', '2025-08-23 18:41:46'),
('estoque@estoque.com', 'estoque', '$2a$10$ZkqXUnOjTmNyzJVC0d1XF.RAA4Ccn83uzg5Z4gGR0nMZCEwGZO8dC', 'ESTOQUE', '2025-08-23 18:42:09');

-- 2) Clientes
INSERT INTO cliente_tb (cpf_ou_cnpj, nome_cliente, tipo_cliente) VALUES
('789.456.123-00', 'Jáo', 'FISICA'),
('78.945.612/3000-00', 'Claudinho', 'JURIDICA'),
('78.945.612/3000-00', 'Claudinho', 'JURIDICA'),
('78.945.612/3000-00', 'Claudinho', 'JURIDICA');

-- 3) Fornecedores
INSERT INTO fornecedor_tb (cpf_ou_cnpj, nome_fornecedor, tipo_fornecedor) VALUES
('789.456.123-00', 'Xinguilingui', 'FISICA'),
('78.945.612/3000-00', 'Xinguilingui', 'JURIDICA');

-- 4) Produtos
INSERT INTO produtos_tb (descricao, nome, preco, quantidade, valor_total, fornecedor_id) VALUES
('Marca LG', 'TV LG', 1000, 2, 2000, 1),
('Marca chines', 'celular xiaomi', 1000, 3, 3000, 2),
('Marca Samsung', 'Celular Samsung', 1500, 11, 16500, 2),
('Marca123', 'xiaomi123', 2000, 35, 70000, 1);

-- 5) Config Estoque
INSERT INTO config_estoque_tb (estoque_maximo, estoque_minimo, status, produto_id) VALUES
(30, 10, 2, 4),
(30, 10, 0, 1),
(15, 10, 1, 3),
(15, 1, 1, 2);

-- 6) Movimentações
INSERT INTO movimentacao_tb (data_hora, descricao, quantidade, tipo_de_movimentacao, produto_id, usuario_id) VALUES
('2025-08-23 19:30:47', '', 20, 'ENTRADA', 4, 1),
('2025-08-23 19:33:26', '', 1, 'ENTRADA', 4, 1),
('2025-08-23 19:34:58', 'Venda', 1, 'SAIDA', 4, 1),
('2025-08-25 23:36:21', 'Venda', 5, 'ENTRADA', 2, 1),
('2025-08-27 00:17:37', 'Venda realizada para o cliente: Jáo', 2, 'SAIDA', 2, 1),
('2025-08-29 01:19:01', 'compra', 10, 'ENTRADA', 4, 1),
('2025-08-29 01:38:13', 'Venda realizada para o cliente: Jáo', 5, 'SAIDA', 4, 1),
('2025-08-29 14:35:33', 'compra', 10, 'ENTRADA', 4, 3),
('2025-08-30 19:33:55', 'compra', 10, 'ENTRADA', 4, 3),
('2025-08-30 19:39:42', 'compra', 10, 'ENTRADA', 4, 3),
('2025-09-08 22:24:16', 'compra', 1, 'ENTRADA', 3, 1);

-- 7) Vendas
INSERT INTO vendas_tb (data_venda, quantidade, valor_total_vendido, cliente_id, movimentacao_id, produto_id, usuario_id) VALUES
('2025-08-27 00:17:37', 2, 2100, 1, 5, 2, 1),
('2025-08-29 01:38:13', 5, 10500, 1, 7, 4, 1);

-- 8) Sequence
INSERT INTO config_estoque_tb_seq (next_val) VALUES (101);
