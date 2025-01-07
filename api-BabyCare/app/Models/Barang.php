<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Barang extends Model
{
    use HasFactory;

    protected $fillable = [
        'id_kategori',
        'id_peminjam',
        'namabarang',
        'deskripsi',
        'merk',
        'harga',
        'gambar',
        'status'
    ];
}