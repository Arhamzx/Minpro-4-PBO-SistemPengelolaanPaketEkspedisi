# Minpro-4-PBO-SistemPengelolaanPaketEkspedisi
MUHAMMAD ARHAM ANUGRAH - 2509116044

---

## Deskripsi Singkat Program

Sistem Pengelolaan Paket Ekspedisi merupakan program berbasis Java (CLI) yang dibuat untuk membantu pencatatan dan pengelolaan data paket pada layanan pengiriman. Program ini dapat mengelola paket dari beberapa jasa ekspedisi sekaligus, yaitu **J&T**, **JNE**, dan **SiCepat**, sehingga pengguna tidak perlu membuat sistem yang berbeda untuk setiap perusahaan ekspedisi.

Setiap paket menyimpan informasi nomor resi, nama pengirim, tanggal masuk, status, berat paket, serta data penerima, ekspedisi (beserta jenis layanannya), dan kurir. Program menyediakan fitur CRUD (Create, Read, Update, Delete): menambah paket baru, menampilkan semua paket, mencari paket berdasarkan ID atau nomor resi, mengubah status/data penerima/data kurir, dan menghapus paket. Program juga menghitung **biaya kirim** setiap paket, di mana rumus ongkir berbeda untuk setiap ekspedisi dan layanan (nilai tarif pada program adalah nilai contoh/fiktif).

Pada Minpro 4 ini program dikembangkan dari Minpro 3 dengan menghubungkannya ke **database MySQL** menggunakan **JDBC** dan pola **DAO (Data Access Object)**. Pada Minpro 3 seluruh data disimpan di `ArrayList` sehingga hilang ketika program ditutup, dan data awal dibuat lewat kode di `main`. Pada Minpro 4 data disimpan permanen di database, seluruh query memakai **PreparedStatement**, dan data awal (dummy) diisi lewat file **`.sql`**, bukan lewat kode, sehingga saat menu *Tampilkan Semua Paket* dijalankan data langsung tampil tanpa harus input dari awal.

---

## Struktur Package

Program menerapkan struktur **MVC** yang dilengkapi lapisan akses data (DAO) dan konfigurasi koneksi, dibagi ke dalam enam package:

```
Minpro4/
├── database/
│   └── minpro4_paket.sql                 -> script database: struktur tabel + data dummy awal
├── pom.xml                               -> konfigurasi Maven (dependency MySQL Connector/J)
└── src/main/java/
    ├── main/
    │   └── Minpro4.java                  -> entry point (main), merangkai DAO -> controller -> view
    ├── config/
    │   ├── DatabaseConnection.java       -> pembuat koneksi JDBC ke database
    │   └── TestKoneksi.java              -> program kecil untuk menguji koneksi database
    ├── dao/
    │   ├── PaketDAO.java                 -> INTERFACE kontrak akses data paket
    │   └── PaketDAOImpl.java             -> implementasi JDBC (PreparedStatement) untuk semua query
    ├── model/
    │   ├── Person.java                   -> abstract class (induk Kurir dan Penerima)
    │   ├── Kurir.java
    │   ├── Penerima.java
    │   ├── Ekspedisi.java                -> abstract class (induk JNE, JNT, SiCepat)
    │   ├── JNE.java
    │   ├── JNT.java
    │   ├── SiCepat.java
    │   ├── EkspedisiFactory.java         -> membentuk objek JNE/JNT/SiCepat dari data database
    │   └── Paket.java
    ├── controller/
    │   └── ManajemenPaketController.java -> penghubung view dan DAO
    └── view/
        └── ManajemenPaketView.java       -> input/output, menu, dan validasi input
```

Package **main** berisi `Minpro4.java` sebagai titik awal program. Package **config** berisi `DatabaseConnection` yang bertugas membuka koneksi JDBC, serta `TestKoneksi` untuk mengecek bahwa koneksi ke database berhasil. Package **dao** berisi interface `PaketDAO` dan implementasinya `PaketDAOImpl`, tempat seluruh perintah SQL berada. Package **model** berisi class data dan logika bisnis (termasuk abstract class dan factory). Package **controller** berisi `ManajemenPaketController` yang meneruskan permintaan dari view ke DAO. Package **view** berisi `ManajemenPaketView` yang menangani tampilan menu, input, validasi input, dan output ke pengguna. Folder **database** berisi file `.sql` yang dijalankan sekali untuk membuat database beserta data awalnya.

