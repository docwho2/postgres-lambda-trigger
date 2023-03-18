/**
 * Author:  sjensen
 * Created: Mar 15, 2023
 */

CREATE TABLE IF NOT EXISTS address (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created timestamp without time zone NOT NULL DEFAULT timezone('utc'::text, now()),
    address_1 text NOT NULL,
    city text NOT NULL,
    district text NOT NULL,
    postal_code text,
    address_entered text,
    geo_coded timestamp without time zone,
    geo_last_coding jsonb,
    geo_latitude double precision,
    geo_longitude double precision,
    address_formatted text,
    address_notes text,
    requires_geo_coding boolean NOT NULL DEFAULT true
);

COMMENT ON TABLE address IS 'Storage of addresses for various entities';
COMMENT ON COLUMN address.address_1 IS 'First line of the address';
COMMENT ON COLUMN address.city IS 'The city for this address';
COMMENT ON COLUMN address.district IS 'The region of an address, this may be a state, province, prefecture, etc.';
COMMENT ON COLUMN address.postal_code IS 'The postal code or ZIP code of the address (where applicable).';
COMMENT ON COLUMN address.geo_coded IS 'when this entry was last geo coded, null if no geo coding or could not geo code the address';
COMMENT ON COLUMN address.geo_last_coding IS 'The reponse from google from the last geo coding for reference';
COMMENT ON COLUMN address.geo_latitude IS 'latitude for this address if properly geo coded';
COMMENT ON COLUMN address.geo_longitude IS 'longitude for this address if properly geo coded';
COMMENT ON COLUMN address.address_formatted IS 'If the address has been geo coded, then this will contain the fully formatted address returned from google';
COMMENT ON COLUMN address.address_notes IS 'a description or any notes or comments about this address';
COMMENT ON COLUMN address.requires_geo_coding IS 'Whether this address should be geo coded.  Generally Empoyee addresses should, contancts for example don''t need it';
