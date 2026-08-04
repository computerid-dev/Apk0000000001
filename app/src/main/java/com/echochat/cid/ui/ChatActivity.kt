package com.echochat.cid.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.echochat.cid.data.AppDatabase
import com.echochat.cid.data.Message
import com.echochat.cid.databinding.ActivityChatBinding
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var friendUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendUid = intent.getStringExtra(EXTRA_FRIEND_UID).orEmpty()
        val nickname = intent.getStringExtra(EXTRA_FRIEND_NICKNAME).orEmpty()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = nickname
        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = MessageAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerMessages.layoutManager = layoutManager
        binding.recyclerMessages.adapter = adapter

        binding.buttonSend.setOnClickListener { sendMessage() }

        observeMessages(layoutManager)
    }

    private fun observeMessages(layoutManager: LinearLayoutManager) {
        val messageDao = AppDatabase.getInstance(this).messageDao()
        lifecycleScope.launch {
            messageDao.observeChat(friendUid).collect { messages ->
                adapter.submitList(messages) {
                    if (messages.isNotEmpty()) {
                        binding.recyclerMessages.scrollToPosition(messages.size - 1)
                    }
                }
                val isEmpty = messages.isEmpty()
                binding.textEmptyChat.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    private fun sendMessage() {
        val content = binding.inputMessage.text.toString().trim()
        if (content.isEmpty()) return

        val messageDao = AppDatabase.getInstance(this).messageDao()
        lifecycleScope.launch {
            messageDao.insert(
                Message(chatWithUid = friendUid, content = content, isMine = true)
            )
            binding.inputMessage.text?.clear()
        }
    }

    companion object {
        const val EXTRA_FRIEND_UID = "extra_friend_uid"
        const val EXTRA_FRIEND_NICKNAME = "extra_friend_nickname"
    }
}