<img width="425" height="620" alt="image" src="https://github.com/user-attachments/assets/415809de-95e5-4ba9-90a1-270e61f92708" />


### Perubahan dari Minpro 3

Class `PengelolaPaket` dan interface `PenyimpananPaket` pada Minpro 3 sudah tidak ada. `PengelolaPaket` hanya berfungsi menyimpan paket di `ArrayList`, dan `PenyimpananPaket` hanya kontrak untuk penyimpanan tersebut. Keduanya digantikan oleh pasangan `PaketDAO` (kontrak) dan `PaketDAOImpl` (pelaksana) yang menyimpan data ke database. Method `buatEkspedisi` yang dulu berada di view dipindah menjadi `EkspedisiFactory` di package model, dan input manual untuk ID paket, ID penerima, ID ekspedisi, serta ID kurir dihapus karena ID kini dibuat otomatis oleh database (`AUTO_INCREMENT`).

---

## Struktur Database

Database bernama **`minpro4_paket`** dan terdiri dari empat tabel yang mengikuti class pada program. Tabel `paket` memegang tiga *foreign key* ke tabel `ekspedisi`, `penerima`, dan `kurir`.

```
ekspedisi (id_ekspedisi PK, nama_ekspedisi, jenis_layanan)       UNIQUE(nama_ekspedisi, jenis_layanan)
penerima  (id_penerima  PK, nama, nomor_hp, alamat, kode_pos)
kurir     (id_kurir     PK, nama, nomor_hp, jenis_kendaraan)
paket     (id_paket PK, no_resi UNIQUE, nama_pengirim, tanggal_masuk DATE, status, berat_kg,
           id_ekspedisi FK, id_penerima FK, id_kurir FK)
```

Data awal diisi lewat file `database/minpro4_paket.sql`. Isinya adalah sembilan kombinasi ekspedisi dan layanan (J&T Cargo/Reguler/Ekonomi, JNE REG/YES/OKE, SiCepat BEST/REGULAR/HALU) serta dua paket dummy: **RESI001** (JNE REG, 1 kg, penerima Arham) dan **RESI002** (J&T Reguler, 2,5 kg, penerima Rina).

### Cara Menjalankan

Nyalakan MySQL (misalnya lewat Laragon dengan menekan *Start All*), lalu import `database/minpro4_paket.sql` melalui HeidiSQL (*File → Run SQL file...*) atau phpMyAdmin (tab *Import*). Script ini membuat databasenya sendiri dan aman dijalankan berulang kali. Setelah itu buka project di NetBeans, biarkan Maven mengunduh dependency, pastikan user dan password di `DatabaseConnection.java` sesuai dengan MySQL di komputer (bawaan Laragon: user `root`, password kosong), lalu jalankan `Minpro4.java`.

---

## Alur Program

**1.** Program dijalankan melalui `Minpro4.java` (package `main`). Di sini dibuat objek `PaketDAOImpl`, kemudian `ManajemenPaketController` dengan DAO tersebut, lalu `ManajemenPaketView`. Tidak ada data yang dibuat lewat kode karena data awal sudah berada di database. Program kemudian masuk ke menu utama yang berisi 6 pilihan: tambah paket, tampilkan semua paket, cari paket, update paket, hapus paket, dan keluar.

<img width="736" height="233" alt="image" src="https://github.com/user-attachments/assets/259ad33c-64f0-4369-adb1-4f2f2ae0dcad" />


**2.** Menu **1 - Tambah Paket (Create)**. Pengguna mengisi nomor resi (program mengecek ke database apakah resi sudah terdaftar), nama pengirim, dan tanggal masuk (format `DD-MM-YYYY`). Setelah itu mengisi data penerima (nama, alamat, kode pos, nomor HP), lalu memilih ekspedisi (J&T/JNE/SiCepat) beserta jenis layanannya. Selanjutnya pengguna mengisi berat paket (kg), data kurir (nama, nomor HP, jenis kendaraan), dan memilih status paket. Seluruh data kemudian disimpan ke database dalam satu transaksi, dan ID paket, penerima, serta kurir dibuat otomatis oleh database.

<img width="751" height="849" alt="image" src="https://github.com/user-attachments/assets/069db8cc-3a7d-4bd2-898e-5ce21c862e68" />

<img width="664" height="459" alt="image" src="https://github.com/user-attachments/assets/6c020166-cc6a-479c-814e-6342bedc2fb5" />



