/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class Penerima extends Person {
    private String alamat;
    private String kodePos;

    public Penerima(int id, String nama, String nomorHp, String alamat, String kodePos) {
        super(id, nama, nomorHp);
        this.alamat = alamat;
        this.kodePos = kodePos;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }
    
    @Override
    public String getPeran() { return "Penerima"; }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Alamat: " + alamat + " (" + kodePos + ")";
    }
}
