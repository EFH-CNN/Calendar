package Logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import Logic.instance.*;
import Exception.ItemCreateException;
import Exception.IsFormatException;

public class Schedule {
    private List<Item> items;

    public Schedule() {
        items = new ArrayList<>();
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public int itemPosition(Item item) {
        if (item == null) {
            return -1;
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (item.itemEqual(items.get(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    public String addItem(Item item) {
        if (item == null) {
            return "添加的待办事项不能为空！！";
        } else if (item.getParentItem() != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).itemEqual(item.getParentItem())) {
                    items.add(i + 1, item);
                    break;
                }
            }
            return "";
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item1 = items.get(i);
                if ((item instanceof Appointment || item instanceof Course || item instanceof Interview
                        || item instanceof Meeting || item instanceof Trip)
                        && (item1 instanceof Appointment || item1 instanceof Course || item1 instanceof Interview
                        || item1 instanceof Meeting || item1 instanceof Trip)) {
                    if (item instanceof Course && !(item1 instanceof Course)) {
                        for (int j = 0; j < ((Course) item).getDateList().size(); j++) {
                            CalendarDate calendarDate = ((Course) item).getDateList().get(j);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(item.getStartTime());
                            String startTime = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                            calendar.setTime(item.getEndTime());
                            String endTime = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                            Date start = item.setTime(startTime);
                            Date end = item.setTime(endTime);
                            if ((item1.getEndTime().after(start) || item1.getEndTime().toString().equals(start.toString()))
                                    && (end.after(item1.getStartTime()) || end.toString().equals(item1.getStartTime().toString()))) {
                                return "时间有重复！！";
                            }
                        }
                    } else if (!(item instanceof Course) && item1 instanceof Course) {
                        for (int j = 0; j < ((Course) item1).getDateList().size(); j++) {
                            CalendarDate calendarDate = ((Course) item1).getDateList().get(j);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(item1.getStartTime());
                            String startTime = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                            calendar.setTime(item1.getEndTime());
                            String endTime = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                            Date start = item1.setTime(startTime);
                            Date end = item1.setTime(endTime);
                            if ((item.getEndTime().after(start) || item.getEndTime().toString().equals(start.toString()))
                                    && (end.after(item.getStartTime()) || end.toString().equals(item.getStartTime().toString()))) {
                                return "时间有重复！！";
                            }
                        }
                    } else if (item instanceof Course && item1 instanceof Course) {
                        for (int j = 0; j < ((Course) item).getDateList().size(); j++) {
                            for (int k = 0; k < ((Course) item1).getDateList().size(); k++) {
                                if (((Course) item).getDateList().get(j).equals(((Course) item1).getDateList().get(k))) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(item.getStartTime());
                                    int minute1 = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                                    calendar.setTime(item.getEndTime());
                                    int minute2 = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                                    calendar.setTime(item1.getStartTime());
                                    int minute3 = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                                    calendar.setTime(item1.getEndTime());
                                    int minute4 = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                                    if (minute1 <= minute4 && minute2 >= minute3) {
                                        return "时间有重复！！";
                                    }
                                }
                            }
                        }
                    } else {
                        if ((item.getEndTime().after(item1.getStartTime()) || item.getEndTime().toString().equals(item1.getStartTime().toString()))
                                && (item1.getEndTime().after(item.getStartTime()) || item1.getEndTime().toString().equals(item.getStartTime().toString()))) {
                            return "时间有重复！！";
                        }
                    }
                }
            }
            if (item.getParentItem() == null) {
                items.add(item);
            } else {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).itemEqual(item.getParentItem())) {
                        items.add(i + 1, item);
                        break;
                    }
                }
            }

