<?php

namespace App\Http\Controllers;

use App\Models\Barang;
use App\Models\Pinjaman;
use Carbon\Carbon;
use Illuminate\Http\Request;

class PinjamanController extends Controller
{
    public function pinjam(Request $request)
    {
        $tglPinjam = Carbon::now(); // Current date for 'tgl_pinjam'
        $tglKembali = $tglPinjam->copy()->addWeeks((int) $request->durasiminggu); // Add weeks based on 'durasiminggu'

        // Create and save a new Pinjaman record
        $pinjaman = Pinjaman::create([
            'id_peminjam' => $request->id_pelanggan,
            'id_barang' => $request->id_barang,
            'tgl_pinjam' => $tglPinjam,
            'tgl_kembali' => $tglKembali,
            'status' => 'Dipinjam', // Default to 'Dipinjam' status
            'denda' => 0, // Initial denda value, update as necessary
        ]);

        // Update barang status
        Barang::where('id_barang', $request->id_barang)->update(['status' => 'Dipinjam']);

        // Return response
        return response()->json([
            'message' => 'Item has been borrowed successfully!',
            'data' => $pinjaman
        ]);
    }

    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $data = Pinjaman::all();

        return response()->json([
            'success' => true,
            'message' => 'List Semua Data Pinjaman',
            'data' => $data
        ], 200);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     */
    public function show(Pinjaman $pinjaman)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(Pinjaman $pinjaman)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Pinjaman $pinjaman)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Pinjaman $pinjaman)
    {
        //
    }
}
