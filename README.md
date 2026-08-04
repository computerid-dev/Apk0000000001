# EchoChat

Aplikasi chat pribadi, ringan, dan sepenuhnya lokal — alternatif WhatsApp tanpa server dan tanpa akun email/OTP.

**Slogan:** Ngobrol tanpa jejak, sepenuhnya milikmu
**Package:** `com.echochat.cid`

## Cara kerja

- Saat pertama kali dibuka, aplikasi membuat **akun tamu** otomatis: sebuah kode ID unik (format `XXXX-XXXX`) dibuat secara acak di perangkat, lalu kamu tinggal isi nama panggilan.
- Untuk menambah teman, masukkan **kode ID milik teman** + beri **nama panggilan** untuk teman itu di aplikasimu sendiri.
- Semua data (daftar teman & isi pesan) disimpan di **database lokal (Room/SQLite)** di perangkat — tidak dikirim ke server mana pun.

> Catatan: karena versi ini murni database lokal tanpa backend, pesan yang kamu kirim tersimpan di perangkatmu sendiri. Supaya dua perangkat benar-benar bisa saling kirim-terima pesan secara real-time, perlu lapisan server/backend tambahan (misalnya Firebase) di versi berikutnya.

## Build APK

### Lewat Android Studio
Buka folder ini di Android Studio → Run/Build → `app-debug.apk` akan muncul di `app/build/outputs/apk/debug/`.

### Lewat GitHub Actions
Push ke branch `main` (atau jalankan manual lewat tab **Actions** → **Build APK** → *Run workflow*). Hasil `app-debug.apk` bisa diunduh dari bagian **Artifacts** pada run tersebut.

## Struktur proyek

```
app/src/main/java/com/echochat/cid/
├── data/       Entity, DAO, Room database
├── ui/         Activity & adapter RecyclerView
└── util/       SessionManager (akun tamu), UidGenerator
```
