package au.com.dw.paging3contentprovider.flow

import androidx.paging.PagingSource
import au.com.dw.paging3contentprovider.Message
import au.com.dw.paging3contentprovider.MessageRepository

/**
 * PagingSource that loads data from a content provider.
 */
class MessagePagingSource(val repo: MessageRepository) : PagingSource<Int, Message>() {

    // the initial load size for the first page may be different from the requested size
    var initialLoadSize: Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            if (params.key == null)
            {
                initialLoadSize = params.loadSize
            }

            // work out the offset into the database to retrieve records from the page number,
            // allow for a different load size for the first page
            val offsetCalc = {
                if (nextPageNumber == 2)
                    initialLoadSize
                else
                    ((nextPageNumber - 1) * params.loadSize) + (initialLoadSize - params.loadSize)
            }
            val offset = offsetCalc.invoke()

            val messages = repo.getMessages(params.loadSize, offset)
            val count = messages.size

            return LoadResult.Page(
                data = messages,
                prevKey = null, // Only paging forward.
                // assume that if a full page is not loaded, that means the end of the data
                nextKey = if (count < params.loadSize) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error<Int, Message>(e)
        }
    }
}