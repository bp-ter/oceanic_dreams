<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;

class Berles extends Model
{
    protected $table = 'berlesek';

    protected $fillable = [
        'uid', 'yacht_id', 'start_date', 'end_date', 'daily_price', 'deposit'
    ];

    public function getTotalPriceAttribute()
    {
        $start = Carbon::parse($this->start_date);
        $end = Carbon::parse($this->end_date);
        $days = $start->diffInDays($end) + 1;
        return $days * $this->daily_price;
    }
}
