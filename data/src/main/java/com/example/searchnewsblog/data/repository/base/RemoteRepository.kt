package com.example.searchnewsblog.data.repository.base

import kotlinx.coroutines.flow.Flow

abstract class RemoteRepository<Param, Result> : Repository<Param, Result>() {

    //======================================================================
    // Private Variables
    //======================================================================

    private val remote: RemoteDataSource<Param, *, Result> by lazy {
        createRemoteDataSource()
    }

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract fun createRemoteDataSource(): RemoteDataSource<Param, *, Result>

    //======================================================================
    // Override Methods
    //======================================================================

    override fun execute(): Flow<Result?> {
        try {
            return remote.execute(param!!)
        } catch (e: Exception) {
            throw e
        } finally {
            onDestroy()
        }
    }
}