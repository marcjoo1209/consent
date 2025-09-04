package com.ctp.consent.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
    
    // 날짜 포맷
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATETIME_WITH_MS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter DATE_KOREAN_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    private static final DateTimeFormatter DATETIME_KOREAN_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
    private static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    // 현재 시간 (한국 시간대)
    public static LocalDateTime nowKorea() {
        return LocalDateTime.now(KOREA_ZONE_ID);
    }
    
    public static LocalDate todayKorea() {
        return LocalDate.now(KOREA_ZONE_ID);
    }
    
    // 날짜/시간 포맷팅
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    public static String formatDateTimeWithMs(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_WITH_MS_FORMATTER);
    }
    
    public static String formatDateKorean(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_KOREAN_FORMATTER);
    }
    
    public static String formatDateTimeKorean(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_KOREAN_FORMATTER);
    }
    
    public static String formatCompactDate(LocalDate date) {
        if (date == null) return "";
        return date.format(COMPACT_DATE_FORMATTER);
    }
    
    public static String formatCompactDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(COMPACT_DATETIME_FORMATTER);
    }
    
    // 문자열 -> 날짜/시간 변환
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static LocalDate parseCompactDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateString, COMPACT_DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    // Date <-> LocalDateTime 변환
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    // 날짜 계산
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }
    
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }
    
    // 날짜 비교
    public static boolean isToday(LocalDate date) {
        if (date == null) return false;
        return date.equals(todayKorea());
    }
    
    public static boolean isPast(LocalDate date) {
        if (date == null) return false;
        return date.isBefore(todayKorea());
    }
    
    public static boolean isFuture(LocalDate date) {
        if (date == null) return false;
        return date.isAfter(todayKorea());
    }
    
    public static boolean isWithinDays(LocalDate date, int days) {
        if (date == null) return false;
        LocalDate today = todayKorea();
        return !date.isBefore(today) && !date.isAfter(today.plusDays(days));
    }
    
    // 시간 경과 표시 ("방금 전", "3시간 전", "2일 전" 등)
    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        
        LocalDateTime now = nowKorea();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + "시간 전";
        
        long days = hours / 24;
        if (days < 7) return days + "일 전";
        if (days < 30) return (days / 7) + "주 전";
        if (days < 365) return (days / 30) + "개월 전";
        
        return (days / 365) + "년 전";
    }
    
    // 영업일 계산 (주말 제외)
    public static LocalDate addBusinessDays(LocalDate date, int businessDays) {
        if (date == null) return null;
        
        LocalDate result = date;
        int addedDays = 0;
        
        while (addedDays < businessDays) {
            result = result.plusDays(1);
            if (result.getDayOfWeek() != DayOfWeek.SATURDAY && 
                result.getDayOfWeek() != DayOfWeek.SUNDAY) {
                addedDays++;
            }
        }
        
        return result;
    }
    
    public static boolean isWeekend(LocalDate date) {
        if (date == null) return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
    
    // 월의 첫날/마지막 날
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.withDayOfMonth(1);
    }
    
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.withDayOfMonth(date.lengthOfMonth());
    }
    
    // 나이 계산
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, todayKorea()).getYears();
    }
}