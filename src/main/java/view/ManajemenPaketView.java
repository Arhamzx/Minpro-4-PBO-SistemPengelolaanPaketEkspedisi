/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import controller.ManajemenPaketController;
import model.Ekspedisi;
import model.EkspedisiFactory;
import model.Person;
import model.Kurir;
import model.Paket;
import model.Penerima;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


/**
 *
 * @author LENOVO
 */
public class ManajemenPaketView {

    private final Scanner input;
    private final ManajemenPaketController controller;

    public ManajemenPaketView(ManajemenPaketController controller) {
        this.controller = controller;
        this.input = new Scanner(System.in);
    }

    // JALANKAN
    public void jalankan() {
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = inputInt("Pilih menu: ");

            try {
                switch (pilihan) {
                    case 1:
                        tambahPaket();
                        break;

                    case 2:
                        tampilkanSemuaPaket();
                        break;

                    case 3:
                        cariPaket();
                        break;

                    case 4:
                        updatePaket();
                        break;

                    case 5:
                        hapusPaket();
                        break;

                    case 6:
                        System.out.println("\nProgram selesai.");
                        break;

                    default:
                        System.out.println("\nPilihan menu tidak tersedia!");
                }
            } catch (SQLException e) {
                System.out.println("\nTerjadi kesalahan database: " + e.getMessage());
                e.printStackTrace();
            }

        } while (pilihan != 6);

