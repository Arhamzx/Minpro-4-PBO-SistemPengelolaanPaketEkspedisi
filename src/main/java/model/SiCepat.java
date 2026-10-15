/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class SiCepat extends Ekspedisi {
    public SiCepat(int idEkspedisi, String jenisLayanan) {
        super(idEkspedisi, "SiCepat", jenisLayanan);
    }

    @Override
    public double hitungOngkir(double beratKg) {
        double tarifPerKg = switch (getJenisLayanan()) {
            case "BEST" -> 20000;
            case "HALU" -> 8000;
            default     -> 11000; // REGULAR
        };
        return Math.max(1, Math.ceil(beratKg)) * tarifPerKg;
    }
}
