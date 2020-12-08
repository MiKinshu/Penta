<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Channel;
use Illuminate\Support\Facades\Auth;

class ChannelsController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function addChannel(Request $request)
    {
        $channel = new \App\Channel;
        $channel->name=$request->channelName;
        $channel->type=0;
        $channel->user_id=Auth::user()->id;
        $channel->data="No data added";
        $channel->members="";
        $channel->save();
        return redirect()->back();
    }

    public function editChannel(Request $request, $id)
    {
        $channel = \App\Channel::where('id', $id)->get()[0];
        $members = $channel->members;
        $members = explode(",", $members);
        return view('channel',compact('channel','members'));
    }
    
    public function changeType(Request $request, $id)
    {
        $channel = \App\Channel::where('id', $id)->get()[0];
        $channel->type=$request->type;
        $channel->save();
        return redirect()->back();
    }

    public function changeData(Request $request, $id)
    {
        $channel = \App\Channel::where('id', $id)->get()[0];
        $channel->data=$request->data;
        $channel->save();
        return redirect()->back();
    }

    public function addMember(Request $request, $id)
    {
        $channel = \App\Channel::where('id', $id)->get()[0];
       
        $channel->members.=",".$request->member;
        $channel->save();
        
        return redirect()->back();
    }
}
