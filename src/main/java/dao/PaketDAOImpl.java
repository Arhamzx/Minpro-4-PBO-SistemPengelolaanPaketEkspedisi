package dao;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Ekspedisi;
import model.EkspedisiFactory;
import model.Kurir;
import model.Paket;
import model.Penerima;

public class PaketDAOImpl implements PaketDAO {
    private static final DateTimeFormatter FORMAT_TAMPIL =
        DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final String SELECT_PAKET = """
        SELECT p.id_paket, p.no_resi, p.nama_pengirim, p.tanggal_masuk,
               p.status, p.berat_kg,
               e.id_ekspedisi, e.nama_ekspedisi, e.jenis_layanan,
               pn.id_penerima, pn.nama AS nama_penerima, pn.nomor_hp AS hp_penerima,
               pn.alamat, pn.kode_pos,
               k.id_kurir, k.nama AS nama_kurir, k.nomor_hp AS hp_kurir,
               k.jenis_kendaraan
        FROM paket p
        JOIN ekspedisi e ON p.id_ekspedisi = e.id_ekspedisi
        JOIN penerima pn ON p.id_penerima  = pn.id_penerima
        JOIN kurir k     ON p.id_kurir     = k.id_kurir
        """;

    // CREATE
    @Override
    public void tambahPaket(Paket paket) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idEkspedisi = cariAtauBuatEkspedisi(conn, paket.getEkspedisi());
                int idPenerima  = simpanPenerima(conn, paket.getPenerima());
                int idKurir     = simpanKurir(conn, paket.getKurir());

