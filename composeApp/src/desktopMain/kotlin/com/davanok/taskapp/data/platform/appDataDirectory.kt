package com.davanok.taskapp.data.platform

import com.davanok.taskapp.BuildConfig
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

actual fun appDataDirectory(): Path {
    val appId = BuildConfig.PACKAGE_NAME
    val getEnvPath: (String) -> Path = { System.getenv(it).toPath(normalize = true) }
    return when (hostOs) {
        OS.MacOS -> getEnvPath("HOME") / "Library" / "Application Support" / appId
        OS.Windows -> getEnvPath("LOCALAPPDATA") / appId
        OS.Linux -> getEnvPath("HOME") / ".local" / "share" / appId
        else -> getEnvPath("HOME") / ".$appId"
    }
}
actual fun appCacheDirectory() = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / BuildConfig.PACKAGE_NAME