package com.programmers.smrtstore.util;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month 값은 1 - 12 사이만 가능합니다.");
        }
    }

    public static void validateYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("year 값은 0 초과만 가능합니다.");
        }
    }

    /**
     * 해당 월의 시작 시간과 종료 시간을 LocalDateTime 배열로 반환한다.
     * @param month 월
     * @param year 년
     * @return LocalDateTime 배열 (0: 시작 시간, 1: 종료 시간)
     */
    public static LocalDateTime[] getMonthBoundaries(int month, int year) {
        validateMonth(month);
        validateYear(year);

        YearMonth yearMonth = YearMonth.of(year, month);
        // 해당 월의 첫날 00:00 (시작 시간)
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        // 해당 월의 마지막날 23:59:59 (종료 시간)
        LocalDateTime endDateTime = yearMonth.atEndOfMonth()
            .atTime(23, 59, 59);

        return new LocalDateTime[]{startDateTime, endDateTime};
    }



}
