
CREATE TABLE IF NOT EXISTS product (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        title VARCHAR(255) NOT NULL,
                                        price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
                                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                         name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
                                      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                      customer_id UUID NOT NULL REFERENCES customer(id) ON DELETE CASCADE,
                                      product_id UUID NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                                      quantity INTEGER NOT NULL
);