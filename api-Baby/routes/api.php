<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\BarangController;
use App\Http\Controllers\KategoriController;
use App\Http\Controllers\PelangganController;
use App\Http\Controllers\PinjamanController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::middleware([
    'api',
])->group(function ($router) {
    Route::group([
        'prefix' => 'auth',
        'middleware' => 'accessToken',
    ], function ($router) {
        Route::post('/logout', [AuthController::class, 'logout']);
        Route::get('/me', [AuthController::class, 'userProfile']);
    });

    Route::group([
        'prefix' => 'auth',
    ], function ($router) {
        Route::post('/login', [AuthController::class, 'login']);
        Route::post('/register', [AuthController::class, 'register']);
        Route::post('/verify', [AuthController::class, 'sendOtpMail']);
        Route::put('/verify', [AuthController::class, 'verifyByOtp']);
        Route::post('/forgot-password', [AuthController::class, 'sendForgotPasswordMail']);
        Route::put('/forgot-password', [AuthController::class, 'updatePassword']);
    });
});

Route::middleware([
    'api',
    'accessToken'
])->group(function ($router) {
    Route::apiResource('/barang', BarangController::class);
    Route::apiResource('/kategori', KategoriController::class);
    Route::apiResource('/pinjaman', PinjamanController::class);
    Route::apiResource('/pelanggan', PelangganController::class);

    Route::post('/pinjam', [PinjamanController::class, 'pinjam']);
    Route::post('/pinjaman/{id}/kembali', [PinjamanController::class, 'kembali']);
});
