<?php

namespace App\Models;

use Illuminate\Contracts\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Prunable;

class AccessToken extends Model
{
    use HasFactory, Prunable;

    protected $fillable = [
        'user_id',
        'name',
        'token',
        'lastOnline',
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function prunable(): Builder
    {
        return static::where('lastOnline', '<', now()->subWeek());
    }

    public function updateLastOnline()
    {
        $this->lastOnline = now();
        $this->save();
    }
}
