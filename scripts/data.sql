insert into user_role(name)
values ('ADMIN');
insert into user_role(name)
values ('USER');

insert into country (name)
values ('USA');
insert into country (name)
values ('India');

insert into state(abbreviation, code, name, country_id)
values ('TX', 'TX', 'Texas', 1);
insert into state(abbreviation, code, name, country_id)
values ('CA', 'CA', 'California', 1);
insert into state(abbreviation, code, name, country_id)
values ('NY', 'NY', 'New York', 1);


insert into category (name, img_url)
values ('Appetizers', '');
insert into category (name, img_url)
values ('Entrees', '');

insert into product(description, is_favorite, img_url, price, name, category_id)
VALUEs ('description 1', true, '', 10, 'Butter Chicken', 2);

insert into product(description, is_favorite, img_url, price, name, category_id)
VALUEs ('description 2', true, '', 16.90, 'Samosa', 1);

insert into product(description, is_favorite, img_url, price, name, category_id)
VALUEs ('description 3', true, '', 9.99, 'Mango Lassi', 1);

insert into product(description, is_favorite, img_url, price, name, category_id)
VALUEs ('description 4', true, '', 19.99, 'Mutton Curry', 2);

insert into product(description, is_favorite, img_url, price, name, category_id)
VALUEs ('description 5', true, '', 10, 'Tandoori murg', 1);

insert into carousel(description, height, img_url, priority, title, width)
VALUEs ('this is first slide', 500, '/img/carousel/pancake-2.jpg', 1, 'First slide', 400);
insert into carousel(description, height, img_url, priority, title, width)
VALUEs ('this is second slide', 500, '/img/carousel/pancake-3.jpg', 2, 'Second slide', 400);


