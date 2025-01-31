<?php

namespace App\Http\Controllers;

use App\Models\Barang;
use App\Models\Kategori;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class BarangController extends Controller
{
    public function index()
    {
        $barang = Barang::all();
        return response()->json([
            'success' => true,
            'message' => 'List Semua Data Barang',
            'data' => $barang
        ], 200);
    }

    public function show($id)
    {
        $barang = Barang::find($id);

        if (!$barang) {
            return response()->json([
                'success' => false,
                'message' => 'Data Barang Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            return response()->json([
                'success' => true,
                'message' => 'Show Data Barang',
                'data' => $barang
            ], 200);
        }
    }

    public function store(Request $request)
    {
        // Validate required fields
        $validatedData = $request->validate([
            'id_kategori' => 'required|exists:kategoris,id',
            'namabarang' => 'required|string|max:255',
            'deskripsi' => 'nullable|string',
            'harga' => 'required|numeric',
            'merk' => 'required|string|max:100',
        ]);

        // Retrieve the Kategori instance
        $kategori = Kategori::find($validatedData['id_kategori']);

        // Prepare data for the Barang instance
        $barangData = [
            'namabarang' => $validatedData['namabarang'],
            'id_kategori' => $kategori->id,  // Ensuring foreign key is set
            'kategori' => $kategori->kategori,        // Store category name
            'deskripsi' => $validatedData['deskripsi'],
            'harga' => $validatedData['harga'],
            'merk' => $validatedData['merk'],
        ];

        // Create Barang
        $barang = Barang::create($barangData);

        // Return success response
        return response()->json([
            'success' => true,
            'message' => 'Data Barang Berhasil Ditambahkan',
            'data' => $barang
        ], 200);
    }

    public function update(Request $request, $id)
    {
        $barang = Barang::find($id);

        if (!$barang) {
            return response()->json([
                'success' => false,
                'message' => 'Data Barang Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            $data = [
                'namabarang' => $request->namabarang,
                'id_jenis' => $request->id_jenis,
                'deskripsi' => $request->deskripsi,
                'harga' => $request->harga,
                'merk' => $request->merk,
                'status' => $request->status
            ];

            $barang->update($data);
            return response()->json([
                'success' => true,
                'message' => 'Data Barang Berhasil Diubah',
                'data' => $barang
            ], 200);
        }
    }

    public function destroy($id)
    {
        $barang = Barang::find($id);

        if (!$barang) {
            return response()->json([
                'success' => false,
                'message' => 'Data Barang Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            $barang->delete();
            return response()->json([
                'success' => true,
                'message' => 'Data Barang Berhasil Dihapus',
                'data' => $barang
            ], 200);
        }
    }
}
