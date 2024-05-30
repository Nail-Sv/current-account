truncate table customer;
truncate table account;
truncate table transaction;

-- Insert individual customers into the customer table

INSERT INTO customer (customer_id, name, surname, balance) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'John', 'Doe', '0');

INSERT INTO customer (customer_id, name, surname, balance) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Jane', 'Smith', '0');
