-- CREATE sequence
create sequence if not exists dacha_number_seq;

-- ALTER TABLE SET SEQ.
ALTER TABLE dacha_number_sequence
    ALTER COLUMN seq_value
        SET DEFAULT nextval('dacha_number_seq');