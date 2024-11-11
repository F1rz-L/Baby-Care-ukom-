<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Pelanggan extends Model
{
    use HasFactory;
    protected $primaryKey = 'id_pelanggan';

    protected $fillable = [
        'nama',
        'alamat',
        'no_telepon',
        'nik',
    ];
}
