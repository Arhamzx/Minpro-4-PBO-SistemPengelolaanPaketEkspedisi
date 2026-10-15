/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public abstract class Person {
    private int id;
    private String nama;
    private String nomorHp;

    public Person(int id, String nama, String nomorHp) {
        this.id = id;
        this.nama = nama;
        this.nomorHp = nomorHp;
    }

    // GET DAN SET
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }
    
    // ABSTRACT METHOD NYA
    public abstract String getPeran();
    
    
    
    public String getInfo() {
        return "[" + getPeran() + "] " + nama + " | HP: " + nomorHp;
    }
}
