package com.smileidentity.react.expo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.size
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView
import timber.log.Timber

/**
 * A base Compose-enabled view to be used within Expo Modules in React Native.
 *
 * This class manages the setup of a [ComposeView], optionally hosting it directly or
 * acting as a container for other nested [SmileIDExpoComposeView] instances. It provides lifecycle
 * management for Compose content and a local [ViewModelStore] for view-level state isolation.
 *
 * Inspired by:
 * https://github.com/expo/expo/blob/main/packages/expo-modules-core/android/src/compose/expo/modules/kotlin/views/ExpoComposeView.kt
 *
 * @param context The view context, typically provided by the React Native host.
 * @param appContext The [AppContext] provided by Expo Modules.
 * @param shouldHostComposeContent If true, this view directly hosts Compose content;
 *        otherwise, it acts as a container for nested Compose views.
 *
 * @note We explicitly manage a [ViewModelStoreOwner] to handle cases where the
 * Smile ID native SDK components retain ViewModel state across navigations. This
 * ensures that any internal state (such as image paths, steps, or loading indicators)
 * does not persist unintentionally when re-opening views, avoiding stale or duplicated UI.
 */
abstract class SmileIDExpoComposeView(
    context: Context,
    appContext: AppContext,
    private val shouldHostComposeContent: Boolean = false
) : ExpoView(context, appContext) {

    /**
     * Optional custom ViewModelStoreOwner when outside of a FragmentActivity.
     * This is primarily used to explicitly clear ViewModel state from Smile ID native SDK components.
     */
    private var customViewModelStoreOwner: ViewModelStoreOwner? = null

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * Implement this to define the root Composable UI content for this view.
     */
    @Composable
    abstract fun Content()

    /**
     * Used by React Native to determine if Android layout system should measure and layout children.
     */
    override val shouldUseAndroidLayout: Boolean = shouldHostComposeContent

    init {
        if (shouldHostComposeContent) {
            addComposeView()
        } else {
            visibility = GONE
            setWillNotDraw(true)
        }
    }

    /**
     * Handles view measurement depending on whether the view is attached.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (shouldUseAndroidLayout && !isAttachedToWindow) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * Renders the content of this view or delegates to child views.
     */
    @Composable
    protected fun Children() {
        if (shouldHostComposeContent) {
            RenderContent()
        } else {
            RenderChildren()
        }
    }

    /**
     * Renders the root content defined by [Content].
     */
    @Composable
    private fun RenderContent() {
        Content()
    }

    /**
     * Iterates through all child views and invokes their [Content] composables.
     */
    @Composable
    private fun RenderChildren() {
        for (index in 0 until size) {
            (getChildAt(index) as? SmileIDExpoComposeView)?.Content()
        }
    }

    /**
     * Adds a [ComposeView] and sets up the composition and lifecycle handling.
     */
    private fun addComposeView() {
        val composeView = ComposeView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setupViewModelStoreOwner(this)

            setContent {
                Children()
            }

            addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    // Optional: handle re-attachment if needed
                }

                override fun onViewDetachedFromWindow(v: View) {
                    disposeComposition()
                    cleanup()
                }
            })
        }

        addView(composeView)
    }

    /**
     * Assigns a [ViewModelStoreOwner] to the [ComposeView]. If the context is not
     * a [FragmentActivity], a new custom store owner is created and tracked for later cleanup.
     */
    private fun setupViewModelStoreOwner(composeView: ComposeView) {
        val viewModelStoreOwner: ViewModelStoreOwner = when (context) {
            is FragmentActivity -> context
            else -> object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore = ViewModelStore()
            }.also { customViewModelStoreOwner = it }
        } as ViewModelStoreOwner

        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
    }

    /**
     * Clears the [ViewModelStore] of the custom owner (if used) and resets it.
     * This is essential for cleaning up retained state from Smile ID native components.
     */
    private fun cleanup() {
        customViewModelStoreOwner?.viewModelStore?.let { store ->
            try {
                store.clear()
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear ViewModelStore")
            }
        }
        customViewModelStoreOwner = null
    }

    /**
     * Called when the view is detached from the window.
     * Removes all views and performs cleanup of ViewModel and Compose resources.
     *
     * The children are already detached by the time this runs, and removing them now detaches
     * the [ComposeView] a second time, which crashes in Compose's accessibility delegate once
     * its handler is gone ("Unable to destroy activity": NullPointerException). The removal
     * waits until the detach pass has finished, and is skipped if the view was attached again;
     * the ViewModel cleanup goes with it, so a reattached view keeps the state it shows.
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mainHandler.post {
            if (!isAttachedToWindow) {
                removeAllViews()
                cleanup()
            }
        }
    }
}
