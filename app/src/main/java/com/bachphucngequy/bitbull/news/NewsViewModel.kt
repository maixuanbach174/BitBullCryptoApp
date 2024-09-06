package com.bachphucngequy.bitbull.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachphucngequy.bitbull.news.api.NetworkResponse
import com.bachphucngequy.bitbull.news.api.NewsModel
import com.bachphucngequy.bitbull.news.api.RetrofitInstance
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val newsApi = RetrofitInstance.newsApi
    private val _newsResult = MutableLiveData<NetworkResponse<NewsModel>>()
    val newsResult : LiveData<NetworkResponse<NewsModel>> = _newsResult

    fun getData(topic : String){
        _newsResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response = newsApi.getNews(topic = topic)
                if(response.isSuccessful){
                    response.body()?.let {
                        Log.i("Success", "Success")
                        _newsResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    Log.i("Error", "Error")
                    _newsResult.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e : Exception){
                Log.i("Exception", "Exception")
                _newsResult.value = NetworkResponse.Error("Exception")
            }

        }
    }

}