package org.cazait.cazait_android.data.api

import org.cazait.cazait_android.data.model.remote.request.CafeListRequest
import org.cazait.cazait_android.data.model.remote.response.*

import retrofit2.Call
import retrofit2.http.*

interface CafeService {
    /**
     * 근처 카페들에 대한 정보를 불러온다.
     */
    @GET("/api/cafes/all/user/{userId}")
    fun getCafes(
        @Path("userId") userId: Long,
        @Query("latitude") latitude: String,
        @Query("limit") limit: String,
        @Query("longitude") longitude: String,
        @Query("sort") sort: String
    ): Call<CafeListResponse>

    @GET("/api/menus/cafe/{cafeId}")
    fun getMenus(
        @Path("cafeId") cafeId: Long
    ): Call<MenuResponse>

    @GET("/api/reviews/{cafeId}/all")
    fun getReviews(
        @Path("cafeId") cafeId: Int
    ): Call<ReviewResponse>

    @GET("/api/favorites/user/{userId}")
    fun getInterestCafes(
        @Path("userId") userId: Long
    ): Call<InterestCafesResponse>

    @POST("/api/favorites/user/{userId}/cafe/{cafeId}")
    fun postInterestCafe(
        @Path("userId") userId: Long,
        @Path("cafeId") cafeId: Long
    ): Call<PostInterestCafeResponse>

    @DELETE("/api/favorites/delete/{favoritesId}")
    fun deleteInterestCafe(
        @Path("favoritesId") cafeId: Long
    ): Call<DeleteInterestCafeResponse>
}