package com.paxus.pay.poslinkui.demo.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.EntryRequest

/**
 * Layout payload for [EntryRequest.ACTION_SECURITY_AREA] when the host expects bounds and styling hints.
 */
data class SecureAreaBoundsPayload(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val fontSize: Int,
    val hint: String?,
    val fontColor: String?
)

/**
 * Utils for handle EntryRequest
 */
object EntryRequestUtils {

    private const val LOG_ACTION_NEXT_BASE = "Send Request Broadcast ACTION_NEXT from action  "

    /**
     * Sends a targeted in-app broadcast only when [packageName] is non-blank (explicit [Intent.setPackage]).
     */
    private fun Context.dispatchTargetedBroadcast(intent: Intent, packageName: String?) {
        val pkg = packageName?.trim().orEmpty()
        if (pkg.isEmpty()) {
            Logger.e(
                "EntryRequestUtils: skipped broadcast (blank package) action=${intent.action}"
            )
            return
        }
        intent.setPackage(pkg)
        sendBroadcast(intent)
    }

    private fun logActionNextUnquoted(action: String?) {
        Logger.i(LOG_ACTION_NEXT_BASE + (action ?: ""))
    }

    private fun logActionNextQuoted(action: String?) {
        Logger.i(LOG_ACTION_NEXT_BASE + "\"" + action + "\"")
    }

    fun sendNext(
        context: Context,
        packageName: String?,
        action: String?,
        param: String?,
        value: String?
    ) {
        logActionNextUnquoted(action)

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)
        bundle.putString(param, value)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(
        context: Context,
        packageName: String?,
        action: String?,
        param: String?,
        value: Long
    ) {
        logActionNextQuoted(action)

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)
        bundle.putLong(param, value)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(
        context: Context,
        packageName: String?,
        action: String?,
        param: String?,
        value: Int
    ) {
        logActionNextQuoted(action)

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)
        bundle.putInt(param, value)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(
        context: Context,
        packageName: String?,
        action: String?,
        param: String?,
        value: Boolean
    ) {
        logActionNextQuoted(action)
        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)
        bundle.putBoolean(param, value)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(
        context: Context,
        packageName: String?,
        action: String?,
        param: String?,
        value: ShortArray?
    ) {
        logActionNextQuoted(action)

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)
        bundle.putShortArray(param, value)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(context: Context, packageName: String?, action: String?) {
        logActionNextQuoted(action)

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_NEXT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendNext(context: Context, packageName: String?, action: String?, bundle: Bundle?) {
        var bundle = bundle
        logActionNextQuoted(action)
        if (bundle == null) bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_NEXT)
            .putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendTimeout(context: Context, packageName: String?, action: String?) {
        Logger.i("Entry Request:ACTION_TIME_OUT \"" + action + "\"")

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_TIME_OUT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendAbort(context: Context, packageName: String?, action: String?) {
        Logger.i("Entry Request:ACTION_ABORT \"" + action + "\"")

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_ABORT)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendSecureArea(
        context: Context,
        packageName: String?,
        action: String?,
        bounds: SecureAreaBoundsPayload
    ) {
        Logger.i("Send Request Broadcast ACTION_SECURITY_AREA for action \"" + action + "\"")

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        bundle.putInt(EntryRequest.PARAM_X, bounds.x)
        bundle.putInt(EntryRequest.PARAM_Y, bounds.y)
        bundle.putInt(EntryRequest.PARAM_WIDTH, bounds.width)
        bundle.putInt(EntryRequest.PARAM_HEIGHT, bounds.height)
        //bundle.putInt(EntryRequest.PARAM_FONT_SIZE, bounds.fontSize); //Testing BroadPOS Default Font Size
        bundle.putString(EntryRequest.PARAM_HINT, bounds.hint)
        bundle.putString(EntryRequest.PARAM_COLOR, bounds.fontColor)

        val intent = Intent(EntryRequest.ACTION_SECURITY_AREA)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    /**
     * For [com.pax.us.pay.ui.constant.entry.SecurityEntry.ACTION_ENTER_PIN] use only
     * For this action, EntryRequest.ACTION_SECURITY_AREA is just used to tell BroadPOS you are ready.
     * @param context Context
     * @param packageName package name
     * @param action action name
     */
    fun sendSecureArea(context: Context, packageName: String?, action: String?) {
        Logger.i("Send Request Broadcast ACTION_SECURITY_AREA for action \"" + action + "\"")

        val bundle = Bundle()
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_SECURITY_AREA)
        intent.putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }

    fun sendSetPinKeyLayout(
        context: Context,
        packageName: String?,
        action: String?,
        keyLocations: Bundle?
    ) {
        Logger.i("Send Request Broadcast ACTION_SET_PIN_KEY_LAYOUT for action \"" + action + "\"")

        val bundle = Bundle(keyLocations)
        bundle.putString(EntryRequest.PARAM_ACTION, action)

        val intent = Intent(EntryRequest.ACTION_SET_PIN_KEY_LAYOUT)
            .putExtras(bundle)
        Logger.intent(intent)
        context.dispatchTargetedBroadcast(intent, packageName)
    }
}
