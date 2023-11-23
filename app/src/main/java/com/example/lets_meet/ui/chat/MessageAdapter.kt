package com.example.lets_meet.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lets_meet.databinding.ItemMessageReceivedBinding
import com.example.lets_meet.databinding.ItemMessageSentBinding
import com.example.lets_meet.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val currentUserId: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messages: MutableList<Message> = mutableListOf()

    companion object {
        const val VIEW_TYPE_SENT = 0
        const val VIEW_TYPE_RECEIVED = 1
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderUid == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> SentMessageViewHolder(ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            VIEW_TYPE_RECEIVED -> ReceivedMessageViewHolder(ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalStateException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun setMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    inner class SentMessageViewHolder(private val binding: ItemMessageSentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewSentMessage.text = message.content
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewReceivedMessage.text = message.content
            binding.textViewSenderName.text = message.senderName
        }
    }
}
