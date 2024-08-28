package com.example.customkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;

public class CustomKeyboardService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        //Disable the popup preview
        keyboardView.setPreviewEnabled(false);

        return keyboardView;
    }


    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                getCurrentInputConnection().deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                // Handle shift key press
                break;
            case Keyboard.KEYCODE_DONE:
                // Handle done key press
                break;
            default:
                char code = (char) primaryCode;
                getCurrentInputConnection().commitText(String.valueOf(code), 1);
        }
    }


    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}
