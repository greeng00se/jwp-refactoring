ALTER TABLE product
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE product
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE order_table
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE order_table
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE table_group
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE table_group
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE menu_group
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE menu_group
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE menu_product
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE menu_product
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE menu
    ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE menu
    ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
