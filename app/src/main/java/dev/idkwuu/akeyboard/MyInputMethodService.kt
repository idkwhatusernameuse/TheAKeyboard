package dev.idkwuu.akeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager


class MyInputMethodService : InputMethodService() {

    lateinit var keyboardView: KeyboardView
    lateinit var keyboard: Keyboard

    override fun onCreateInputView(): View {
        keyboardView = (layoutInflater.inflate(R.layout.keyboard_view, null)) as KeyboardView
        keyboard = Keyboard(this, R.xml.keyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener)
        return keyboardView
    }

    private val onKeyboardActionListener: OnKeyboardActionListener =
        object : OnKeyboardActionListener {
            override fun onKey(primaryCode: Int, keyCodes: IntArray) {
                val ic = currentInputConnection ?: return
                when (primaryCode) {
                    Keyboard.KEYCODE_DELETE -> {
                        val selectedText = ic.getSelectedText(0)
                        if (TextUtils.isEmpty(selectedText)) {
                            // no selection, so delete previous character
                            ic.deleteSurroundingText(1, 0)
                        } else {
                            // delete the selection
                            ic.commitText("", 1)
                        }
                    }
                    1 -> ic.commitText("A", 1)
                    2 -> ic.commitText("a", 1)
                    3 -> ic.commitText("á", 1)
                    4 -> ic.commitText("ª", 1)
                    5 -> ic.commitText("à", 1)
                    6 -> ic.commitText("ä", 1)
                    7 -> ic.commitText("â", 1)
                    8 -> ic.commitText("ã", 1)
                    9 -> ic.commitText("æ", 1)
                    10 -> ic.commitText("ā", 1)
                    11 -> ic.commitText("å", 1)
                    12 -> ic.commitText("ą", 1)
                    13 -> {
                        val imeManager: InputMethodManager =
                            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imeManager.showInputMethodPicker()
                    }
                    else -> {
                        val code = primaryCode.toChar()
                        ic.commitText(code.toString(), 1)
                    }
                }
            }

            override fun onPress(arg0: Int) {}
            override fun onRelease(primaryCode: Int) {}
            override fun onText(text: CharSequence) {}
            override fun swipeDown() {}
            override fun swipeLeft() {}
            override fun swipeRight() {}
            override fun swipeUp() {}
        }
}