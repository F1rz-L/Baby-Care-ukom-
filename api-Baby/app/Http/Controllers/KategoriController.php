<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Models\Kategori;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class KategoriController extends Controller
{
    public function index()
    {
        $data = Kategori::all();

        return new ResponseResource(200, "List Semua Data Kategori", $data);
    }

    public function show($id)
    {
        $data = Kategori::find($id);

        if (!$data) {
            return new ResponseResource(404, 'Data Kategori Tidak Ditemukan');
        } else {
            return new ResponseResource(200, "Show Data Kategori", $data);
        }
    }

    public function update(Request $request, $id)
    {
        // Validate the request data
        $validator = Validator::make($request->all(), [
            'kategori' => 'required|string|max:255',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        // Find the category by ID
        $kategori = Kategori::find($id);

        if (!$kategori) {
            return new ResponseResource(404, 'Data Kategori Tidak Ditemukan');
        }

        // Update the category with validated data
        $kategori->update(['kategori' => $request->kategori]);

        return new ResponseResource(200, "Data Kategori Berhasil Diubah", $kategori);
    }


    public function store(Request $request)
    {
        $data = [
            'kategori' => $request->kategori,
        ];

        $kategori = Kategori::create($data);

        return new ResponseResource(200, "Data Kategori Berhasil Ditambahkan", $kategori);
    }

    public function destroy($id)
    {
        $data = Kategori::find($id);

        if (!$data) {
            return new ResponseResource(404, 'Data Kategori Tidak Ditemukan');
        } else {
            $data->delete();
            return new ResponseResource(200, "Data Kategori Berhasil Dihapus", $data);
        }
    }
}
