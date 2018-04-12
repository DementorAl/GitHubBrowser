package ru.danilov.githubbrowse.view.peopleSearch

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ru.danilov.githubbrowse.R
import ru.danilov.githubbrowse.model.PeopleSearchItemModel
import ru.danilov.githubbrowse.utils.find
import ru.danilov.githubbrowse.utils.inflate
import ru.danilov.githubbrowse.utils.onClick

interface GetNextPageCallback {
    fun loadNextPage()
}


class PeopleSearchAdapter(private val callback: GetNextPageCallback) : RecyclerView.Adapter<PeopleSearchItemViewHolder>() {

    private val data = ArrayList<PeopleSearchItemModel>()

    fun clearData() {
        data.clear()
    }

    fun refillData(page: ArrayList<PeopleSearchItemModel>) {
        data.addAll(page)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleSearchItemViewHolder = PeopleSearchItemViewHolder(parent.inflate(R.layout.item_people_search))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PeopleSearchItemViewHolder, position: Int) {
        if (data.isEmpty() || position == data.size - 1) {
            callback.loadNextPage()
        }
        holder.bind(data[position])
    }

}

class PeopleSearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val userAvatarView: CircleImageView by lazy { itemView.find<CircleImageView>(R.id.userAvatarView) }
    val userNameView: TextView by lazy { itemView.find<TextView>(R.id.userNameView) }
    val userScoreView: TextView by lazy { itemView.find<TextView>(R.id.userScoreView) }
    val userLinkView: ImageButton by lazy { itemView.find<ImageButton>(R.id.userLinkView) }

    fun bind(item: PeopleSearchItemModel) {
        Picasso.with(itemView.context).load(item.avatar).resizeDimen(R.dimen.user_avatar_size, R.dimen.user_avatar_size)
                .into(userAvatarView)
        userNameView.text = item.login
        userScoreView.text = itemView.context.getString(R.string.id_placeholder).format(item.id.toString())
        userLinkView.onClick {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.url)
            itemView.context.startActivity(i)
        }
    }

}