**3.** Menu **2 - Tampilkan Semua Paket (Read)**. Program mengambil seluruh paket dari database dan menampilkannya beserta ekspedisi, layanan, berat, **biaya kirim**, serta informasi penerima dan kurir. Karena data awal sudah ada di database, menu ini langsung menampilkan RESI001 dan RESI002 tanpa input apa pun. Biaya kirim dihitung otomatis sesuai rumus ekspedisi yang dipakai paket.

<img width="1209" height="860" alt="image" src="https://github.com/user-attachments/assets/d681c26d-38c8-4741-a447-5211b1c763b5" />


**4.** Menu **3 - Cari Paket (Read)**. Pengguna memilih pencarian berdasarkan ID paket atau nomor resi, dan program menjalankan query `SELECT ... WHERE` sesuai pilihan tersebut. Jika data ditemukan, program menampilkan detail paket; jika tidak, tampil pesan "Paket tidak ditemukan!".

<img width="1196" height="599" alt="image" src="https://github.com/user-attachments/assets/f204e72e-a6d1-44c3-8af8-66b8ed13adb7" />


**5.** Menu **4 - Update Paket (Update)**. Pengguna memasukkan nomor resi, lalu memilih data yang diubah: status, data penerima, atau data kurir. Perubahan langsung dijalankan sebagai query `UPDATE` ke database, sehingga tetap tersimpan walaupun program ditutup. Pengguna dapat memilih keluar jika tidak jadi mengubah data.

<img width="1033" height="677" alt="image" src="https://github.com/user-attachments/assets/b19f2737-0df9-464f-b480-c8221049298c" />


**6.** Menu **5 - Hapus Paket (Delete)**. Pengguna memasukkan ID paket yang ingin dihapus. Program menghapus baris paket beserta data penerima dan kurir miliknya dari database, lalu menampilkan pesan hasilnya.

<img width="784" height="373" alt="image" src="https://github.com/user-attachments/assets/8e1051aa-9de1-4625-adeb-979c7b9c3b2b" />


**7.** Menu **6 - Keluar**. Program menampilkan "Program selesai." dan berhenti.

**Validasi input.** Pilihan menu dan input angka (ID, pilihan) divalidasi agar hanya menerima angka. Nomor HP hanya menerima angka, tanggal masuk wajib berformat `DD-MM-YYYY` dan harus berupa tanggal yang valid, nomor resi tidak boleh sama dengan yang sudah ada, dan berat paket wajib berupa angka lebih dari 0. Pilihan menu di luar 1-6 menampilkan pesan "Pilihan menu tidak tersedia!". Jika terjadi kesalahan pada database (misalnya MySQL belum menyala), program menampilkan pesan "Terjadi kesalahan database" dan kembali ke menu tanpa berhenti.

---

## Penerapan Encapsulation dan Inheritance

### Encapsulation

Seluruh atribut pada class model dibuat `private` dan hanya dapat diakses melalui getter dan setter yang bersifat `public`. Contohnya pada `Person` (`id`, `nama`, `nomorHp`), `Paket` (`idPaket`, `noResi`, `status`, `beratKg`, dan lainnya), `Ekspedisi`, `Kurir` (`jenisKendaraan`), dan `Penerima` (`alamat`, `kodePos`). Dengan cara ini data tidak dapat diubah sembarangan dari luar class.

Pada Minpro 4 prinsip yang sama diterapkan juga pada lapisan data. Setter ID (`setId` pada `Person`, `setIdPaket` pada `Paket`, dan `setIdEkspedisi` pada `Ekspedisi`) disediakan supaya DAO dapat menyimpan ID yang dibuat database ke dalam objek setelah proses insert, sementara atribut lainnya tetap hanya bisa diubah lewat setter yang memang dibutuhkan. Pada `DatabaseConnection`, constructor dibuat `private` dan konfigurasi koneksi (`URL`, `USER`, `PASSWORD`) berupa konstanta `private static final`, sehingga detail koneksi tersembunyi dan hanya dapat diakses lewat method `getConnection()`. Pada `PaketDAOImpl`, method bantu seperti `simpanPenerima`, `simpanKurir`, `cariAtauBuatEkspedisi`, dan `petakanPaket` dibuat `private` karena hanya dipakai di dalam class tersebut.

<img width="868" height="635" alt="image" src="https://github.com/user-attachments/assets/52f170bd-e6b1-4c6c-8af4-a87a259f27d9" />


