package com.echochat.cid.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.echochat.cid.R
import com.echochat.cid.data.AppDatabase
import com.echochat.cid.databinding.ActivityMainBinding
import com.echochat.cid.util.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: FriendListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        setSupportActionBar(binding.toolbar)

        binding.textMyUid.text = session.myUid
        binding.buttonCopyUid.setOnClickListener { copyUidToClipboard() }

        adapter = FriendListAdapter { friend ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(ChatActivity.EXTRA_FRIEND_UID, friend.friendUid)
            intent.putExtra(ChatActivity.EXTRA_FRIEND_NICKNAME, friend.nickname)
            startActivity(intent)
        }

        binding.recyclerFriends.layoutManager = LinearLayoutManager(this)
        binding.recyclerFriends.adapter = adapter

        binding.fabAddFriend.setOnClickListener {
            startActivity(Intent(this, AddFriendActivity::class.java))
        }

        observeFriends()
    }

    private fun observeFriends() {
        val friendDao = AppDatabase.getInstance(this).friendDao()
        lifecycleScope.launch {
            friendDao.observeAll().collect { friends ->
                adapter.submitList(friends)
                val isEmpty = friends.isEmpty()
                binding.textEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.recyclerFriends.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }
        }
    }

    private fun copyUidToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("EchoChat UID", session.myUid))
        Toast.makeText(this, R.string.id_copied, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuProfile) {
            startActivity(Intent(this, ProfileActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