            return "";
        }
    }

    public boolean deleteItem(Item item) {
        if (item == null) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (item.itemEqual(items.get(i))) {
                if (item.getSubItems().size() != 0) {
                    return false;
                } else {
                    for (int j = 0; j < items.size(); j++) {
                        if (item.getParentItem() != null && item.getParentItem().itemEqual(items.get(j))) {
                            if (items.get(j).deleteSubItem(item)) {
                                items.remove(i);
                                updateStatus();
                                return true;
                            }
                        }
                    }
                    items.remove(i);
                    updateStatus();
                    return true;
                }
            }
        }
        return false;
    }

    public List<Item> getTheListOfItem(String dateString) throws ItemCreateException, IsFormatException {
        List<Item> getTheListOfItem = new ArrayList<>();
        if (dateString == null || !DateUtil.isFormatted(dateString)) {
            return null;
        } else {
            Item item = new Other("创建", "不紧急 & 不重要", null);
            Date start = item.setTime(dateString + " 0:0");
            Date end = item.setTime(dateString + " 23:59");
            Item item1;
            for (int i = 0; i < items.size(); i++) {
                item1 = items.get(i);
                if (item1 instanceof Course) {
                    for (int j = 0; j < ((Course) item1).getDateList().size(); j++) {
                        CalendarDate calendarDate = new CalendarDate(dateString);
                        if (((Course) item1).getDateList().get(j).equals(calendarDate)) {
                            getTheListOfItem.add(item1);
                            break;
                        }
                    }
                } else if (item1 instanceof Anniversary) {
                    CalendarDate calendarDate = new CalendarDate(dateString);
                    if (calendarDate.getMonth() == ((Anniversary) item1).getStartDate().getMonth() && calendarDate.getDay() == ((Anniversary) item1).getStartDate().getDay()) {
                        getTheListOfItem.add(item1);
                    }
                } else {
                    if (end.after(items.get(i).getStartTime()) && items.get(i).getEndTime().after(start)) {
                        getTheListOfItem.add(item1);
                    }
                }
            }
            return getTheListOfItem;
        }
    }

    public List<Item> getTheListOfItem(String startTime, String endTime) throws ItemCreateException {
        List<Item> getTheListOfItem = new ArrayList<>();
        Item item = new Other("创建", "不紧急 & 不重要", null);
        if (item.isValid(startTime) && item.isValid(endTime)) {
            getTheListOfItem = new ArrayList<>();
            Date start = item.setTime(startTime);
            Date end = item.setTime(endTime);
            Item item1;
            if (start.after(end)) {
                return null;
            }
            for (int i = 0; i < items.size(); i++) {
                item1 = items.get(i);
                Date startOfTime, endOfTime;
                CalendarDate calendarDate;
                Calendar calendar = Calendar.getInstance();
                if (item1 instanceof Course) {
                    for (int j = 0; j < ((Course) item1).getDateList().size(); j++) {
                        calendarDate = ((Course) item1).getDateList().get(j);
                        calendar.setTime(item1.getStartTime());
                        String startStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                        startOfTime = item.setTime(startStr);
                        calendar.setTime(item1.getEndTime());
                        String endStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                        endOfTime = item.setTime(endStr);
                        if ((end.after(startOfTime) || end.toString().equals(startOfTime.toString()))
                                && (endOfTime.after(start) || endOfTime.toString().equals(start.toString()))) {
                            getTheListOfItem.add(item1);
                            break;
                        }
                    }
                } else if (item1 instanceof Anniversary) {
                    calendarDate = ((Anniversary) item1).getStartDate();
                    int month = calendarDate.getMonth();
                    int day = calendarDate.getDay();
                    calendar.setTime(start);
                    int startMonth = calendar.get(Calendar.MONTH) + 1;
                    int startDay = calendar.get(Calendar.DAY_OF_MONTH);
                    calendar.setTime(end);
                    int endMonth = calendar.get(Calendar.MONTH) + 1;
                    int endDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if (month > startMonth && month < endMonth) {
                        getTheListOfItem.add(item1);
                    } else if (month == startMonth && month < endMonth) {
                        if (day >= startDay) {
                            getTheListOfItem.add(item1);
                        }
                    } else if (month > startMonth && month == endMonth) {
                        if (day <= endDay) {
                            getTheListOfItem.add(item1);
                        }
                    } else if (month == startMonth && month == endMonth) {
                        if (day >= startDay && day <= endDay) {
                            getTheListOfItem.add(item1);
                        }
                    }


                } else {
                    if ((end.after(item1.getStartTime()) || end.toString().equals(item1.getStartTime().toString()))
                            && (item1.getEndTime().after(start) || item1.getEndTime().toString().equals(start.toString()))) {
                        getTheListOfItem.add(item1);
                    }
                }
            }
        } else {
            return null;
        }
        return getTheListOfItem;
    }

    public List<String> notice() {
        updateStatus();
        List<String> reminder = new ArrayList<>();
        Item itemExample = null;
        try {
            itemExample = new Other("创建", "不紧急 & 不重要", null);
        } catch (ItemCreateException e) {
            e.printStackTrace();
        }
        if (items.size() == 0) {
            return reminder;
        } else {
            List<Item> needRemind = new ArrayList<>();
            Item itemOfUse;
            for (int j = 0; j < items.size(); j++) {
                itemOfUse = items.get(j);
                int times = itemOfUse.getMinutes();
                Date time = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date now = itemExample.setTime(df.format(time));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + times);
                String nowStr = df.format(time);
                Date notice = itemExample.setTime(calendar1.get(Calendar.YEAR) + "-" + (calendar1.get(Calendar.MONTH) + 1) + "-" + calendar1.get(Calendar.DAY_OF_MONTH)
                        + " " + calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE));
                String noticeStr = df.format(notice);
                List<Item> resultList = new ArrayList<>();
                try {
                    resultList = getTheListOfItem(nowStr, noticeStr);
                    if (resultList == null) {
                        return reminder;
                    }
                    if (resultList.size() == 0) {
                        return reminder;
                    } else {
                        for (int k = 0; k < resultList.size(); k++) {
                            if (itemOfUse.itemEqual(resultList.get(k))) {
                                if (itemOfUse.getStatus().equals(Item.Status.NOSTART) && !itemOfUse.getRemindType().equals("不用提醒")) {
                                    needRemind.add(itemOfUse);
                                    break;
                                }
                            }
                        }
                    }
                } catch (ItemCreateException e) {
                    e.printStackTrace();
                }
            }

            if (needRemind.size() == 0) {
                return reminder;
            }
            String result = "";
            for (int i = 0; i < needRemind.size(); i++) {
                itemOfUse = needRemind.get(i);
                int times = itemOfUse.getMinutes();
                Date time = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date now = itemExample.setTime(df.format(time));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + times);
                Date notice = itemExample.setTime(calendar1.get(Calendar.YEAR) + "-" + (calendar1.get(Calendar.MONTH) + 1) + "-" + calendar1.get(Calendar.DAY_OF_MONTH)
                        + " " + calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE));
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) + itemOfUse.getRemindAgain());
                Date afterNotice = itemExample.setTime(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                calendar.setTime(itemOfUse.getStartTime());
                if (afterNotice.before(notice)) {
                    if (itemOfUse instanceof Course) {
                        if (now.before(itemOfUse.getStartTime())) {
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 + "分钟就要开始了" + "\n";
                            if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 > 0) {
                                if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 < times) {
                                    itemOfUse.setMinutes((int) ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000) - itemOfUse.getRemindAgain());
                                } else {
                                    itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                }
                                reminder.add(result);
                            }

                        } else {
                            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
                            int startMinute = calendar.get(Calendar.MINUTE);
                            if ((60 * (startHour - hour) + startMinute - minute) > 0) {
                                result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (60 * (startHour - hour) + startMinute - minute) + "分钟就要开始了" + "\n";
                                if ((60 * (startHour - hour) + startMinute - minute) >= 0) {
                                    if ((60 * (startHour - hour) + startMinute - minute) < times) {
                                        itemOfUse.setMinutes((60 * (startHour - hour) + startMinute - minute) - itemOfUse.getRemindAgain());
                                    } else {
                                        itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                    }
                                    reminder.add(result);
                                }
                            } else {
                                result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (60 * (24 - startHour + hour) + startMinute - minute) + "分钟就要开始了" + "\n";
                                if ((60 * (24 - startHour + hour) + startMinute - minute) > 0) {
                                    if ((60 * (24 - startHour + hour) + startMinute - minute) < times) {
                                        itemOfUse.setMinutes((60 * (24 - startHour + hour) + startMinute - minute) - itemOfUse.getRemindAgain());
                                    } else {
                                        itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                    }
                                    reminder.add(result);
                                }
                            }
                        }
                    } else if (itemOfUse instanceof Anniversary) {
                        CalendarDate calendarDate = ((Anniversary) itemOfUse).getStartDate();
                        int startYear = DateUtil.getToday().getYear();
                        int startMonth = calendarDate.getMonth();
                        int startDay = calendarDate.getDay();
                        if (startMonth < DateUtil.getToday().getMonth()) {
                            startYear = DateUtil.getToday().getYear() + 1;
                        }
                        Date startTime = itemExample.setTime(startYear + "-" + startMonth + "-" + startDay + " 0:0");
                        if (itemOfUse.getStatus().equals(Item.Status.NOSTART)) {
                            long minutes = (startTime.getTime() - now.getTime()) / 60000;
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + minutes + "分钟就要开始了" + "\n";
                            if (minutes >= 0) {
                                if (minutes < times) {
                                    itemOfUse.setMinutes((int) minutes - itemOfUse.getRemindAgain());
                                } else {
                                    itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                }
                                reminder.add(result);
                            }
                        }

                    } else {
                        if (itemOfUse.getStatus().equals(Item.Status.NOSTART)) {
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 + "分钟就要开始了" + "\n";
                            if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 > 0) {
                                if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 < times) {
                                    itemOfUse.setMinutes((int) ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000) - itemOfUse.getRemindAgain());
                                } else {
                                    itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                }
                                reminder.add(result);
                            }
                        }
                    }
                } else {
                    if (itemOfUse instanceof Course) {
                        if (itemOfUse.getStatus().equals(Item.Status.NOSTART)) {
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 + "分钟就要开始了" + "\n";
                            if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 < times && (itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 >= 0) {
                                itemOfUse.setMinutes((int) ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000) - itemOfUse.getRemindAgain());
                            } else if ((itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 > 0) {
                                itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                            }
                            reminder.add(result);
                        } else {
                            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
                            int startMinute = calendar.get(Calendar.MINUTE);
                            if ((60 * (startHour - hour) + startMinute - minute) > 0) {
                                result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (60 * (startHour - hour) + startMinute - minute) + "分钟就要开始了" + "\n";
                                if ((60 * (startHour - hour) + startMinute - minute) < times) {
                                    itemOfUse.setMinutes((60 * (startHour - hour) + startMinute - minute) - itemOfUse.getRemindAgain());
                                } else {
                                    itemOfUse.setMinutes(itemOfUse.getMinutes() - itemOfUse.getRemindAgain());
                                }
                                reminder.add(result);
                            } else {
                                result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (60 * (24 - startHour + hour) + startMinute - minute) + "分钟就要开始了" + "\n";
                                itemOfUse.setMinutes(times - itemOfUse.getRemindAgain());
                                reminder.add(result);
                            }
                        }
                    } else if (itemOfUse instanceof Anniversary) {
                        CalendarDate calendarDate = ((Anniversary) itemOfUse).getStartDate();
                        int startYear = calendarDate.getYear();
                        int startMonth = calendarDate.getMonth();
                        int startDay = calendarDate.getDay();
                        Date startTime = itemExample.setTime(startYear + "-" + startMonth + "-" + startDay + " 0:0");
                        if (itemOfUse.getStatus().equals(Item.Status.NOSTART)) {
                            long minutes = (startTime.getTime() - now.getTime()) / 60000;
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + minutes + "分钟就要开始了" + "\n";
                            itemOfUse.setMinutes(times - itemOfUse.getRemindAgain());
                            reminder.add(result);
                        } else {
                            CalendarDate calendarDate1 = DateUtil.getToday();
                            int month = calendarDate1.getMonth();
                            int day = calendarDate1.getDay();
                            Date date = itemExample.setTime(startYear + "-" + month + "-" + day + " 0:0");
                            if (date.getTime() > startTime.getTime()) {
                                startTime = itemExample.setTime((startYear + 1) + "-" + startMonth + "-" + startDay + " 0:0");
                            }
                            long minutes = (startTime.getTime() - now.getTime()) / 60000;
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + minutes + "分钟就要开始了" + "\n";
                            itemOfUse.setMinutes(times - itemOfUse.getRemindAgain());
                            reminder.add(result);

                        }
                    } else {
                        if (itemOfUse.getStatus().equals(Item.Status.NOSTART)) {
                            result = itemOfUse.getRemindType() + "/" + itemOfUse.getMessage() + "还有" + (itemOfUse.getStartTime().getTime() - now.getTime()) / 60000 + "分钟就要开始了" + "\n";
                            itemOfUse.setMinutes(times - itemOfUse.getRemindAgain());
                            reminder.add(result);
                        }
                    }
                }
            }
            return reminder;
        }

    }

    public void updateStatus() {
        Item item;
        Item item1 = null;
        try {
            item1 = new Other("创建", "不紧急 & 不重要", null);
        } catch (ItemCreateException e) {
            e.printStackTrace();
        }
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = item1.setTime(df.format(time));
        Date start, end;
        String status;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            start = item.getStartTime();
            end = item.getEndTime();
            if (item.getSubItems().size() != 0) {
                boolean finish = true;
                for (int j = 0; j < item.getSubItems().size(); j++) {
                    if (!(item.getSubItems().get(j).getStatus().equals(Item.Status.FINISHED))) {
                        finish = false;
                        break;
                    }
                }
                if (finish) {
                    item.setStatus("已完成");
                }
            }
            if (!(item.getStatus().equals(Item.Status.FINISHED))) {
                if (item instanceof Anniversary) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(now);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(start);
                    if (calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH) &&
                            calendar1.get(Calendar.YEAR) <= calendar.get(Calendar.YEAR)) {
                        status = "进行中";
                    } else {
                        status = "未开始";
                    }

                } else if (item instanceof Course) {
                    boolean hasStart = false;
                    SimpleDateFormat dfOfDate = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat dfOfTime = new SimpleDateFormat("HH:mm");
                    Date endDateAndTime = item1.setTime(((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getYear() + "-" +
                            +((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getMonth() + "-" +
                            ((Course) item).getDateList().get(((Course) item).getDateList().size() - 1).getDay() + " " + dfOfTime.format(end));
                    ;
                    CalendarDate calendarDateOfNow = null;
                    try {
                        calendarDateOfNow = new CalendarDate(dfOfDate.format(time));
                    } catch (IsFormatException e) {
                        e.printStackTrace();
                    }
                    for (int m = 0; m < ((Course) item).getDateList().size(); m++) {
                        if (calendarDateOfNow.equals(((Course) item).getDateList().get(m))) {
                            Date nowStart = item1.setTime(dfOfDate.format(time) + " " + dfOfTime.format(start));
                            Date nowEnd = item1.setTime(dfOfDate.format(time) + " " + dfOfTime.format(end));
                            if ((nowStart.before(now) || nowStart.toString().equals(now.toString())) && (now.before(nowEnd) || now.toString().equals(nowEnd.toString()))) {
                                hasStart = true;
                            }
                        }
                    }

                    if ((now.before(endDateAndTime) || now.toString().equals(endDateAndTime.toString()))) {
                        if (hasStart) {
                            status = "进行中";
                        } else {
                            status = "未开始";
                        }
                    } else {
                        status = "过期";
                    }

                } else {
                    if (start.after(now)) {
                        status = "未开始";
                    } else if ((now.after(start) || now.toString().equals(start.toString()))
                            && (end.after(now) || end.toString().equals(now.toString()))) {
                        status = "进行中";
                    } else {
                        status = "过期";
                    }
                }
            } else {
                status = "已完成";
            }
            item.setStatus(status);
        }
    }

    public boolean itemFinished(Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (item.itemEqual(items.get(i))) {
                item = items.get(i);
                Item.Status status = item.getStatus();
                if (status.equals(Item.Status.UNDERWAY)) {
                    if (item.getSubItems() != null) {
                        for (int j = 0; j < item.getSubItems().size(); j++) {
                            if (!(item.getSubItems().get(i).getStatus()).equals(Item.Status.FINISHED)) {
                                return false;
                            }
                        }
                        items.get(i).setStatus("已完成");
                        updateStatus();
                        return true;
                    } else {
                        items.get(i).setStatus("已完成");
                        updateStatus();
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public void save() throws IOException {
        updateStatus();
        FileWriter fileWriter = new FileWriter("src/Logic/schedule.txt");
        int[] saves = new int[items.size()];
        for (int i = 0; i < saves.length; i++) {
            saves[i] = 1;
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (saves[i] == 1) {
                if (item.getParentItem() == null) {
                    fileWriter.write(item.getItemSaveInfo());
                    saves[i] = 0;
                    if (item.getSubItems() != null) {
                        for (int j = 0; j < item.getSubItems().size(); j++) {
                            fileWriter.write(item.getSubItems().get(j).getItemSaveInfo());
                            saves[itemPosition(item.getSubItems().get(j))] = 0;
                        }
                    }
                    fileWriter.write("\n");
                }
            }
        }


        fileWriter.close();
    }

    public void load() throws IOException {
        Scanner scanner = new Scanner(new File("src/Logic/schedule.txt"));
        List<String> loadStr = new ArrayList<>();
        List<Item> loadItem = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("")) {
                Item item = null;
                for (int i = 0; i < loadStr.size(); i++) {
                    String[] info = loadStr.get(i).split("/");
                    if (info[0].equals("Anniversary")) {
                        String date = info[1];
                        String name = info[2];
                        String type = info[3];
                        String description = info[4];
                        String priority = info[5];
                        try {
                            item = new Anniversary(date, name, type, description, priority, null);
                            item.setStatus(info[6]);
                            item.setRemindType(info[7]);
                            item.setMinutes(Integer.valueOf(info[10]));
                            item.setRemindAgain(Integer.valueOf(info[9]));
                            item.setEarliestReminder(Integer.valueOf(info[10]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        } catch (IsFormatException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);

                    } else if (info[0].equals("Appointment")) {
                        String start = info[1];
                        String end = info[2];
                        String participant = info[3];
                        String place = info[4];
                        String content = info[5];
                        String priority = info[6];
                        try {
                            item = new Appointment(start, end, participant, place, content, priority, null);
                            item.setStatus(info[7]);
                            item.setRemindType(info[8]);
                            item.setMinutes(Integer.valueOf(info[11]));
                            item.setRemindAgain(Integer.valueOf(info[10]));
                            item.setEarliestReminder(Integer.valueOf(info[11]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        } catch (IsFormatException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);

                    } else if (info[0].equals("Course")) {
                        String name = info[1];
                        String start = info[2];
                        String end = info[3];
                        String startDate = info[4];
                        String content = info[5];
                        String place = info[6];
                        String duration = info[7];
                        String teacher = info[8];
                        String remark = info[9];
                        String week = "" + info[10];
                        String priority = info[11];
                        try {
                            item = new Course(name, start, end, startDate, content, place, duration, teacher, remark, week, priority, null);
                            item.setStatus(info[12]);
                            item.setRemindType(info[13]);
                            item.setMinutes(Integer.valueOf(info[16]));
                            item.setRemindAgain(Integer.valueOf(info[15]));
                            item.setEarliestReminder(Integer.valueOf(info[16]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        } catch (IsFormatException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    } else if (info[0].equals("Interview")) {
                        try {
                            item = new Interview(info[1], info[2], info[3], info[4], info[5], info[6], info[7], null);
                            item.setStatus(info[8]);
                            item.setRemindType(info[9]);
                            item.setMinutes(Integer.valueOf(info[12]));
                            item.setRemindAgain(Integer.valueOf(info[11]));
                            item.setEarliestReminder(Integer.valueOf(info[12]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        } catch (IsFormatException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    } else if (info[0].equals("Meeting")) {
                        try {
                            item = new Meeting(info[1], info[2], info[3], info[4], info[5], info[6], null);
                            item.setStatus(info[7]);
                            item.setRemindType(info[8]);
                            item.setMinutes(Integer.valueOf(info[11]));
                            item.setRemindAgain(Integer.valueOf(info[10]));
                            item.setEarliestReminder(Integer.valueOf(info[11]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        } catch (IsFormatException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    } else if (info[0].equals("Other1")) {
                        try {
                            item = new Other(info[1], info[2], info[3], info[4], null);
                            item.setStatus(info[5]);
                            item.setRemindType(info[6]);
                            item.setMinutes(Integer.valueOf(info[9]));
                            item.setRemindAgain(Integer.valueOf(info[8]));
                            item.setEarliestReminder(Integer.valueOf(info[9]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    } else if (info[0].equals("Other2")) {
                        try {
                            item = new Other(info[3], info[4], null);
                            item.setStatus(info[5]);
                            item.setRemindType(info[6]);
                            item.setMinutes(Integer.valueOf(info[9]));
                            item.setRemindAgain(Integer.valueOf(info[8]));
                            item.setEarliestReminder(Integer.valueOf(info[9]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    } else {
                        try {
                            item = new Trip(info[1], info[2], info[3], info[4], info[5], info[6], info[7], null);
                            item.setStatus(info[8]);
                            item.setRemindType(info[9]);
                            item.setMinutes(Integer.valueOf(info[12]));
                            item.setRemindAgain(Integer.valueOf(info[11]));
                            item.setEarliestReminder(Integer.valueOf(info[12]));
                        } catch (ItemCreateException e) {
                            e.printStackTrace();
                        }
                        loadItem.add(item);
                    }
                }
                for (int i = 1; i < loadItem.size(); i++) {
                    loadItem.get(0).addSubItem(loadItem.get(i));
                    loadItem.get(i).setParentItem(loadItem.get(0));
                }
                for (int i = 0; i < loadItem.size(); i++) {
                    items.add(loadItem.get(i));
                }
                loadItem.clear();
                loadStr.clear();
            } else {
                loadStr.add(line);
            }
        }
        scanner.close();
    }
}
