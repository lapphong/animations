package com.animations.utils

import io.flutter.embedding.android.FlutterActivity

class AppFlutterActivity : FlutterActivity() {
    override fun onResume() {
        super.onResume()
        // Engine đã chạy Dart trước khi Activity attach nên tín hiệu từ PopScope bị lỡ,
        // ở đây đăng ký lại để back (nút, cử chỉ, predictive back) được chuyển vào Flutter.
        setFrameworkHandlesBack(true)
    }
}
