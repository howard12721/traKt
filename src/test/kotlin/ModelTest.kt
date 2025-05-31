import com.github.howard12721.trakt.websocket.Channel
import com.github.howard12721.trakt.websocket.DeletedDirectMessage
import com.github.howard12721.trakt.websocket.DeletedMessage
import com.github.howard12721.trakt.websocket.Embedded
import com.github.howard12721.trakt.websocket.Message
import com.github.howard12721.trakt.websocket.ReactionStamp
import com.github.howard12721.trakt.websocket.RoledUserGroupMember
import com.github.howard12721.trakt.websocket.User
import com.github.howard12721.trakt.websocket.UserGroup
import com.github.howard12721.trakt.websocket.UserGroupMember
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ModelTest {

    @Test
    fun deserializeTest001() {
        val json = """
            {
              "groupId": "f265bde2-04cc-4856-9008-3db1d953a539",
              "userId": "8e6a088f-9274-42c0-bb20-cee7913d144b"
            }
        """.trimIndent()
        Json.decodeFromString<UserGroupMember>(json)
    }

    @Test
    fun deserializeTest002() {
        val json = """
            {
              "groupId": "f265bde2-04cc-4856-9008-3db1d953a539",
              "userId": "8e6a088f-9274-42c0-bb20-cee7913d144b",
              "role": "admin"
            }
        """.trimIndent()
        Json.decodeFromString<RoledUserGroupMember>(json)
    }

    @Test
    fun deserializeTest003() {
        val json = """
            {
              "id": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45",
              "name": "takashi_trap",
              "displayName": "",
              "iconId": "2bc06cda-bdb9-4a68-8000-62f907f36a92",
              "bot": false
            }
        """.trimIndent()
        Json.decodeFromString<User>(json)
    }

    @Test
    fun deserializeTest004() {
        val json = """
            [
              {
                "raw": "@takashi_trap",
                "type": "user",
                "id": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45"
              }
            ]
        """.trimIndent()
        Json.decodeFromString<List<Embedded>>(json)
    }

    @Test
    fun deserializeTest005() {
        val json = """
            {
              "id": "bc9106b3-f9b2-4eca-9ba1-72b39b40954e",
              "channelId": "9aba50da-f605-4cd0-a428-5e4558cb911e"
            }
        """.trimIndent()
        Json.decodeFromString<DeletedMessage>(json)
    }

    @Test
    fun deserializeTest006() {
        val json = """
            {
              "id": "2d7ff3f5-c313-4f4a-a9bb-0b5f84d2b6f8",
              "userId": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45",
              "channelId": "c5a5a697-3bad-4540-b2da-93dc88181d34"
            }
        """.trimIndent()
        Json.decodeFromString<DeletedDirectMessage>(json)
    }

    @Test
    fun deserializeTest007() {
        val json = """
            [
              {
                "stampId": "1cd58034-8998-4b1c-afe4-fcd591354a97",
                "userId": "b80551a5-2768-4d29-ad78-8e0e92330c8d",
                "count": 22,
                "createdAt": "2020-10-17T03:35:17.89545Z",
                "updatedAt": "2020-10-17T03:35:34Z"
              },
              {
               "stampId": "6fc62b49-dea0-45b8-8c0c-38035082b111",
               "userId": "b80551a5-2768-4d29-ad78-8e0e92330c8d",
                "count": 23,
                "createdAt": "2020-10-17T03:35:17.737127Z",
                "updatedAt": "2020-10-17T03:35:34Z"
              },
              {
                "stampId": "b77fad4e-b63f-42a2-916c-5cfe5af3d8b9",
                "userId": "b80551a5-2768-4d29-ad78-8e0e92330c8d",
                "count": 24,
                "createdAt": "2020-10-17T03:34:56.575099Z",
                "updatedAt": "2020-10-17T03:35:34Z"
              }
            ]
        """.trimIndent()
        Json.decodeFromString<List<ReactionStamp>>(json)
    }

    @Test
    fun deserializeTest008() {
        val json = """
            {
              "id": "f265bde2-04cc-4856-9008-3db1d953a539",
              "name": "fugafuga",
              "description": "FUGA_FUGA",
              "type": "ふがふが",
              "icon": "81f6da0d-eaab-4c42-84ac-74f5111e1eaa",
              "admins": [
                {
                  "groupId": "f265bde2-04cc-4856-9008-3db1d953a539",
                  "userId": "8e6a088f-9274-42c0-bb20-cee7913d144b"
                }
              ],
              "members": [
                {
                  "groupId": "f265bde2-04cc-4856-9008-3db1d953a539",
                  "userId": "8e6a088f-9274-42c0-bb20-cee7913d144b",
                  "role": ""
                }
              ],
              "createdAt": "2023-08-25T04:04:32.912312Z",
              "updatedAt": "2023-08-25T04:04:32.912312Z"
            }
        """.trimIndent()
        Json.decodeFromString<UserGroup>(json)
    }

    @Test
    fun deserializeTest009() {
        val json = """
            {
              "id": "711afb4c-23e7-46dc-b845-5160f7088ce9",
              "name": "yamada",
              "path": "#gps/yamada",
              "parentId": "ea452867-553b-4808-a14f-a47ee0009ee6",
              "creator": {
                "id": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45",
                "name": "takashi_trap",
                "displayName": "寺田 健二",
                "iconId": "2bc06cda-bdb9-4a68-8000-62f907f36a92",
                "bot": false
              },
            "createdAt": "2019-05-08T13:45:51.487718Z",
            "updatedAt": "2019-05-08T13:45:51.487718Z"
            }
        """.trimIndent()
        Json.decodeFromString<Channel>(json)
    }

    @Test
    fun deserializeTest010() {
        val json = """
            {
              "id": "bc9106b3-f9b2-4eca-9ba1-72b39b40954e",
              "user": {
                "id": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45",
                "name": "takashi_trap",
                "displayName": "寺田 健二",
                "iconId": "2bc06cda-bdb9-4a68-8000-62f907f36a92",
                "bot": false
              },
              "channelId": "9aba50da-f605-4cd0-a428-5e4558cb911e",
              "text": "!{\"type\": \"user\", \"raw\": \"@takashi_trap\", \"id\": \"dfdff0c9-5de0-46ee-9721-2525e8bb3d45\"} こんにちは",
              "plainText": "@takashi_trap こんにちは",
              "embedded": [
                {
                  "raw": "@takashi_trap",
                  "type": "user",
                  "id": "dfdff0c9-5de0-46ee-9721-2525e8bb3d45"
                }
              ],
              "createdAt": "2019-05-08T13:33:51.632149265Z",
              "updatedAt": "2019-05-08T13:33:51.632149265Z"
            }
        """.trimIndent()
        Json.decodeFromString<Message>(json)
    }

}
