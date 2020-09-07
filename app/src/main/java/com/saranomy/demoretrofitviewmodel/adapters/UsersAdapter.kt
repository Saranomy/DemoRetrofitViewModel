package com.saranomy.demoretrofitviewmodel.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saranomy.demoretrofitviewmodel.R
import com.saranomy.demoretrofitviewmodel.activities.MainActivity
import com.saranomy.demoretrofitviewmodel.data.model.User

class UsersAdapter constructor(
    private val context: MainActivity,
    private var users: List<User>,
    private var totalUsers: Int,
    private val loadMore: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_USERS = 0
        const val VIEW_TYPE_PROGRESS_BAR = 1
    }

    fun addUsers(moreUsers: List<User>) {
        users = users.plus(moreUsers.asIterable())
        notifyDataSetChanged()
    }

    class UsersViewHolder constructor(private var view: View) : RecyclerView.ViewHolder(view) {
        lateinit var cardView: CardView
        lateinit var thumbnail: ImageView
        lateinit var title: TextView
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < users.size) VIEW_TYPE_USERS else VIEW_TYPE_PROGRESS_BAR
    }

    class ProgressBarViewHolder constructor(private var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder
        if (viewType == VIEW_TYPE_USERS) {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)

            val usersViewHolder = UsersViewHolder(root)
            usersViewHolder.cardView = root.findViewById(R.id.card_view)
            usersViewHolder.thumbnail = root.findViewById(R.id.thumbnail)
            usersViewHolder.title = root.findViewById(R.id.title)

            holder = usersViewHolder
        } else {
            val root =
                LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false)
            holder = ProgressBarViewHolder(root)
        }
        return holder
    }

    override fun getItemCount(): Int {
        // not full yet? add one extra view for progress bar
        return if (users.size < totalUsers) users.size + 1 else users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UsersViewHolder) {
            val user = users[position]
            val thumbnailUri = Uri.parse(user.avatar)
            Glide.with(context).load(thumbnailUri).into(holder.thumbnail)

            holder.title.text = "${user.firstName} ${user.lastName}"
        } else if (holder is ProgressBarViewHolder) {
            loadMore()
        }
    }
}