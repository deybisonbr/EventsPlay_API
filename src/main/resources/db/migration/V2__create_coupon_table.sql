CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE coupons (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    CODE varchar(100) NOT NULL,
    discount INT NOT NULL,
    valid TIMESTAMP NOT NULL,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES  events(id) ON DELETE CASCADE
);