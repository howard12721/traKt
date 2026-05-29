package jp.xhw.trakt.bot.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ChannelPathTest {
    @Test
    fun parse_stripsLeadingHash() {
        assertEquals(
            "times/26/Falcon/o_benkyo",
            ChannelPath.parse("#times/26/Falcon/o_benkyo").value,
        )
    }

    @Test
    fun parse_rejectsInvalidPath() {
        assertFailsWith<IllegalArgumentException> {
            ChannelPath.parse("#invalid path")
        }
    }
}
