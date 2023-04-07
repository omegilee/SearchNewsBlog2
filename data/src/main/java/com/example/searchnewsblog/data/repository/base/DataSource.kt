package com.example.searchnewsblog.data.repository.base

import kotlinx.coroutines.flow.Flow

abstract class DataSource<Param, Result> {

    abstract fun execute(p: Param): Flow<Result>
}


