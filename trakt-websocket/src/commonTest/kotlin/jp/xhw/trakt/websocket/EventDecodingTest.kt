package jp.xhw.trakt.websocket

import jp.xhw.trakt.websocket.bot.BotEvent
import jp.xhw.trakt.websocket.bot.UserGroupAdminAdded
import jp.xhw.trakt.websocket.bot.UserGroupAdminRemoved
import jp.xhw.trakt.websocket.bot.UserGroupMember
import jp.xhw.trakt.websocket.user.UserEvent
import jp.xhw.trakt.websocket.user.UserJoined
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.uuid.Uuid

class EventDecodingTest {
    @Test
    fun botEventsIgnoreUnknownKeysAndDecodeGroupAdminPayloads() {
        val frame =
            """
            {
              "type": "USER_GROUP_ADMIN_REMOVED",
              "reqId": "019f6375-a978-761e-845c-7beb81e77d14",
              "futureEnvelopeField": true,
              "body": {
                "eventTime": "2026-07-15T01:48:03.297623478Z",
                "futureBodyField": true,
                "groupMember": {
                  "groupId": "01996560-cb65-7757-85ee-9896e8e97f67",
                  "userId": "0196196f-ae62-7b25-b582-114ceba4a6c3",
                  "futureMemberField": true
                }
              }
            }
            """.trimIndent()
        val expected =
            UserGroupMember(
                groupId = Uuid.parse("01996560-cb65-7757-85ee-9896e8e97f67"),
                userId = Uuid.parse("0196196f-ae62-7b25-b582-114ceba4a6c3"),
            )

        val removed = assertIs<UserGroupAdminRemoved>(BotEvent.decoder.decode(frame))
        val added =
            assertIs<UserGroupAdminAdded>(
                BotEvent.decoder.decode(frame.replace("USER_GROUP_ADMIN_REMOVED", "USER_GROUP_ADMIN_ADDED")),
            )

        assertEquals(expected, removed.groupMember)
        assertEquals(expected, added.groupMember)
    }

    @Test
    fun userEventsIgnoreUnknownKeys() {
        val event =
            UserEvent.decoder.decode(
                """
                {
                  "type": "USER_JOINED",
                  "futureEnvelopeField": true,
                  "body": {
                    "id": "0196196f-ae62-7b25-b582-114ceba4a6c3",
                    "futureBodyField": true
                  }
                }
                """.trimIndent(),
            )

        assertEquals(
            Uuid.parse("0196196f-ae62-7b25-b582-114ceba4a6c3"),
            assertIs<UserJoined>(event).id,
        )
    }
}