        input.close();
    }

    // MENUNYA
    private void tampilkanMenu() {
        System.out.println("\n========================================");
        System.out.println("     SISTEM MANAJEMEN PAKET EKSPEDISI");
        System.out.println("========================================");
        System.out.println("1. Tambah Paket");
        System.out.println("2. Tampilkan Semua Paket");
        System.out.println("3. Cari Paket");
        System.out.println("4. Update Paket");
        System.out.println("5. Hapus Paket");
        System.out.println("6. Keluar");
        System.out.println("========================================");
    }


    // VALIDASI INPUT
    private int inputInt(String pesan) {
        while (true) {
            System.out.print(pesan);

            String data = input.nextLine().trim();

            if (data.matches("\\d+")) {
                return Integer.parseInt(data);
            }

            System.out.println("Input harus berupa angka! Coba lagi.");
        }
    }

    private String inputString(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    private String inputNomorHp(String pesan) {
        while (true) {
            System.out.print(pesan);

            String nomor = input.nextLine().trim();

            if (nomor.matches("\\d+")) {
                return nomor;
            }

            System.out.println("Nomor HP hanya boleh berisi angka!");
        }
    }

    private double inputDouble(String pesan) {
    while (true) {
        System.out.print(pesan);
        String data = input.nextLine().trim().replace(",", ".");
        if (data.matches("\\d+(\\.\\d+)?") && Double.parseDouble(data) > 0) {
            return Double.parseDouble(data);
        }
        System.out.println("Berat harus berupa angka lebih dari 0!");
        }
    }

    private String inputTanggal(String pesan) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);

        while (true) {
            try {
                System.out.print(pesan);

                String data = input.nextLine().trim();

                Date date = sdf.parse(data);

                return sdf.format(date);

            } catch (Exception e) {
                System.out.println(
                    "Format salah! Gunakan DD-MM-YYYY"
                );
            }
        }
    }


    // EKSPEDISI
    private String pilihEkspedisi() {

        while (true) {

            System.out.println("\n--- PILIH EKSPEDISI ---");
            System.out.println("1. J&T");
            System.out.println("2. JNE");
            System.out.println("3. SiCepat");

            int pilihan = inputInt("Pilih ekspedisi: ");

            switch (pilihan) {

                case 1:
                    return "J&T";

                case 2:
                    return "JNE";

                case 3:
                    return "SiCepat";

                default:
                    System.out.println(
                        "Pilihan ekspedisi tidak tersedia!"
                    );
            }
        }
    }


    // LAYANAN
    private String pilihLayanan(String ekspedisi) {

        while (true) {

            System.out.println(
                "\n--- PILIH LAYANAN " +
                ekspedisi.toUpperCase() +
                " ---"
            );

            if (ekspedisi.equals("J&T")) {

                System.out.println("1. Cargo");
                System.out.println("2. Reguler");
                System.out.println("3. Ekonomi");

                int pilihan = inputInt("Pilih layanan: ");

                switch (pilihan) {

                    case 1:
                        return "Cargo";

                    case 2:
                        return "Reguler";

                    case 3:
                        return "Ekonomi";

                    default:
                        System.out.println(
                            "Pilihan layanan tidak tersedia!"
                        );
                }

            } else if (ekspedisi.equals("JNE")) {

                System.out.println("1. REG");
                System.out.println("2. YES");
                System.out.println("3. OKE");

                int pilihan = inputInt("Pilih layanan: ");

                switch (pilihan) {

                    case 1:
                        return "REG";

                    case 2:
                        return "YES";

                    case 3:
                        return "OKE";

                    default:
                        System.out.println(
                            "Pilihan layanan tidak tersedia!"
                        );
                }

            } else if (ekspedisi.equals("SiCepat")) {

                System.out.println("1. BEST");
                System.out.println("2. REGULAR");
                System.out.println("3. HALU");

                int pilihan = inputInt("Pilih layanan: ");

                switch (pilihan) {

                    case 1:
                        return "BEST";

                    case 2:
                        return "REGULAR";

                    case 3:
                        return "HALU";

                    default:
                        System.out.println(
                            "Pilihan layanan tidak tersedia!"
                        );
                }
            }
        }
    }


    // STATUS
    private String pilihStatus() {

        while (true) {

            System.out.println("\n--- STATUS PAKET ---");
            System.out.println("1. Dalam Proses");
            System.out.println("2. Dalam Perjalanan");
            System.out.println("3. Tiba di Hub");
            System.out.println("4. Sedang Dikirim");
            System.out.println("5. Selesai");
            System.out.println("6. Gagal");

            int pilihan = inputInt("Pilih status: ");

            switch (pilihan) {

                case 1:
                    return "Dalam Proses";

                case 2:
                    return "Dalam Perjalanan";

                case 3:
                    return "Tiba di Hub";

                case 4:
                    return "Sedang Dikirim";

                case 5:
                    return "Selesai";

                case 6:
                    return "Gagal";

                default:
                    System.out.println(
                        "Pilihan status tidak tersedia!"
                    );
            }
        }
    }


    // KENDARAANNYA KURIR
    private String pilihKendaraan() {

        while (true) {

            System.out.println("\n--- JENIS KENDARAAN KURIR ---");
            System.out.println("1. Motor");
            System.out.println("2. Mobil");
            System.out.println("3. Van");

            int pilihan = inputInt("Pilih kendaraan: ");

            switch (pilihan) {

                case 1:
                    return "Motor";

                case 2:
                    return "Mobil";

                case 3:
                    return "Van";

                default:
                    System.out.println(
                        "Pilihan kendaraan tidak tersedia!"
                    );
            }
        }
    }


    // CREATE
    private void tambahPaket() throws SQLException {

        System.out.println("\n--- TAMBAH PAKET ---");

        String noResi = inputString("Nomor Resi: ");

        if (controller.cariPaket(noResi) != null) {
            System.out.println("\nNomor resi sudah terdaftar!");
            return;
        }

        String namaPengirim =
            inputString("Nama Pengirim: ");

        String tanggalMasuk =
            inputTanggal("Tanggal Masuk (DD-MM-YYYY): ");

        // DATA PENERIMA
        System.out.println("\n--- DATA PENERIMA ---");

        String namaPenerima =
            inputString("Nama Penerima: ");

        String alamat =
            inputString("Alamat: ");

        String kodePos =
            inputString("Kode Pos: ");

        String nomorHp =
            inputNomorHp("Nomor HP: ");

        Penerima penerima =
            new Penerima(
                0,
                namaPenerima,
                nomorHp,
                alamat,
                kodePos
            );

        // DATA EKSPEDISI
        System.out.println("\n--- DATA EKSPEDISI ---");

        String namaEkspedisi =
            pilihEkspedisi();

        String jenisLayanan =
            pilihLayanan(namaEkspedisi);

        Ekspedisi ekspedisi =
            EkspedisiFactory.buat(
                0,
                namaEkspedisi,
                jenisLayanan
            );
        double beratKg =
                inputDouble("Berat Paket (Kg): ");

        // DATA KURIR
        System.out.println("\n--- DATA KURIR ---");

        String namaKurir =
            inputString("Nama Kurir: ");

        String nomorKurir =
            inputNomorHp("Nomor HP Kurir: ");

        String jenisKendaraan =
            pilihKendaraan();

        Kurir kurir =
            new Kurir(
                0,
                namaKurir,
                nomorKurir,
                jenisKendaraan
            );

        // STATUS
        String status =
            pilihStatus();

        Paket paket =
            new Paket(
                0,
                noResi,
                namaPengirim,
                tanggalMasuk,
                status,
                ekspedisi,
                penerima,
                kurir,
                beratKg
            );

        controller.tambahPaket(paket);

        System.out.println(
            "\nPaket berhasil ditambahkan!"
        );
    }


    // READ
    private void tampilkanSemuaPaket() throws SQLException {

        System.out.println("\n--- DAFTAR PAKET ---");

        List<Paket> daftar = controller.getDaftarPaket();

        if (daftar.isEmpty()) {

            System.out.println(
                "Belum ada data paket."
            );

            return;
        }

        for (Paket paket : daftar) {

            tampilkanDetailPaket(paket);
        }
    }


    // NYARI PAKET
    private void cariPaket() throws SQLException {

        System.out.println("\n--- CARI PAKET ---");

        System.out.println("1. Berdasarkan ID");
        System.out.println("2. Berdasarkan Nomor Resi");

        int pilihan =
            inputInt("Pilih metode pencarian: ");

        Paket paket = null;

        if (pilihan == 1) {

            int id =
                inputInt("Masukkan ID Paket: ");

            paket =
                controller.cariPaket(id);

        } else if (pilihan == 2) {

            String resi =
                inputString("Masukkan Nomor Resi: ");

            paket =
                controller.cariPaket(resi);

        } else {

            System.out.println(
                "Pilihan pencarian tidak tersedia!"
            );

            return;
        }

        if (paket == null) {

            System.out.println(
                "Paket tidak ditemukan!"
            );

        } else {

            tampilkanDetailPaket(paket);
        }
    }


    // DETAIL PAKET
    private void tampilkanDetailPaket(Paket paket) {
    System.out.println("----------------------------------------");
    System.out.println("ID Paket      : " + paket.getIdPaket());
    System.out.println("No. Resi      : " + paket.getNoResi());
    System.out.println("Pengirim      : " + paket.getNamaPengirim());
    System.out.println("Tanggal Masuk : " + paket.getTanggalMasuk());
    System.out.println("Status        : " + paket.getStatus());
    System.out.println("Ekspedisi     : " + paket.getEkspedisi().getNamaEkspedisi());
    System.out.println("Layanan       : " + paket.getEkspedisi().getJenisLayanan());
    System.out.println("Berat         : " + paket.getBeratKg() + " kg");
    System.out.println("Biaya Kirim   : Rp" + String.format("%,.0f", paket.hitungBiayaKirim()));

    Person[] pihakTerkait = { paket.getPenerima(), paket.getKurir() };
    for (Person p : pihakTerkait) {
        System.out.println(p.getInfo());
    }
}


    // UPDATE
    private void updatePaket() throws SQLException {

        System.out.println("\n--- UPDATE PAKET ---");

        String resi =
            inputString("Masukkan Nomor Resi: ");

        Paket paket =
            controller.cariPaket(resi);

        if (paket == null) {

            System.out.println(
                "Paket tidak ditemukan!"
            );

            return;
        }

        System.out.println("\n1. Ubah Status");
        System.out.println("2. Ubah Data Penerima");
        System.out.println("3. Ubah Kurir");
        System.out.println("4. Keluar");

        int pilihan =
            inputInt("Pilih data yang ingin diubah: ");

        switch (pilihan) {

            case 1:

                String statusBaru =
                    pilihStatus();

                controller.updateStatus(paket.getIdPaket(), statusBaru);

                System.out.println(
                    "Status berhasil diubah."
                );

                break;

            case 2:

                String namaBaru =
                    inputString(
                        "Nama Penerima baru: "
                    );

                String alamatBaru =
                    inputString(
                        "Alamat baru: "
                    );

                String kodePosBaru =
                    inputString(
                        "Kode Pos baru: "
                    );

                String hpBaru =
                    inputNomorHp(
                        "Nomor HP baru: "
                    );

                controller.updatePenerima(
                    new Penerima(
                        paket.getPenerima().getId(),
                        namaBaru,
                        hpBaru,
                        alamatBaru,
                        kodePosBaru
                    )
                );

                System.out.println(
                    "Data penerima berhasil diubah."
                );

                break;

            case 3:

                String namaKurirBaru =
                    inputString(
                        "Nama Kurir baru: "
                    );

                String hpKurirBaru =
                    inputNomorHp(
                        "Nomor HP Kurir baru: "
                    );

                String kendaraanBaru =
                    pilihKendaraan();

                controller.updateKurir(
                    new Kurir(
                        paket.getKurir().getId(),
                        namaKurirBaru,
                        hpKurirBaru,
                        kendaraanBaru
                    )
                );

                System.out.println(
                    "Data kurir berhasil diubah."
                );

                break;

            case 4:

                System.out.println(
                    "Update dibatalkan."
                );

                break;

            default:

                System.out.println(
                    "Pilihan tidak tersedia."
                );
        }
    }

    
    // DELETE
    private void hapusPaket() throws SQLException {

        System.out.println("\n--- HAPUS PAKET ---");

        int id =
            inputInt("Masukkan ID Paket: ");

        if (controller.hapusPaket(id)) {

            System.out.println(
                "Paket berhasil dihapus."
            );

        } else {

            System.out.println(
                "Paket tidak ditemukan."
            );
        }
    }
}