### Inheritance

Pewarisan diterapkan pada dua hierarki.

**Hierarki Person.** `Kurir` dan `Penerima` mewarisi `Person`. Atribut umum (`id`, `nama`, `nomorHp`) cukup ditulis sekali di `Person`, sedangkan atribut khusus ditambahkan di tiap subclass: `jenisKendaraan` pada `Kurir`, serta `alamat` dan `kodePos` pada `Penerima`. Pada Minpro 3 `PengelolaPaket` juga merupakan turunan `Person`, tetapi class tersebut sudah digantikan oleh DAO pada Minpro 4.

**Hierarki Ekspedisi.** `JNE`, `JNT` (J&T), dan `SiCepat` mewarisi `Ekspedisi` yang menyimpan data umum ekspedisi (`idEkspedisi`, `namaEkspedisi`, `jenisLayanan`). Setiap subclass memanggil constructor induk dengan `super(...)`, misalnya `JNE` mengirim nama "JNE" ke `Ekspedisi`.

<img width="865" height="188" alt="image" src="https://github.com/user-attachments/assets/d6c0b11e-c921-4f74-ba0d-3e8eafc5f160" />


---

## Penerapan Polymorphism dan Abstraction

### Abstraction

Abstraction diterapkan pada dua abstract class dan satu interface.

**1. `Person` (abstract class).** `Person` tidak pernah dibuat objeknya secara langsung, yang ada selalu `Kurir` atau `Penerima`. Karena itu `Person` dijadikan abstract class dengan abstract method `getPeran()`. Method ini tidak memiliki isi dan wajib diimplementasikan semua subclass, karena setiap jenis orang pasti memiliki peran, tetapi jawabannya berbeda-beda.

**2. `Ekspedisi` (abstract class).** JNE, J&T, dan SiCepat sama-sama ekspedisi dengan data yang sama, tetapi rumus ongkirnya berbeda. Karena itu `Ekspedisi` memiliki abstract method `hitungOngkir(double beratKg)`, dan rumus sebenarnya diserahkan kepada `JNE`, `JNT`, dan `SiCepat`. Konsekuensinya `Ekspedisi` tidak bisa dibuat objeknya langsung dan harus dibuat sebagai salah satu turunannya (misalnya `new JNE(1, "REG")`).

**3. `PaketDAO` (interface).** Interface ini hanya berisi daftar method akses data tanpa isi (`tambahPaket`, `getDaftarPaket`, `cariPaket`, `updateStatus`, `updatePenerima`, `updateKurir`, `hapusPaket`). Controller hanya mengenal interface ini, bukan cara datanya disimpan. Jika kelak penyimpanan diganti (misalnya ke database lain atau file), cukup dibuat class baru yang mengimplementasikan `PaketDAO` tanpa mengubah controller dan view (*loose coupling*). Peran ini sama seperti `PenyimpananPaket` pada Minpro 3, tetapi sekarang dengan penyimpanan yang nyata ke database.

<img width="588" height="111" alt="image" src="https://github.com/user-attachments/assets/b9b11d37-a764-4fd7-bccd-529cd78f623f" />

<img width="1018" height="571" alt="image" src="https://github.com/user-attachments/assets/d5265169-436c-4793-837a-b470834001ca" />



### Polymorphism - Overriding

Overriding terjadi ketika subclass menulis ulang method milik induknya (ditandai `@Override`).

`getPeran()` pada `Kurir` dan `Penerima` mengimplementasikan abstract method milik `Person` (masing-masing mengembalikan "Kurir" dan "Penerima"). `getInfo()` pada `Kurir` dan `Penerima` menimpa `getInfo()` milik `Person`; setiap subclass memanggil `super.getInfo()` lalu menambahkan data khasnya, yaitu kendaraan untuk kurir serta alamat dan kode pos untuk penerima. `hitungOngkir(double beratKg)` pada `JNE`, `JNT`, dan `SiCepat` mengimplementasikan abstract method milik `Ekspedisi` dengan tarif per kilogram yang berbeda sesuai jenis layanan, misalnya REG/YES/OKE pada JNE, Cargo/Reguler/Ekonomi pada J&T, dan BEST/REGULAR/HALU pada SiCepat. Selain itu `PaketDAOImpl` meng-override seluruh method yang dideklarasikan pada interface `PaketDAO`.

