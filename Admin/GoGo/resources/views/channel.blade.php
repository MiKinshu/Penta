@extends('layouts.app')
@section('content')

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">{{$channel->name}}</div>

                <div class="card-body">
                    @if (session('status'))
                        <div class="alert alert-success" role="alert">
                            {{ session('status') }}
                        </div>
                    @endif
                    <form method="POST" action="/channel/{{$channel->id}}/type">
                        @csrf       

                        <div class="form-group row">
                            <label for="SelectType" class="col-md-4 col-form-label text-md-right">{{ __('Select Channel Type') }}</label>

                            <div class="col-md-6">
                                <select class="form-control" name="type" id="SelectType">

                                    <option value="0" @if ($channel->type==0)
                                        selected
                                    @endif>Public</option>
                                    <option value="1" @if ($channel->type==1)
                                        selected
                                    @endif>Private</option>
                                </select>
                            </div>
                        </div>
                    
                        <div class="form-group row mb-0">
                            <div class="col-md-8 offset-md-4" >
                                <button type="submit" class="btn btn-primary">
                                    {{ __('Save') }}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
            <div class="col-md-8" style="padding-top:15px;">
                <div class="card">
                    <div class="card-header">{{ __('Channel Data') }}</div>
              
                    <div class="card-body">
                        <form  method="POST"  action="/channel/{{$channel->id}}/data"> 
@csrf
                            <div class="form-group row">
                                <label for="SelectType" class="col-md-4 col-form-label text-md-right">{{ __('Select Channel Type') }}</label>
    
                                <div class="col-md-6">
                                    <textarea class="form-control" name="data" rows="7" id="comment">{{$channel->data}}</textarea>
                                </div>
                            </div>
              

                        <div class="form-group row mb-0">
                            <div class="col-md-8 offset-md-4" >
                                <button type="submit" class="btn btn-primary">
                                    {{ __('Save') }}
                                </button>
                            </div>
                        </div>
                    </form>
                    </div>
                </div>
            </div>
            <div class="col-md-8" style="padding-top: 15px;">
                <div class="card">
                    <div class="card-header">{{ __('Add a new Member') }}</div>
    
                    <div class="card-body">
                        @if (session('status'))
                            <div class="alert alert-success" role="alert">
                                {{ session('status') }}
                            </div>
                        @endif
                        <form method="POST" action="/channel/{{$channel->id}}/member">
                            @csrf       
    
                            <div class="form-group row">
                                <label for="member" class="col-md-4 col-form-label text-md-right">{{ __('Add Member') }}</label>
                                <div class="col-md-6">
                                    <input id="member" type="email" class="form-control @error('member') is-invalid @enderror" name="member" required>
                                </div>
                            </div>
                            <div class="form-group row mb-0">
                                <div class="col-md-8 offset-md-4" >
                                    <button type="submit" class="btn btn-primary">
                                        {{ __('Add Member') }}
                                    </button>
                                </div>
                            </div>
                        </form>

                        
                        <table class="table" style="margin-top: 15px;">
                            <thead>
                              <tr>
                                <th scope="col">#</th>
                                <th scope="col">Name</th>
                                <th scope="col">Type</th>
                             
                              </tr>
                            </thead>
                            <tbody>
                                <?php $i=1 ?>
                                @foreach ($members as $member)
                                @if ($member!="")                      
                                <tr>
                                    <th scope="row">{{$i}}</th>
                                    <td>{{$member}}</td>
                                    <td><button class="btn"><i class="fa fa-trash"></i></button></td>
                                  </tr>
                                  <?php $i++ ?>
                                  @endif
                                @endforeach
                            </tbody>
                          </table>
                    </div>
                </div>
            </div>
        
        
                    
                         
    
</div>

@endsection
