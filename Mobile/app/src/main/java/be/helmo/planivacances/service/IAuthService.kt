package be.helmo.planivacances.service

import be.helmo.planivacances.service.dto.LoginUserDTO
import be.helmo.planivacances.service.dto.RegisterUserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthService {

    @POST("auth/register")
    suspend fun register(@Body user: RegisterUserDTO): Response<String>

    @POST("auth/login")
    suspend fun login(@Body user: LoginUserDTO): Response<String>

}