package com.saranomy.demoretrofitviewmodel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saranomy.demoretrofitviewmodel.data.ReqResApi
import com.saranomy.demoretrofitviewmodel.data.RetrofitClient
import com.saranomy.demoretrofitviewmodel.data.model.User
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    var isLoading = MutableLiveData<Boolean>()
    var users = MutableLiveData<List<User>>()
    var moreUsers = MutableLiveData<List<User>>()

    var currentPage = MutableLiveData<Int>()
    var perPage = MutableLiveData<Int>()
    var total = MutableLiveData<Int>()
    var totalPages = MutableLiveData<Int>()

    fun retrieveUsers() {
        isLoading.value = true // show progress bar

        CoroutineScope(Dispatchers.IO).launch {
            val reqResApi = RetrofitClient.getClient().create(ReqResApi::class.java)
            val response = reqResApi.getUsers().execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    currentPage.value = response.body()!!.page
                    perPage.value = response.body()?.page
                    total.value = response.body()?.total
                    totalPages.value = response.body()?.totalPages

                    users.value = response.body()?.data // should be the last
                }
                isLoading.value = false
            }
        }
    }

    fun retrieveMoreUsers() {
        if (currentPage.value!! < totalPages.value!!) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000) // add 1 second delay before download a new set of users

                val reqResApi = RetrofitClient.getClient().create(ReqResApi::class.java)
                val response = reqResApi.getUsers(currentPage.value!! + 1).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        moreUsers.value = response.body()?.data
                        currentPage.value = response.body()?.page
                    }
                }
            }
        }
    }
}