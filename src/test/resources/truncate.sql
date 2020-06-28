SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE STATION;
TRUNCATE TABLE LINE;
TRUNCATE TABLE LINE_STATION;
TRUNCATE TABLE MEMBER;
TRUNCATE TABLE FAVORITE;

ALTER TABLE FAVORITE ALTER COLUMN favorite_id RESTART WITH 1;
ALTER TABLE STATION ALTER COLUMN station_id RESTART WITH 1;
ALTER TABLE LINE ALTER COLUMN line_id RESTART WITH 1;
ALTER TABLE LINE_STATION ALTER COLUMN line_station_id RESTART WITH 1;
ALTER TABLE MEMBER ALTER COLUMN member_id RESTART WITH 1;

SET FOREIGN_KEY_CHECKS = 1;