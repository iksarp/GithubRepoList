package com.skierski.github.applause.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.skierski.github.applause.R
import kotlinx.android.synthetic.main.search.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.search, this)
        search_edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                queryChangedListener?.invoke(s.toString())
            }
        })
    }

    @TextProp
    fun setQuery(query: CharSequence) {
        if (query != search_edit.text) {
            search_edit.setText(query)
            search_edit.setSelection(query.length)
        }
    }

    @set:CallbackProp
    var queryChangedListener: ((newQuery: String) -> Unit)? = null
}