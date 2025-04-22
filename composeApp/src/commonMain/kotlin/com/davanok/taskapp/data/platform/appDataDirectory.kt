package com.davanok.taskapp.data.platform

import okio.Path

expect fun appDataDirectory(): Path
expect fun appCacheDirectory(): Path
