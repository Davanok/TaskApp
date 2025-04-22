package com.davanok.taskapp.data.platform

import com.davanok.taskapp.appContext
import okio.Path.Companion.toPath

actual fun appDataDirectory() = appContext.dataDir.absolutePath.toString().toPath(normalize = true)
actual fun appCacheDirectory() = appContext.cacheDir.absolutePath.toString().toPath(normalize = true)
