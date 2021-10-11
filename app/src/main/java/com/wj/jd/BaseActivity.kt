package com.wj.jd

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar

/**
 * Created by Administrator on 2018/1/4.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var barView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        barView = findViewById(R.id.barView)
        immersionBar {
            statusBarColor(R.color.colorPrimary)
            statusBarView(barView)
            navigationBarColorTransform(R.color.white)
            navigationBarAlpha(1.0f)
            navigationBarDarkIcon(true)
            init()
        }

        initView()
        initData()
        setEvent()
    }

    protected abstract fun setLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun setEvent()
}