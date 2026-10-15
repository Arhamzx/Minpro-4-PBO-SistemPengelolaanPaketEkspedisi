/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class JNT extends Ekspedisi {
    public JNT(int idEkspedisi, String jenisLayanan) {
        super(idEkspedisi, "J&T", jenisLayanan);
    }

    @Override
    public double hitungOngkir(double beratKg) {
        double tarifPerKg = switch (getJenisLayanan()) {
            case "Cargo"   -> 7000;
            case "Ekonomi" -> 9000;
            default        -> 13000; // Reguler
        };
        return Math.max(1, Math.ceil(beratKg)) * tarifPerKg;
    }
}
