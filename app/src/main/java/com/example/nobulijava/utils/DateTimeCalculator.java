package com.example.nobulijava.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

//Java class to do dateTime conversion
//Source dateTime is a String the format of "yyyy/MM/dd HH:mm:ss"
public class DateTimeCalculator {
    public static LocalDate getDate(String dateTimeString) {
        String dateString = dateTimeString.substring(0, 10);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
        return localDate;
    }

    public static LocalTime getTime(String dateTimeString) {
        String timeString = dateTimeString.substring(11, 19);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(timeString, timeFormatter);
        return localTime;
    }

    public static long yearsAgo(String startDateTimeString) {
        LocalDate startDate = getDate(startDateTimeString);
        LocalDate endDate = LocalDate.now();

        long yearsAgo = ChronoUnit.YEARS.between(
                Year.from(startDate),
                Year.from(endDate)
        );

        return yearsAgo;
    }

    public static long monthsAgo(String startDateTimeString) {
        LocalDate startDate = getDate(startDateTimeString);
        LocalDate endDate = LocalDate.now();

        long monthsAgo = ChronoUnit.MONTHS.between(
                YearMonth.from(startDate),
                YearMonth.from(endDate)
        );

        return monthsAgo;
    }

    public static long daysAgo(String startDateTimeString) {
        LocalDate startDate = getDate(startDateTimeString);
        LocalDate endDate = LocalDate.now();

        long daysAgo = ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        return daysAgo;
    }

    public static long hoursAgo(String startDateTimeString) {
        LocalTime startTime = getTime(startDateTimeString);
        LocalTime endTime = LocalTime.now();
        long hoursAgo = ChronoUnit.HOURS.between(
                startTime,
                endTime

        );

        return hoursAgo;
    }

    public static long minutesAgo(String startDateTimeString) {
        LocalTime startTime = getTime(startDateTimeString);
        LocalTime endTime = LocalTime.now();
        long minutesAgo = ChronoUnit.MINUTES.between(
                startTime,
                endTime

        );

        return minutesAgo;
    }

    public static long secondsAgo(String startDateTimeString) {
        LocalTime startTime = getTime(startDateTimeString);
        LocalTime endTime = LocalTime.now();
        long secondsAgo = ChronoUnit.SECONDS.between(
                startTime,
                endTime

        );

        return secondsAgo;
    }

    //Method to round of the time ago to the nearest single unit of time.
    public static String[] simpleDuration(String startDateTimeString) {
        long yearsAgo = DateTimeCalculator.yearsAgo(startDateTimeString);
        long monthsAgo = DateTimeCalculator.monthsAgo(startDateTimeString);
        long daysAgo = DateTimeCalculator.daysAgo(startDateTimeString);
        long hoursAgo = DateTimeCalculator.hoursAgo(startDateTimeString);
        long minutesAgo = DateTimeCalculator.minutesAgo(startDateTimeString);
        long secondsAgo = DateTimeCalculator.secondsAgo(startDateTimeString);

        String[] durationDetails = {"-", "-"};

        if (yearsAgo >= 1) {
            durationDetails[0] = Long.toString(yearsAgo);
            durationDetails[1] = "Year(s)";

        } else if (monthsAgo >= 1) {
            durationDetails[0] = Long.toString(monthsAgo);
            durationDetails[1] = "Month(s)";

        } else if (daysAgo >= 1) {
            durationDetails[0] = Long.toString(daysAgo);
            durationDetails[1] = "Day(s)";

        } else if (hoursAgo >= 1) {
            durationDetails[0] = Long.toString(hoursAgo);
            durationDetails[1] = "Hour(s)";

        } else if (minutesAgo >= 1) {
            durationDetails[0] = Long.toString(minutesAgo);
            durationDetails[1] = "Minute(s)";

        } else if (secondsAgo >= 1) {
            durationDetails[0] = Long.toString(secondsAgo);
            durationDetails[1] = "Second(s)";

        }
        return durationDetails;
    }
}
