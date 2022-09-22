package Logic;

import Exception.IsFormatException;

/**
 * We have finished part of this class yet, you should finish the rest.
 * 1. A constructor that can return a Logic.CalendarDate object through the given string.
 * 2. A method named getDayOfWeek() that can get the index of a day in a week.
 */
public class CalendarDate {
    private int year;
    private int month;
    private int day;

    public CalendarDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * a constructor that can return a Logic.CalendarDate object through the given string.
     *
     * @param dateString format: 2018-3-18
     */
    public CalendarDate(String dateString) throws IsFormatException {

        if (DateUtil.isFormatted(dateString)) {

            String[] date = dateString.split("-");
            this.year = Integer.parseInt(date[0]);
            this.month = Integer.parseInt(date[1]);
            this.day = Integer.parseInt(date[2]);

        } else {

            throw new IsFormatException("输入格式不符合要求，标准格式为YYYY-MM-DD！");

        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Get index of the day in a week for one date.
     * <p>
     * Don't use the existing implement like Calendar.setTime(),
     * try to implement your own algorithm.
     *
     * @return 1-7, 1 stands for Monday and 7 stands for Sunday
     */
    public int getDayOfWeek() {
        if (!DateUtil.isValid(this)) {
            return -1;
        }

        int month = this.month;
        int year = this.year;
        int day = this.day;

        if (month == 1 || month == 2) {
            month += 12;
            year--;
        }
        int week = (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400 + 1) % 7;//计算星期几的公式
        if (week == 0) {
            week = 7;
        }
        if (week >= 1 && week <= 7)
            return week;
        else return -1;
    }

    @Override
    public boolean equals(Object obj) {
        int year = ((CalendarDate) obj).getYear();
        int month = ((CalendarDate) obj).getMonth();
        int day = ((CalendarDate) obj).getDay();
        return this.getYear() == year && this.getMonth() == month && this.getDay() == day;
    }

    public int compareTo(CalendarDate date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();

        if (this.year < year) {
            return -1;
        } else if (this.year > year) {
            return 1;
        } else {
            if (this.month < month) {
                return -1;
            } else if (this.month > month) {
                return 1;
            } else {
                return this.day - day;
            }
        }
    }
}
