package com.animations.utils.settings

object AppSettings {
    private const val KEY_OPEN_TO = "KEY_OPEN_TO"

    private var _openTo: String = ""

    private lateinit var _storage: SharedPrefs

    fun init(shared: SharedPrefs) {
        _storage = shared
        _openTo = _storage.get(KEY_OPEN_TO, "").toString()
    }

    var openTo: String
        get() = _openTo
        set(value) {
            _openTo = value
            _storage.put(KEY_OPEN_TO, value)
        }
}
