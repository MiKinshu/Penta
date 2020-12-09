<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');
Route::post('/channel','ChannelsController@addChannel');
Route::get('/channel/all','ChannelsController@getAllChannels');
Route::get('/channel/{id}','ChannelsController@editChannel');
Route::post('/channel/{id}/type','ChannelsController@changeType');
Route::post('/channel/{id}/data','ChannelsController@changeData');
Route::post('/channel/{id}/member','ChannelsController@addMember');
Route::get('/channel/{id}/query','ChannelsController@getQueryAnswer');
