insert into role(id,name, description) value(1, 'ROLE_ADMIN','Admin');
insert into role(id,name,description) value(2, 'ROLE_SPACE_PLANNER','Space planner');
insert into role(id,name, description) value(3, 'ROLE_DESIGNER','Designer');
insert into role(id,name, description) value(4, 'ROLE_COMMERCIAL','Commercial');
insert into user(id,created_at, updated_at,accountNonExpired,accountNonLocked,credentialsNonExpired, enabled,
  first_name, last_name, username, email, password, password_type)
   value(1, '2015-06-26 12:40:55', '2015-06-26 12:40:55',1,1,1,1, 'Murthy','M','murthy.m@futurelifestyle.in','murthy.m@futureliestyle.in',
'8e5d3299950d242bd51ea91c68ac08f9a79852b83a84822e3cc33ae51c3bea4208800afba20c6637c0c1d102bffbdf9cd3ddeb30658992c5553ab6d8ba0363f5',0);

insert into user_role(user_id,role_id) value(1,1);


insert into category_division(id,category, division) value(1, 'Our Brands','Apparels');
insert into category_division(id,category, division) value(2, 'Indus League','Apparels');
insert into category_division(id,category, division) value(3, 'Own Brands','Apparels');
insert into category_division(id,category, division) value(4, 'Youth','Apparels');
insert into category_division(id,category, division) value(5, 'Kids','Apparels');
insert into category_division(id,category, division) value(6, 'Ladies Western','Apparels');
insert into category_division(id,category, division) value(7, 'Mens Casuals','Apparels');
insert into category_division(id,category, division) value(8, 'Mens formal','Apparels');
insert into category_division(id,category, division) value(9, 'Ladies Ethnic','Apparels');
insert into category_division(id,category, division) value(10, 'Handbags','Lifestyle');
insert into category_division(id,category, division) value(11, 'FOOTWEAR','Lifestyle');
insert into category_division(id,category, division) value(12, 'Watches','Lifestyle');
insert into category_division(id,category, division) value(13, 'Luggage','Lifestyle');
insert into category_division(id,category, division) value(14, 'Mens Accessories','Lifestyle');
insert into category_division(id,category, division) value(15, 'Toys & INF','Lifestyle');
insert into category_division(id,category, division) value(16, 'MENS UG','Lifestyle');
insert into category_division(id,category, division) value(17, 'Fragrance','Lifestyle');
insert into category_division(id,category, division) value(18, 'Sunglasses','Lifestyle');
insert into category_division(id,category, division) value(19, 'Fashion Jewellery','Lifestyle');
insert into category_division(id,category, division) value(20, 'Cosmetics','Lifestyle');
insert into category_division(id,category, division) value(21, 'Lingerie','Lifestyle');
insert into category_division(id,category, division) value(22, 'Home Fashion','Lifestyle');
insert into category_division(id,category, division) value(23, 'Baby World','Lifestyle');
insert into category_division(id,category, division) value(24, 'Sports Wear','Lifestyle');
insert into category_division(id,category, division) value(25, 'JIV','Lifestyle');



