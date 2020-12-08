@extends('layouts.app')

@section('content')
<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">{{ __('Add a new channel') }}</div>

                <div class="card-body">
                    @if (session('status'))
                        <div class="alert alert-success" role="alert">
                            {{ session('status') }}
                        </div>
                    @endif
                    <form method="POST" action="/channel">
                        @csrf       

                        <div class="form-group row">
                            <label for="password" class="col-md-4 col-form-label text-md-right">{{ __('Channel Name') }}</label>

                            <div class="col-md-6">
                                <input id="channelName" type="text" class="form-control @error('channelName') is-invalid @enderror" name="channelName" required autocomplete="current-password">

                                @error('password')
                                    <span class="invalid-feedback" role="alert">
                                        <strong>{{ $message }}</strong>
                                    </span>
                                @enderror
                            </div>
                        </div>


                        <div class="form-group row mb-0">
                            <div class="col-md-8 offset-md-4" >
                                <button type="submit" class="btn btn-primary">
                                    {{ __('Add Channel') }}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-8" style="padding-top:15px;">
                <div class="card">
                    <div class="card-header">{{ __('Your Channels') }}</div>
    
                    <div class="card-body">
                        <table class="table">
                            <thead>
                              <tr>
                                <th scope="col">#</th>
                                <th scope="col">Name</th>
                                <th scope="col">Type</th>
    
                                <th scope="col">Settings</th>
                              </tr>
                            </thead>
                            <tbody>
                                @foreach ($channels as $chan)
                                <tr>
                                    <th scope="row">{{$chan->id}}</th>
                                    <td>{{$chan->name}}</td>
                                    @if ($chan->type==0)
                                    <td>Public </td>
                                    @else
                                    <td>Private </td>
                                    @endif
                                    <td><button class="btn"><a href="/channel/{{$chan->id}}"><i class="fa fa-pencil"></a></i></button></td>
                                  </tr>
                                @endforeach
                            </tbody>
                          </table>
                        
                    </div>
                </div>
        </div>
    </div>
    
</div>
@endsection

