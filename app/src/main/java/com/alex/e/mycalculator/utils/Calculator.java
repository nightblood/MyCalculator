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
            if (TextUtils.isEmpty(mScreenChar)) {
                mDataNum.clear();
                mDataChar.clear();
                mScreenResult = "";
                mOnChangedListener.onResultChanged(mScreenResult);
                mOnChangedListener.onListDataChanged("", "");
            }
        } else {
            if (isOutLimitLength(mScreenTemp)) {
                return;
            }
            mScreenTemp += num;
        }
        mOnChangedListener.onTempChanged(mScreenTemp);
    }
    public void onCharClick(String ch) {
        if (isZero(mScreenTemp) && TextUtils.isEmpty(mScreenResult)) {
            if (".".equals(ch)) {
                mScreenTemp = "0.";
                mOnChangedListener.onTempChanged(mScreenTemp);
            }
            return;
        }
        switch (ch) {
            case "+":
            case "-":
            case "*":
            case "/":
                if (isZero(mScreenTemp) && mDataNum.size() == 0) {
                    break;
                }
                if (!isZero(mScreenTemp) && mDataNum.size() == 0) {
                    mScreenChar = ch;
                    mDataNum.add(trimDot(mScreenTemp));
                    mScreenTemp = "";
                    mOnChangedListener.onTempChanged(mScreenTemp);
                    mOnChangedListener.onCharChanged(mScreenChar);
                    mOnChangedListener.onListDataChanged("", mDataNum.get(mDataNum.size() - 1));
                } else if (!isZero(mScreenTemp) && mDataNum.size() != 0) {
                    mDataChar.add(mScreenChar);
                    mScreenChar = ch;
                    mDataNum.add(mScreenTemp);
                    mScreenResult = getResultFromList();
                    if (mScreenResult == null) {
                        mScreenResult = "除数不能为0";
                    }
                    mScreenTemp = "";
                    mOnChangedListener.onTempChanged(mScreenTemp);
                    mOnChangedListener.onResultChanged(mScreenResult);
                    mOnChangedListener.onCharChanged(mScreenChar);
                    mOnChangedListener.onListDataChanged(mDataChar.get(mDataChar.size() - 1), mDataNum.get(mDataNum.size() - 1));
                } else if (isZero(mScreenTemp) && mDataNum.size() != 0) {
                    mScreenChar = ch;
                    mOnChangedListener.onCharChanged(mScreenChar);
                }
                break;
            case "=":
                if (TextUtils.isEmpty(mScreenChar))
                    break;
                if (!TextUtils.isEmpty(mScreenTemp)) {
                    mScreenTemp = trimDot(mScreenTemp);
                    mDataChar.add(mScreenChar);
                    mDataNum.add(mScreenTemp);
                }
                mScreenResult = getResultFromList();
                if (mScreenResult == null) {
                    mScreenResult = "除数不能为0";
                }
                mScreenChar = "";
                mScreenTemp = "";
                mOnChangedListener.onCharChanged(mScreenChar);
                mOnChangedListener.onTempChanged(mScreenTemp);
                mOnChangedListener.onResultChanged(mScreenResult);
                mOnChangedListener.onListDataChanged(mDataChar.get(mDataChar.size() - 1), mDataNum.get(mDataNum.size() - 1));
                break;
            case ".":
                if (isZero(mScreenTemp) || mScreenTemp.contains("."))
                    break;
                mScreenTemp += ch;
                mOnChangedListener.onTempChanged(mScreenTemp);
                break;
        }
    }

    private String getResultFromList() {
        String tempRes = mDataNum.get(0);
        for (int i = 0; i < mDataNum.size() - 1; ++i) {
            tempRes = calculate(tempRes, mDataNum.get(i + 1), mDataChar.get(i));
            if (tempRes == null)
                return null;
        }
        return tempRes;
    }

    private String trimDot(String num) {
        if (num.charAt(num.length() - 1) == '.')
            return num.replace(".", "");
        return num;
    }

    public static String calculate(String a, String b, String ch) {
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
                if (isZero(b)) {
                    return null;
                }
                res = arg1.divide(arg2, 3, BigDecimal.ROUND_HALF_UP);
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
        mScreenChar = "";
        mDataChar.clear();
        mDataNum.clear();
        mOnChangedListener.onResultChanged("");
        mOnChangedListener.onTempChanged("");
        mOnChangedListener.onCharChanged("");
    }
    public void onDelClick() {
        if (TextUtils.isEmpty(mScreenTemp))
            return;
        if (mScreenTemp.length() == 1) {
            mScreenTemp = "";
        } else {
            mScreenTemp = mScreenTemp.substring(0, mScreenTemp.length() - 1);
        }

        mOnChangedListener.onTempChanged(mScreenTemp);
    }
    public boolean isOutLimitLength(String num) {
        return num.length() > 9;
    }
    public static boolean isZero(String num) {
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
    public boolean saveData() {
        if (mDataNum.size() == 0)
            return false;
        SPUtils spUtils = new SPUtils(TimeUtils.date2String(TimeUtils.getNowTimeDate(), "yyyy-MM-dd"));
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <mDataNum.size(); ++i) {
            if (i == 0) {
                buffer.append(mDataNum.get(i));
            } else {
                buffer.append(mDataChar.get(i - 1) + mDataNum.get(i));
            }
        }
        String dataList = spUtils.getString("data_list");
        if (dataList == null) {
            dataList = "";
        } else {
            dataList += "@";
        }
        spUtils.putString("data_list", dataList + buffer.toString());

        SPUtils database = new SPUtils("DateList");
        String dateList = database.getString("date_list");
        if (dateList == null) {
            dateList = "";
        } else {
            String[] date = dateList.split("@");
            for (int i = 0; i < date.length; ++i) {
                if (date[i].equals(TimeUtils.date2String(TimeUtils.getNowTimeDate(), "yyyy-MM-dd"))) {
                    return true;
                }
            }
            dateList += "@";
        }
        database.putString("date_list",  dateList + TimeUtils.date2String(TimeUtils.getNowTimeDate(), "yyyy-MM-dd"));
        return true;
    }

    public List<String> getDataChar() {
        return mDataChar;
    }

    public List<String> getDataNum() {
        return mDataNum;
    }

    public void removeItem(int pos) {
        mDataNum.remove(pos);
        if (pos > 0)
            mDataChar.remove(pos);
    }

    public interface OnDataChangedListener {
        void onResultChanged(String res);
        void onTempChanged(String temp);
        void onCharChanged(String ch);
        void onListDataChanged(String ch, String num);
    }
}
