<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Models\AccessToken;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    protected function createNewToken()
    {
        // generate token random number
        $randomNumber = mt_rand(100000, 999999);
        $token = Hash::make($randomNumber);
        $data['token'] = $token;

        // create new access token
        AccessToken::create([
            'token' => $token,
            'user_id' => auth()->user()->id,
            'lastOnline' => now()
        ]);

        $data['user'] = auth()->user();
        return new ResponseResource(200, "Berhasil Autentikasi", $data);
    }
}
