<?php

namespace App\Http\Controllers;

use App\Models\Barang;
use App\Models\Pinjaman;
use Illuminate\Http\Request;

class StatsController extends Controller
{
    public function totalBarang()
    {
        return Barang::count();
    }

    public function totalPinjaman()
    {
        return Pinjaman::where('status', 'Dipinjam')->count();
    }

    public function totalBarangDipinjam()
    {
        return Pinjaman::where('status', 'Dikembalikan')->count();
    }
}
