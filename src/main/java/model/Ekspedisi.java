/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public abstract class Ekspedisi {
    private int idEkspedisi;
    private String namaEkspedisi;
    private String jenisLayanan;

    public Ekspedisi(int idEkspedisi, String namaEkspedisi, String jenisLayanan) {
        this.idEkspedisi = idEkspedisi;
        this.namaEkspedisi = namaEkspedisi;
        this.jenisLayanan = jenisLayanan;
    }
    
    public abstract double hitungOngkir(double beratKg);
    
    public double hitungOngkir(double beratKg, double nilaiBarang) {
        return hitungOngkir(beratKg) + (nilaiBarang * 0.002);
    }

    public int getIdEkspedisi() { return idEkspedisi; }
    public void setIdEkspedisi(int idEkspedisi) { this.idEkspedisi = idEkspedisi; }
        public String getNamaEkspedisi() { return namaEkspedisi; }
        public String getJenisLayanan() { return jenisLayanan; }
    }

