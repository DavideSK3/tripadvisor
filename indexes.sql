CREATE INDEX cities_index ON CITIES (state, region, name);
CREATE INDEX regions_index ON REGIONS (state, name);
CREATE INDEX states_index ON STATES (name);

CREATE INDEX user_id ON USERS (id);
CREATE INDEX user_password_id ON USERS (id, password);

CREATE INDEX token_idx ON TOKENS (token, expiration_time, id);

CREATE INDEX restaurant_owner_idx ON RESTAURANTS (id_owner, id);
CREATE INDEX restaurant_name_idx ON RESTAURANTS (name);
--CREATE INDEX restaurant_place_idx ON RESTAURANTS (state || ' ' || region || ' ' || city);
CREATE INDEX restaurant_places_idx ON RESTAURANTS (state, region, city);
--CREATE INDEX restaurant_places_valutation_idx ON RESTAURANTS (state, region, city, (CASE WHEN REVIEW_COUNTER > 0 THEN GLOBAL_REVIEW/REVIEW_COUNTER ELSE 0 END));

CREATE INDEX restaurants_cuisines_idx ON RESTAURANTS_CUISINES (id_restaurant, id_cuisine);

CREATE INDEX orari_idx ON ORARIO (id, giorno, apertura, chiusura);

CREATE INDEX photos_idx ON PHOTOS(id_restaurant, type);

CREATE INDEX reviews_idx ON REVIEWS (id_restaurant, title);

