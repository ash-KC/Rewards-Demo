-- Customers
INSERT INTO customers (name) VALUES ('Alice Johnson');
INSERT INTO customers (name) VALUES ('Bob Smith');
INSERT INTO customers (name) VALUES ('Charlie Davis');

-- Alice's transactions (customer_id = 1)
-- January
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 120.00, '2026-01-10');  -- 2x20 + 1x50 = 90 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 75.00, '2026-01-25');   -- 1x25 = 25 pts
-- February
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 200.00, '2026-02-15'); -- 2x100 + 1x50 = 250 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 55.50, '2026-02-22');  -- 1x5 = 5 pts
-- March
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 45.00, '2026-03-05');  -- 0 pts (below $50)
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (1, 105.00, '2026-03-18'); -- 2x5 + 1x50 = 60 pts

-- Bob's transactions (customer_id = 2)
-- January
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 50.00, '2026-01-12');  -- 0 pts (exactly $50, not over)
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 85.00, '2026-01-20');  -- 1x35 = 35 pts
-- February
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 150.00, '2026-02-10'); -- 2x50 + 1x50 = 150 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 100.00, '2026-02-28'); -- 1x50 = 50 pts
-- March
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 300.00, '2026-03-15'); -- 2x200 + 1x50 = 450 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (2, 30.00, '2026-03-22');  -- 0 pts (below $50)

-- Charlie's transactions (customer_id = 3)
-- January
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 90.00, '2026-01-05');  -- 1x40 = 40 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 110.00, '2026-01-28'); -- 2x10 + 1x50 = 70 pts
-- February
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 60.00, '2026-02-14');  -- 1x10 = 10 pts
-- March
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 175.00, '2026-03-01'); -- 2x75 + 1x50 = 200 pts
INSERT INTO transactions (customer_id, amount, transaction_date) VALUES (3, 25.00, '2026-03-20');  -- 0 pts (below $50)