                String sql = "INSERT INTO paket (no_resi, nama_pengirim, tanggal_masuk, "
                           + "status, berat_kg, id_ekspedisi, id_penerima, id_kurir) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps =
                        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, paket.getNoResi());
                    ps.setString(2, paket.getNamaPengirim());
                    ps.setDate(3, Date.valueOf(
                        LocalDate.parse(paket.getTanggalMasuk(), FORMAT_TAMPIL)));
                    ps.setString(4, paket.getStatus());
                    ps.setDouble(5, paket.getBeratKg());
                    ps.setInt(6, idEkspedisi);
                    ps.setInt(7, idPenerima);
                    ps.setInt(8, idKurir);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            paket.setIdPaket(keys.getInt(1));
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private int cariAtauBuatEkspedisi(Connection conn, Ekspedisi ekspedisi)
            throws SQLException {
        String cari = "SELECT id_ekspedisi FROM ekspedisi "
                    + "WHERE nama_ekspedisi = ? AND jenis_layanan = ?";
        try (PreparedStatement ps = conn.prepareStatement(cari)) {
            ps.setString(1, ekspedisi.getNamaEkspedisi());
            ps.setString(2, ekspedisi.getJenisLayanan());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ekspedisi.setIdEkspedisi(rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }

        String tambah = "INSERT INTO ekspedisi (nama_ekspedisi, jenis_layanan) VALUES (?, ?)";
        try (PreparedStatement ps =
                conn.prepareStatement(tambah, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ekspedisi.getNamaEkspedisi());
            ps.setString(2, ekspedisi.getJenisLayanan());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                ekspedisi.setIdEkspedisi(keys.getInt(1));
                return keys.getInt(1);
            }
        }
    }

    private int simpanPenerima(Connection conn, Penerima penerima) throws SQLException {
        String sql = "INSERT INTO penerima (nama, nomor_hp, alamat, kode_pos) "
                   + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, penerima.getNama());
            ps.setString(2, penerima.getNomorHp());
            ps.setString(3, penerima.getAlamat());
            ps.setString(4, penerima.getKodePos());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                penerima.setId(keys.getInt(1));
                return keys.getInt(1);
            }
        }
    }

    private int simpanKurir(Connection conn, Kurir kurir) throws SQLException {
        String sql = "INSERT INTO kurir (nama, nomor_hp, jenis_kendaraan) VALUES (?, ?, ?)";
        try (PreparedStatement ps =
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kurir.getNama());
            ps.setString(2, kurir.getNomorHp());
            ps.setString(3, kurir.getJenisKendaraan());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                kurir.setId(keys.getInt(1));
                return keys.getInt(1);
            }
        }
    }

    // READ
    @Override
    public List<Paket> getDaftarPaket() throws SQLException {
        List<Paket> daftar = new ArrayList<>();
        String sql = SELECT_PAKET + " ORDER BY p.id_paket";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(petakanPaket(rs));
            }
        }
        return daftar;
    }

    @Override
    public Paket cariPaket(int id) throws SQLException {
        String sql = SELECT_PAKET + " WHERE p.id_paket = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakanPaket(rs) : null;
            }
        }
    }

    @Override
    public Paket cariPaket(String resi) throws SQLException {
        String sql = SELECT_PAKET + " WHERE p.no_resi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resi);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakanPaket(rs) : null;
            }
        }
    }

    private Paket petakanPaket(ResultSet rs) throws SQLException {
        Ekspedisi ekspedisi = EkspedisiFactory.buat(
            rs.getInt("id_ekspedisi"),
            rs.getString("nama_ekspedisi"),
            rs.getString("jenis_layanan"));

        Penerima penerima = new Penerima(
            rs.getInt("id_penerima"),
            rs.getString("nama_penerima"),
            rs.getString("hp_penerima"),
            rs.getString("alamat"),
            rs.getString("kode_pos"));

        Kurir kurir = new Kurir(
            rs.getInt("id_kurir"),
            rs.getString("nama_kurir"),
            rs.getString("hp_kurir"),
            rs.getString("jenis_kendaraan"));

        return new Paket(
            rs.getInt("id_paket"),
            rs.getString("no_resi"),
            rs.getString("nama_pengirim"),
            rs.getDate("tanggal_masuk").toLocalDate().format(FORMAT_TAMPIL),
            rs.getString("status"),
            ekspedisi,
            penerima,
            kurir,
            rs.getDouble("berat_kg"));
    }

    // UPDATE
    @Override
    public void updateStatus(int idPaket, String status) throws SQLException {
        String sql = "UPDATE paket SET status = ? WHERE id_paket = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idPaket);
            ps.executeUpdate();
        }
    }

    @Override
    public void updatePenerima(Penerima penerima) throws SQLException {
        String sql = "UPDATE penerima SET nama = ?, nomor_hp = ?, alamat = ?, "
                   + "kode_pos = ? WHERE id_penerima = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, penerima.getNama());
            ps.setString(2, penerima.getNomorHp());
            ps.setString(3, penerima.getAlamat());
            ps.setString(4, penerima.getKodePos());
            ps.setInt(5, penerima.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateKurir(Kurir kurir) throws SQLException {
        String sql = "UPDATE kurir SET nama = ?, nomor_hp = ?, jenis_kendaraan = ? "
                   + "WHERE id_kurir = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kurir.getNama());
            ps.setString(2, kurir.getNomorHp());
            ps.setString(3, kurir.getJenisKendaraan());
            ps.setInt(4, kurir.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    @Override
    public boolean hapusPaket(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idPenerima;
                int idKurir;

                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT id_penerima, id_kurir FROM paket WHERE id_paket = ?")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            return false;
                        }
                        idPenerima = rs.getInt("id_penerima");
                        idKurir = rs.getInt("id_kurir");
                    }
                }

                // paket dihapus dulu karena memegang foreign key
                jalankanHapus(conn, "DELETE FROM paket WHERE id_paket = ?", id);
                jalankanHapus(conn, "DELETE FROM penerima WHERE id_penerima = ?", idPenerima);
                jalankanHapus(conn, "DELETE FROM kurir WHERE id_kurir = ?", idKurir);

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void jalankanHapus(Connection conn, String sql, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
