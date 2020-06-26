package au.com.dw.paging3contentprovider

import android.content.Context
import android.provider.Telephony.Sms
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 * Repository to retrieve SMS messages from Telephony content provider.
 */
class MessageRepository (
    private val context: Context) {

    /**
     * Retrieve SMS messages by page
     * @return
     */
    fun getMessages(limit: Int, offset: Int): List<Message>
    {
        var messages = mutableListOf<Message>()
        val cursor = context.contentResolver.query(
            Sms.CONTENT_URI,
            arrayOf<String>(
                Sms.Inbox.ADDRESS,
                Sms.Inbox.BODY,
                Sms.Inbox.DATE
            ),
            null,
            null,
            Sms.Inbox.DEFAULT_SORT_ORDER + " LIMIT " + limit + " OFFSET " + offset
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val phoneOrSender =
                    cursor.getString(cursor.getColumnIndexOrThrow(Sms.ADDRESS))
                val timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(Sms.DATE))
                val msgText = cursor.getString(cursor.getColumnIndexOrThrow(Sms.BODY))

                messages.add(Message(phoneOrSender, msgText, timeStamp))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return messages
    }

    /**
     * Retrieve SMS messages by page as Observable.
     *
     * @return
     */
    fun getMessagesRx(limit: Int, offset: Int): Observable<Message> {
        return Observable.create<Message>(object : ObservableOnSubscribe<Message> {
            @Throws(Exception::class)
            override fun subscribe(emitter: ObservableEmitter<Message>) {
                val cursor = context.contentResolver.query(
                    Sms.CONTENT_URI,
                    arrayOf<String>(
                        Sms.Inbox.ADDRESS,
                        Sms.Inbox.BODY,
                        Sms.Inbox.DATE
                    ),
                    null,
                    null,
                    Sms.Inbox.DEFAULT_SORT_ORDER + " LIMIT " + limit + " OFFSET " + offset
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    while (!cursor.isAfterLast) {
                        val phoneOrSender =
                            cursor.getString(cursor.getColumnIndexOrThrow(Sms.ADDRESS))
                        val timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(Sms.DATE))
                        val msgText = cursor.getString(cursor.getColumnIndexOrThrow(Sms.BODY))

                        emitter.onNext(Message(phoneOrSender, msgText, timeStamp))
                        cursor.moveToNext()
                    }
                    cursor.close()
                }
                emitter.onComplete()
            }
        })
    }
}