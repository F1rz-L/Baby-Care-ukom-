<?php

namespace App\Http\Controllers;

use App\Models\Pelanggan;
use Illuminate\Http\Request;

class PelangganController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $pelanggans = Pelanggan::all();
        return response()->json([
            'success' => true,
            'message' => 'List of Pelanggan',
            'data' => $pelanggans
        ], 200);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validatedData = $request->validate([
            'id_peminjaman' => 'nullable|integer',
            'nama' => 'required|string|max:255',
            'alamat' => 'required|string|max:255',
            'nomor_telepon' => 'required|string|max:20|regex:/^[0-9\-\+\(\)\s]+$/',
            'nik' => 'nullable|string|max:12',
        ]);

        $pelanggan = Pelanggan::create($validatedData);

        return response()->json([
            'success' => true,
            'message' => 'Pelanggan created successfully',
            'data' => $pelanggan
        ], 201);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $pelanggan = Pelanggan::find($id);

        if (!$pelanggan) {
            return response()->json([
                'success' => false,
                'message' => 'Pelanggan not found',
                'data' => null
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Show Data Pelanggan',
            'data' => $pelanggan
        ], 200);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $pelanggan = Pelanggan::find($id);

        if (!$pelanggan) {
            return response()->json([
                'success' => false,
                'message' => 'Pelanggan not found',
                'data' => null
            ], 404);
        }

        $validatedData = $request->validate([
            'id_peminjaman' => 'nullable|integer',
            'nama' => 'required|string|max:255',
            'alamat' => 'required|string|max:255',
            'nomor_telepon' => 'required|string|max:20',
            'nik' => 'nullable|string|max:12',
        ]);

        $pelanggan->update($validatedData);

        return response()->json([
            'success' => true,
            'message' => 'Pelanggan updated successfully',
            'data' => $pelanggan
        ], 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        $pelanggan = Pelanggan::find($id);
        $savedPelanggan = Pelanggan::find($id);

        if (!$pelanggan) {
            return response()->json([
                'success' => false,
                'message' => 'Pelanggan not found',
            ], 404);
        }

        $pelanggan->delete();

        return response()->json([
            'success' => true,
            'message' => 'Pelanggan deleted successfully',
            'data' => $savedPelanggan
        ], 200);
    }
}
