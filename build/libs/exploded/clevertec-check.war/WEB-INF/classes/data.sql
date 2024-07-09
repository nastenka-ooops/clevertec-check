create table public.product
(
    id                bigserial
        constraint product_pk
            primary key,
    description       varchar(50)    default ''::character varying not null,
    price             numeric(10, 2) default 0.00                  not null,
    quantity_in_stock integer        default 0                     not null,
    wholesale_product boolean        default false                 not null
);

create table public.discount_card
(
    id     bigserial
        constraint discount_card_pk
            primary key,
    "number" integer  not null,
    amount smallint not null CHECK (amount >= 0 AND amount <= 100)
);

create table products_check
(
    id                        integer generated always as identity
        primary key,
    date                      date    not null,
    time                      time    not null,
    total_price               numeric not null,
    total_discount            numeric not null,
    total_price_with_discount numeric not null
);

create table check_items
(
    id          integer generated always as identity
        primary key,
    check_id    integer not null
        references products_check,
    product_id  integer not null
        references product,
    quantity    integer not null,
    total_price numeric not null,
    discount    numeric not null
);

INSERT INTO product (description, price, quantity_in_stock, wholesale_product) VALUES
                                                                              ('Milk', 1.07, 10, TRUE),
                                                                              ('Cream 400g', 2.71, 20, TRUE),
                                                                              ('Yogurt 400g', 2.10, 7, TRUE),
                                                                              ('Packed potatoes 1kg', 1.47, 30, FALSE),
                                                                              ('Packed cabbage 1kg', 1.19, 15, FALSE),
                                                                              ('Packed tomatoes 350g', 1.60, 50, FALSE),
                                                                              ('Packed apples 1kg', 2.78, 18, FALSE),
                                                                              ('Packed oranges 1kg', 3.20, 12, FALSE),
                                                                              ('Packed bananas 1kg', 1.10, 25, TRUE),
                                                                              ('Packed beef fillet 1kg', 12.80, 7, FALSE),
                                                                              ('Packed pork fillet 1kg', 8.52, 14, FALSE),
                                                                              ('Packed chicken breasts 1kg', 10.75, 18, FALSE),
                                                                              ('Baguette 360g', 1.30, 10, TRUE),
                                                                              ('Drinking water 1.5l', 0.80, 100, FALSE),
                                                                              ('Olive oil 500ml', 5.30, 16, FALSE),
                                                                              ('Sunflower oil 1l', 1.20, 12, FALSE),
                                                                              ('Chocolate Ritter sport 100g', 1.10, 50, TRUE),
                                                                              ('Paulaner 0.5l', 1.10, 100, FALSE),
                                                                              ('Whiskey Jim Beam 1l', 13.99, 30, FALSE),
                                                                              ('Whiskey Jack Daniels 1l', 17.19, 20, FALSE);

-- DML for discount_card table
INSERT INTO discount_card (number, amount) VALUES
                                               (1111, 3),
                                               (2222, 3),
                                               (3333, 4),
                                               (4444, 5);


