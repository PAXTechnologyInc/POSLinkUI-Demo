package com.paxus.pay.poslinkui.demo.entry.compose

import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.view.KeyEvent
import androidx.compose.ui.graphics.toArgb
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

class ShowTextBoxLegacyBehaviorTest {

    /**
     * Local JVM: `testOptions.unitTests.isReturnDefaultValues` makes [Bundle] a stub; gets do not
     * return stored values. Run under Robolectric or instrumented tests for full resolution coverage.
     */
    @Test
    @Ignore("Requires real android.os.Bundle (Robolectric or device); JVM unit test Bundle is stubbed")
    fun resolvePoslinkTextBoxButtons_trimsCustomColorPayload() {
        val extras = Bundle().apply {
            putString(EntryExtraData.PARAM_BUTTON_1_NAME, "Button1")
            putString(EntryExtraData.PARAM_BUTTON_2_NAME, "Button2")
            putString(EntryExtraData.PARAM_BUTTON_3_NAME, "Button3")
            // Legacy payload key; helpers also accept PARAM_BUTTON_3_COLOR.
            putString("button3Color", "00ff00 ")
        }

        val buttons = resolvePoslinkTextBoxButtons(extras)

        assertEquals(3, buttons.size)
        assertNull(buttons[0].containerColor)
        assertEquals(AndroidColor.parseColor("#00ff00"), buttons[2].containerColor?.toArgb())
    }

    @Test
    fun resolvePoslinkTextBoxButtonRows_keepsThreeButtonsInSingleLegacyRow() {
        val rows = resolvePoslinkTextBoxButtonRows(
            listOf(
                PoslinkTextBoxButtonSpec("Button1", "KEY0", 1),
                PoslinkTextBoxButtonSpec("Button2", "KEY1", 2),
                PoslinkTextBoxButtonSpec("Button3", "KEY2", 3)
            )
        )

        assertEquals(listOf(3), rows.map { it.size })
        assertEquals(listOf("Button1", "Button2", "Button3"), rows.single().map { it.name })
    }

    @Test
    @Ignore("Requires real android.os.Bundle (Robolectric or device); JVM unit test Bundle is stubbed")
    fun shouldDisplayPoslinkTextBoxButtons_hidesButtonsWhenHardKeyModeIsActive() {
        val extras = Bundle().apply {
            putString("enableHardKey", "1")
            putBoolean("hasPhyKeyboard", true)
        }

        assertFalse(shouldDisplayPoslinkTextBoxButtons(extras))
    }

    @Test
    @Ignore("Requires real android.os.Bundle (Robolectric or device); JVM unit test Bundle is stubbed")
    fun shouldDisplayPoslinkTextBoxButtons_keepsButtonsWhenPhysicalKeyboardIsMissing() {
        val extras = Bundle().apply {
            putString("enableHardKey", "1")
            putBoolean("hasPhyKeyboard", false)
        }

        assertTrue(shouldDisplayPoslinkTextBoxButtons(extras))
    }

    @Test
    fun resolvePoslinkTextBoxHardKeyResponse_returnsMappedKeyName() {
        val buttons = listOf(
            PoslinkTextBoxButtonSpec("Button1", "KEY0", 1),
            PoslinkTextBoxButtonSpec("Button2", "KEY1", 2),
            PoslinkTextBoxButtonSpec("Button3", "KEY2", 3)
        )

        assertEquals(
            "KEY1",
            resolvePoslinkTextBoxHardKeyResponse(
                buttons = buttons,
                enabledHardKeys = setOf("KEY0", "KEY1", "KEY2"),
                keyCode = KeyEvent.KEYCODE_1
            )
        )
        assertNull(
            resolvePoslinkTextBoxHardKeyResponse(
                buttons = buttons,
                enabledHardKeys = setOf("KEY0", "KEY1"),
                keyCode = KeyEvent.KEYCODE_2
            )
        )
    }
}
