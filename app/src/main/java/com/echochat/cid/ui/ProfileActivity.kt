package com.echochat.cid.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.echochat.cid.R
import com.echochat.cid.databinding.ActivityProfileBinding
import com.echochat.cid.util.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.textMyUid.text = session.myUid
        binding.inputDisplayName.setText(session.displayName)

        binding.buttonCopyUid.setOnClickListener { copyUidToClipboard() }
        binding.buttonSaveName.setOnClickListener { saveName() }
    }

    private fun saveName() {
        val name = binding.inputDisplayName.text.toString().trim()
        if (name.isEmpty()) {
            binding.inputDisplayName.error = getString(R.string.error_name_empty)
            return
        }
        session.displayName = name
        Toast.makeText(this, R.string.name_saved, Toast.LENGTH_SHORT).show()
    }

    private fun copyUidToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("EchoChat UID", session.myUid))
        Toast.makeText(this, R.string.id_copied, Toast.LENGTH_SHORT).show()
    }
}
