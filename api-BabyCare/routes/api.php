<?php

use App\Http\Controllers\BarangController;
use App\Http\Controllers\KategoriController;
use App\Http\Controllers\PelangganController;
use App\Http\Controllers\PinjamanController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::apiResource('/barang', BarangController::class);
Route::apiResource('/kategori', KategoriController::class);
Route::apiResource('/pinjaman', PinjamanController::class);
Route::apiResource('/pelanggan', PelangganController::class);

Route::post('/pinjam', [PinjamanController::class, 'pinjam']);