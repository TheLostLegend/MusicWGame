package com.example.restapiidemo.network

import com.music.waves.model.entity.LeaderboardALTER
import com.serwylo.beatgame.network.data.LeaderboardModel
import com.serwylo.beatgame.network.data.Message
import com.serwylo.beatgame.network.data.RegModel
import com.serwylo.beatgame.network.data.TrackModel
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

//    @GET("posts")
//    fun fetchAllPosts(): Call<List<PostModel>>
//
//    @POST("posts")
//    fun createPost(@Body postModel: PostModel):Call<PostModel>
//
//    @DELETE("posts/{id}")
//    fun deletePost(@Path("id") id:Int):Call<String>

    @GET("api/leaderboard/show")
    fun getRecords(@Query("title") title:String): Call<List<LeaderboardALTER?>>

    @GET("api/track/show")
    fun getTracks(@Query("title") title:String): Call<List<TrackModel?>>

    @GET("api/track/is_exist")
    fun getTrackID(@Query("title") title:String): Call<Message?>

    @GET("api/player/login")
    fun loginUser(@Query("login") login:String , @Query("password") password:String): Call<Message?>

    @POST("api/player/sing_up")
    fun registerUser(@Body data: RegModel?): Call<Message?>?

    @POST("api/track/create")
    fun createTrack(@Body data: TrackModel?): Call<Message?>?

    @POST("api/leaderboard/create")
    fun createRecord(@Body data: LeaderboardModel?): Call<Message?>?

    @GET("api/player/reset_pas")
    fun resetPass(@Query("email") email:String): Call<Message?>

//    @GET("api/news/dto/news")
//    fun getNewsList(): Call<List<News?>>?
//
//    @GET("api/offer/dto/offers")
//    fun getOfferList(): Call<List<Offer?>>?
//
//    @GET("api/flight/dto/flights")
//    fun getFlightList(): Call<List<Flight?>>?
//
//    @GET("api/flight/cities")
//    fun getCitys(): Call<List<String?>>?
//
//    @GET("/api/customer/{id}/passportData")
//    fun getPData(@Path("id") id:Int):Call<PassportData?>?
//
//    @PUT("/api/customer/{id}/passportData")
//    fun passUpdate(@Path("id") id:Int, @Body data: PassportDataALTER?): Call<Message?>?



//    @GET("/api/customer/{id}/is_exist")
//    fun chechPID(@Path("id") id:Int):Call<Message>?
//
//    @GET("api/promocode/is_exist")
//    fun checkPromo(@Query("value") value:String, @Query("flightID") flightID:Int): Call<Message?>
//
//    @POST("api/pay/ticket")
//    fun pay(@Body data: Order?): Call<Message?>?
//
//    @GET("api/fortune/tracks")
//    fun getWheel(): Call<List<WheelItem>>?
//
//    @POST("api/pay/fortune")
//    fun payF(@Body data: Order?): Call<Message2?>?
}