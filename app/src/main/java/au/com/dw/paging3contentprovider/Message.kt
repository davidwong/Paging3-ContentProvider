package au.com.dw.paging3contentprovider

data class Message(
    /** Sender phone number or no-reply name  */
    val fromAddress: String,
    val body: String,
    val timestamp: Long,

    // util to hold formatted datetime string
    val dateTime: String? = null
)
