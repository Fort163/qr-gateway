package com.quick.recording.gateway.enumerated;

public enum DayOfWeek {

    monday("Понедельник"),
    tuesday("Вторник"),
    wednesday("Среда"),
    thursday("Четверг"),
    friday("Пятница"),
    saturday("Суббота"),
    sunday("Воскресенье");

    private String value;

    DayOfWeek(String value) {
        this.value = value;
    }

}
