INSERT INTO public.roles (id, name) VALUES (DEFAULT, 'ROLE_USER');
INSERT INTO public.roles (id, name) VALUES (DEFAULT, 'ROLE_ADMIN');
INSERT INTO public.roles (id, name) VALUES (DEFAULT, 'ROLE_DEV');

INSERT INTO public.users (id, created_at, updated_at, email, name, password, username) VALUES (DEFAULT, null, null, 'asd@asd.com', 'readdeo', '$2a$10$0Bxh9jqp2BhEzkCiGOWtTeAUg7/YZ9oPw8y5P/3khYG..2RQAkmAu', 'readdeo');
INSERT INTO public.user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO public.user_roles(user_id, role_id) VALUES (1, 2);
INSERT INTO public.user_roles(user_id, role_id) VALUES (1, 3);

INSERT INTO public.accounts (id, name, currency, description, user_id, is_credit) VALUES (DEFAULT, 'name1', 'HUF', 'Rafi', 1, false);
INSERT INTO public.accounts (id, name, currency, description, user_id, is_credit) VALUES (DEFAULT, 'name2', 'HUF', 'OTP', 1, false);

INSERT INTO public.users (id, created_at, updated_at, email, name, password, username) VALUES (DEFAULT, null, null, 'asd2@asd.com', 'readdeo2', '$2a$10$0Bxh9jqp2BhEzkCiGOWtTeAUg7/YZ9oPw8y5P/3khYG..2RQAkmAu', 'readdeo2');
INSERT INTO public.user_roles(user_id, role_id) VALUES (2, 1);

INSERT INTO public.accounts (id, name, currency, description, user_id, is_credit) VALUES (DEFAULT, 'name3', 'HUF', 'Raiffeisen', 2, false);
INSERT INTO public.accounts (id, name, currency, description, user_id, is_credit) VALUES (DEFAULT, 'name4', 'HUF', 'OTP', 2, false);
INSERT INTO public.accounts (id, name, currency, description, user_id, is_credit) VALUES (DEFAULT, 'name5', 'HUF', 'CreditRaiffeisen', 2, true);

INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Food', null, 1);
INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Grocery', 1, 1);
INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Housing', null, 1);
INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Rent', 3, 1);
INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Car', null, 2);
INSERT INTO public.category (id, color, name, parent_id, user_id) VALUES (DEFAULT, null, 'Gift', 5, 2);

INSERT INTO public.transaction (id, amount, date_time, description, source_account_id, main_category_id, sub_category_id, destination_account_id, user_id) VALUES ('1', '-1000', '2021-03-17 18:16:31.000000', 'sajt', 3, 1, 2, null, 1);
INSERT INTO public.transaction (id, amount, date_time, description, source_account_id, main_category_id, sub_category_id, destination_account_id, user_id) VALUES ('2', '-1000', '2021-03-17 18:16:31.000000', 'sajt', 3, 1, 2, null, 1);
INSERT INTO public.transaction (id, amount, date_time, description, source_account_id, main_category_id, sub_category_id, destination_account_id, user_id) VALUES ('3', '-1000', '2021-03-17 18:16:31.000000', 'sajt', 3, 1, 2, null, 1);
INSERT INTO public.transaction (id, amount, date_time, description, source_account_id, main_category_id, sub_category_id, destination_account_id, user_id) VALUES ('4', '-1000', '2021-03-17 18:16:31.000000', 'sajt', 5, 6, 2, null, 2);
