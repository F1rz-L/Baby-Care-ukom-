<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Models\Barang;
use App\Models\Pinjaman;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;

class PinjamanController extends Controller
{
    public function pinjam(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'id_pelanggan' => 'required|exists:pelanggans,id',
            'id_barang' => 'required|exists:barangs,id',
            'durasiminggu' => 'required|integer|min:1',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $tglPinjam = Carbon::now();
        $tglKembali = $tglPinjam->copy()->addWeeks((int) $request->durasiminggu);

        // Ensure the item is available
        if (!Barang::where('id', $request->id_barang)->where('status', '!=', 'Dipinjam')->exists()) {
            return new ResponseResource(404, 'Data barang tidak ditemukan atau tidak tersedia');
        }

        DB::beginTransaction();

        try {
            $pinjaman = Pinjaman::create([
                'id_peminjam' => $request->id_pelanggan,
                'id_barang' => $request->id_barang,
                'tgl_pinjam' => $tglPinjam,
                'tgl_kembali' => $tglKembali,
                'status' => 'Dipinjam',
                'denda' => 0,
            ]);

            Barang::where('id', $request->id_barang)->update(['status' => 'Dipinjam']);

            DB::commit();
        } catch (\Exception $e) {
            DB::rollBack();
            return new ResponseResource(500, 'Terjadi kesalahan saat memproses pinjaman: ' . $e->getMessage());
        }

        return new ResponseResource(200, "Item has been borrowed successfully!", $pinjaman);
    }

    public function index()
    {
        // Retrieve all Pinjaman records and sort by status
        $data = Pinjaman::orderByRaw('CASE WHEN status = "Dipinjam" THEN 1 ELSE 2 END')->get();

        return new ResponseResource(200, "List Semua Data Pinjaman", $data);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function kembali($id)
    {
        DB::beginTransaction();

        try {
            // Find the active loan by its ID
            $pinjaman = Pinjaman::where('id', $id)
                ->where('status', 'Dipinjam')
                ->first();

            if (!$pinjaman) {
                return new ResponseResource(404, 'Barang dipinjam tidak ditemukan atau sudah dikembalikan.');
            }

            // Calculate penalty if the item is returned late
            $tglKembali = Carbon::parse($pinjaman->tgl_kembali);
            $tglSekarang = Carbon::now();
            $denda = 0;

            if ($tglSekarang->greaterThan($tglKembali)) {
                $hariTerlambat = $tglSekarang->diffInDays($tglKembali);
                $denda = $hariTerlambat * 5000; // Example penalty: 5000 per day
            }

            // Update the pinjaman record
            $pinjaman->update([
                'tgl_kembali' => $tglSekarang,
                'status' => 'Dikembalikan',
                'denda' => $denda,
            ]);

            // Update the barang status to 'Available'
            Barang::where('id', $pinjaman->id_barang)->update(['status' => 'Tersedia']);

            DB::commit();

            return new ResponseResource(200, 'Barang berhasil dikembalikan!', [
                'pinjaman' => $pinjaman,
                'denda' => $denda,
            ]);
        } catch (\Exception $e) {
            DB::rollBack();
            return new ResponseResource(500, 'Terjadi kesalahan saat memproses pengembalian: ' . $e->getMessage());
        }
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
