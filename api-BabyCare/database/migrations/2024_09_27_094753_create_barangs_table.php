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
        Schema::create('barangs', function (Blueprint $table) {
            $table->id();
            $table->integer('id_kategori')->nullable();
            $table->integer('id_peminjam')->nullable();
            $table->string('namabarang');
            $table->string('merek');
            $table->text('deskripsi');
            $table->integer('harga');
            $table->string('gambar')->nullable();
            $table->enum('status', ['Tersedia', 'Dipinjam', 'Tidak Tersedia'])->default('Tersedia');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('barangs');
    }
};
