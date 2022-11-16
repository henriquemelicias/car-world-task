package com.car_world.ui.utils

import android.graphics.Rect
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.car_world.ui.MainActivity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Information about the posture of the device
 */
sealed interface DevicePosture {
    object NormalPosture : DevicePosture

    data class TableTopPosture(
        val hingePosition: Rect,
    ) : DevicePosture

    data class BookPosture(
        val hingePosition: Rect,
    ) : DevicePosture

    data class Separating(
        val hingePosition: Rect,
        var orientation: FoldingFeature.Orientation
    ) : DevicePosture
}

@OptIn(ExperimentalContracts::class)
fun isTableTopPosture(foldFeature : FoldingFeature?) : Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.HORIZONTAL
}

@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.HALF_OPENED &&
            foldFeature.orientation == FoldingFeature.Orientation.VERTICAL
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return foldFeature?.state == FoldingFeature.State.FLAT && foldFeature.isSeparating
}

/**
 * Content shown depending on size and state of device.
 * TODO: change location or remove
 */
enum class CarworldContentType {
    LIST_ONLY, LIST_AND_DETAIL
}

fun onWindowLayoutChangeFlow(activityInstance: MainActivity) = WindowInfoTracker.getOrCreate(
    activityInstance
)
    .windowLayoutInfo(activityInstance)
    .flowWithLifecycle(activityInstance.lifecycle)
    .map { layoutInfo ->
        val foldingFeature = layoutInfo
            .displayFeatures
            .filterIsInstance<FoldingFeature>()
            .firstOrNull()
        when {
            isTableTopPosture(foldingFeature) ->
                DevicePosture.TableTopPosture(foldingFeature.bounds)

            isBookPosture(foldingFeature) ->
                DevicePosture.BookPosture(foldingFeature.bounds)

            isSeparating(foldingFeature) ->
                DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

            else -> DevicePosture.NormalPosture
        }
    }
    .stateIn(
        scope = activityInstance.lifecycleScope,
        started = SharingStarted.Eagerly,
        initialValue = DevicePosture.NormalPosture
    )