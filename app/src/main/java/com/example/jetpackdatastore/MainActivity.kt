package com.example.jetpackdatastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var bookmarkDataStore: BookmarkDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPreferences = UserPreferences(this)
        bookmarkDataStore = BookmarkDataStore(this)

        buttonSaveBookmark.setOnClickListener {
            val bookmark = editTextBookmark.text.toString().trim()
            lifecycleScope.launch {
//                userPreferences.saveBookmark(bookmark)
                bookmarkDataStore.saveBookmark(bookmark)
            }
        }

        bookmarkDataStore.bookmark.asLiveData().observe(this, Observer {
            textViewCurrentBookmark.text = it
        })
    }
}