package com.toters.mykeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.toters.tboard.CustomKeyBoard;

public class MainActivity extends AppCompatActivity {

    private CustomKeyBoard customKeyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customKeyBoard = new CustomKeyBoard(this, R.id.keyboardview, R.xml.qwerty,R.id.et, CustomKeyBoard.INPUT_TYPE_TEXT);
    }

    @Override
    public void onBackPressed() {
        if (customKeyBoard.isCustomKeyboardVisible()) {
            customKeyBoard.hideCustomKeyboard();
        } else {
            finish();
        }
    }
}
