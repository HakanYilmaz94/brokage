INSERT INTO CUSTOMER (id, status, created_by, created_At, updated_by, updated_At, name, surname, email, username, password, role) VALUES
  (CUSTOMER_SEQ.nextval, 'ACTIVE', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'Hakan', 'Yılmaz', 'hkn94@windowslive.com', 'HY',  'password', 'USER');

INSERT INTO ASSET (id, customer_id, asset_name, size, usable_size, status, created_by, created_at, updated_by, updated_at)
VALUES (
           ASSET_SEQ.nextval,
           (SELECT id FROM CUSTOMER WHERE username = 'HY'),
           'TRY',
           100000,
           100000,
           'ACTIVE',
           'SYSTEM',
           CURRENT_TIMESTAMP,
           'SYSTEM',
           CURRENT_TIMESTAMP
       );

INSERT INTO CUSTOMER (id, status, created_by, created_At, updated_by, updated_At, name, surname, email, username, password, role) VALUES
    (CUSTOMER_SEQ.nextval, 'ACTIVE', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'AA', 'BB', 'aa@gmail.com', 'admin',  'admin', 'ADMIN');

INSERT INTO CUSTOMER (id, status, created_by, created_At, updated_by, updated_At, name, surname, email, username, password, role) VALUES
    (CUSTOMER_SEQ.nextval, 'ACTIVE', 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'Hakan2', 'Yılmaz2', 'hkn95@windowslive.com', 'HY2',  'password2', 'USER');
INSERT INTO ASSET (id, customer_id, asset_name, size, usable_size, status, created_by, created_at, updated_by, updated_at)
VALUES (
           ASSET_SEQ.nextval,
           (SELECT id FROM CUSTOMER WHERE username = 'HY2'),
           'TRY',
           50000,
           50000,
           'ACTIVE',
           'SYSTEM',
           CURRENT_TIMESTAMP,
           'SYSTEM',
           CURRENT_TIMESTAMP
       );