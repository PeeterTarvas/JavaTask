\c dwh;

INSERT INTO helmes.sector (sector_id, sector_name, sector_parent_id) VALUES
    (1, 'Manufacturing', NULL),
    (2, 'Construction materials', 1),
    (3, 'Electronics and Optics', 1),
    (4, 'Food and Beverage', 1),
    (5,'Bakery & confectionery products', 4),
    (6,'Beverages', 4),
    (7,'Fish & fish products', 4),
    (8,'Meat & meat products',4 ),
    (9,'Meat & meat products', 4),
    (10,'Other', 4),
    (11,'Sweets & snack food', 4);
