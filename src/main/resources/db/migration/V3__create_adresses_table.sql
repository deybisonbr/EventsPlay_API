CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE adresses (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    uf VARCHAR(10) NOT NULL,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);