package dao;

import java.sql.SQLException;
import java.util.List;
import model.Kurir;
import model.Paket;
import model.Penerima;

public interface PaketDAO {

    // CREATE
    void tambahPaket(Paket paket) throws SQLException;

    // READ
    List<Paket> getDaftarPaket() throws SQLException;

    // OVERLOADING
    Paket cariPaket(int id) throws SQLException;

    Paket cariPaket(String resi) throws SQLException;

    // UPDATE
    void updateStatus(int idPaket, String status) throws SQLException;

    void updatePenerima(Penerima penerima) throws SQLException;

    void updateKurir(Kurir kurir) throws SQLException;

    // DELETE
    boolean hapusPaket(int id) throws SQLException;
}
