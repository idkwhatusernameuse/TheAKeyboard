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

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard

    override fun onCreateInputView(): View {
        // Get secret_mode setting from SharedPreferences
        val preferences = getSharedPreferences("dev.idkwuu.theakeyboard.prefs", Context.MODE_PRIVATE)
        secretMode = preferences.getBoolean("secret_mode", false)
        // Create the keyboard
        keyboardView = (layoutInflater.inflate(R.layout.keyboard_view, null)) as KeyboardView
        val xmlKeyboard = if (caps) {
            if (secretMode) {
                R.xml.keyboard_uppercase_secret
            } else {
                R.xml.keyboard_uppercase
            }
        } else {
            if (secretMode) {
                R.xml.keyboard_lowercase_secret
            } else {
                R.xml.keyboard_lowercase
            }
        }
        keyboard = Keyboard(this, xmlKeyboard)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(onKeyboardActionListener)
        return keyboardView
    }

    private val letters = Pair(
        listOf("a", "á", "ª", "à", "ä", "â", "ã", "æ", "ā", "å", "ą"),
        listOf("A", "Á", "ª", "À", "Ä", "Â", "Ã", "Æ", "Ā", "Å", "Ą")
    )

    private val bottomKeysmash = Pair(
        listOf("f", "h", "g", "d", "k", "l", "s", "j"),
        listOf("F", "H", "G", "D", "K", "L", "S", "J")
    )

    private var caps = false
    private var secretMode = false

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
                    -1 -> {
                        caps = !caps
                        keyboard.isShifted = caps
                        keyboardView.invalidateAllKeys()
                    }
                    2 -> ic.commitText(if (caps) letters.second[0] else letters.first[0], 1)
                    3 -> ic.commitText(if (caps) letters.second[1] else letters.first[1], 1)
                    4 -> ic.commitText(if (caps) letters.second[2] else letters.first[2], 1)
                    5 -> ic.commitText(if (caps) letters.second[3] else letters.first[3], 1)
                    6 -> ic.commitText(if (caps) letters.second[4] else letters.first[4], 1)
                    7 -> ic.commitText(if (caps) letters.second[5] else letters.first[5], 1)
                    8 -> ic.commitText(if (caps) letters.second[6] else letters.first[6], 1)
                    9 -> ic.commitText(if (caps) letters.second[7] else letters.first[7], 1)
                    10 -> ic.commitText(if (caps) letters.second[8] else letters.first[8], 1)
                    11 -> ic.commitText(if (caps) letters.second[9] else letters.first[9], 1)
                    12 -> ic.commitText(if (caps) letters.second[10] else letters.first[10], 1)
                    13 -> {
                        val imeManager: InputMethodManager =
                            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imeManager.showInputMethodPicker()
                    }
                    // keysmash a
                    69420 -> {
                        val randomNumber = (0..10).random()
                        ic.commitText(
                            if (caps) letters.second[randomNumber] else letters.first[randomNumber],
                            1
                        )
                    }
                    // yed
                    69421 -> {
                        ic.commitText(if (caps) "YED" else "yed", 1)
                    }
                    // bottom keysmash
                    69422 -> {
                        val randomNumber = (0..7).random()
                        ic.commitText(
                            if (caps) bottomKeysmash.second[randomNumber] else bottomKeysmash.first[randomNumber],
                            1
                        )
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