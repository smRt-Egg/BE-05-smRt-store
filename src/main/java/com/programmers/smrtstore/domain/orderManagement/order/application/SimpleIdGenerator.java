package com.programmers.smrtstore.domain.orderManagement.order.application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleIdGenerator {
    private static final AtomicInteger count = new AtomicInteger(0);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private SimpleIdGenerator() {
    }

    public static synchronized String generateId() {
        Date now = new Date();
        String datePart = dateFormat.format(now);

        // 매일 카운터를 초기화합니다.
        if (!datePart.equals(dateFormat.format(countLastUpdated))) {
            count.set(0);
            countLastUpdated = now;
        }

        int currentCount = count.incrementAndGet();
        return datePart + "-" + String.format("%04d", currentCount);
    }

    private static Date countLastUpdated = new Date();
}

