package com.valentina.siuuu.glyph

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphMatrixManager
import com.nothing.ketchum.GlyphToy

/**
 * Boilerplate for a Glyph Toy: binds [GlyphMatrixManager], registers the Phone (3) matrix and
 * turns the system's Messenger traffic into plain callbacks.
 *
 * Adapted from the MIT-licensed `GlyphMatrixService` in Nothing's official
 * GlyphMatrix-Example-Project, with an added hook for always-on-display ticks and a frame helper
 * that swallows `GlyphException` so a dropped frame can never take the toy down.
 */
abstract class GlyphToyService(private val tag: String) : Service() {

    var glyphMatrixManager: GlyphMatrixManager? = null
        private set

    private val eventHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what != GlyphToy.MSG_GLYPH_TOY) {
                super.handleMessage(msg)
                return
            }
            when (msg.data?.getString(GlyphToy.MSG_GLYPH_TOY_DATA)) {
                GlyphToy.EVENT_ACTION_DOWN -> onGlyphButtonPressed()
                GlyphToy.EVENT_ACTION_UP -> onGlyphButtonReleased()
                GlyphToy.EVENT_CHANGE -> onGlyphButtonLongPressed()
                GlyphToy.EVENT_AOD -> onAlwaysOnDisplayTick()
            }
        }
    }

    private val messenger = Messenger(eventHandler)

    private val managerCallback = object : GlyphMatrixManager.Callback {
        override fun onServiceConnected(component: ComponentName?) {
            val manager = glyphMatrixManager ?: return
            manager.register(Glyph.DEVICE_23112)
            Log.d(tag, "Glyph Matrix registered")
            onGlyphMatrixReady(applicationContext, manager)
        }

        override fun onServiceDisconnected(component: ComponentName?) = Unit
    }

    final override fun onBind(intent: Intent?): IBinder {
        GlyphMatrixManager.getInstance(applicationContext)?.let { manager ->
            glyphMatrixManager = manager
            manager.init(managerCallback)
        }
        return messenger.binder
    }

    final override fun onUnbind(intent: Intent?): Boolean {
        onGlyphMatrixReleased(applicationContext)
        glyphMatrixManager?.turnOff()
        glyphMatrixManager?.unInit()
        glyphMatrixManager = null
        return false
    }

    /** Pushes one 25x25 frame to the matrix. Must be called from the main thread. */
    protected fun showFrame(frame: IntArray) {
        try {
            glyphMatrixManager?.setMatrixFrame(frame)
        } catch (error: Exception) {
            Log.w(tag, "Dropped a frame: ${error.message}")
        }
    }

    /** The toy has been selected and the matrix is ours: start drawing. */
    protected open fun onGlyphMatrixReady(context: Context, manager: GlyphMatrixManager) = Unit

    /** The toy is going away: stop every coroutine and timer here. */
    protected open fun onGlyphMatrixReleased(context: Context) = Unit

    /** Glyph Button touched (Phone (3) only). */
    protected open fun onGlyphButtonPressed() = Unit

    /** Glyph Button released (Phone (3) only). */
    protected open fun onGlyphButtonReleased() = Unit

    /** Glyph Button long-pressed; needs `com.nothing.glyph.toy.longpress` = 1 in the manifest. */
    protected open fun onGlyphButtonLongPressed() = Unit

    /** Fires roughly once a minute while the toy is the selected AOD toy. */
    protected open fun onAlwaysOnDisplayTick() = Unit
}
