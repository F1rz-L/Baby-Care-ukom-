<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('pinjamen', function (Blueprint $table) {
            $table->increments("id_pinjaman");
            $table->integer('id_peminjam')->nullable();
            $table->integer('id_barang')->nullable();
            $table->date('tgl_pinjam');
            $table->date('tgl_kembali');
            $table->enum('status', ['Dipinjam', 'Tersedia']);
            $table->integer('denda');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('pinjamen');
    }
};
