package au.com.dw.paging3contentprovider.flow

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import au.com.dw.paging3contentprovider.Message
import au.com.dw.paging3contentprovider.MessageRepository
import kotlinx.coroutines.flow.map

class MessageViewModel (app: Application) : AndroidViewModel(app) {

    val context = app.applicationContext
    val repo =  MessageRepository(app.applicationContext)

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 10)
    ) {
        MessagePagingSource(repo)
    }.flow
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