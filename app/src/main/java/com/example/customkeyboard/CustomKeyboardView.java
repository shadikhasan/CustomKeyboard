package com.example.customkeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomKeyboardView extends KeyboardView {

    private PopupWindow previewPopup;
    private TextView previewTextView;

    private Paint paint;
    private Map<String, String> keyMappings;
    private boolean isLongPressHandled = false;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(40);  // Adjust size as needed
        paint.setColor(Color.parseColor("#FFEFEDED"));
        paint.setAntiAlias(true);  // For smoother text rendering

        // Define mappings for keys to custom text
        keyMappings = new HashMap<>();

        keyMappings.put("q", "%");
        keyMappings.put("w", "\\"); // Note: use "\\" for backslash
        keyMappings.put("e", "k");
        keyMappings.put("r", "=");
        keyMappings.put("t", "[");
        keyMappings.put("y", "]");
        keyMappings.put("u", "<");
        keyMappings.put("i", ">");
        keyMappings.put("o", "{");
        keyMappings.put("p", "}");

        keyMappings.put("a", "@");
        keyMappings.put("s", "#");
        keyMappings.put("d", "%");
        keyMappings.put("f", "_");
        keyMappings.put("g", "j");
        keyMappings.put("h", "-");
        keyMappings.put("j", "+");
        keyMappings.put("k", "(");
        keyMappings.put("l", ")");

        keyMappings.put("z", "*");
        keyMappings.put("x", "\"");
        keyMappings.put("c", "'");
        keyMappings.put("v", ":");
        keyMappings.put("b", ";");
        keyMappings.put("n", "!");
        keyMappings.put("m", "?");

        keyMappings.put(",", ",");
        keyMappings.put("SPACE", " "); // Handle space bar separately
        keyMappings.put(".", ".");
        keyMappings.put("DONE", "DONE"); // Special key background

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
                    float xPos = key.x + key.width - 15; // 15 pixels from the right edge
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
       //Toast.makeText(getContext(), "Long press detected", Toast.LENGTH_SHORT).show();
        if (key.popupCharacters != null && key.popupCharacters.length() > 0) {
            showPreview(key);

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
        if (isLongPressHandled) {
            isLongPressHandled = false; // Reset the flag
            return true; // Indicate that the long click was handled
        }
        return super.performLongClick();
    }

    private void showPreview(Keyboard.Key key) {
        if (previewPopup == null) {
            // Inflate the key preview layout
            View previewView = LayoutInflater.from(getContext()).inflate(R.layout.key_preview2, null);

            // Access the TextView by its ID
            previewTextView = previewView.findViewById(R.id.key_preview_text);

            // Create the PopupWindow with the inflated layout
            previewPopup = new PopupWindow(previewView, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
            previewPopup.setFocusable(false); // Prevent focus issues
            previewPopup.setTouchable(true);  // Allow touch events
        }

        // Set the text to display the first character of popupCharacters
        previewTextView.setText(String.valueOf(key.popupCharacters.charAt(0)));

        // Measure the preview TextView to get its dimensions
        previewTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int previewWidth = previewTextView.getMeasuredWidth();
        int previewHeight = previewTextView.getMeasuredHeight();

        // Get the location of the key on the screen
        int[] keyLocation = new int[2];
        getLocationOnScreen(keyLocation);


        // Calculate position for the preview
        int x = keyLocation[0] + key.x + key.width / 2; // Center horizontally
        int y =  key.y - key.height; // Display above the original key

        // Show the PopupWindow
        previewPopup.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
    }




    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_UP || me.getAction() == MotionEvent.ACTION_CANCEL) {
            if (previewPopup != null && previewPopup.isShowing()) {
                previewPopup.dismiss();
            }
        }
        return super.onTouchEvent(me);
    }
}
