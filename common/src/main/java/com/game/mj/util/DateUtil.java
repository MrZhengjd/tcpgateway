package com.game.mj.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final long ONEDAY_MILLISECOND=86400000;//一天毫秒数
    /**
     * 当天的开始时间
     *
     * @return
     */
    public static long startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date.getTime();
    }
}
