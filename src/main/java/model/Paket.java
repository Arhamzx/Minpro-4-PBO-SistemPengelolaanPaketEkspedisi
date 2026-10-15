/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class Paket {
    private int idPaket;
    private String noResi;
    private String namaPengirim;
    private String tanggalMasuk;
    private String status;
    private Ekspedisi ekspedisi;
    private Penerima penerima;
    private Kurir kurir;
    private double beratKg;

    public Paket(int idPaket, String noResi, String namaPengirim,String tanggalMasuk, String status, Ekspedisi ekspedisi, Penerima penerima, Kurir kurir, double beratKg) {
        this.beratKg = beratKg;
        this.idPaket = idPaket;
        this.noResi = noResi;
        this.namaPengirim = namaPengirim;
        this.tanggalMasuk = tanggalMasuk;
        this.status = status;
        this.ekspedisi = ekspedisi;
        this.penerima = penerima;
        this.kurir = kurir;
    }
    
    // OVERLOADING
    public Paket(int idPaket, String noResi, String namaPengirim,
             String tanggalMasuk, String status,
             Ekspedisi ekspedisi, Penerima penerima, Kurir kurir) {
    this(idPaket, noResi, namaPengirim, tanggalMasuk, status,
         ekspedisi, penerima, kurir, 1.0);
}

    public int getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(int idPaket) {
        this.idPaket = idPaket;
    }

    public String getNoResi() {
        return noResi;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public String getStatus() {
        return status;
    }

    public Ekspedisi getEkspedisi() {
        return ekspedisi;
    }

    public Penerima getPenerima() {
        return penerima;
    }

    public Kurir getKurir() {
        return kurir;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPenerima(Penerima penerima) {
        this.penerima = penerima;
    }

    public void setKurir(Kurir kurir) {
        this.kurir = kurir;
    }
    
    public double getBeratKg() { 
        return beratKg; 
    }
    
    public double hitungBiayaKirim() {
    return ekspedisi.hitungOngkir(beratKg);
    }
    
    
}
