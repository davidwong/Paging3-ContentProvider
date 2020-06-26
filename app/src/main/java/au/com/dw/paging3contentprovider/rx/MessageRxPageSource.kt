package au.com.dw.paging3contentprovider.rx

import androidx.paging.rxjava2.RxPagingSource
import au.com.dw.paging3contentprovider.Message
import au.com.dw.paging3contentprovider.MessageRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * PagingSource that loads data from a content provider.
 */
class MessageRxPageSource(val repo: MessageRepository) : RxPagingSource<Int, Message>() {

    // the initial load size for the first page may be different from the requested size
    var initialLoadSize: Int = 0

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Message>> {
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

            return repo.getMessagesRx(params.loadSize, offset)
                .subscribeOn(Schedulers.io())
                .toList()
                .map<LoadResult<Int, Message>> { list ->
                    LoadResult.Page(
                        data = list,
                        prevKey = null,
                        // assume that if a full page is not loaded, that means the end of the data
                        nextKey = if (list.size < params.loadSize) null else nextPageNumber + 1
                    )
                }
                .onErrorReturn { e -> LoadResult.Error(e) }
    }

}