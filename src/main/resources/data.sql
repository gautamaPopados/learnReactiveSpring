INSERT INTO product (id, title, price) VALUES
    (gen_random_uuid(), 'Ноутбук', 100000.00),
    (gen_random_uuid(), 'Телефон', 80000.00),
    (gen_random_uuid(), 'Планшет', 50000.00),
    (gen_random_uuid(), 'Наушники', 1500.00),
    (gen_random_uuid(), 'Часы', 3000.00);

INSERT INTO customer (id, name) VALUES
    (gen_random_uuid(), 'Антон'),
    (gen_random_uuid(), 'Иван'),
    (gen_random_uuid(), 'Игорь'),
    (gen_random_uuid(), 'Александр'),
    (gen_random_uuid(), 'Борис');