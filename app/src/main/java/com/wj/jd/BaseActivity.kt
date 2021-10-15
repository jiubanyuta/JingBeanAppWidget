package com.wj.jd

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.wj.jd.dialog.NewStyleDialog

/**
 * Created by Administrator on 2018/1/4.
 */
abstract class BaseActivity : AppCompatActivity() {
    private var barView: View? = null
    private var titleTv: TextView? = null
    public var back: LinearLayout? = null
    private lateinit var currentActivity: FragmentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        barView = findViewById(R.id.barView)
        titleTv = findViewById(R.id.title)
        back = findViewById(R.id.back)
        currentActivity = this
        immersionBar {
            statusBarView(barView)
            navigationBarColorTransform(R.color.white)
            navigationBarAlpha(1.0f)
            navigationBarDarkIcon(true)
            init()
        }

        back?.setOnClickListener {
            finish()
        }

        initView()
        initData()
        setEvent()
    }

    protected abstract fun setLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun setEvent()

    fun setTitle(str: String) {
        titleTv?.text = str
    }


    protected var dialog: NewStyleDialog? = null

    open fun createDialogTip(content: String?) {
        createDialog("温馨提示", content, "确定", null)
    }

    open fun createDialogTip(content: String?, onRightClickListener: NewStyleDialog.OnRightClickListener?) {
        createDialog("温馨提示", content, "确定", onRightClickListener)
    }


    open fun createDialog(
        title: String?,
        content: String?,
        rightBtn: String?,
        onRightClickListener: NewStyleDialog.OnRightClickListener?
    ) {
        createDialog(title, content, "", rightBtn, null, onRightClickListener)
    }

    open fun createDialog(
        title: String?,
        content: String?,
        leftBtn: String?,
        rightBtn: String?,
        onRightClickListener: NewStyleDialog.OnRightClickListener?
    ) {
        createDialog(title, content, leftBtn, rightBtn, null, onRightClickListener)
    }

    open fun createDialog(
        title: String?,
        content: String?,
        leftBtn: String?,
        rightBtn: String?,
        onLeftClickListener: NewStyleDialog.OnLeftClickListener?,
        onRightClickListener: NewStyleDialog.OnRightClickListener?
    ) {
        if (dialog != null) {
            disMissDialog()
        }
        dialog = NewStyleDialog(currentActivity, title, content, leftBtn, rightBtn)
        dialog?.apply {
            setCancelable(false)
            setLeftClickListener(onLeftClickListener)
            setRightClickListener(onRightClickListener)
            pop()
        }
    }

    open fun disMissDialog() {
        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
            dialog = null
        }
    }

}