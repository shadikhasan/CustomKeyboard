package com.example.customkeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomKeyboardView extends KeyboardView {

    private Paint paint;
    private Map<String, String> keyMappings;
    private int pressedKeyIndex = -1;
    private boolean isLongPressHandled = false;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // Initialize the paint object and key mappings
    private void init() {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(40);  // Adjust size as needed
        paint.setColor(Color.parseColor("#FFEFEDED"));
        paint.setAntiAlias(true);  // For smoother text rendering

        // Define mappings for keys to custom text
        keyMappings = new HashMap<>();
        keyMappings.put("q", "1");
        keyMappings.put("w", "2");
        keyMappings.put("e", "3");
        keyMappings.put("r", "4");
        keyMappings.put("t", "5");
        // Add more mappings as needed...
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.label != null) {
                String keyLabel = key.label.toString().toLowerCase();
                if (keyMappings.containsKey(keyLabel)) {
                    // Calculate position for top-right corner
                    float xPos = key.x + key.width - 15; // 25 pixels from the right edge
                    float yPos = key.y + paint.getTextSize() + 10; // Adjust as needed to position vertically

                    // Draw custom text on the key
                    canvas.drawText(
                            keyMappings.get(keyLabel),
                            xPos,
                            yPos,
                            paint
                    );
                }
            }
        }
    }


    @Override
    public boolean onLongPress(Keyboard.Key key) {
        Toast.makeText(getContext(), "Long press detected", Toast.LENGTH_LONG).show();
        if (key.popupCharacters != null && key.popupCharacters.length() > 0) {
            char alternateCode = key.popupCharacters.charAt(0);
            getOnKeyboardActionListener().onKey(alternateCode, null);
            isLongPressHandled = true;
            return true;
        } else {
            return super.onLongPress(key);
        }
    }

    @Override
    public boolean performLongClick() {
        if (pressedKeyIndex >= 0) {
            Keyboard.Key key = getKeyboard().getKeys().get(pressedKeyIndex);
            return onLongPress(key);
        }
        return super.performLongClick();
    }
}
