package com.example.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerAdapter : Adapter<ChatRecyclerAdapter.ChatVH>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    class ChatVH(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {

        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {

            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.reycycler_row, parent, false)

            return ChatVH(view)

        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.reycycler_row_right, parent, false)
            return ChatVH(view)

        }
    }

    // have you a new item or not, control and new items add
    private val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {

            return oldItem == newItem


        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var chat: List<Chat>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {

        val chat = chat.get(position)

        return if (chat.user == FirebaseAuth.getInstance().currentUser!!.email.toString()) {

            VIEW_TYPE_SENT

        } else {

            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {

        val textView = holder.itemView.findViewById<TextView>(R.id.recyclerText)
        textView.text = "${chat.get(position).user} : ${chat.get(position).text}"

    }

    override fun getItemCount(): Int {

        return chat.size
    }
}