/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class Kurir extends Person {
    private String jenisKendaraan;

    public Kurir(int id, String nama, String nomorHp, String jenisKendaraan) {
        super(id, nama, nomorHp);
        this.jenisKendaraan = jenisKendaraan;
    }

    public String getJenisKendaraan() {
        return jenisKendaraan;
    }

    public void setJenisKendaraan(String jenisKendaraan) {
        this.jenisKendaraan = jenisKendaraan;
    }
    
    @Override
    public String getPeran() { return "Kurir"; }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Kendaraan: " + jenisKendaraan;
    }
}
