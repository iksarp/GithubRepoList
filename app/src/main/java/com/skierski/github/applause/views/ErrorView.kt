package com.skierski.github.applause.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.skierski.github.applause.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val messageTextView: TextView
    private val retryButton: TextView

    init {
        inflate(context, R.layout.error, this)
        messageTextView = findViewById(R.id.message)
        retryButton = findViewById(R.id.retry)
        orientation = VERTICAL
    }

    @TextProp
    fun setMessage(message: CharSequence) {
        messageTextView.text = message
    }

    @CallbackProp
    fun setRetryClickListener(clickListener: OnClickListener?) {
        retryButton.setOnClickListener(clickListener)
    }
}