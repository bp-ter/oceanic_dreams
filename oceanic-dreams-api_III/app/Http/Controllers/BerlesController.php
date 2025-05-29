<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Berles;
use Illuminate\Validation\ValidationException;
use Carbon\Carbon;

class BerlesController extends Controller
{
    public function index()
    {
        return Berles::all();
    }

    public function show($id)
    {
        $berles = Berles::find($id);
        if (!$berles) {
            return response()->json(['error' => 'Nem található a bérlés'], 404);
        }
        return $berles;
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'uid' => 'required|integer',
            'yacht_id' => 'required|integer',
            'start_date' => 'required|date',
            'end_date' => 'required|date|after_or_equal:start_date',
            'daily_price' => 'required|integer|min:0',
            'deposit' => 'required|integer|min:0',
        ]);

        $start = Carbon::parse($data['start_date']);
        $end = Carbon::parse($data['end_date']);
        $tomorrow = Carbon::tomorrow();

        if ($start->lt($tomorrow)) {
            return response()->json(['error' => 'A bérlés kezdőnapja nem lehet korábbi, mint holnap.'], 400);
        }

        $days = $start->diffInDays($end) + 1;
        if ($days < 5) {
            return response()->json(['error' => 'A bérlés időtartama legalább 5 nap kell legyen.'], 400);
        }

        if ($days > 90) {
            return response()->json(['error' => 'A bérlés időtartama legfeljebb 90 nap lehet.'], 400);
        }

        $overlap = Berles::where('yacht_id', $data['yacht_id'])
            ->where(function($query) use ($start, $end) {
                $query->whereBetween('start_date', [$start, $end])
                      ->orWhereBetween('end_date', [$start, $end])
                      ->orWhere(function($q) use ($start, $end) {
                          $q->where('start_date', '<=', $start)
                            ->where('end_date', '>=', $end);
                      });
            })->exists();

        if ($overlap) {
            return response()->json(['error' => 'Az adott időszakban a yacht már foglalt.'], 400);
        }

        $berles = Berles::create($data);
        return response()->json($berles, 201);
    }

    public function destroy($id)
    {
        $berles = Berles::find($id);
        if (!$berles) {
            return response()->json(['error' => 'Nem található a bérlés'], 404);
        }
        $berles->delete();
        return response()->json(null, 204);
    }
}
