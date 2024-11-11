<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Pinjaman extends Model
{
    use HasFactory;

    protected $fillable = [
        'id_peminjam',
        'id_barang',
        'tgl_pinjam',
        'tgl_kembali',
        'status',
        'denda',
    ];
}
