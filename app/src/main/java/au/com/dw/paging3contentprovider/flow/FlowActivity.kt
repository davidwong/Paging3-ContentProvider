package au.com.dw.paging3contentprovider.flow

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import au.com.dw.paging3contentprovider.MessageAdapter
import au.com.dw.paging3contentprovider.MessageComparator
import au.com.dw.paging3contentprovider.R
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Activity to retrieve the data with pagination using Kotlin flow.
 */
class FlowActivity : AppCompatActivity() {
    val viewModel by viewModels<MessageViewModel>()
    val pagingAdapter =
        MessageAdapter(MessageComparator)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.message_list)
        recyclerView.adapter = pagingAdapter

        loadMessages()
    }

    private fun loadMessages() = runWithPermissions(
        Manifest.permission.READ_SMS) {
        lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

}
