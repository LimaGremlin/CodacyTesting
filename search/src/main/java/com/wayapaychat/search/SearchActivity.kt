package com.wayapaychat.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.wayapaychat.core.contracts.InterModuleIntent
import com.wayapaychat.local.room.models.WayaGramUser

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //check if intent came with a bundle
        val bundle = intent.extras
        if(bundle != null){
            val param = bundle.getString(InterModuleIntent.SEARCH_EXTRA) ?: ""
        }
    }
}