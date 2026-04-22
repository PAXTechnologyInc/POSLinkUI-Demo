package com.paxus.pay.poslinkui.demo.status

import com.pax.us.pay.ui.constant.entry.EntryResponse
import com.pax.us.pay.ui.constant.status.BatchStatus
import com.pax.us.pay.ui.constant.status.CardStatus
import com.pax.us.pay.ui.constant.status.ClssLightStatus
import com.pax.us.pay.ui.constant.status.LanguageStatus
import com.pax.us.pay.ui.constant.status.PINStatus
import com.pax.us.pay.ui.constant.status.SecurityStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusIntentPolicyTest {

    @Test
    fun `frd live-update statuses stay passive in activity overlay handling`() {
        val passiveActions = listOf(
            CardStatus.CARD_INSERTED,
            ClssLightStatus.CLSS_LIGHT_PROCESSING,
            PINStatus.PIN_ENTERING,
            SecurityStatus.SECURITY_ENTERING,
            LanguageStatus.SET_LANGUAGE
        )

        passiveActions.forEach { action ->
            assertTrue("Expected $action to be passive", StatusIntentPolicy.isPassive(action))
            assertFalse("Passive action $action must not be conclusive", StatusIntentPolicy.isConclusive(action))
        }
    }

    @Test
    fun `response and batch completion broadcasts keep their original flow`() {
        assertFalse(StatusIntentPolicy.isPassive(EntryResponse.ACTION_DECLINED))
        assertFalse(StatusIntentPolicy.isConclusive(EntryResponse.ACTION_DECLINED))

        assertFalse(StatusIntentPolicy.isPassive(BatchStatus.BATCH_CLOSE_COMPLETED))
        assertTrue(StatusIntentPolicy.isConclusive(BatchStatus.BATCH_CLOSE_COMPLETED))

        assertFalse(StatusIntentPolicy.isPassive(BatchStatus.BATCH_SF_COMPLETED))
        assertTrue(StatusIntentPolicy.isConclusive(BatchStatus.BATCH_SF_COMPLETED))
    }
}
