package com.tuuzed.androidx.exdialog

import androidx.annotation.IntDef

@IntDef(
    flag = true, value = [
        ExDialogEvent.ON_PRE_SHOW,
        ExDialogEvent.ON_SHOW,
        ExDialogEvent.ON_CANCEL,
        ExDialogEvent.ON_DISMISS,
        ExDialogEvent.ON_CLICK_POSITIVE_BUTTON,
        ExDialogEvent.ON_CLICK_NEGATIVE_BUTTON,
        ExDialogEvent.ON_CLICK_NEUTRAL_BUTTON
    ]
)
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ExDialogEvent {
    companion object Constants {
        const val ON_PRE_SHOW = 1
        const val ON_SHOW = 2
        const val ON_CANCEL = 3
        const val ON_DISMISS = 4
        const val ON_CLICK_POSITIVE_BUTTON = 5
        const val ON_CLICK_NEGATIVE_BUTTON = 6
        const val ON_CLICK_NEUTRAL_BUTTON = 7
    }
}
