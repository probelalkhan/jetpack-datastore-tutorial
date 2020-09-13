package com.example.jetpackdatastore

import android.content.Context
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class BookmarkDataStore(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Bookmark>

    init {
        dataStore = applicationContext.createDataStore(
            fileName = "bookmark.pb",
            serializer = BookmarkSerializer
        )
    }

    val bookmark = dataStore.data
        .map { bookmarkSchema ->
            bookmarkSchema.bookmark
        }

    suspend fun saveBookmark(bookmark: String) {
        dataStore.updateData { currentBookmark ->
            currentBookmark.toBuilder()
                .setBookmark(bookmark)
                .build()
        }
    }

    object BookmarkSerializer : Serializer<Bookmark> {
        override fun readFrom(input: InputStream): Bookmark {
            try {
                return Bookmark.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override fun writeTo(
            t: Bookmark,
            output: OutputStream
        ) = t.writeTo(output)
    }
}