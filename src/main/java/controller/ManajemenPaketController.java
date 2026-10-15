package controller;

import dao.PaketDAO;
import java.sql.SQLException;
import java.util.List;
import model.Kurir;
import model.Paket;
import model.Penerima;

public class ManajemenPaketController {

    private final PaketDAO paketDAO;

    public ManajemenPaketController(PaketDAO paketDAO) {
        this.paketDAO = paketDAO;
    }

    public void tambahPaket(Paket paket) throws SQLException {
        paketDAO.tambahPaket(paket);
    }

    public List<Paket> getDaftarPaket() throws SQLException {
        return paketDAO.getDaftarPaket();
    }

    // OVERLOADING DARI DAO
    public Paket cariPaket(int id) throws SQLException {
        return paketDAO.cariPaket(id);
    }

    // OVERLOADING DARI DAO
    public Paket cariPaket(String resi) throws SQLException {
        return paketDAO.cariPaket(resi);
    }

    public void updateStatus(int idPaket, String status) throws SQLException {
        paketDAO.updateStatus(idPaket, status);
    }

    public void updatePenerima(Penerima penerima) throws SQLException {
        paketDAO.updatePenerima(penerima);
    }

    public void updateKurir(Kurir kurir) throws SQLException {
        paketDAO.updateKurir(kurir);
    }

    public boolean hapusPaket(int id) throws SQLException {
        return paketDAO.hapusPaket(id);
    }
}
