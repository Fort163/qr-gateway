package com.quick.recording.gateway.util;

import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

import static com.quick.recording.gateway.Constant.PART_TIME_MINUTES;

public class LocalTimeUtil {

    public static byte getPartTime(LocalTime time){
        Assert.notNull(time, "LocalTime cannot be null");
        return (byte) Math.round(
                Math.ceil((float) time.getLong(ChronoField.MINUTE_OF_DAY) / (float) PART_TIME_MINUTES)
        );
    }

}
