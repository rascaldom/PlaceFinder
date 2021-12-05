package com.hyundai.placefinder.ui.group

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyundai.placefinder.R
import com.hyundai.placefinder.common.BaseActivity
import com.hyundai.placefinder.databinding.ActivityGroupBinding
import com.hyundai.placefinder.ui.place.PlaceListActivity
import com.hyundai.placefinder.viewmodel.GroupListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupListActivity : BaseActivity() {

    private val binding: ActivityGroupBinding by binding(R.layout.activity_group)
    private val viewModel: GroupListViewModel by viewModel()

    private val groupListAdapter: GroupListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@GroupListActivity
            viewModel = this@GroupListActivity.viewModel
        }

        initializeView()
        initializeGroupListAdapter()

        viewModel.getGroupInfo()
    }

    private fun initializeView() {
        initializeClickListener()
        initializeGroupListAdapter()
    }

    private fun initializeClickListener() {
        binding.ivExportJson.setOnClickListener {
            lifecycleScope.launch {
                viewModel.getAllPlaces().collect {
                    if (it.isNotEmpty()) {
                        Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, it.json())
                            type = "text/json"
                            startActivity(this)
                        }
                    } else {
                        Toast.makeText(this@GroupListActivity, getString(R.string.no_place_data), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.ivAddGroup.setOnClickListener {
            val input = AppCompatEditText(this@GroupListActivity).apply {
                setPadding(paddingLeft + 30, paddingTop, paddingRight + 30, paddingBottom)
            }
            AlertDialog.Builder(this@GroupListActivity).apply {
                setTitle(getString(R.string.new_group))
                setView(input)
                setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    viewModel.addGroup(input.text.toString())
                    lifecycleScope.launch {
                        delay(200)
                        viewModel.getGroupInfo()
                    }
                }
            }.show()
        }
    }

    private fun initializeGroupListAdapter() {
        with(binding.rvGroupList) {
            adapter = groupListAdapter.apply {
                onItemClick = {
                    startActivity(Intent(this@GroupListActivity, PlaceListActivity::class.java).apply {
                        it?.let {
                            putExtra(PlaceListActivity.EXTRA_KEY_GROUP_ID, it.id)
                            putExtra(PlaceListActivity.EXTRA_KEY_GROUP_TITLE, it.title)
                        }
                    })
                }
                onDeleteButtonClick = {
                    AlertDialog.Builder(this@GroupListActivity).apply {
                        it?.let {
                            setTitle(it.title)
                            setMessage(R.string.group_delete_question)
                            setPositiveButton(getString(R.string.confirm)) { _, _ ->
                                viewModel.deleteGroup(it)
                                lifecycleScope.launch {
                                    delay(200)
                                    viewModel.getGroupInfo()
                                }
                            }
                        }
                        setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.show()
                }
            }
            addItemDecoration(DividerItemDecoration(this@GroupListActivity, LinearLayoutManager.VERTICAL))
        }
    }

}