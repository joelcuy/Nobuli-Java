package com.example.nobulijava.adapters
//
//import android.graphics.Color
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView.*
//import com.bumptech.glide.Glide
//import com.example.nobulijava.R
//import com.firebase.ui.database.FirebaseRecyclerAdapter
//import com.firebase.ui.database.FirebaseRecyclerOptions
//import com.example.nobulijava.activity.HomeActivity.Companion.ANONYMOUS
//import com.example.nobulijava.databinding.ImageMessageBinding
//import com.example.nobulijava.databinding.MessageBinding
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.ktx.storage
//
//// The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
//// See: https://github.com/firebase/FirebaseUI-Android
//class FriendlyMessageAdapter(
//    private val options: FirebaseRecyclerOptions<MessageObj>,
//    private val currentUserName: String?
//) :
//    FirebaseRecyclerAdapter<MessageObj, ViewHolder>(options) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return if (viewType == VIEW_TYPE_TEXT) {
//            val view = inflater.inflate(R.layout.template_message, parent, false)
//            val binding = MessageBinding.bind(view)
//            MessageViewHolder(binding)
//        } else {
//            val view = inflater.inflate(R.layout.image_message, parent, false)
//            val binding = ImageMessageBinding.bind(view)
//            ImageMessageViewHolder(binding)
//        }
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MessageObj) {
//        if (options.snapshots[position].text != null) {
//            (holder as MessageViewHolder).bind(model)
//        } else {
//            (holder as ImageMessageViewHolder).bind(model)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (options.snapshots[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
//    }
//
//    inner class MessageViewHolder(private val binding: MessageBinding) : ViewHolder(binding.root) {
//        fun bind(item: MessageObj) {
//            binding.messageTextView.text = item.text
//            setTextColor(item.name, binding.messageTextView)
//
//            binding.messengerTextView.text = if (item.name == null) ANONYMOUS else item.name
//            if (item.photoUrl != null) {
//                loadImageIntoView(binding.messengerImageView, item.photoUrl!!)
//            } else {
//                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_foreground)
//            }
//        }
//
//        private fun setTextColor(userName: String?, textView: TextView) {
//            if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
//                textView.setBackgroundResource(R.drawable.bg_message_send)
//                textView.setTextColor(Color.WHITE)
//            } else {
//                textView.setBackgroundResource(R.drawable.bg_message_receive)
//                textView.setTextColor(Color.BLACK)
//            }
//        }
//    }
//
//    inner class ImageMessageViewHolder(private val binding: ImageMessageBinding) :
//        ViewHolder(binding.root) {
//        fun bind(item: MessageObj) {
//            loadImageIntoView(binding.messageImageView, item.imageUrl!!)
//
//            binding.messengerTextView.text = if (item.name == null) ANONYMOUS else item.name
//            if (item.photoUrl != null) {
//                loadImageIntoView(binding.messengerImageView, item.photoUrl!!)
//            } else {
//                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_foreground)
//            }
//        }
//    }
//
//    private fun loadImageIntoView(view: ImageView, url: String) {
//        if (url.startsWith("gs://")) {
//            val storageReference = Firebase.storage.getReferenceFromUrl(url)
//            storageReference.downloadUrl
//                .addOnSuccessListener { uri ->
//                    val downloadUrl = uri.toString()
//                    Glide.with(view.context)
//                        .load(downloadUrl)
//                        .into(view)
//                }
//                .addOnFailureListener { e ->
//                    Log.w(
//                        TAG,
//                        "Getting download url was not successful.",
//                        e
//                    )
//                }
//        } else {
//            Glide.with(view.context).load(url).into(view)
//        }
//    }
//
//    companion object {
//        const val TAG = "MessageAdapter"
//        const val VIEW_TYPE_TEXT = 1
//        const val VIEW_TYPE_IMAGE = 2
//    }
//}
