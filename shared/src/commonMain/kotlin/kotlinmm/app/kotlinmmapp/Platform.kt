package kotlinmm.app.kotlinmmapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform