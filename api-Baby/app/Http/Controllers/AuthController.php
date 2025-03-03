<?php

namespace App\Http\Controllers;

use App\Http\Resources\ResponseResource;
use App\Mail\OtpVerifyMail;
use App\Mail\PasswordResetMail;
use App\Models\AccessToken;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    public function register(Request $request)
    {

        $userValidator = Validator::make($request->all(), [
            'nama' => 'required|string',
            'email' => 'required|string|email|unique:users',
            'nomor_telepon' => 'required|string|unique:users',
            'alamat' => 'required|string',
            'nik' => 'required|string|unique:users',
            // 'password' => 'required|string|min:6',
            'password' => [
                'required',
                'string',
                'min:6',
                'regex:/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{6,}$/',
            ],

        ]);

        if ($userValidator->fails()) {
            return new ResponseResource(400, $userValidator->errors()->first());
        }

        $user = User::create(array_merge($userValidator->validated(), [
            'password' => bcrypt($request->password),
            'otp' => random_int(100000, 999999),
        ]));
        if ($user) {
            auth()->login($user);
            return $this->createNewToken();
        }

        auth()->login($user);

        return $this->createNewToken();
    }

    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required|string|min:6',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $userByEmail = User::where([['email', $request['email']]])->first();
        if (!$userByEmail) {
            return new ResponseResource(400, "Email tidak terdaftar");
        }
        // check password
        if (!Hash::check($request['password'], $userByEmail->password)) {
            return new ResponseResource(400, "Password salah");
        }
        auth()->attempt($validator->validated());
        return $this->createNewToken();
    }

    public function userProfile()
    {
        return new ResponseResource(200, "Berhasil mengakses profil", auth()->user());
    }

    public function logout(Request $request)
    {
        auth()->logout();
        // delete token
        $token = $request->bearerToken();
        if ($token) {
            AccessToken::where('token', $token)->delete();
        }
        return new ResponseResource(200, "Berhasil logout");
    }

    protected function createNewToken()
    {
        // generate token random number
        $randomNumber = mt_rand(100000, 999999);
        $token = Hash::make($randomNumber);
        $data['token'] = $token;

        // create new access token
        try {
            AccessToken::create([
                'token' => $token,
                'user_id' => auth()->user()->id,
                'lastOnline' => now(),
            ]);
        } catch (\Exception $e) {
            return new ResponseResource(500, "Failed to create access token: " . $e->getMessage());
        }

        $data['user'] = auth()->user();
        return new ResponseResource(200, "Berhasil Autentikasi", $data);
    }

    public function sendOtpMail(Request $request)
    {
        $validatedData = $request->only(['email']);

        $validator = Validator::make($validatedData, [
            'email' => 'required|email',
        ]);
        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return new ResponseResource(404, "Email tidak valid");
        }

        if ($user->email_verified_at != null) {
            return new ResponseResource(200, "Email telah terverifikasi");
        }
        if ($user->otp == null) {
            $user->otp = random_int(100000, 999999);
            $user->save();
        }

        $details = ['name' => $user->nama, 'otp' => $user->otp];
        Mail::to($user->email)->send(new OtpVerifyMail($details));

        return new ResponseResource(200, "Sukses mengirim OTP");
    }

    public function verifyByOtp(Request $request)
    {
        $validatedData = $request->only(['email', 'otp']);

        $validator = Validator::make($validatedData, [
            'email' => 'required|email',
            'otp' => 'required|size:6|regex:/^[0-9]+$/',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors());
        }

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return new ResponseResource(400, "User not found");
        }

        if ($user->otp == $request->otp) {
            $user->update([
                'email_verified_at' => now(),
                'otp' => null,
            ]);

            return new ResponseResource(200, "Berhasil verifikasi");
        } else {
            return new ResponseResource(400, "OTP tidak valid");
        }
    }


    public function sendForgotPasswordMail(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return new ResponseResource(404, "Email tidak valid");
        }

        $otp = random_int(100000, 999999);

        $details = ['name' => $user->name, 'otp' => $otp];

        DB::table('password_resets')->insert([
            'email' => $user->email,
            'otp' => $otp,
            'created_at' => now()
        ]);

        Mail::to($user->email)->send(new PasswordResetMail($details));

        return new ResponseResource(200, "Email reset password berhasil dikirim");
    }

    public function updatePassword(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'otp' => 'required|size:6|regex:/^[0-9]+$/',
            // 'password' => 'required|string|min:6',
            // 'password_confirmation' => 'required|string',
            'password', 'password_confirmation' => [
                'required',
                'string',
                'min:6',
                'regex:/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{6,}$/',
            ],
        ]);

        if ($validator->fails()) {
            return new ResponseResource(400, $validator->errors()->first());
        }

        $validReset = DB::table('password_resets')->where([['email', $request->email], ['otp', $request->otp]])->first();

        if (!$validReset) {
            return new ResponseResource(404, "Email atau OTP tidak valid");
        }

        $user = User::where('email', $request->email)->first();
        $user->update(['password' => bcrypt($request->password)]);

        DB::table('password_resets')->where('email', $request->email)->delete();

        return new ResponseResource(200, "Berhasil reset password");
    }
}
