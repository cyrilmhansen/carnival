package com.example.identitymanager

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class MyAccessibilityService : AccessibilityService() {

    private val TAG = "MyAccessibilityService"

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: Service is connected.")
        // You can refine serviceInfo here if needed, e.g., by package names
        // val info = AccessibilityServiceInfo()
        // info.eventTypes = AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ...
        // info.packageNames = arrayOf("com.example.targetapp") // To focus on specific apps
        // serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        Log.d(TAG, "onAccessibilityEvent: type=${AccessibilityEvent.eventTypeToString(event.eventType)}, text=${event.text}, contentDescription=${event.contentDescription}")

        val sourceNode: AccessibilityNodeInfo? = event.source
        if (sourceNode != null) {
            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                    Log.i(TAG, "Focused Node: className='${sourceNode.className}', " +
                              "viewIdResourceName='${sourceNode.viewIdResourceName}', " +
                              "text='${sourceNode.text}', " +
                              "hint='${sourceNode.hintText}', " + // Changed from hintText to hintText
                              "contentDescription='${sourceNode.contentDescription}', " +
                              "isEditable='${sourceNode.isEditable}', " +
                              "isPassword='${sourceNode.isPassword}'")

                    // Log window information
                    val windowId = sourceNode.windowId
                    // val windowInfo = rootInActiveWindow // Can be null - DEPRECATED
                    // Use findFocus(AccessibilityNodeInfo.FOCUS_INPUT) or AccessibilityWindowInfo for more robust window info
                    // For now, let's get basic root info if available for the current source node's window
                    val rootNode = rootInActiveWindow // This is the root of the active window, may not always be the sourceNode's direct window root in complex views.
                                                    // For more specific window info, one might need to iterate through getWindow() on nodes or use AccessibilityWindowManager.
                    if (rootNode != null) {
                         // Attempt to get package name from the root node if possible
                        val appPackageName = rootNode.packageName?.toString() ?: "UnknownPackage"
                        Log.i(TAG, "Parent Window (from rootInActiveWindow): id=$windowId, appPackage='$appPackageName'")
                        // rootNode.recycle() // Recycle if we are done with it, but careful if it's the main rootInActiveWindow
                    } else {
                        Log.i(TAG, "Parent Window: id=$windowId, (rootInActiveWindow is null for this event or not available)")
                    }
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    Log.i(TAG, "Window State Changed: EventText='${event.text}', ContentDescription='${event.contentDescription}'")
                    // event.source might be null for some window state changes, or represent the root of the window
                    sourceNode.packageName?.let {
                         Log.i(TAG, "Window State Change for app: $it")
                    }
                    // Optionally, traverse the new window's content if needed
                }
                // Add more event types to handle as needed
            }
            // It's crucial to recycle the node info objects to avoid memory leaks
            // However, event.source is managed by the framework and should not be recycled by us.
            // Nodes obtained via other methods like getChild, getParent, findAccessibilityNodeInfosByText etc. MUST be recycled.
            // sourceNode.recycle() // DO NOT RECYCLE event.source
        } else {
            Log.w(TAG, "Event source node is null for event type: ${AccessibilityEvent.eventTypeToString(event.eventType)}")
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "onInterrupt: Service interrupted")
    }

    // Optional: Add a utility function to log node hierarchy if needed for debugging
    // private fun logNodeHierarchy(node: AccessibilityNodeInfo?, depth: Int = 0) { ... }
}
