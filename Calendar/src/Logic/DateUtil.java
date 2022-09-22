package Logic;/*
 * This class provides some utils that may help you to finish this lab.
 * getToday() is finished, you can use this method to get the current date.
 * The other four methods getDaysInMonth(), isValid(), isFormatted() and isLeapYear() are not finished,
 * you should implement them before you use.
 *
 * */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exception.IsFormatException;

public class DateUtil {
    /**
     * get a Logic.CalendarDate instance point to today
     *
     * @return a Logic.CalendarDate object
     */
    public static CalendarDate getToday() {
        Calendar calendar = Calendar.getInstance();
        return new CalendarDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * get all dates in the same month with given date
     *
     * @param date the given date
     * @return a list of days in a whole month
     */
    public static List<CalendarDate> getDaysInMonth(CalendarDate date) {

        if (!DateUtil.isValid(date)) {
            return null;
        }

        int year = date.getYear();
        int month = date.getMonth();
        int days;//一个月里的总天数
        boolean isLeapYear = isLeapYear(year);
        List<CalendarDate> calendarDateList = new ArrayList<>();

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            days = 31;//大月
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            days = 30;//小月
        } else {
            //二月特殊情况
            if (isLeapYear)
                days = 29;
            else days = 28;
        }

        for (int i = 1; i <= days; i++) {
            calendarDateList.add(new CalendarDate(year, month, i));
        }

        return calendarDateList;
    }

    /**
     * Judge whether the input date is valid. For example, 2018-2-31 is not valid
     *
     * @param date the input date
     * @return true if the date is valid, false if the date is not valid.
     */
    public static boolean isValid(CalendarDate date) {
        if (date == null) {
            return false;
        }
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        if (month < 1 || month > 12) return false;
        int[] monthLengths = new int[]{31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};//代表一年中各个月份（0-11）的天数，二月还要经过判断

        //二月的处理
        if (isLeapYear(year))
            monthLengths[1] = 29;
        else monthLengths[1] = 28;

        int monthLength = monthLengths[month - 1];
        return day >= 1 && day <= monthLength;
    }

    /**
     * Judge whether the input is formatted.
     * For example, 2018/2/1 is not valid and 2018-2-1 is valid.
     *
     * @param dateString
     * @return true if the input is formatted, false if the input is not formatted.
     */
    public static boolean isFormatted(String dateString) {

        if (dateString == null) {
            return false;
        }
        Pattern p = Pattern.compile("\\d{1,4}+[-]\\d{1,2}+[-]\\d{1,2}+");//判断输入格式
        Matcher m = p.matcher(dateString);
        return m.matches();

    }

    /**
     * Judge whether the input year is a leap year or not.
     * For example, year 2000 is a leap year, and 1900 is not.
     *
     * @param year
     * @return true if the input year is a leap year, false if the input is not.
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;//闰年判断
    }

    /**
     * @param date
     * @return 0, if the date is ordinary;
     * 1, if the date is a special day, but need to work, e.g. 3-8;
     * 2, if the date is a special day, and not need to work, e.g. 10-1;
     * 3, if the date is among the holiday, e.g. 10-2;
     * 4, if the date is Saturday or Sunday, but need to work;
     */
    public static int isHoliday(CalendarDate date) {
        String jsonStr = "";
        try {
            Scanner jsonScanner = new Scanner(new File("src/Logic/holiday.json"));
            while (jsonScanner.hasNext()) {
                jsonStr += jsonScanner.nextLine();
            }
            jsonScanner.close();
            JSONObject jsonObject = new JSONObject(jsonStr);

            int year = date.getYear();
            if (year != Integer.parseInt(jsonObject.getString("year"))) {
                return 0;
            }

            JSONArray workdayArray = jsonObject.getJSONArray("workday");
            for (int i = 0; i < workdayArray.length(); i++) {
                CalendarDate workdayDate = new CalendarDate(workdayArray.getString(i));
                if (date.equals(workdayDate)) {
                    return 4;
                }
            }

            JSONArray holidayArray = jsonObject.getJSONArray("holiday");
            for (int i = 0; i < holidayArray.length(); i++) {
                JSONObject holidayDetail = holidayArray.getJSONObject(i);
                CalendarDate holidayDate = new CalendarDate(holidayDetail.getString("holiday_time"));
                if (date.equals(holidayDate)) {
                    return 2;
                } else {
                    CalendarDate holidayStartDate = new CalendarDate(holidayDetail.getString("start_time"));
                    CalendarDate holidayEndDate = new CalendarDate(holidayDetail.getString("end_time"));
                    if (date.compareTo(holidayStartDate) >= 0 &&
                            date.compareTo(holidayEndDate) <= 0) {
                        return 3;
                    }
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        } catch (IsFormatException e3) {
            e3.printStackTrace();
        }
        return 0;
    }

    public static String getHolidayName(CalendarDate date) {
        int result = isHoliday(date);
        if (result == 1 || result == 2) {
            String jsonStr = "";
            try {
                Scanner jsonScanner = new Scanner(new File("src/Logic/holiday.json"));
                while (jsonScanner.hasNext()) {
                    jsonStr += jsonScanner.nextLine();
                }
                jsonScanner.close();
                JSONObject jsonObject = new JSONObject(jsonStr);

                JSONArray holidayArray = jsonObject.getJSONArray("holiday");
                for (int i = 0; i < holidayArray.length(); i++) {
                    JSONObject holidayDetail = holidayArray.getJSONObject(i);
                    CalendarDate holidayDate = new CalendarDate(holidayDetail.getString("holiday_time"));
                    if (date.equals(holidayDate)) {
                        return holidayDetail.getString("zh_name");
                    }
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            } catch (IsFormatException e3) {
                e3.printStackTrace();
            }
        }

        return null;
    }
}

