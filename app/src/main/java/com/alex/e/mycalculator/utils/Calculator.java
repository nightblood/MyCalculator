package com.alex.e.mycalculator.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class Calculator {
    private String mScreenChar = "";
    private String mScreenTemp = "";
    private String mScreenResult = "";
    private List<String> mDataNum = new ArrayList<>();
    private List<String> mDataChar = new ArrayList<>();
    private OnDataChangedListener mOnChangedListener;

    public Calculator(OnDataChangedListener listener) {
        mOnChangedListener = listener;
    }

    // 1.第一次点击数字（a.首次进入、b.点击等号后）。
    // 2.点击过操作符之后，点击数字。2.
    public void onNumClick(String num) {
        if (TextUtils.isEmpty(mScreenTemp)) {
            mScreenTemp = num;
        } else {
            if (isOutLimitLength(mScreenTemp)) {
                return;
            }
            mScreenTemp += num;
        }
        mOnChangedListener.onTempChanged(mScreenTemp);
    }
    public void onCharClick(String ch) {
        if (TextUtils.isEmpty(mScreenTemp) && TextUtils.isEmpty(mScreenResult)) {
            if (".".equals(ch)) {
                mScreenTemp = "0.";
                mOnChangedListener.onTempChanged(mScreenTemp);
            }
            return;
        }
        switch (ch) {
            case "+" :
            case "-":
            case "*":
            case "/":
                if (!isZero(mScreenTemp)) {
                    if (mScreenTemp.charAt(mScreenTemp.length() - 1) == '.')
                        mScreenTemp = mScreenTemp.replace(".", "");
                    mScreenChar = ch;
                    mDataNum.add(mScreenTemp);
//                    mDataChar.add(mScreenChar);
                    mScreenTemp = "";
                    mOnChangedListener.onTempChanged(mScreenTemp);

                } else {
                    mDataChar.add(mScreenChar);
                    mScreenChar = ch;
                    mScreenTemp = "";
                    mOnChangedListener.onTempChanged(mScreenTemp);
                    String tempRes = mDataNum.get(0);
                    for (int i = 0; i < mDataNum.size() - 1; ++i) {
                        if (i == 0) {
                            tempRes = calculate(mDataNum.get(i), mDataNum.get(i + 1), mDataChar.get(i));
                        } else {
                            tempRes = calculate(tempRes, mDataNum.get(i + 1), mDataChar.get(i));
                        }
                    }
                    mScreenResult = tempRes;
                }

                mOnChangedListener.onResultChanged(mScreenResult);
                break;
            case "=":
                if (TextUtils.isEmpty(mScreenChar))
                    break;
                if (mScreenTemp.charAt(mScreenTemp.length() - 1) == '.')
                    mScreenTemp = mScreenTemp.replace(".", "");
                mDataChar.add(mScreenChar);
                mDataNum.add(mScreenTemp);

                String tempRes = mDataNum.get(0);
                for (int i = 0; i < mDataNum.size() - 1; ++i) {
                    if (i == 0) {
                        tempRes = calculate(mDataNum.get(i), mDataNum.get(i + 1), mDataChar.get(i));
                    } else {
                        tempRes = calculate(tempRes, mDataNum.get(i + 1), mDataChar.get(i));
                    }
                }
                mScreenResult = tempRes;

//                mScreenResult = calculate(mScreenResult, mScreenChar, mScreenTemp);

                mScreenTemp = "";
                mOnChangedListener.onTempChanged(mScreenTemp);
                mOnChangedListener.onResultChanged(mScreenResult);
                break;
            case ".":
                if (isZero(mScreenTemp) || mScreenTemp.contains("."))
                    break;
                mScreenTemp += ch;
                mOnChangedListener.onTempChanged(mScreenTemp);
                break;
        }
    }

    private String calculate(String a, String b, String ch) {
        BigDecimal res;
        BigDecimal arg1 = new BigDecimal(a);
        BigDecimal arg2 = new BigDecimal(b);
        switch (ch) {
            case "+" :
                res = arg1.add(arg2);
                break;
            case "-":
                res = arg1.subtract(arg2);
                break;
            case "*":
                res = arg1.multiply(arg2);
                break;
            case "/":
                res = arg1.divide(arg2);
                break;
            default:
                res = new BigDecimal("0");
        }
        res.setScale(1, BigDecimal.ROUND_HALF_UP);
        return res.toString();
    }

    public void onClearClick() {
        mScreenResult = "";
        mScreenTemp = "";
        mDataChar.clear();
        mDataNum.clear();
    }
    public void onDelClick() {

    }
    public boolean isOutLimitLength(String num) {
        return num.length() > 10;
    }
    public boolean isZero(String num) {
        if (TextUtils.isEmpty(num)) {
            return true;
        }
        String replace = num.replace(".", "");
        for (int i = 0; i < replace.length(); ++i) {
            if (replace.charAt(i) != '0')
                return false;
        }
        return true;
    }
    public void saveData() {

    }

    public interface OnDataChangedListener {
        void onResultChanged(String res);
        void onTempChanged(String temp);
    }
}
