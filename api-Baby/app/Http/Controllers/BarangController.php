<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Models\Barang;
use App\Models\Kategori;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;

class BarangController extends Controller
{
    public function index()
    {
        $barang = Barang::all();
        return new ResponseResource(200, "List Semua Data Barang", $barang);
    }

    public function show($id)
    {
        $barang = Barang::find($id);

        if (!$barang) {
            return new ResponseResource(404, 'Data Barang Tidak Ditemukan');
        } else {
            return new ResponseResource(200, "Show Data Barang", $barang);
        }
    }

    public function store(Request $request)
    {
        // Validate the request data
        $validator = Validator::make($request->all(), [
            'id_kategori' => 'required|exists:kategoris,id',
            'namabarang' => 'required|string|max:255',
            'gambar' => 'nullable|file|mimes:jpeg,png,jpg,gif,webp|max:2048', // Validate file type and size
            'deskripsi' => 'nullable|string',
            'harga' => 'required|numeric|min:0',
            'merk' => 'required|string|max:50',
        ]);

        // Check for validation errors
        if ($validator->fails()) {
            return new ResponseResource(400, 'Validation failed', $validator->errors());
        }

        // Retrieve validated data
        $validatedData = $validator->validated();

        // Process file upload if present
        $gambarPath = null;
        if ($request->hasFile('gambar')) {
            $file = $request->file('gambar');
            $gambarPath = $file->store('uploads/gambar', 'public'); // Save file to public storage
        }

        // Retrieve the Kategori instance
        $kategori = Kategori::find($validatedData['id_kategori']);

        if (!$kategori) {
            return new ResponseResource(404, 'Kategori tidak ditemukan.');
        }

        // Prepare data for the Barang instance
        $barangData = [
            'namabarang' => $validatedData['namabarang'],
            'id_kategori' => $kategori->id,  // Ensuring foreign key is set
            'kategori' => $kategori->kategori, // Store category name
            'gambar' => $gambarPath, // Save uploaded file path or null
            'deskripsi' => $validatedData['deskripsi'] ?? null,
            'harga' => $validatedData['harga'],
            'merk' => $validatedData['merk'],
        ];

        // Create Barang
        $barang = Barang::create($barangData);

        // Return a success response
        return new ResponseResource(200, "Data Barang Berhasil Ditambahkan", $barang);
    }



    public function update(Request $request, $id)
    {
        // Validate the incoming request
        $validator = Validator::make($request->all(), [
            'namabarang' => 'required|string|max:255',
            'id_kategori' => 'required|integer|exists:kategoris,id',
            'id_peminjam' => 'nullable|integer|exists:pelanggans,id',
            'gambar' => 'nullable|file|mimes:jpeg,png,jpg,gif,webp|max:2048', // Validate file type and size
            'deskripsi' => 'nullable|string',
            'harga' => 'required|numeric|min:0',
            'merk' => 'required|string|max:255',
            'status' => 'nullable|string|in:Tersedia,Dipinjam,Tidak Tersedia',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, 'Validation failed', $validator->errors());
        }

        // Find the Barang record
        $barang = Barang::find($id);
        if (!$barang) {
            return new ResponseResource(404, 'Data Barang Tidak Ditemukan!');
        }

        // Process uploaded file (if exists)
        $path = $barang->gambar; // Default to the current image
        if ($request->hasFile('gambar')) {
            $file = $request->file('gambar');
            $path = $file->store('uploads/gambar', 'public'); // Save to storage/app/public/uploads/gambar

            // Optionally delete the old image if it exists
            if (!empty($barang->gambar) && Storage::disk('public')->exists($barang->gambar)) {
                Storage::disk('public')->delete($barang->gambar);
            }
        }

        // Prepare data for update
        $data = $request->only(['namabarang', 'id_kategori', 'id_peminjam', 'deskripsi', 'harga', 'merk', 'status']);
        $data['gambar'] = $path;

        // Update the record
        $barang->update($data);

        return new ResponseResource(200, "Data Barang Berhasil Diubah", $barang);
    }




    public function destroy($id)
    {
        $barang = Barang::find($id);

        if (!$barang) {
            return new ResponseResource(404, 'Data Barang Tidak Ditemukan!');
        } else {
            $barang->delete();
            return new ResponseResource(200, 'Data Barang Berhasil Dihapus', $barang);
        }
    }
}
