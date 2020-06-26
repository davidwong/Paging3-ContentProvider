package au.com.dw.paging3contentprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(diffCallback: DiffUtil.ItemCallback<Message>) :
    PagingDataAdapter<Message, MessageViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        return MessageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item!!)
    }
}

class MessageViewHolder(parent :ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.message_row, parent, false)) {

    private val senderView = itemView.findViewById<TextView>(R.id.sender)
    private val dateTimeView = itemView.findViewById<TextView>(R.id.datetime)
    private val bodyView = itemView.findViewById<TextView>(R.id.body)

    fun bindTo(message: Message) {
        senderView.text = message.fromAddress
        dateTimeView.text = message.dateTime
        bodyView.text = message.body
    }
}

object MessageComparator : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.equals(newItem)
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}