### Polymorphism - Overloading

Overloading terjadi ketika beberapa method memiliki nama yang sama tetapi parameter berbeda dalam satu class.

`cariPaket(int id)` dan `cariPaket(String resi)` terdapat pada `PaketDAO`, `PaketDAOImpl`, dan `ManajemenPaketController`. Java memilih versi yang dipanggil berdasarkan tipe argumen: angka untuk pencarian ID (query `WHERE id_paket = ?`), teks untuk pencarian nomor resi (query `WHERE no_resi = ?`). `hitungOngkir(double beratKg)` dan `hitungOngkir(double beratKg, double nilaiBarang)` pada `Ekspedisi`, di mana versi kedua menambahkan biaya asuransi 0,2% dari nilai barang di atas ongkir dasar. Constructor `Paket` juga memiliki dua versi: versi lengkap dengan parameter `beratKg`, dan versi tanpa berat yang otomatis memakai berat 1 kg.

### Polymorphism - Penerapan saat program berjalan

Polymorphism terlihat nyata di tiga tempat.

**Biaya kirim.** Pada `Paket`, atribut `ekspedisi` bertipe `Ekspedisi`, tetapi objek aslinya bisa berupa `JNE`, `JNT`, atau `SiCepat`. Method `hitungBiayaKirim()` cukup memanggil `ekspedisi.hitungOngkir(beratKg)`, dan Java memilih rumus yang sesuai dengan objek aslinya saat program berjalan, tanpa percabangan `if` pada class `Paket`. Jika kelak ada ekspedisi baru seperti Anteraja atau Ninja Xpress, cukup dibuat satu class baru turunan `Ekspedisi` tanpa mengubah class `Paket`.

**Objek yang dibentuk dari database.** Di database, ekspedisi hanya tersimpan sebagai teks nama dan layanan. Saat membaca data, `PaketDAOImpl` memanggil `EkspedisiFactory.buat(...)` yang membuat objek `JNE`, `JNT`, atau `SiCepat` sesuai nama yang tersimpan, lalu objek tersebut dipegang sebagai tipe `Ekspedisi`. Dengan begitu perilaku polymorphism `hitungOngkir` tetap berjalan walaupun objeknya lahir dari hasil query, bukan dari `new` di `main`.

**Informasi penerima dan kurir.** Pada `ManajemenPaketView.tampilkanDetailPaket()`, penerima dan kurir dimasukkan ke dalam satu array bertipe `Person`, kemudian `getInfo()` dipanggil dalam satu perulangan. Hasilnya berbeda untuk setiap elemen karena Java menjalankan `getInfo()` milik `Penerima` dan `Kurir` sesuai objek aslinya.

---

## Penerapan JDBC

JDBC (Java Database Connectivity) adalah API standar Java untuk menghubungkan program dengan database. Pada project ini JDBC dipakai bersama driver **MySQL Connector/J** yang ditambahkan sebagai dependency pada `pom.xml` (`com.mysql:mysql-connector-j`), lalu dikelola lewat pola **DAO** sehingga seluruh kode SQL terkumpul di satu tempat dan tidak tercampur dengan tampilan.

### 1. Koneksi database (`DatabaseConnection`)

Class `DatabaseConnection` menyimpan alamat database, user, dan password, lalu menyediakan method `getConnection()` yang memanggil `DriverManager.getConnection(...)`. Setiap kali dipanggil, method ini membuat koneksi baru, sehingga koneksi aman ditutup otomatis oleh `try-with-resources` di DAO tanpa mengganggu method lain. Class `TestKoneksi` dipakai untuk menguji koneksi secara terpisah dan mencetak "Koneksi Berhasil" jika database dapat dihubungi.

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/minpro4"
    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Makassar";
private static final String USER = "root";
private static final String PASSWORD = "";

