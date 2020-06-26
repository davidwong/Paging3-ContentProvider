package au.com.dw.paging3contentprovider.rx

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import au.com.dw.paging3contentprovider.MessageAdapter
import au.com.dw.paging3contentprovider.MessageComparator
import au.com.dw.paging3contentprovider.R
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import io.reactivex.disposables.CompositeDisposable

/**
 * Activity to retrieve the data with pagination using RxJava 2.
 */
class RxActivity : AppCompatActivity() {
    val viewModel by viewModels<MessageRxViewModel>()
    val pagingAdapter =
        MessageAdapter(MessageComparator)
    val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.message_list)
        recyclerView.adapter = pagingAdapter

        loadMessages()
    }

    private fun loadMessages() = runWithPermissions(
        Manifest.permission.READ_SMS) {

            disposable.add(viewModel.getMessages()
                .subscribe { pagingData ->
                pagingAdapter.submitData(lifecycle, pagingData)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
