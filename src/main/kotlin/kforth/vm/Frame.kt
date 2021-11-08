package kforth.vm

data class Frame(
    var ip: Int = 0,
    var a: Int = 0,
    var b: Int = 0,
    var c: Int,
    var d: Int,
    var e: Int,
    var f: Int
)
