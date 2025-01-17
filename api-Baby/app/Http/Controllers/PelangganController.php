<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Models\Pelanggan;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class PelangganController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $pelanggans = Pelanggan::all();
        return new ResponseResource(200, "List Semua Data Pelanggan", $pelanggans);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'id_peminjaman' => 'nullable|integer',
            'nama' => 'required|string|max:255',
            'alamat' => 'required|string|max:255',
            'nomor_telepon' => 'required|string|max:20|regex:/^[0-9\-\+\(\)\s]+$/',
            'nik' => 'nullable|string|max:12',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $pelanggan = Pelanggan::create($validator->validated());

        return new ResponseResource(200, "Data Pelanggan Berhasil Ditambahkan", $pelanggan);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        $pelanggan = Pelanggan::find($id);

        if (!$pelanggan) {
            return new ResponseResource(404, 'Pelanggan not found');
        }

        return new ResponseResource(200, 'Show Data Pelanggan', $pelanggan);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $pelanggan = Pelanggan::find($id);

        if (!$pelanggan) {
            return new ResponseResource(404, 'Pelanggan not found');
        }

        $validator = Validator::make($request->all(), [
            'id_peminjaman' => 'nullable|integer',
            'nama' => 'required|string|max:255',
            'alamat' => 'required|string|max:255',
            'nomor_telepon' => 'required|string|max:20|regex:/^[0-9\-\+\(\)\s]+$/',
            'nik' => 'nullable|string|max:12',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $pelanggan->update($validator->validated());

        return new ResponseResource(200, 'Pelanggan updated successfully', $pelanggan);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
    {
        try {
            // Attempt to find the Pelanggan
            $pelanggan = Pelanggan::find($id);

            if (!$pelanggan) {
                return new ResponseResource(404, 'Pelanggan not found');
            }

            // Store the pelanggan data before deletion for response
            $deletedData = $pelanggan->toArray();

            // Delete the pelanggan
            $pelanggan->delete();

            return new ResponseResource(200, 'Pelanggan deleted successfully', $deletedData);
        } catch (\Exception $e) {
            return new ResponseResource(500, 'Failed to delete Pelanggan: ' . $e->getMessage());
        }
    }
}
