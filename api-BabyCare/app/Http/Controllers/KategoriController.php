<?php

namespace App\Http\Controllers;

use App\Models\Kategori;
use Illuminate\Http\Request;

class KategoriController extends Controller
{
    public function index()
    {
        $data = Kategori::all();

        return response()->json([
            'success' => true,
            'message' => 'List Semua Data Kategori',
            'data' => $data
        ], 200);
    }

    public function show($id)
    {
        $data = Kategori::find($id);

        if (!$data) {
            return response()->json([
                'success' => false,
                'message' => 'Data Kategori Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            return response()->json([
                'success' => true,
                'message' => 'Show Data Kategori',
                'data' => $data
            ], 200);
        }
    }

    public function update(Request $request, $id)
    {
        $data = [
            'kategori' => $request->kategori,
        ];

        $kategori = Kategori::find($id);

        if (!$kategori) {
            return response()->json([
                'success' => false,
                'message' => 'Data Kategori Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            $kategori->update($data);
            return response()->json([
                'success' => true,
                'message' => 'Data Kategori Berhasil Diubah',
                'data' => $kategori
            ], 200);
        }
    }

    public function store(Request $request)
    {
        $data = [
            'kategori' => $request->kategori,
        ];

        $kategori = Kategori::create($data);

        return response()->json([
            'success' => true,
            'message' => 'Data Kategori Berhasil Ditambahkan',
            'data' => $kategori
        ], 200);
    }

    public function destroy($id)
    {
        $data = Kategori::find($id);

        if (!$data) {
            return response()->json([
                'success' => false,
                'message' => 'Data Kategori Tidak Ditemukan',
                'data' => ''
            ], 404);
        } else {
            $data->delete();
            return response()->json([
                'success' => true,
                'message' => 'Data Kategori Berhasil Dihapus',
                'data' => $data
            ], 200);
        }
    }
}
