package com.alianza.test.cm.constant;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ClientConstantTest {

    // default values
    public final static String DEFAULT_NAME = "Daniel Guancha";
    public final static String DEFAULT_SHARED_KEY = "dguancha";
    public final static String DEFAULT_EMAIL = "dguancha@gmail.com";
    public final static String DEFAULT_PHONE = "3113852414";
    public final static ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.of(2023, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));
    public final static ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));


    // update values
    public final static String UPDATE_NAME = "Jhon Doe";
    public final static String UPDATE_SHARED_KEY = "jdoe";
    public final static String UPDATE_EMAIL = "jdoe@gmail.com";
    public final static String UPDATE_PHONE = "3004784125";
    public final static ZonedDateTime UPDATE_START_DATE = ZonedDateTime.of(2022, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));
    public final static ZonedDateTime UPDATE_END_DATE = ZonedDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));
}
