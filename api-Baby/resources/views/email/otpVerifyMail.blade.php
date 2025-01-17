<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>BabyCare - Verifikasi Akun</title>
</head>
<body style="background:#f9fcff; font-family: montserrat; ">
	<h3 style="line-height: 0.1;">BabyCare</h3>
	<hr style="border: 1px solid gray;">
	<h3>Hi <span>{{ $details['name'] }}</span></h3>
    <p>Terima kasih telah mendaftar di BabyCare. Untuk melakukan verifikasi pada akun Anda, gunakanlah One Time Password (OTP) berikut.</p>
	<div style="background: #4C84FF; text-align: center; color: white; border-radius: 10px; margin: 20px 0px;">
      		<p style="padding-top: 15px; margin: 0;">Berikut adalah OTP anda: </p>
      	    <p style="padding-bottom: 15px; font-size: 2.5rem; margin: 0;">{{ $details['otp'] }}</p>
  	</div>
     <p style="line-height: 1">Dimohon agar tidak menyebarkan OTP ini ke siapapun</p>
    <p style="line-height: 1">Jika anda tidah tahu mengapa anda menerima otp ini, tolong abaikan saja pesan ini</p>
    <p style="line-height: 1">Terima kasih</p>
	<hr style="border: 1px solid gray;">
	<div>
      	<p style="line-height: 1; text-align: end;">© 2024 BabyCare</p>
		<p style="line-height: 1; text-align: end;">Buduran, Jawa Timur</p>
		<p style="line-height: 1; text-align: end;">Indonesia</p>
	</div>
</body>
</html>