package com.example.edwin.appretrofit2
import io.reactivex.Observable
import retrofit2.http.GET

interface RestReuniones {
    @GET("reunionms")
    fun getReuniones() : Observable<ReunionesResponse>
}