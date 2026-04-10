package com.paxus.pay.poslinkui.demo.utils

import android.os.Build
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.paxus.pay.poslinkui.demo.BuildConfig
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Kim.L on 2018/5/25.
 */
class FileLogAdapter(
    formatStrategy: FormatStrategy,
    private val logPath: String?,
    private val filenamePrefix: String?
) : AndroidLogAdapter(formatStrategy), Thread.UncaughtExceptionHandler {
    private val dateFormatFileName = SimpleDateFormat("yyyyMMdd", Locale.US)
    private val dateFormatLog = SimpleDateFormat("MM-dd-HH-mm-ss.SSS", Locale.US)
    private val mHandler: Thread.UncaughtExceptionHandler?
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        mHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun log(priority: Int, tag: String?, message: String) {
        var effectiveTag = tag
        if (priority >= Log.INFO) {
            if (effectiveTag.isNullOrEmpty()) {
                effectiveTag = priorityToTag(priority)
            }
            writeToFile(priority, effectiveTag, message)
        }
        if (BuildConfig.DEBUG) {
            super.log(priority, effectiveTag, message)
        }
    }

    override fun uncaughtException(thread: Thread, ex: Throwable?) {
        if (handleExceptions(ex) && mHandler != null) {
            mHandler.uncaughtException(thread, ex)
        }
    }

    private fun handleExceptions(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        log(Log.ASSERT, "App Crash", ex.message ?: "")
        log(Log.ASSERT, "Terminal Info", Build.MODEL)
        uploadExceptionToServer()
        // Arvind: Print the log stacktrace to the logfile and display it
        Logger.e("FileLogAdaptor::handleExceptions() => " + ex + "\n" + getStackTrace(ex.getStackTrace()))
        return true
    }

    private fun writeToFile(priority: Int, tag: String?, msg: String?) {
        val stackTrace = Throwable().getStackTrace()
        val stacktraceCnt = 5
        if (stackTrace.size <= stacktraceCnt) {
            return
        }
        val frame = stackTrace[stacktraceCnt] ?: return
        val codeFile = frame.fileName
        val codeLineNum = frame.lineNumber
        val codeMethod = frame.methodName
        executorService.submit(Runnable {
            doWriteToFile(
                priority,
                tag,
                msg,
                codeFile,
                codeLineNum,
                codeMethod
            )
        })
    }

    private fun doWriteToFile(
        priority: Int,
        tag: String?,
        msg: String?,
        codeFile: String?,
        codeLineNum: Int,
        codeMethod: String?
    ) {
        if (null == logPath) {
            Logger.e("logPath == null ，should init LogToFile")
            return
        }


        val log =
            dateFormatLog.format(Date()) + " " + tag + " " + codeFile + "[" + codeLineNum + "]" + "(" + codeMethod + ") " + msg + "\n"

        val dir = File(logPath)
        if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
            Logger.e("FileLogAdapter: failed to create log directory")
            return
        }
        val childName = filenamePrefix.orEmpty() + dateFormatFileName.format(Date()) + FILE_NAME_SUFFIX
        val outFile = File(dir, childName).canonicalFile
        val base = dir.canonicalFile
        if (!outFile.path.startsWith(base.path + File.separator) && outFile.path != base.path) {
            Logger.e("FileLogAdapter: resolved log file escaped base directory")
            return
        }

        try {
            FileOutputStream(outFile, true).use { fos ->
                BufferedWriter(OutputStreamWriter(fos)).use { bw ->
                    bw.write(log)
                    if (!BuildConfig.DEBUG) {
                        val simpleLog =
                            codeFile + "[" + codeLineNum + "]" + "(" + codeMethod + ") " + msg + "\n"
                        //tag + "\b" avoids tag string being empty.
                        Log.println(priority, tag + "\b", simpleLog)
                    }
                }
            }
        } catch (e: IOException) {
            Logger.e(e.message ?: "IOException")
        }
    }

    private fun uploadExceptionToServer() {
        // Demo build: no remote crash upload endpoint is configured.
    }

    // Arvind: To get the stack trace from the Throwable
    private fun getStackTrace(stackTraceElements: Array<StackTraceElement?>): String {
        val sb = StringBuilder()
        for (element in stackTraceElements) sb.append(element.toString() + "\n")
        return sb.toString()
    }

    private fun priorityToTag(priority: Int): String {
        when (priority) {
            Log.VERBOSE -> return "V"
            Log.DEBUG -> return "D"
            Log.INFO -> return "I"
            Log.WARN -> return "W"
            Log.ERROR -> return "E"
            Log.ASSERT -> return "A"
        }

        return "D"
    }

    companion object {
        private const val FILE_NAME_SUFFIX = ".log"
    }
}
