INSERT INTO pizza_order_status (id, code, descr) VALUES
(1, 'new', 'New'), 
(2, 'inProgress', 'In Progress'), 
(3, 'ready', 'Ready')
ON CONFLICT DO NOTHING;

INSERT INTO ingredient_type (id, code) VALUES
(1, 'cheese'), 
(2, 'sauce'), 
(3, 'meat'), 
(4, 'vegetable'), 
(5, 'herb'), 
(6, 'oil')
ON CONFLICT DO NOTHING;

INSERT INTO ingredient (code, descr, ingredient_type_id) VALUES
('mozzarella', 'Mozzarella', 1), 
('tomatoSauce', 'Tomato sauce', 2), 
('basil', 'Basil', 5), 
('spicySalami', 'Spicy salami', 3), 
('oregano', 'Oregano', 5)
ON CONFLICT DO NOTHING;

INSERT INTO pizza_base (id, code, descr) VALUES
(1, 'classic', 'Classic wheat dough'), 
(2, 'glutenFree', 'Gluten-free dough')
ON CONFLICT DO NOTHING;

INSERT INTO pizza (code, descr, base_id) VALUES
('margherita', 'Margherita', 1), 
('margheritaGlutenFree', 'Margherita (gluten free)', 2), 
('diavola', 'Diavola', 1), 
('diavolaGlutenFree', 'Diavola (gluten free)', 2)
ON CONFLICT DO NOTHING;

INSERT INTO pizza_ingredient_link (pizza_id, ingredient_id) values
((select id from pizza where code = 'margherita'), (select id from ingredient where code = 'tomatoSauce')),
((select id from pizza where code = 'margherita'), (select id from ingredient where code = 'mozzarella')),
((select id from pizza where code = 'margherita'), (select id from ingredient where code = 'basil')),
((select id from pizza where code = 'margheritaGlutenFree'), (select id from ingredient where code = 'tomatoSauce')),
((select id from pizza where code = 'margheritaGlutenFree'), (select id from ingredient where code = 'mozzarella')),
((select id from pizza where code = 'margheritaGlutenFree'), (select id from ingredient where code = 'basil')),
((select id from pizza where code = 'diavola'), (select id from ingredient where code = 'tomatoSauce')),
((select id from pizza where code = 'diavola'), (select id from ingredient where code = 'mozzarella')),
((select id from pizza where code = 'diavola'), (select id from ingredient where code = 'spicySalami')),
((select id from pizza where code = 'diavola'), (select id from ingredient where code = 'oregano')),
((select id from pizza where code = 'diavolaGlutenFree'), (select id from ingredient where code = 'tomatoSauce')),
((select id from pizza where code = 'diavolaGlutenFree'), (select id from ingredient where code = 'mozzarella')),
((select id from pizza where code = 'diavolaGlutenFree'), (select id from ingredient where code = 'spicySalami')),
((select id from pizza where code = 'diavolaGlutenFree'), (select id from ingredient where code = 'oregano'))
ON CONFLICT DO NOTHING;
