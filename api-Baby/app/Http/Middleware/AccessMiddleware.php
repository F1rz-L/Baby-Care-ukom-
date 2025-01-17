<?php

namespace App\Http\Middleware;

use App\Http\Resources\ResponseResource;
use App\Models\AccessToken;
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Symfony\Component\HttpFoundation\Response;

class AccessMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Closure(\Illuminate\Http\Request): (\Symfony\Component\HttpFoundation\Response)  $next
     */
    public function handle(Request $request, Closure $next): Response
    {
        $token = $request->bearerToken();
        if (!$token) {
            return (new ResponseResource(401, "Token not provided"))->toResponse($request);
        }

        // check token in AccessToken table
        $accessToken = AccessToken::with('user')->where('token', $token)->first();
        if (!$accessToken) {
            return (new ResponseResource(401, "Invalid token"))->toResponse($request);
        }
        auth()->login($accessToken->user);
        $accessToken->updateLastOnline();
        return $next($request);
    }
}
