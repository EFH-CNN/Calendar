package Logic;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exception.IsFormatException;
import Logic.instance.Anniversary;
import Logic.instance.Course;
import Logic.instance.Other;

public abstract class Item {
    private Priority priority;
    private Status status;
    private Item parentItem;
    private int minutes;
    private String remindType = "不用提醒";
    private int remindAgain;
    private int earliestReminder;
    private List<Item> subItems = new ArrayList<>();

    public Item(String priority, Item parentItem) {
        this.priority = Priority.parseStringToPriority(priority);
        this.parentItem = parentItem;
    }

    public boolean addSubItem(Item item) {
        if (item == null) {
            return false;
        }
        if ((this instanceof Other && !(((Other) this).getHasTime())) || (item instanceof Other && !(((Other) item).getHasTime()))) {
            return false;
        }
        if (this instanceof Anniversary) {
            if (item instanceof Anniversary) {
                if (((Anniversary) this).getStartDate().compareTo(((Anniversary) item).getStartDate()) == 0
                        || ((Anniversary) this).getStartDate().compareTo(((Anniversary) item).getStartDate()) < 0) {
                    this.subItems.add(item);
                    item.setParentItem(this);
                    return true;
                }
                return false;
            } else if (item instanceof Course) {
                if (((Course) item).getDuration() == 1 && ((Anniversary) this).getStartDate().equals(((Course) item).getDateList().get(0))) {
                    this.subItems.add(item);
                    item.setParentItem(this);
                    return true;
                }
                return false;
            } else {
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.setTime(item.getStartTime());
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTime(item.getEndTime());
                if (((Anniversary) this).getStartDate().getYear() <= calendarStart.get(Calendar.YEAR)
                        && calendarStart.get(Calendar.YEAR) == calendarEnd.get(Calendar.YEAR)
                        && ((Anniversary) this).getStartDate().getMonth() == calendarStart.get(Calendar.MONTH) + 1
                        && ((Anniversary) this).getStartDate().getMonth() == calendarEnd.get(Calendar.MONTH) + 1
                        && ((Anniversary) this).getStartDate().getDay() == calendarStart.get(Calendar.DAY_OF_MONTH)
                        && ((Anniversary) this).getStartDate().getDay() == calendarEnd.get(Calendar.DAY_OF_MONTH)) {
                    this.subItems.add(item);
                    item.setParentItem(this);
                    return true;
                }
                return false;
            }
        }
        if (item instanceof Anniversary) {
            if (this instanceof Anniversary && ((Anniversary) this).getStartDate().compareTo(((Anniversary) item).getStartDate()) <= 0) {
                this.subItems.add(item);
                item.setParentItem(this);
                return true;
            }
            return false;
        }
        if (this instanceof Course) {
            if (item instanceof Course) {
                if (((Course) this).getDateList().get(((Course) this).getDateList().size() - 1).compareTo(((Course) item).getDateList().get(((Course) item).getDateList().size() - 1)) >= 0
                        && ((Course) this).getDateList().get(0).compareTo(((Course) item).getDateList().get(0)) <= 0
                        && ((Course) this).getWeek() == ((Course) item).getWeek()) {
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date parentStart = setTime("1900-5-2 " + df.format(this.getStartTime()));
                    Date parentEnd = setTime("1900-5-2 " + df.format(this.getEndTime()));
                    Date start = setTime("1900-5-2 " + df.format(item.getStartTime()));
                    Date end = setTime("1900-5-2 " + df.format(item.getEndTime()));
                    if ((parentStart.before(start) || parentStart.toString().equals(start.toString())) && (parentEnd.after(end) || parentEnd.toString().equals(end.toString()))) {
                        this.subItems.add(item);
                        item.setParentItem(this);
                        return true;
                    }
                }
                return false;
            } else {
                for (int i = 0; i < ((Course) this).getDateList().size(); i++) {
                    CalendarDate calendarDate = ((Course) this).getDateList().get(i);
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    Date parentStart = setTime(calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + df.format(this.getStartTime()));
                    Date parentEnd = setTime(calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + df.format(this.getEndTime()));
                    if ((parentStart.before(item.getStartTime()) || parentStart.toString().equals(item.getStartTime().toString()))
                            && (parentEnd.after(item.getEndTime()) || parentEnd.toString().equals(item.getEndTime().toString()))) {
                        this.subItems.add(item);
                        item.setParentItem(this);
                        return true;
                    }
                }
                return false;
            }
        }
        if (item instanceof Course) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date start = setTime(((Course) item).getDateList().get(0).getYear() + "-" + ((Course) item).getDateList().get(0).getMonth() + "-"
                    + ((Course) item).getDateList().get(0).getDay() + " " + df.format(item.getStartTime()));
            Date end = setTime(((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getYear() + "-" + ((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getMonth()
                    + "-" + ((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getDay() + " " + df.format(item.getEndTime()));
            if ((this.getStartTime().before(start) || this.getStartTime().toString().equals(start.toString()))
                    && (this.getEndTime().after(end) || this.getEndTime().toString().equals(end.toString()))) {
                this.subItems.add(item);
                item.setParentItem(this);
                return true;
            }
            return false;
        }
        if ((item.getStartTime().after(this.getStartTime()) || item.getStartTime().toString().equals(this.getStartTime().toString()))
                && (this.getEndTime().after(item.getEndTime()) || this.getEndTime().toString().equals(item.getEndTime().toString()))) {
            if (this.parentItem == null && item.subItems.size() == 0) {
                this.subItems.add(item);
                item.setParentItem(this);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean deleteSubItem(Item item) {
        if (subItems.size() == 0) {
            return false;
        }

        int index = subItemPosition(item);
        if (index != -1) {
            subItems.remove(index);
            return true;
        } else {
            return false;
        }
    }

    public int subItemPosition(Item item) {
        for (int i = 0; i < subItems.size(); i++) {
            if (subItems.get(i).itemEqual(item)) {
                return i;
            }
        }
        return -1;
    }

    public List<Item> getSubItems() {
        return this.subItems;
    }

    public List<Item> getSubItems(String dateString) {
        List<Item> getSubItemsOfDay = new LinkedList<>();
        Date start = setTime(dateString + " 0:0");
        Date end = setTime(dateString + " 23:59");
        for (int i = 0; i < subItems.size(); i++) {
            if (end.after(subItems.get(i).getStartTime()) && subItems.get(i).getEndTime().after(start)) {
                getSubItemsOfDay.add(subItems.get(i));
            }
        }
        return getSubItemsOfDay;
    }

    public List<Item> getSubItems(String startTime, String endTime) {
        List<Item> getSubItemsOfDay = new LinkedList<>();
        Date start = setTime(startTime);
        Date end = setTime(endTime);
        for (int i = 0; i < subItems.size(); i++) {
            if (end.after(subItems.get(i).getStartTime()) && subItems.get(i).getEndTime().after(start)) {
                getSubItemsOfDay.add(subItems.get(i));
            }
        }
        return getSubItemsOfDay;
    }

    public boolean isValid(String time) {
        if (!isFormatted(time)) {
            return false;
        }
        String[] date = time.split(" ");
        try {
            if (DateUtil.isValid(new CalendarDate(date[0]))) {
                String[] times = date[1].split(":");
                int hour = Integer.parseInt(times[0]);
                int minute = Integer.parseInt(times[1]);
                return hour >= 0 && minute >= 0 && hour < 24 && minute < 60;
            } else {
                return false;
            }
        } catch (IsFormatException e) {
            return false;
        }
    }

    public boolean isFormatted(String time) {
        if (time == null)
            return false;

        Pattern p = Pattern.compile("\\d{1,4}+[-]\\d{1,2}+[-]\\d{1,2}+ +\\d{1,2}+:+\\d{1,2}");//判断输入格式
        Matcher m = p.matcher(time);
        return m.matches();
    }

    public Date setTime(String time) {
        if (isValid(time)) {
            String[] date = time.split(" ");
            String[] dates = date[0].split("-");
            String[] times = date[1].split(":");
            int year = Integer.parseInt(dates[0]);

            if (year < 1800 || year > 2300) {
                return null;
            }

            int month = Integer.parseInt(dates[1]);
            int day = Integer.parseInt(dates[2]);
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day, hour, minute, 0);
            return calendar.getTime();
        } else {
            return null;
        }
    }

    public Status getStatus() {
        return status;
    }

    public boolean setStatus(String status) {
        if (status.equals("已完成")) {
            if (!(Status.parseStatusToString(this.getStatus()).equals("进行中"))) {
                return false;
            }
            if (this instanceof Anniversary) {
                return false;
            }
            if (this instanceof Other && !(((Other) this).getHasTime())) {
                Date now = new Date();
                ((Other) this).setEndTime(now);
                ((Other) this).setHasTime(true);
                this.status = Item.Status.parseStringToStatus(status);
            }
            if (this.getSubItems().size() != 0) {
                for (int i = 0; i < this.getSubItems().size(); i++) {
                    if (!(Status.parseStatusToString(this.getSubItems().get(i).getStatus()).equals("已完成"))) {
                        return false;
                    }
                }
            }
        } else {
            this.status = Item.Status.parseStringToStatus(status);
        }
        this.status = Item.Status.parseStringToStatus(status);
        return true;
    }

    public abstract String getMessage();

    public abstract boolean itemEqual(Item item);

    public abstract Date getStartTime();

    public abstract Date getEndTime();

    public Priority getPriority() {
        return this.priority;
    }

    public Item getParentItem() {
        return this.parentItem;
    }

    public void setParentItem(Item item) {
        this.parentItem = item;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getRemindAgain() {
        return remindAgain;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setRemindAgain(int remindAgain) {
        this.remindAgain = remindAgain;
    }

    public int getEarliestReminder() {
        return earliestReminder;
    }

    public void setEarliestReminder(int earliestReminder) {
        this.earliestReminder = earliestReminder;
    }

    public abstract String[] getUncontradictoryTypesItems();

    public abstract String getItemSaveInfo();

    public enum Status {
        NOSTART, UNDERWAY, OVERDUE, FINISHED;

        public static Status parseStringToStatus(String status) {
            if (status.equals("未开始")) {
                return NOSTART;
            } else if (status.equals("进行中")) {
                return UNDERWAY;
            } else if (status.equals("过期")) {
                return OVERDUE;
            } else {
                return FINISHED;
            }
        }

        public static String parseStatusToString(Status status) {
            if (status.equals(NOSTART)) {
                return "未开始";
            } else if (status.equals(UNDERWAY)) {
                return "进行中";
            } else if (status.equals(OVERDUE)) {
                return "过期";
            } else {
                return "已完成";
            }
        }
    }

    public enum Priority {
        IMPORTANT_AND_URGENT, UNIMPORTANT_AND_NOTURGENT, UNIMPORTANT_AND_URGENT, IMPORTANT_AND_NOTURGENT;

        public static Priority parseStringToPriority(String priority) {
            if (priority.equals("紧急 & 重要")) {
                return IMPORTANT_AND_URGENT;
            } else if (priority.equals("不紧急 & 不重要")) {
                return UNIMPORTANT_AND_NOTURGENT;
            } else if (priority.equals("紧急 & 不重要")) {
                return UNIMPORTANT_AND_URGENT;
            } else return IMPORTANT_AND_NOTURGENT;
        }

        public static String parsePriorityToString(Priority priority) {
            if (priority.equals(IMPORTANT_AND_URGENT)) {
                return "紧急 & 重要";
            } else if (priority.equals(IMPORTANT_AND_NOTURGENT)) {
                return "不紧急 & 重要";
            } else if (priority.equals(UNIMPORTANT_AND_URGENT)) {
                return "紧急 & 不重要";
            } else return "不紧急 & 不重要";
        }
    }
}


