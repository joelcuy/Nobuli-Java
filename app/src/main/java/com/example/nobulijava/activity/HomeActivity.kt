package com.example.nobulijava.activity
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.system.Os
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.ProgressBar
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.nobulijava.*
//import com.example.nobulijava.DialogflowManager.*
//import com.example.nobulijava.adapters.FriendlyMessageAdapter
//import com.firebase.ui.auth.AuthUI
//import com.firebase.ui.auth.AuthUI.IdpConfig.*
//import com.firebase.ui.database.FirebaseRecyclerOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.example.nobulijava.databinding.ActivityHomeBinding
//import com.google.auth.oauth2.GoogleCredentials
//import com.google.cloud.storage.StorageOptions
//import com.google.common.collect.Lists
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.StorageReference
//import com.google.firebase.storage.ktx.storage
//import java.io.IOException
//import java.io.InputStream
//import java.util.concurrent.Executors
//
//
//class HomeActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var manager: LinearLayoutManager
//
//    // Firebase instance variables
//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseDatabase
//    private lateinit var adapter: FriendlyMessageAdapter
//
////    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
////        onImageSelected(uri)
////    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        var inputStream: InputStream = this.resources.openRawResource(R.raw.credential)
//        Os.setenv("GOOGLE_APPLICATION_CREDENTIALS", "/", true)
////        authExplicit(inputStream)
//
//        Executors.newSingleThreadExecutor().execute {
//
//            // You can specify a credential file by providing a path to GoogleCredentials.
//            // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
//            var credentials: GoogleCredentials? = null
//            try {
//                credentials = GoogleCredentials.fromStream(inputStream)
//                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            val storage =
//                StorageOptions.newBuilder().setCredentials(credentials).build().service
//            println("Buckets:")
//            val buckets =
//                storage.list()
//            for (bucket in buckets.iterateAll()) {
//                println(bucket.toString())
//            }
//        }
//
//
//        // uses View Binding
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize Firebase Auth and check if the user is signed in
//        auth = Firebase.auth
//        if (auth.currentUser == null) {
//            // Not logged in, launch login activity
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//            return
//        }
//
//        // Initialize Realtime Database
//        db = Firebase.database
//        val messagesRef = db.reference.child(MESSAGES_CHILD)
//
//        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
//        // See: https://github.com/firebase/FirebaseUI-Android
//        val options = FirebaseRecyclerOptions.Builder<MessageObj>()
//            .setQuery(messagesRef, MessageObj::class.java)
//            .build()
//        adapter = FriendlyMessageAdapter(options, getUserName())
//        binding.progressBar.visibility = ProgressBar.INVISIBLE
//        manager = LinearLayoutManager(this)
//        manager.stackFromEnd = true
//        binding.messageRecyclerView.layoutManager = manager
//        binding.messageRecyclerView.adapter = adapter
//
//        // Scroll down when a new message arrives
//        // See MyScrollToBottomObserver for details
//        adapter.registerAdapterDataObserver(
//            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
//        )
//
//        // Disable the send button when there's no text in the input field
//        // See MyButtonObserver for details
//        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))
//
//        // When the send button is clicked, send a text message
//        binding.sendButton.setOnClickListener {
//            val friendlyMessage = MessageObj(
//                binding.messageEditText.text.toString(),
//                getUserName(),
//                getPhotoUrl(),
//                null
//            )
//            db.reference.child(MESSAGES_CHILD).push().setValue(friendlyMessage)
//            binding.messageEditText.setText("")
//
//            val projectID: String = "nobuli-ysta"
//            val text: List<String> = listOf(binding.messageEditText.text.toString())
//            val sessionID: String = generateSessionID()
//            val langCode: String = "en-US"
//
//            detectIntentTexts(projectID, text, sessionID, langCode);
//
//
//        }
//
//
//
////        // When the image button is clicked, launch the image picker
////        binding.addMessageImageView.setOnClickListener {
////            openDocument.launch(arrayOf("image/*"))
////        }
//    }
//
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in.
//        if (auth.currentUser == null) {
//            // Not signed in, launch the Sign In activity
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish()
//            return
//        }
//    }
//
//    public override fun onPause() {
//        adapter.stopListening()
//        super.onPause()
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        adapter.startListening()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.sign_out_menu -> {
//                signOut()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun onImageSelected(uri: Uri) {
//        Log.d(TAG, "Uri: $uri")
//        val user = auth.currentUser
//        val tempMessage = MessageObj(null, getUserName(), getPhotoUrl(), LOADING_IMAGE_URL)
//        db.reference
//            .child(MESSAGES_CHILD)
//            .push()
//            .setValue(
//                tempMessage,
//                DatabaseReference.CompletionListener { databaseError, databaseReference ->
//                    if (databaseError != null) {
//                        Log.w(
//                            TAG, "Unable to write message to database.",
//                            databaseError.toException()
//                        )
//                        return@CompletionListener
//                    }
//
//                    // Build a StorageReference and then upload the file
//                    val key = databaseReference.key
//                    val storageReference = Firebase.storage
//                        .getReference(user!!.uid)
//                        .child(key!!)
//                        .child(uri.lastPathSegment!!)
//                    putImageInStorage(storageReference, uri, key)
//                })
//    }
//
//    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
//        // First upload the image to Cloud Storage
//        storageReference.putFile(uri)
//            .addOnSuccessListener(
//                this
//            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
//                // and add it to the message.
//                taskSnapshot.metadata!!.reference!!.downloadUrl
//                    .addOnSuccessListener { uri ->
//                        val friendlyMessage =
//                            MessageObj(null, getUserName(), getPhotoUrl(), uri.toString())
//                        db.reference
//                            .child(MESSAGES_CHILD)
//                            .child(key!!)
//                            .setValue(friendlyMessage)
//                    }
//            }
//            .addOnFailureListener(this) { e ->
//                Log.w(
//                    TAG,
//                    "Image upload task was unsuccessful.",
//                    e
//                )
//            }
//    }
//
//    private fun signOut() {
//        AuthUI.getInstance().signOut(this)
//        startActivity(Intent(this, LoginActivity::class.java))
//        finish()
//    }
//
//    //Get photo from google sign in
//    private fun getPhotoUrl(): String? {
//        val user = auth.currentUser
//        return user?.photoUrl?.toString()
//    }
//
//    //Get username from google sign in
//    private fun getUserName(): String? {
//        val user = auth.currentUser
//        return if (user != null) {
//            user.displayName
//        } else ANONYMOUS
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//        const val MESSAGES_CHILD = "messages"
//        const val ANONYMOUS = "anonymous"
//        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
//    }
//}
