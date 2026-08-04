package com.echochat.cid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.echochat.cid.R
import com.echochat.cid.data.AppDatabase
import com.echochat.cid.data.Friend
import com.echochat.cid.databinding.ActivityAddFriendBinding
import com.echochat.cid.util.SessionManager
import kotlinx.coroutines.launch

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.buttonSaveFriend.setOnClickListener { saveFriend() }
    }

    private fun saveFriend() {
        val friendUid = binding.inputFriendUid.text.toString().trim().uppercase()
        val nickname = binding.inputFriendNickname.text.toString().trim()

        if (friendUid.isEmpty()) {
            binding.inputFriendUid.error = getString(R.string.error_friend_id_empty)
            return
        }
        if (nickname.isEmpty()) {
            binding.inputFriendNickname.error = getString(R.string.error_friend_nickname_empty)
            return
        }
        if (friendUid == session.myUid) {
            binding.inputFriendUid.error = getString(R.string.error_friend_id_self)
            return
        }

        val friendDao = AppDatabase.getInstance(this).friendDao()
        lifecycleScope.launch {
            val existing = friendDao.findByUid(friendUid)
            if (existing != null) {
                binding.inputFriendUid.error = getString(R.string.error_friend_exists)
                return@launch
            }
            friendDao.insert(Friend(friendUid = friendUid, nickname = nickname))
            finish()
        }
    }
}
