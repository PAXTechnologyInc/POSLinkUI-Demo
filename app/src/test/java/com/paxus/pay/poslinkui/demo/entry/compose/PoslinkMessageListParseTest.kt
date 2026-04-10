package com.paxus.pay.poslinkui.demo.entry.compose

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PoslinkMessageListParseTest {

    @Test
    fun normalizePoslinkPayload_unescapesQuotesAndBrackets() {
        val raw = """{\"messageList\":[{\"msg1\":\"a\",\"msg2\":\"b\"}]}"""
        val n = normalizePoslinkPayload(raw)
        assertTrue(n.startsWith("{"))
        assertTrue(n.contains("\"messageList\""))
        assertTrue(n.contains("\"msg1\":\"a\""))
    }

    @Test
    fun parsePoslinkMessageList_excelStyleEscapedJson_extractsLines() {
        val raw =
            """{\"messageList\":[{\"msg1\":\"message1\",\"msg2\":\"sub1\"},{\"message1\":\"message12\",\"message2\":\"sub2\"}]}"""
        val out = parsePoslinkMessageList(raw)
        assertEquals(
            "message1\nsub1\nmessage12\nsub2",
            out
        )
    }

    @Test
    fun parsePoslinkMessageList_plainJson_extractsLines() {
        val raw = """{"messageList":[{"msg1":"m1","msg2":"m2"}]}"""
        assertEquals("m1\nm2", parsePoslinkMessageList(raw))
    }

    @Test
    fun parsePoslinkMessageList_posuinewShowMessageMultiplePayload_extractsAllLines() {
        val raw = """[{"index":"x","MsgInfo":{"msg1":"message1","msg2":"message12"}},{"index":"x","MsgInfo":{"msg1":"message1","msg2":"message12"}}]"""
        assertEquals(
            "message1\nmessage12\nmessage1\nmessage12",
            parsePoslinkMessageList(raw)
        )
    }

    @Test
    fun parsePoslinkMessageList_msgInfoSerializedAsString_extractsLine() {
        val raw =
            """[{"index":"x","MsgInfo":"{\"msg1\":\"message1\",\"msg2\":\"message12\"}"}]"""
        assertEquals("message1\nmessage12", parsePoslinkMessageList(raw))
    }
}
