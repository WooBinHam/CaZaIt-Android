package org.cazait.cazait_android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.emptyPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import org.cazait.cazait_android.data.Resource
import org.cazait.cazait_android.data.model.remote.datasource.CafeRemoteData
import org.cazait.cazait_android.data.model.remote.request.CafeListRequest
import org.cazait.cazait_android.data.model.remote.response.CafeListResponse
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataRepositoryImpl @Inject constructor(
    private val remoteData: CafeRemoteData,
    private val ioDispatcher: CoroutineContext,
    @ApplicationContext private val context: Context
) : DataRepository {
    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavourite(cafeId: String): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCafes(userId: Long, query: CafeListRequest): Flow<Resource<CafeListResponse>> {
        return flow {
            emit(remoteData.getCafeList(userId, query))
        }.flowOn(ioDispatcher)
    }
}