-- Users table
CREATE TABLE users (
    user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    is_staff BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id)
);

-- Access Logs table
CREATE TABLE access_logs (
    log_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    login_time TIMESTAMP NOT NULL,
    logout_time TIMESTAMP,
    ip_address VARCHAR(45),
    PRIMARY KEY (log_id)
);

-- Products table
CREATE TABLE PRODUCTS (
    PRODUCT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    NAME VARCHAR(255) NOT NULL,
    IMAGE_URL VARCHAR(1024),
    DESCRIPTION LONG VARCHAR,
    PRICE DOUBLE NOT NULL,
    QUANTITY INTEGER NOT NULL,
    FAVOURITED BOOLEAN DEFAULT FALSE,
    PRODUCT_TYPE VARCHAR(50) NOT NULL,
    PRIMARY KEY (PRODUCT_ID)
);

-- Sample product data
INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED, PRODUCT_TYPE) 
VALUES ('Smart Thermostat', 'https://m.media-amazon.com/images/I/5118X+rWiOL.jpg', 'Control your home temperature remotely with this smart thermostat.', 129.99, 50, FALSE, 'Climate Control');

INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED, PRODUCT_TYPE) 
VALUES ('Security Camera', 'https://thespystore.com.au/cdn/shop/products/1_91e20459-f870-4d6f-b9a9-afe34a9497ba.jpg', 'HD security camera with motion detection and night vision.', 89.99, 30, FALSE, 'Security');

INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED, PRODUCT_TYPE) 
VALUES ('Smart Light Bulb', 'https://cdn11.bigcommerce.com/s-apgyqyq0gk/images/stencil/500x500/products/3434/4856/laser-10w-smart-white-bulb-b22-2295__40336.1725334359.jpg?c=1', 'Color-changing smart light bulb that can be controlled via app.', 29.99, 100, FALSE, 'Lighting');

INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED, PRODUCT_TYPE) 
VALUES ('Smart Speaker', 'https://i5.walmartimages.com/asr/92a6e18c-c8e9-471f-bc92-f40cfa38f40b.5c900739f23c354c54e29fb7eb89b0ac.jpeg', 'Voice-controlled smart speaker with virtual assistant.', 79.99, 45, FALSE, 'Entertainment');

INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED, PRODUCT_TYPE) 
VALUES ('Door Lock', 'https://www.jbhifi.com.au/cdn/shop/products/608109-Product-0-I-638004961640652731.jpg', 'Smart door lock with fingerprint and PIN access.', 149.99, 20, FALSE, 'Security');

-- Orders table
CREATE TABLE orders (
    order_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (order_id)
);

-- Order Items table
CREATE TABLE order_items (
    order_item_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(PRODUCT_ID)
);

-- Cart Items table
CREATE TABLE cart_items (
    cart_item_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (cart_item_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
); 