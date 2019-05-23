package com.scott.coroutineproofofconcept

import androidx.lifecycle.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.HttpException


class MainViewModel(private val repository: NetworkService) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::MainViewModel)
    }

    override fun onCleared() {
        super.onCleared()

        //cancels all jobs when viewmodel disappears
        viewModelScope.coroutineContext.cancel()
    }

    //1,2
    private val _token = MutableLiveData<String>()

    val token : LiveData<String>
        get() = _token

    fun getToken() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val t = NetworkService().getGuestToken().token
                _token.postValue(t)
            } catch (e : HttpException) {
                println("message: ${e.localizedMessage}")
            }

        }
    }

    suspend fun getOneOfManyTokens(secs: Long) : String {
        delay(secs)
        return NetworkService().getGuestToken().token
    }

//    fun getSeveralTokensWithDelay() {
//        viewModelScope.async {
//            try {
//                val a =  getOneOfManyTokens(1000)
//                val b =  getOneOfManyTokens(2000)
//                val c = getOneOfManyTokens(3000)
//                val d = getOneOfManyTokens(4000)
//                _token.postValue("${a} ${b} ${c} ${d}")
//            } catch (e: HttpException) {
//
//            }
//        }
//    }

    fun getSeveralTokensWithDelay(data: String) {
        println(data)
        viewModelScope.async {
            try {
                val a = async { getOneOfManyTokens(1000) }
                val b = async { getOneOfManyTokens(2000) }
                val c = async { getOneOfManyTokens(3000) }
                val d = async { getOneOfManyTokens(4000) }
                _token.postValue("${a.await()} ${b.await()} ${c.await()} ${d.await()}")
            } catch (e: HttpException) {
                println(e.message())
            }
        }
    }
}

fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T):
            (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}