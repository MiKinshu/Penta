<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Channel;
use App\User;
use Illuminate\Support\Facades\Auth;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Contracts\Support\Renderable
     */
    public function index()
    {
        $channel = new Channel;
        $user = Auth::user();
        $channels = $user->channels()->get();
       
        return view('home',compact('channels'));
    }
}
