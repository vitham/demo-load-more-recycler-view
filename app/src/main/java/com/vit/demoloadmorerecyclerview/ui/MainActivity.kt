package com.vit.demoloadmorerecyclerview.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vit.demoloadmorerecyclerview.R
import com.vit.demoloadmorerecyclerview.databinding.MainActivityBinding
import com.vit.demoloadmorerecyclerview.ui.base.BaseActivity
import com.vit.demoloadmorerecyclerview.utils.EndlessRecyclerViewScrollListener
import com.vit.demoloadmorerecyclerview.utils.postDelay

class MainActivity : BaseActivity<MainActivityBinding>() {

    override fun getLayoutResource(): Int = R.layout.main_activity

    private val mainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            adapter = mainAdapter

            val scrollListener = object: EndlessRecyclerViewScrollListener(rcv.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.i("Mainnn", "loadmore")
                    postDelay(500) { mainAdapter.addList(fetchData(totalItemsCount)) }
                }
            }

            rcv.addOnScrollListener(scrollListener)

            layoutRefresh.setOnRefreshListener {
                getListFromServer()
                mainAdapter.isLoadMore = true
                scrollListener.resetState()
            }
        }

        getListFromServer()
    }

    fun fetchData(offset: Int = 0) = ArrayList<MainModel>().apply {
        if (offset < LIST_SIZE) for (i in offset + 1..offset + LOAD_MORE_LIMIT)
            add(MainModel("id$i", "title $i"))
    }

    fun getListFromServer() {
        mainAdapter.loading()
        postDelay(1000) { mainAdapter.setList(fetchData(0)) }
//        when (Random.nextInt(1, 4)) {
//            1 -> postDelay(1000) { mainAdapter.setList(fetchData(0)) }
//            2 -> postDelay(1000) { mainAdapter.setList(emptyList()) }
//            3 -> postDelay(1000) { mainAdapter.error() }
//        }
    }

    companion object {
        private const val LIST_SIZE = 50
        private const val LOAD_MORE_LIMIT = 10
    }
}
