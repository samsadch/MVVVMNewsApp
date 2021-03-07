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
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed() { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {

            breakingNewsFragment = BreakingNewsFragment()
            searchNewsFragment = SearchNewsFragment()
            bookmarksFragment = BookmarksFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, breakingNewsFragment, TAG_BREAKING_NEWS_FRAGMENT)
                .add(R.id.fragment_container, searchNewsFragment, TAG_SEARCH_NEWS_FRAGMENT)
                .add(R.id.fragment_container, bookmarksFragment, TAG_BOOKMARKS_FRAGMENT)
                .commit()
        } else {

        }

        selectFragment(selectedFragment)
    }
}

private const val TAG_BREAKING_NEWS_FRAGMENT = "TAG_BREAKING_NEWS_FRAGMENT"
private const val TAG_SEARCH_NEWS_FRAGMENT = "TAG_SEARCH_NEWS_FRAGMENT"
private const val TAG_BOOKMARKS_FRAGMENT = "TAG_BOOKMARKS_FRAGMENT"