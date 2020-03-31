/*
 * Copyright 2019, Supermac
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.toters.tboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import timber.log.Timber;

public class CustomKeyBoard implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView mKeyboardView;
    private EditText mEditText;
    private Activity mHostActivity;
    private View mInflater;
    private Dialog mDialog = null;

    private final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
    private final static int CODE_LANGUAGE_EN = -6;
    private final static int CODE_LANGUAGE_AR = -7;
    private final static int CodeSpace = 32;

    public final static int INPUT_TYPE_TEXT = InputType.TYPE_CLASS_TEXT;
    public final static int INPUT_TYPE_NUMBER = InputType.TYPE_NUMBER_FLAG_DECIMAL;

    private boolean isEnglish = true;

    /**
     * @param host Activity class
     * @param viewId keyboardView component id defined in layout xml
     * @param layoutId keyboard layout (text or number)
     * @param editTextId Edit text keyboard to be attached to view id
     * @param etInputType Edit text input type {INPUT_TYPE_TEXT,INPUT_TYPE_NUMBER}
     */
    public CustomKeyBoard(Activity host, int viewId, int layoutId, int editTextId, int etInputType) {
        mHostActivity = host;
        mKeyboardView = mHostActivity.findViewById(viewId);
        registerEditText(editTextId);
        mEditText.setInputType(etInputType);
        notifyKeyBoardLayout(layoutId);
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(this);

        // Hide the standard keyboard initially
        mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public CustomKeyBoard (Activity host, int viewId){
        this.mHostActivity = host;
        mKeyboardView = mHostActivity.findViewById(viewId);
        mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview balloons
        mKeyboardView.setOnKeyboardActionListener(this);

        // Hide the standard keyboard initially
        host.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public CustomKeyBoard(DialogFragment dialogFragment, View inflater, int viewid, int layoutid) {
        mHostActivity = dialogFragment.getActivity();
        mInflater = inflater;
        mDialog = dialogFragment.getDialog();
        mKeyboardView = (KeyboardView) mInflater.findViewById(viewid);
        Keyboard mKeyBoard = new Keyboard(dialogFragment.getContext(), layoutid);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(this);
        dialogFragment.getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void notifyKeyBoardLayout(int layoutId) {
        Keyboard keyboard = null;
        if(mDialog != null) {
            keyboard = new Keyboard(mDialog.getContext(), layoutId);
        } else {
            keyboard = new Keyboard(mHostActivity, layoutId);
        }
        switch (mEditText.getInputType()) {
            case INPUT_TYPE_TEXT:
                mKeyboardView.setKeyboard(keyboard);
                break;
            case INPUT_TYPE_NUMBER:
                mKeyboardView.setKeyboard(keyboard);
                break;
            default:
                mKeyboardView.setKeyboard(keyboard);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        View focusCurrent = null;
        if(mDialog != null) {
            focusCurrent = mDialog.getWindow().getCurrentFocus();
        } else {
            focusCurrent = mHostActivity.getWindow().getCurrentFocus();
        }
        mEditText = (EditText) focusCurrent;
        if (mEditText != null) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();

            Timber.i("KeyCode: %s", primaryCode);
            // Apply the key to the edittext
            if (primaryCode == CodeDelete) {
                if (editable != null && start > 0) editable.delete(start - 1, start);
            } else if (primaryCode == CodeSpace) {
                if (editable != null && editable.length() > 0) {
                    editable.insert(editable.length(), " ");
                }
            } else if(primaryCode == CODE_LANGUAGE_AR) {
                if (isEnglish){
                    notifyKeyBoardLayout(R.xml.qwerty_arabic);
                    isEnglish = false;
                }
            } else if (primaryCode == CODE_LANGUAGE_EN) {
                if (!isEnglish) {
                    notifyKeyBoardLayout(R.xml.qwerty);
                    isEnglish = true;
                }
            } else {
                // insert character
                editable.insert(start, Character.toString((char) primaryCode));
            }
            if (editable != null) {
                Timber.i("Editable : %s", editable.toString());
            }
        }
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

    /**
     * Returns whether the CustomKeyboard is visible.
     */
    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * Make the CustomKeyboard visible, and hide the system keyboard for view v.
     */
    public void showCustomKeyboard(View v) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            if(mDialog != null) {
                ((InputMethodManager) mDialog.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            } else {
                ((InputMethodManager) mHostActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
    }

    /**
     * Make the CustomKeyboard invisible.
     */
    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    /**
     * Register <var>EditText<var> with resource id <var>resid</var> (on the hosting activity) for using this custom keyboard.
     *
     * @param resid The resource id of the EditText that registers to the custom keyboard.
     */
    @SuppressLint("ClickableViewAccessibility")
    public void registerEditText(int resid) {
        // Find the EditText 'resid'
        mEditText = mHostActivity.findViewById(resid);
        // Make the custom keyboard appear
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCustomKeyboard(v);
                else hideCustomKeyboard();
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        mEditText.setInputType(mEditText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void registerRecyclerEditText(EditText editText, int etInputType, int layoutId){
        this.mEditText = editText;
        notifyKeyBoardLayout(layoutId);
        // Make the custom keyboard appear
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // NOTE By setting the on focus listener, we can show the custom keyboard when the edit box gets focus, but also hide it when the edit box loses focus
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showCustomKeyboard(v);
                else hideCustomKeyboard();
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            // NOTE By setting the on click listener, we can show the custom keyboard again, by tapping on an edit box that already had focus (but that had the keyboard hidden).
            @Override
            public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        // NOTE There is also an easy way: 'edittext.setInputType(InputType.TYPE_NULL)' (but you will not have a cursor, and no 'edittext.setCursorVisible(true)' doesn't work )
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        mEditText.setInputType(mEditText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mEditText.setInputType(etInputType);
    }

    public void setLanguage(int layoutId) {
        notifyKeyBoardLayout(layoutId);
    }
}