public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
```

### 2. Pola DAO (`PaketDAO` dan `PaketDAOImpl`)

Alur pemanggilan program adalah **View → Controller → DAO → Database**. View hanya mengurus input dan output, controller meneruskan permintaan ke `PaketDAO`, dan `PaketDAOImpl` yang menjalankan perintah SQL. Karena controller hanya bergantung pada interface `PaketDAO`, lapisan data dapat diganti tanpa mengubah controller dan view.

### 3. PreparedStatement

Seluruh query memakai `PreparedStatement` dengan tanda tanya (`?`) sebagai placeholder, dan nilainya diisi lewat `setString`, `setInt`, `setDouble`, atau `setDate`. Cara ini mencegah serangan *SQL injection* karena input pengguna tidak pernah digabung langsung ke teks SQL, dan juga membuat query lebih rapi. Contoh pada pencarian berdasarkan nomor resi:

```java
String sql = SELECT_PAKET + " WHERE p.no_resi = ?";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, resi);
    try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? petakanPaket(rs) : null;
    }
}
```

### 4. Operasi CRUD lewat JDBC

| Fitur | Method DAO | Perintah SQL |
|---|---|---|
| Create | `tambahPaket` | `INSERT INTO ekspedisi/penerima/kurir/paket` |
| Read semua | `getDaftarPaket` | `SELECT ... JOIN` keempat tabel, `ORDER BY id_paket` |
| Read cari | `cariPaket(int)` / `cariPaket(String)` | `SELECT ... WHERE id_paket = ?` / `WHERE no_resi = ?` |
| Update | `updateStatus`, `updatePenerima`, `updateKurir` | `UPDATE paket/penerima/kurir SET ... WHERE ...` |
| Delete | `hapusPaket` | `DELETE FROM paket`, `penerima`, dan `kurir` |

Perintah `executeUpdate()` dipakai untuk INSERT, UPDATE, dan DELETE, sedangkan `executeQuery()` dipakai untuk SELECT dan menghasilkan `ResultSet`.

### 5. Membaca data dengan JOIN dan `ResultSet`

Data satu paket tersebar di empat tabel, sehingga query SELECT menggabungkannya dengan `JOIN` pada foreign key. Hasilnya dibaca baris demi baris dari `ResultSet`, lalu method `petakanPaket(rs)` merakit satu baris itu menjadi objek `Paket` lengkap dengan objek `Penerima`, `Kurir`, dan `Ekspedisi` di dalamnya. Dengan begitu bagian lain program tetap bekerja dengan objek, bukan dengan baris tabel.

### 6. ID otomatis dan transaksi

ID setiap tabel dibuat otomatis oleh database (`AUTO_INCREMENT`). Setelah INSERT, DAO mengambil ID baru lewat `Statement.RETURN_GENERATED_KEYS` dan `getGeneratedKeys()`, lalu menyimpannya ke objek. Proses **tambah paket** menyimpan data ke beberapa tabel sekaligus (ekspedisi dicari dulu dan baru dibuat jika belum ada, kemudian penerima, kurir, dan terakhir paket), sedangkan **hapus paket** menghapus paket, penerima, dan kurir miliknya. Karena itu keduanya dijalankan dalam satu **transaksi**: `setAutoCommit(false)` di awal, `commit()` jika semua berhasil, dan `rollback()` jika ada yang gagal, sehingga data tidak pernah tersimpan atau terhapus setengah-setengah. Paket dihapus lebih dulu sebelum penerima dan kurir karena tabel `paket` memegang foreign key ke keduanya.

### 7. Konversi tanggal

Di program tanggal diketik dan ditampilkan dengan format `DD-MM-YYYY` (tipe `String`), sedangkan di database kolom `tanggal_masuk` bertipe `DATE`. DAO mengonversinya dengan `DateTimeFormatter` dan `java.time.LocalDate`: saat menyimpan, teks diubah menjadi `java.sql.Date` lewat `ps.setDate(...)`, dan saat membaca, `rs.getDate(...)` diubah kembali menjadi teks `DD-MM-YYYY`.

### 8. Penutupan resource dan penanganan error

Semua `Connection`, `PreparedStatement`, dan `ResultSet` dibuka dengan **try-with-resources** sehingga otomatis ditutup walaupun terjadi error. Method DAO mendeklarasikan `throws SQLException`, dan error tersebut ditangkap di `ManajemenPaketView` pada perulangan menu, sehingga ketika database bermasalah program menampilkan pesan dan kembali ke menu, tidak berhenti mendadak.

### 9. Data awal lewat file SQL

Data awal tidak dibuat di kode Java, melainkan di `database/minpro4_paket.sql` lewat perintah `INSERT`. Karena itu begitu database di-import, menu *Tampilkan Semua Paket* langsung menampilkan RESI001 dan RESI002 tanpa perlu menambah data dari awal.

<img width="1677" height="674" alt="image" src="https://github.com/user-attachments/assets/1f95b192-cc2a-49a0-8f9a-4d8b736a9eee" />
