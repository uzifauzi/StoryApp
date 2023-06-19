# Story App

Ini adalah repository untuk aplikasi Story App yang dibangun menggunakan bahasa Kotlin dan mengadopsi arsitektur MVVM.

## Fitur Aplikasi

Aplikasi Story App memiliki beberapa fitur utama:

1. **Register dan Login User**: Pengguna dapat mendaftar dan login ke aplikasi menggunakan akun pengguna yang telah terdaftar sebelumnya.
2. **View All User Story**: Pengguna dapat melihat daftar cerita dari semua pengguna yang ada di aplikasi.
3. **Upload Story**: Pengguna dapat mengunggah cerita mereka sendiri ke aplikasi untuk dibagikan dengan pengguna lainnya.
4. **View Story Location**: Pengguna dapat melihat lokasi cerita yang telah diunggah oleh pengguna lainnya di peta.

## Bahasa yang Digunakan

Aplikasi ini dikembangkan menggunakan bahasa Kotlin, sebuah bahasa pemrograman yang populer untuk pengembangan aplikasi Android.

## Arsitektur MVVM

Aplikasi Story App mengikuti arsitektur MVVM (Model-View-ViewModel) untuk memisahkan logika bisnis, tampilan, dan data. Berikut adalah komponen utama dari arsitektur MVVM dalam aplikasi ini:

- **Model**: Mewakili data dan logika bisnis aplikasi. Ini dapat berupa kelas atau struktur data yang berhubungan dengan pengolahan data, jaringan, penyimpanan lokal, dan lainnya.
- **View**: Bertanggung jawab untuk menampilkan elemen antarmuka pengguna (UI) dan menerima interaksi dari pengguna. Dalam aplikasi ini, View dapat berupa aktivitas, fragmen, atau tampilan kustom.
- **ViewModel**: Menyediakan data dan fungsi yang diperlukan oleh View. ViewModel berinteraksi dengan Model untuk mendapatkan data dan mengubahnya sesuai kebutuhan. Dalam aplikasi ini, ViewModel akan berkomunikasi dengan repository untuk mengakses data cerita pengguna dan fungsi lainnya.

## Instalasi

Untuk menjalankan aplikasi ini di lingkungan pengembangan lokal Anda, ikuti langkah-langkah berikut:

1. Pastikan Anda telah menginstal Android Studio di komputer Anda.
2. Clone repository ini ke direktori lokal Anda.
3. Buka Android Studio dan pilih opsi "Open an existing Android Studio project".
4. Arahkan ke direktori tempat Anda telah melakukan cloning repository ini, lalu pilih proyek "Story App".
5. Tunggu Android Studio mengimpor proyek dan mempersiapkannya untuk digunakan.
6. Sambungkan perangkat Android atau gunakan emulator untuk menjalankan aplikasi.

## Kontribusi

Jika Anda ingin berkontribusi pada proyek ini, silakan ikuti langkah-langkah berikut:

1. Fork repository ini.
2. Buat branch baru untuk fitur atau perbaikan yang akan Anda tambahkan.
3. Commit perubahan Anda dan push ke branch yang telah Anda buat di forked repository.
4. Buat pull request ke branch utama repository ini.
5. Tim pengembang akan melakukan review terhadap perubahan yang Anda usulkan.

---

Terima kasih telah menggunakan aplikasi Story App! Jika Anda memiliki pertanyaan lebih lanjut, jangan ragu untuk menghubungi saya.
