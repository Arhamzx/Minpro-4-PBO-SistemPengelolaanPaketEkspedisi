/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public class TestKoneksi {
    public static void main(String[] args) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null){
            System.out.println("Koneksi Berhasil");
        }else{
            System.out.println("Koneksi Gagal");
        }
    }
}