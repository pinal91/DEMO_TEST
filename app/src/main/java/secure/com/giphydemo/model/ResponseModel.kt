package secure.com.giphydemo.model

data class ResponseModel(
    val title: String? = null,
    val rows: List<DataItem?>? = null
)

data class DataItem(
    val imageHref: String? = null,
    val description: String? = null,
    val title: String? = null
)
