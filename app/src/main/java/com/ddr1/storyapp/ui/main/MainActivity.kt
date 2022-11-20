package com.ddr1.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.ddr1.storyapp.R
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.databinding.ActivityMainBinding
import com.ddr1.storyapp.ui.detail.DetailStoryActivity
import com.ddr1.storyapp.ui.location.MapsActivity
import com.ddr1.storyapp.ui.login.LoginActivity
import com.ddr1.storyapp.ui.new_story.NewStoryActivity
import com.ddr1.storyapp.utils.AppConstants.EXTRA_DETAIL
import com.ddr1.storyapp.view.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setSwipeRefreshLayout()
        checkUserToken()
        setRecyclerView()
        setActions()
    }

    override fun onResume() {
        super.onResume()
        getStories()
    }

    private fun getStories() {
        viewModel.getAllStories().observe(this) { result ->
            updateRecyclerView(result)
        }
    }

    private fun setActions() {
        binding.btnNewStory.setOnClickListener {
            Intent(this, NewStoryActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuLocation -> {
                Intent(this@MainActivity, MapsActivity::class.java).also { intent ->
                    startActivity(intent)
                }
                true
            }
            R.id.menuLogout -> {
                viewModel.saveAuthToken("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkUserToken() {
        lifecycleScope.launchWhenCreated {
            viewModel.getUserToken().collect {
                if (it.isNullOrEmpty()) {
                    Intent(this@MainActivity, LoginActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            getStories()
        }
    }

    private val storiesAdapter by lazy {
        StoriesAdapter(object : StoriesAdapter.OnAdapterListener {
            override fun onClick(story: StoriesEntity, toBundle: Bundle?) {
                Intent(this@MainActivity, DetailStoryActivity::class.java).also { intent ->
                    intent.putExtra(EXTRA_DETAIL, story)
                    startActivity(intent, toBundle)
                }
            }
        })
    }

    private fun setRecyclerView() {
        listAdapter = storiesAdapter
        listAdapter.addLoadStateListener { loadState ->
            if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && listAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                binding.apply {
                    showToast("Stories Not Found")
                }
            } else {
                binding.apply {
                    binding.swipeRefresh.isRefreshing = false
                    binding.loadingProgressBar.visibility = View.GONE
                }
            }
        }
        recyclerView = binding.rvStories
        recyclerView.adapter = listAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listAdapter.retry()
            }
        )
    }

    private fun updateRecyclerView(stories: PagingData<StoriesEntity>) {
        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
        listAdapter.submitData(lifecycle, stories)
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

}