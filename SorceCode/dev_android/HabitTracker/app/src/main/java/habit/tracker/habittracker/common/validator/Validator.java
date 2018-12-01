package habit.tracker.habittracker.common.validator;

import android.text.TextUtils;

import java.util.Date;
import java.util.regex.Pattern;

import habit.tracker.habittracker.common.util.AppGenerator;

public class Validator {
    private ErrorMsg mErrorMsgListener;

    public void setErrorMsgListener(ErrorMsg errorMsgListener) {
        mErrorMsgListener = errorMsgListener;
    }

    public boolean checkEmpty(String key, String value) {
        if (value != null) {
            value = value.trim();
        }
        if (TextUtils.isEmpty(value)) {
            mErrorMsgListener.showError(ValidatorType.EMPTY, key);
            return false;
        }
        return true;
    }

    public boolean checkDate(String date, String format, String key) {
        Date time = AppGenerator.getDate(date, format);
        if (time == null) {
            mErrorMsgListener.showError(ValidatorType.DATE, key);
            return false;
        }
        return true;
    }

    public boolean checkEmail(String email) {
        final String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(email).matches()){
            mErrorMsgListener.showError(ValidatorType.EMAIL, "email");
            return false;
        }
        return true;
    }

    public boolean checkEqual(String str, String str1, String key) {
        if (!str.equals(str1)) {
            mErrorMsgListener.showError(ValidatorType.EQUAL, key);
            return false;
        }
        return true;
    }

    public boolean checkDiff(String str, String str1, String key) {
        if (str.equals(str1)) {
            mErrorMsgListener.showError(ValidatorType.DIFF, key);
            return false;
        }
        return true;
    }

    public boolean checkLength(String str, int length, String key) {
        if (str.length() < length) {
            mErrorMsgListener.showError(ValidatorType.LENGTH, key);
            return false;
        }
        return true;
    }

    public boolean checkPhone(String phone) {
        final String regex = "^[0][0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        if(phone == null || phone.length() < 10
                || phone.length() > 15
                || !pattern.matcher(phone).matches()){
            mErrorMsgListener.showError(ValidatorType.PHONE, "Số điện thoại");
            return false;
        }
        return true;
    }

    public boolean checkNumber(String number, int thre) {
        final String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        if (number == null || !pattern.matcher(number).matches()){
            return false;
        }
        int num = Integer.parseInt(number);
        if (num < thre) {
            return false;
        }
        return true;
    }

    public interface ErrorMsg {
        void showError(ValidatorType type, String key);
    }
}
