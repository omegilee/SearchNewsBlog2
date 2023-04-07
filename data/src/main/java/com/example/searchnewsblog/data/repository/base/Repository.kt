package com.example.searchnewsblog.data.repository.base

import com.example.searchnewsblog.data.repository.Util
import kotlinx.coroutines.flow.Flow

abstract class Repository<Param, Result> {

    //======================================================================
    // Private Variables
    //======================================================================

    protected var param: Param? = null

    //======================================================================
    // Abstract Methods
    //======================================================================

    @Throws(Exception::class)
    abstract fun execute(): Flow<Result?>

    //======================================================================
    // Public Methods
    //======================================================================

    @Suppress("UNCHECKED_CAST")
    fun parameter(init: Param.() -> Unit): Repository<Param, Result> {
        val p: Param =
            Util.getReclusiveGenericClass(this::class.java, 0).newInstance() as Param
        p?.init()
        param = p
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun parameter(param: Param): Repository<Param, Result> {
        this.param = param
        return this
    }

    //======================================================================
    // Protected Methods
    //======================================================================

    protected open fun onDestroy() {
        param = null
    }
}