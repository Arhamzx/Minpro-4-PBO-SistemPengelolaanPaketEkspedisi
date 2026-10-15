package model;


public class EkspedisiFactory {

    private EkspedisiFactory() {
    }

    public static Ekspedisi buat(int id, String nama, String layanan) {
        switch (nama) {
            case "J&T": return new JNT(id, layanan);
            case "JNE": return new JNE(id, layanan);
            default:    return new SiCepat(id, layanan);
        }
    }
}
