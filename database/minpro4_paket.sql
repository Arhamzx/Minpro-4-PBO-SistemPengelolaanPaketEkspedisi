CREATE DATABASE IF NOT EXISTS minpro4_paket
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE minpro4_paket;

DROP TABLE IF EXISTS paket;
DROP TABLE IF EXISTS penerima;
DROP TABLE IF EXISTS kurir;
DROP TABLE IF EXISTS ekspedisi;

CREATE TABLE ekspedisi (
    id_ekspedisi   INT          NOT NULL AUTO_INCREMENT,
    nama_ekspedisi VARCHAR(30)  NOT NULL,
    jenis_layanan  VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id_ekspedisi),
    UNIQUE KEY uq_ekspedisi_layanan (nama_ekspedisi, jenis_layanan)
) ENGINE=InnoDB;

CREATE TABLE penerima (
    id_penerima INT          NOT NULL AUTO_INCREMENT,
    nama        VARCHAR(100) NOT NULL,
    nomor_hp    VARCHAR(20)  NOT NULL,
    alamat      VARCHAR(255) NOT NULL,
    kode_pos    VARCHAR(10)  NOT NULL,
    PRIMARY KEY (id_penerima)
) ENGINE=InnoDB;

CREATE TABLE kurir (
    id_kurir        INT          NOT NULL AUTO_INCREMENT,
    nama            VARCHAR(100) NOT NULL,
    nomor_hp        VARCHAR(20)  NOT NULL,
    jenis_kendaraan VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id_kurir)
) ENGINE=InnoDB;

CREATE TABLE paket (
    id_paket       INT           NOT NULL AUTO_INCREMENT,
    no_resi        VARCHAR(30)   NOT NULL,
    nama_pengirim  VARCHAR(100)  NOT NULL,
    tanggal_masuk  DATE          NOT NULL,
    status         VARCHAR(30)   NOT NULL,
    berat_kg       DECIMAL(8,2)  NOT NULL,
    id_ekspedisi   INT           NOT NULL,
    id_penerima    INT           NOT NULL,
    id_kurir       INT           NOT NULL,
    PRIMARY KEY (id_paket),
    UNIQUE KEY uq_paket_resi (no_resi),
    CONSTRAINT fk_paket_ekspedisi FOREIGN KEY (id_ekspedisi) REFERENCES ekspedisi (id_ekspedisi),
    CONSTRAINT fk_paket_penerima  FOREIGN KEY (id_penerima)  REFERENCES penerima (id_penerima),
    CONSTRAINT fk_paket_kurir     FOREIGN KEY (id_kurir)     REFERENCES kurir (id_kurir)
) ENGINE=InnoDB;

INSERT INTO ekspedisi (nama_ekspedisi, jenis_layanan) VALUES
    ('J&T',     'Cargo'),
    ('J&T',     'Reguler'),
    ('J&T',     'Ekonomi'),
    ('JNE',     'REG'),
    ('JNE',     'YES'),
    ('JNE',     'OKE'),
    ('SiCepat', 'BEST'),
    ('SiCepat', 'REGULAR'),
    ('SiCepat', 'HALU');

INSERT INTO penerima (nama, nomor_hp, alamat, kode_pos) VALUES
    ('Arham', '08123456789', 'Sangatta',  '75683'),
    ('Rina',  '08567812345', 'Samarinda', '75117');

INSERT INTO kurir (nama, nomor_hp, jenis_kendaraan) VALUES
    ('Budi', '08129876543', 'Motor'),
    ('Andi', '08134567890', 'Mobil');

INSERT INTO paket
    (no_resi, nama_pengirim, tanggal_masuk, status, berat_kg, id_ekspedisi, id_penerima, id_kurir)
VALUES
    ('RESI001', 'Ikrar', '2026-09-23', 'Dalam Proses',     1.00, 4, 1, 1),
    ('RESI002', 'Siti',  '2026-10-05', 'Dalam Perjalanan', 2.50, 2, 2, 2);
