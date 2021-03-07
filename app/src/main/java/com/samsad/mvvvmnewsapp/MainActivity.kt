package com.samsad.mvvvmnewsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.samsad.mvvvmnewsapp.features.bookmarks.BookmarksFragment
import com.samsad.mvvvmnewsapp.features.breakingnews.BreakingNewsFragment
import com.samsad.mvvvmnewsapp.features.searchnews.SearchNewsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var breakingNewsFragment: BreakingNewsFragment
    private lateinit var searchNewsFragment: SearchNewsFragment
    private lateinit var bookmarksFragment: BookmarksFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(breakingNewsFragment, searchNewsFragment, breakingNewsFragment)

    private var selectedIndex = 0

    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
         val transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed() { index,fragment->

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}