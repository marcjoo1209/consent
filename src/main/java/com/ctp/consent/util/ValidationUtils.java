package com.ctp.consent.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // 정규식 패턴
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(01[016789])-?([0-9]{3,4})-?([0-9]{4})$");
    private static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^(010)-?([0-9]{4})-?([0-9]{4})$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern BIRTHDATE_PATTERN = Pattern.compile("^(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern NAME_KOREAN_PATTERN = Pattern.compile("^[가-힯]{2,10}$");
    private static final Pattern NAME_ENGLISH_PATTERN = Pattern.compile("^[A-Za-z ]{2,30}$");
    private static final Pattern BUSINESS_NUMBER_PATTERN = Pattern.compile("^([0-9]{3})-?([0-9]{2})-?([0-9]{5})$");

    // 전화번호 검증
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        phoneNumber = phoneNumber.replaceAll("\\s", "");
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isValidMobilePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        phoneNumber = phoneNumber.replaceAll("\\s", "");
        return MOBILE_PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "";
        }

        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        if (phoneNumber.length() == 10) {
            // 010-1234-5678
            if (phoneNumber.startsWith("02")) {
                return phoneNumber.substring(0, 2) + "-" +
                        phoneNumber.substring(2, 6) + "-" +
                        phoneNumber.substring(6);
            } else {
                return phoneNumber.substring(0, 3) + "-" +
                        phoneNumber.substring(3, 6) + "-" +
                        phoneNumber.substring(6);
            }
        } else if (phoneNumber.length() == 11) {
            // 010-1234-5678
            return phoneNumber.substring(0, 3) + "-" +
                    phoneNumber.substring(3, 7) + "-" +
                    phoneNumber.substring(7);
        } else if (phoneNumber.length() == 9 && phoneNumber.startsWith("02")) {
            // 02-123-4567
            return phoneNumber.substring(0, 2) + "-" +
                    phoneNumber.substring(2, 5) + "-" +
                    phoneNumber.substring(5);
        }

        return phoneNumber;
    }

    // 이메일 검증
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // 생년월일 검증 (YYYYMMDD 형식)
    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            return false;
        }

        birthdate = birthdate.replaceAll("[^0-9]", "");

        if (!BIRTHDATE_PATTERN.matcher(birthdate).matches()) {
            return false;
        }

        // 날짜 유효성 검사
        int year = Integer.parseInt(birthdate.substring(0, 4));
        int month = Integer.parseInt(birthdate.substring(4, 6));
        int day = Integer.parseInt(birthdate.substring(6, 8));

        if (month < 1 || month > 12) {
            return false;
        }

        int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // 윤년 처리
        if (isLeapYear(year) && month == 2) {
            daysInMonth[1] = 29;
        }

        return day >= 1 && day <= daysInMonth[month - 1];
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static String formatBirthdate(String birthdate) {
        if (birthdate == null || birthdate.trim().isEmpty()) {
            return "";
        }

        birthdate = birthdate.replaceAll("[^0-9]", "");

        if (birthdate.length() != 8) {
            return birthdate;
        }

        return birthdate.substring(0, 4) + "-" +
                birthdate.substring(4, 6) + "-" +
                birthdate.substring(6, 8);
    }

    // 이름 검증
    public static boolean isValidKoreanName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_KOREAN_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidEnglishName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_ENGLISH_PATTERN.matcher(name.trim()).matches();
    }

    // 사업자등록번호 검증
    public static boolean isValidBusinessNumber(String businessNumber) {
        if (businessNumber == null || businessNumber.trim().isEmpty()) {
            return false;
        }

        businessNumber = businessNumber.replaceAll("[^0-9]", "");

        if (!BUSINESS_NUMBER_PATTERN.matcher(businessNumber).matches()) {
            return false;
        }

        // 공식 알고리즘에 따른 검증
        if (businessNumber.length() != 10) {
            return false;
        }

        int[] weights = { 1, 3, 7, 1, 3, 7, 1, 3, 5 };
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(businessNumber.charAt(i)) * weights[i];
        }

        sum += (Character.getNumericValue(businessNumber.charAt(8)) * 5) / 10;
        int checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit == Character.getNumericValue(businessNumber.charAt(9));
    }

    public static String formatBusinessNumber(String businessNumber) {
        if (businessNumber == null || businessNumber.trim().isEmpty()) {
            return "";
        }

        businessNumber = businessNumber.replaceAll("[^0-9]", "");

        if (businessNumber.length() != 10) {
            return businessNumber;
        }

        return businessNumber.substring(0, 3) + "-" +
                businessNumber.substring(3, 5) + "-" +
                businessNumber.substring(5);
    }

    // 문자열 유효성 검사
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean hasMinLength(String str, int minLength) {
        if (str == null) {
            return false;
        }
        return str.trim().length() >= minLength;
    }

    public static boolean hasMaxLength(String str, int maxLength) {
        if (str == null) {
            return true;
        }
        return str.trim().length() <= maxLength;
    }

    public static boolean isInRange(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }

    // 숫자 검증
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.trim().matches("^[0-9]+$");
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.trim().matches("^[A-Za-z0-9]+$");
    }

    // HTML/Script 태그 제거 (기본 XSS 방어)
    public static String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("<[^>]*>", "")
                .replaceAll("[<>]", "")
                .trim();
    }

    // SQL Injection 방어를 위한 기본 검증
    public static boolean containsSqlKeywords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String upperInput = input.toUpperCase();
        String[] sqlKeywords = {
                "SELECT", "INSERT", "UPDATE", "DELETE", "DROP",
                "CREATE", "ALTER", "EXEC", "EXECUTE", "UNION",
                "--", "/*", "*/", ";"
        };

        for (String keyword : sqlKeywords) {
            if (upperInput.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}