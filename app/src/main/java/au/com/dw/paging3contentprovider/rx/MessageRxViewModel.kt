package au.com.dw.paging3contentprovider.rx

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import au.com.dw.paging3contentprovider.Message
import au.com.dw.paging3contentprovider.MessageRepository

class MessageRxViewModel (app: Application) : AndroidViewModel(app) {

    val context = app.applicationContext
    val repo =
        MessageRepository(app.applicationContext)

    fun getMessages() = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 10)
    ) {
        MessageRxPageSource(repo)
    }.observable
        .map { pagingData ->
            pagingData.map { message -> addFormattedDateTime(message) }
        }
        .cachedIn(viewModelScope)

    fun addFormattedDateTime(oldMessage: Message): Message {
        val formattedDateTime = DateUtils.formatDateTime(context, oldMessage?.timestamp,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_SHOW_TIME)
        return oldMessage.copy(dateTime = formattedDateTime)
    }

}