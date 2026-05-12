package jp.xhw.trakt.bot.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/** グループID。 */
@JvmInline
value class GroupId(
    val value: Uuid,
) {
    companion object {
        operator fun invoke(value: String): GroupId = parse(value)

        fun parse(value: String): GroupId = GroupId(Uuid.parse(value))
    }
}

/** グループ。 */
sealed interface Group {
    val id: GroupId

    /** ID のみを持つグループ参照。 */
    @JvmInline
    value class Ref(
        override val id: GroupId,
    ) : Group

    /** グループ詳細情報。 */
    class Detail internal constructor(
        override val id: GroupId,
        val name: String,
        val description: String?,
        val type: String,
        val iconFile: File.Ref,
        val members: List<GroupMember>,
        val createdAt: Instant,
        val updatedAt: Instant,
        val adminUsers: List<User.Ref>,
    ) : Group {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Group) return false
            return this.id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
    }
}

/** グループメンバー情報。 */
@ConsistentCopyVisibility
data class GroupMember internal constructor(
    val user: User.Ref,
    val role: String,
)
