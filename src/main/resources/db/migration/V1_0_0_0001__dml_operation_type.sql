INSERT INTO transactions.operation_type VALUES (1, 'COMPRA A VISTA');
INSERT INTO transactions.operation_type VALUES (2, 'COMPRA PARCELADA');
INSERT INTO transactions.operation_type VALUES (3, 'SAQUE');
INSERT INTO transactions.operation_type VALUES (4, 'PAGAMENTO');

ALTER SEQUENCE transactions.seq_operation_type_id RESTART WITH 5;