CREATE TABLE PRODUCTS (
    PRODUCT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    NAME VARCHAR(255) NOT NULL,
    IMAGE_URL VARCHAR(1024),
    DESCRIPTION LONG VARCHAR,
    PRICE DOUBLE NOT NULL,
    QUANTITY INTEGER NOT NULL,
    FAVOURITED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (PRODUCT_ID)
);

INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Thermostat', 'https://m.media-amazon.com/images/I/5118X+rWiOL.jpg', 'Control your home temperature remotely with this smart thermostat.', 129.99, 50, FALSE);
INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Security Camera', 'https://example.com/images/camera.jpg', 'HD security camera with motion detection and night vision.', 89.99, 30, FALSE);
INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Light Bulb', 'https://example.com/images/bulb.jpg', 'Color-changing smart light bulb that can be controlled via app.', 29.99, 100, FALSE);
INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Smart Speaker', 'https://example.com/images/speaker.jpg', 'Voice-controlled smart speaker with virtual assistant.', 79.99, 45, FALSE);
INSERT INTO PRODUCTS (NAME, IMAGE_URL, DESCRIPTION, PRICE, QUANTITY, FAVOURITED) VALUES ('Door Lock', 'https://example.com/images/lock.jpg', 'Smart door lock with fingerprint and PIN access.', 149.99, 20, FALSE); 