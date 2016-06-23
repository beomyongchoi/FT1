package kr.co.fintalk.fintalkone.common;

import java.text.DecimalFormat;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by BeomyongChoi on 6/23/16
 */
public class CurrencyTextWatcher implements TextWatcher {
    @SuppressWarnings("unused")
    private EditText mEditText;
    String strAmount = ""; // 임시 저장값 (콤마)

    public CurrencyTextWatcher(EditText e) {
        mEditText = e;
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {    //텍스트가 변경될때마다 실행
        if (!s.toString().equals(strAmount)) { // StackOverflow 방지
            strAmount = makeStringComma(s.toString().replace(",", ""));
            mEditText.setText(strAmount);
            Editable e = mEditText.getText();
            Selection.setSelection(e, strAmount.length());
        }
    }

    protected String makeStringComma(String str) {    // 천단위 콤마 처리
        if (str.length() == 0)
            return "";
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

}