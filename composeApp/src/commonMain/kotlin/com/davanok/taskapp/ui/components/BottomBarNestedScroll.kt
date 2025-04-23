package com.davanok.taskapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class BottomBarNestedScroll(private val onVisibleChange: (visible: Boolean) -> Unit) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y < -1) onVisibleChange(false)
        if (available.y > 1) onVisibleChange(true)
        return Offset.Zero
    }
}

@Composable
fun rememberBottomBarNestedScroll(onVisibleChange: (Boolean) -> Unit) = remember {
    BottomBarNestedScroll(onVisibleChange)
}