import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("air_date")
    var airDate: String? = null,
    @SerialName("episode_number")
    var episodeNumber: Int? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("overview")
    var overview: String? = null,
    @SerialName("production_code")
    var productionCode: String? = null,
    @SerialName("rating")
    var rating: Int? = null,
    @SerialName("season_number")
    var seasonNumber: Int? = null,
    @SerialName("show_id")
    var showId: Int? = null,
    @SerialName("still_path")
    var stillPath: String? = null,
    @SerialName("vote_average")
    var voteAverage: Double? = null,
    @SerialName("vote_count")
    var voteCount: Int? = null
)