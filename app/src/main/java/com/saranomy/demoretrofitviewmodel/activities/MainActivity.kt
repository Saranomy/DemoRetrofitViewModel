package com.saranomy.demoretrofitviewmodel.activities

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saranomy.demoretrofitviewmodel.R
import com.saranomy.demoretrofitviewmodel.adapters.UsersAdapter
import com.saranomy.demoretrofitviewmodel.data.model.User
import com.saranomy.demoretrofitviewmodel.viewmodels.MainViewModel
import com.saranomy.demoretrofitviewmodel.views.GridSpacingItemDecoration

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)

        mainViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainViewModel::class.java)
        mainViewModel.isLoading.observe(this, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        mainViewModel.users.observe(this, Observer {
            usersRetrieved(it)
        })
        mainViewModel.moreUsers.observe(this, Observer {
            moreUsersRetrieved(it)
        })

        mainViewModel.retrieveUsers()
    }

    private fun usersRetrieved(users: List<User>) {
        usersAdapter = UsersAdapter(this, users, mainViewModel.total.value!!, mainViewModel::retrieveMoreUsers)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = usersAdapter
            addItemDecoration(
                GridSpacingItemDecoration(2, (8 * resources.displayMetrics.density).toInt(), true)
            )
        }
    }

    private fun moreUsersRetrieved(moreUsers: List<User>) {
        usersAdapter.addUsers(moreUsers)
    }
}