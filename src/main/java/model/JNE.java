/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class JNE extends Ekspedisi {
    public JNE(int idEkspedisi, String jenisLayanan) {
        super(idEkspedisi, "JNE", jenisLayanan);
    }

    @Override
    public double hitungOngkir(double beratKg) {
        double tarifPerKg = switch (getJenisLayanan()) {
            case "YES" -> 18000;
            case "OKE" -> 9000;
            default    -> 12000; // REG
        };
        return Math.max(1, Math.ceil(beratKg)) * tarifPerKg;
    }
}
