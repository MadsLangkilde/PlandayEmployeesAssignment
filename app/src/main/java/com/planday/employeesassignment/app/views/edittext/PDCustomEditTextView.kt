package com.planday.employeesassignment.app.views.edittext

import android.content.Context
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.*
import com.planday.employeesassignment.R
import kotlinx.android.synthetic.main.view_custom_edittext.view.*
import org.jetbrains.anko.textColor


open class PDCustomEditTextView : LinearLayout {

    interface TextSubmitListener {
        fun onSubmitInputText(text: String?) {}
    }

    @InverseBindingMethods(InverseBindingMethod(type = PDCustomEditTextView::class, attribute = "inputText", method = "getInputTextValue"))

    companion object {
        @BindingAdapter("inputText")
        @JvmStatic fun setInputTextValue(view: PDCustomEditTextView, inputText: String?) {
            if (view.text?.equals(inputText) ?: false)
                return

            view.text = inputText ?: ""
        }

        @BindingAdapter("inputHint")
        @JvmStatic fun setInputHint(view: PDCustomEditTextView, hintText: String?) {
            hintText?.let {
                view.getInputField()?.hint = it
            }
        }

        @InverseBindingAdapter(attribute = "inputText")
        @JvmStatic fun getInputTextValue(view: PDCustomEditTextView) : String? {
            return view.text
        }

        @BindingAdapter("app:inputTextAttrChanged")
        @JvmStatic fun setListeners(view: PDCustomEditTextView, attrChange: InverseBindingListener) {
            view.getInputField()?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    attrChange.onChange()
                }
            })
        }

        @BindingAdapter("textInputListener")
        @JvmStatic fun setTextInputListener(view: PDCustomEditTextView, listener: TextSubmitListener?) {
            listener?.let {
                view.mTextSubmitListener = it
            }
        }

        @BindingAdapter("textInputShowError")
        @JvmStatic fun setTextInputShowError(view: PDCustomEditTextView, showError: Boolean?) {
            showError?.let {
                view.setFailed(it)
            }
        }
        @BindingAdapter("textInputEditTextInputType")
        @JvmStatic fun setTextInputEditTextInputType(view: PDCustomEditTextView, inputType: String) {
            view.getInputField()?.let {
                it.inputType = when (inputType) {
                    "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    "textCapSentences" -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    else -> InputType.TYPE_CLASS_TEXT
                }
            }
        }
    }


    protected var mTextSubmitListener:        TextSubmitListener?         = null
    protected var mAttrs:                     AttributeSet?               = null
    protected var mLayout:                    View?                       = null
    protected var mOnKeyBoardEnterListener:   OnKeyBoardEnterListener?    = null
    protected var mOnTextChangedListener:     OnTextChangedListener?      = null
    protected var mOnFocusChangedListener:    OnFocusChangedListener?     = null

    enum class InputFieldType {
        TEXT, NUMBER, PASSWORD, EMAIL
    }

    var text: String?
        get() = textInputView?.getText()?.toString()
        set(value) {
            textInputView?.setText(value)
        }

    var hint: String?
        get() = textInputView?.hint.toString()
        set(value) {
            textInputView?.hint = value ?: ""
        }

    var maxLength: Int?
        get() = textInputView?.length() ?: 10
        set(value) {
            value?.let { textInputView?.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(it))) }
        }

    interface OnKeyBoardEnterListener {
        fun onKeyBoardEnterPressed(view: EditText)
    }

    interface OnTextChangedListener {
        fun onTextChanged(view: View, text: String?)
    }

    interface OnFocusChangedListener {
        fun onFocusChanged(view: View, focusGained: Boolean)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mAttrs = attrs
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mAttrs = attrs
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr) {
        mAttrs = attrs
    }


    init {
        inflate(context, R.layout.view_custom_edittext, this)

        textInputView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text != null && text.isNotEmpty())
                    setFailed(false)

                mLayout?.let {
                    mOnTextChangedListener?.onTextChanged(it, text?.toString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {

            }
        })

        textInputView.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.getAction() == KeyEvent.ACTION_DOWN) {
                    when(keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER -> {
                            getInputField()?.let { mTextSubmitListener?.onSubmitInputText(it.text.toString()) }
                            return false
                        }
                    }
                }
                return false
            }
        })

        textInputView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event?.action == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    getInputField()?.let { mTextSubmitListener?.onSubmitInputText(it.text.toString()) }
                    return true
                }
                return false;
            }
        })

        setInputType()
    }

    fun getInputField(): EditText? {
        return textInputView
    }

    fun setFailed(failed: Boolean) {
        textInputErrorCircle?.visibility = if (failed) View.VISIBLE else View.GONE
        textInputView?.textColor = ContextCompat.getColor(context, if (failed) R.color.red else R.color.grey_dark)
        textInputView?.background  = if (failed) ContextCompat.getDrawable(context, R.drawable.textinput_edittext_round_invalid) else ContextCompat.getDrawable(context, R.drawable.textinput_edittext_round)
    }

    fun setMaxLength(max: Int) {
        textInputView.filters = arrayOf(InputFilter.LengthFilter(max))
    }

    fun setOnKeyboardEnterListener(listener: OnKeyBoardEnterListener) {
        mOnKeyBoardEnterListener = listener
        textInputView.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN) {
                    when(keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER -> {
                            getInputField()?.let { mOnKeyBoardEnterListener?.onKeyBoardEnterPressed(it) }
                            return true
                        }
                    }
                }
                return false
            }
        })

        textInputView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event?.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    getInputField()?.let { mOnKeyBoardEnterListener?.onKeyBoardEnterPressed(it) }
                    return true
                }
                return false;
            }
        })

    }

    fun setOnTextChangedListener(listener: OnTextChangedListener) {
        mOnTextChangedListener = listener
    }

    fun setOnFocusChangedListener(listener: OnFocusChangedListener) {
        mOnFocusChangedListener = listener
        textInputView.onFocusChangeListener =
            OnFocusChangeListener { _, focusGained ->
                mLayout?.let {
                    mOnFocusChangedListener?.onFocusChanged(it, focusGained)
                }
            }
    }

    fun doRequestFocus() {
        textInputView?.requestFocus()
    }

    override fun isEnabled(): Boolean {
        return textInputView?.isEnabled ?: true
    }

    override fun setEnabled(enabled: Boolean) {
        textInputView?.isEnabled = enabled
    }

    private fun setInputType() {
        when(getInputType()) {
            InputFieldType.TEXT -> {
                textInputView?.setRawInputType(InputType.TYPE_CLASS_TEXT)
            }
            InputFieldType.NUMBER -> {
                textInputView?.setRawInputType(InputType.TYPE_CLASS_NUMBER)
            }
            InputFieldType.PASSWORD -> {
                textInputView?.setTransformationMethod(PasswordTransformationMethod.getInstance())
                textInputView?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            InputFieldType.EMAIL -> {
                textInputView?.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            }
        }
    }

    open fun getInputType() : InputFieldType {
        return InputFieldType.TEXT
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        textInputView?.addTextChangedListener(textWatcher)
    }
}