/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit.ui;

import java.util.regex.Matcher;

public class Time {
    private int hours;
    private int minutes;

    public Time() {
        this(0, 0);
    }
    public Time(int hours, int minutes) {
        setHours(hours);
        setMinutes(minutes);
    }
    public Time(Time time) {
        setHours(time.getHours());
        setMinutes(time.getMinutes());
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
    
    public void addHours(int hours) {
        this.hours += hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.hours += minutes / 60;
        this.minutes = minutes % 60;
    }

    public void addMinutes(int minutes) {
        this.minutes += minutes;
        this.hours += this.minutes / 60;
        this.minutes %= 60;
        if (this.minutes < 0) {
            this.minutes += 60;
            hours--;
        }
    }

    public void addTime(Time time) {
        addHours(time.getHours());
        addMinutes(time.getMinutes());
    }

    public void subtractTime(Time time) {
        addHours(-time.getHours());
        addMinutes(-time.getMinutes());
    }

    public boolean isLongerThan(Time other) {
        if (this.hours > other.hours) return true;
        if (this.hours < other.hours) return false;
        return this.minutes > other.minutes;
    }

    public static Time parseTime(String string) {
        if (string == null) return new Time(0, 0);
        Matcher matcher = DialogHelper.TIME_PATTERN.matcher(string);
        if (!matcher.matches()) return new Time();
        return new Time(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }
}
