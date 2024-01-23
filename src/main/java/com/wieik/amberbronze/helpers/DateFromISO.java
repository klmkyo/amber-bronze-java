package com.wieik.amberbronze.helpers;

import java.time.Instant;
import java.util.Date;

public class DateFromISO {
    public static Date fromISO(String iso) {
        return Date.from(Instant.parse(iso));
    }
}
