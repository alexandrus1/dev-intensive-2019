package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initView()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initView() {
        chatAdapter = ChatAdapter { item, pos ->
            when (item.chatType) {
                ChatType.ARCHIVE -> {
                    val intent = Intent(this, ArchiveActivity::class.java)
                    startActivity(intent)
                }
                else -> Snackbar.make(
                    rv_chat_list,
                    "click on ${item.title}, position $pos",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        // https://stackoverflow.com/questions/41546983/add-margins-to-divider-in-recyclerview/41547051
        divider.setDrawable(resources.getDrawable(R.drawable.item_divider, theme))

        val touchCallback =
            ChatItemTouchHelperCallback(chatAdapter, R.drawable.ic_archive_white_24dp, theme) {
                val item = it
                viewModel.addToArchive(item.id)
                val snackbar =
                    Snackbar.make(
                        rv_chat_list,
                        "Вы точно хотите добавить ${item.title} в архив?",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction(R.string.archive_undo_string) { viewModel.restoreFromArchive(item.id) }

                snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)

                val textView =
                    snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Utils.getThemeColor(R.attr.colorSnackbarText, theme))
                snackbar.show()
            }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }


        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getArchiveData().observe(this, Observer { chatAdapter.updateData(it) })
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}
