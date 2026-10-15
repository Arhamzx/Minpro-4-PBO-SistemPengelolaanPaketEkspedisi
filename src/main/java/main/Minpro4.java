package main;

import controller.ManajemenPaketController;
import dao.PaketDAO;
import dao.PaketDAOImpl;
import view.ManajemenPaketView;

public class Minpro4 {

    public static void main(String[] args) {

        PaketDAO paketDAO = new PaketDAOImpl();

        ManajemenPaketController controller =
            new ManajemenPaketController(paketDAO);

        ManajemenPaketView view =
            new ManajemenPaketView(controller);

        view.jalankan();
    }
}
