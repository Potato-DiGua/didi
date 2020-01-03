package com.example.didi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * 检查昵称是否为2-16个字符
     *
     * @param nickName
     * @return
     */
    public static boolean isNickNameValid(String nickName) {
        int len = nickName.length();
        if (len > 2 && len < 16) {
            Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\\\\\.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]");
            Matcher matcher = pattern.matcher(nickName);
            return !matcher.find();
        }
        return false;
    }

    /**
     * 检查密码是否为8-16位并且包含数字和字母
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {

        if (password == null)
            return false;
        int length = password.length();

        if (length >= 8 && length <= 16) {
            boolean hasNum = false, hasChar = false;
            for (int i = 0; i < length; i++) {
                char ch = password.charAt(i);
                if (Character.isLetter(ch)) {
                    hasChar = true;
                } else if (Character.isDigit(ch)) {
                    hasNum = true;
                }
                if (hasChar && hasNum)
                    return true;
            }
        }
        return false;
    }

    public static boolean isAccountValid(String account) {
        if (account == null || account.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0,5-9]))\\d{8}$");
        return pattern.matcher(account).matches();
    }

    public static String getJsonFromStream(InputStream is) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null)
                    br.close();
                if(is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";

    }
}
