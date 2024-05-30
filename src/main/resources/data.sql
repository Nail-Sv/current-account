
-- Insert individual customers into the customer table

INSERT INTO customer (customer_id, name, surname, balance) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'John', 'Doe', '10');

INSERT INTO customer (customer_id, name, surname, balance) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Jane', 'Smith', '0');



INSERT INTO account (account_id, customer_id, balance) VALUES
    ('150e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000', '10');

INSERT INTO transaction (transaction_id, account_id, amount, created_at) VALUES
    ('110e8400-e29b-41d4-a716-446655440000', '150e8400-e29b-41d4-a716-446655440000', '10', now());
