# Cara paling mudah build APK tanpa Android Studio

Project ini sudah disediakan dengan GitHub Actions. GitHub akan menyediakan Android SDK + Java 17 + Gradle dan membina APK untuk anda.

1. Buat satu repository GitHub kosong, contoh: `PregnaCareQR`.
2. Upload SEMUA kandungan folder project ini ke repository tersebut (bukan fail ZIP sebagai satu-satunya fail).
3. Di GitHub, buka tab **Actions**.
4. Pilih workflow **Build PregnaCare QR APK**.
5. Tekan **Run workflow** jika belum bermula secara automatik.
6. Tunggu sampai status hijau.
7. Buka run tersebut → bahagian **Artifacts** → download `PregnaCareQR-debug-apk`.
8. Extract ZIP artifact itu. APK ialah `app-debug.apk`.

## Alternatif Windows

Jika komputer sudah mempunyai Java 17 dan Android SDK yang dikonfigurasi, double-click `BUILD_APK.bat`.

## Nota MyKad

UI dan aliran aplikasi sudah siap, tetapi bacaan MyKad sebenar masih memerlukan integrasi rasmi SecureMetric MYID/MyKad SDK/API. Butang sample data hanya untuk menguji aliran app.
