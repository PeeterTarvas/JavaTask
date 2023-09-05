\c dwh;

CREATE OR REPLACE FUNCTION GetSectorDepth(sector_id integer)
    RETURNS integer AS $$
DECLARE
    depth integer := 0;
BEGIN
    WITH RECURSIVE SectorCTE AS (
        SELECT
            sector_id,
            sector_parent_id
        FROM
            helmes.sector
        WHERE
            sector_id = sector_id  -- Filter for the input sector_id

        UNION ALL

        SELECT
            s.sector_id,
            s.sector_parent_id
        FROM
            helmes.sector AS s
        JOIN
            SectorCTE AS cte ON s.sector_id = cte.sector_parent_id
    )

    SELECT INTO depth
        count(*) - 1  -- Subtract 1 to get the depth excluding the root node itself
    FROM
        SectorCTE;

    RETURN depth;
END $$;


INSERT INTO helmes.sector (sector_id, sector_name, sector_parent_id) VALUES
    (1, 'Manufacturing', NULL),
    (2, 'Construction materials', 1),
    (3, 'Electronics and Optics', 1),
    (4, 'Food and Beverage', 1, GetSectorDepth(1)),
    (5,'Bakery & confectionery products', 4),
    (6,'Beverages', 4),
    (7,'Fish & fish products', 4),
    (8,'Meat & meat products',4 ),
    (9,'Meat & meat products', 4),
    (10,'Other', 4),
    (11,'Sweets & snack food', 4);
