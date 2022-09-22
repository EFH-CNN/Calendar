package Logic.instance;

import Logic.CalendarDate;
import Logic.Item;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Exception.ItemCreateException;
import Exception.IsFormatException;

public class Course extends Item {
    private String name;//课程的名称
    private Date startTime;//第一次课的date类型的开始时间
    private Date endTime;//第一次课的date类型的结束时间
    private String startDate;//第一次课的日子 yyyy-MM-dd类型
    private String content;//上课的内容
    private String place;//上课的地点
    private int duration;//持续的周
    private String teacher;//任课老师
    private String remark;//备注
    private int week;//在周几上课
    private List<CalendarDate> dateList;//所有上课的日子

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Course(String name, String startTime, String endTime, String startDate, String content,
                  String place, String duration, String teacher, String remark, String week,
                  String priority, Item parentItem) throws ItemCreateException, IsFormatException {
        super(priority, parentItem);
        dateList = new ArrayList<>();
        CalendarDate calendarDate = new CalendarDate(startDate);
        CalendarDate startCalenderDate;

        if (Integer.valueOf(week) >= calendarDate.getDayOfWeek()) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendarDate.getYear(), calendarDate.getMonth() - 1, calendarDate.getDay() + Integer.valueOf(week) - calendarDate.getDayOfWeek());
            startCalenderDate = new CalendarDate("" + calendar.get(Calendar.YEAR) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendarDate.getYear(), calendarDate.getMonth() - 1, calendarDate.getDay() + Integer.valueOf(week) + 7 - calendarDate.getDayOfWeek());
            startCalenderDate = new CalendarDate("" + calendar.get(Calendar.YEAR) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        }
        for (int i = 0; i < Integer.valueOf(duration); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(startCalenderDate.getYear(), startCalenderDate.getMonth() - 1, startCalenderDate.getDay() + 7 * i);
            CalendarDate calendarDate1 = new CalendarDate("" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            dateList.add(calendarDate1);
        }

        Date start = setTime("" + calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + startTime);
        Date end = setTime("" + calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay() + " " + endTime);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (name == null || start == null || end == null || start.after(end) || startDate == null || content == null || place == null || duration == null || teacher == null || remark == null || week == null) {
            throw new ItemCreateException("输入错误，无法构造待办事项！！");
        }

        this.startTime = start;
        this.endTime = end;
        this.name = name;
        this.startDate = startDate;
        this.content = content;
        this.place = place;
        this.duration = Integer.valueOf(duration);
        this.teacher = teacher;
        this.remark = remark;
        this.week = Integer.valueOf(week);
        String status;

        end = setTime("" + dateList.get(dateList.size()-1).getYear() + "-" + dateList.get(dateList.size()-1).getMonth() + "-" + dateList.get(dateList.size()-1).getDay() + " " + endTime);
        boolean hasStart = false;
        SimpleDateFormat dfOfDate = new SimpleDateFormat("yyyy-MM-dd");
        CalendarDate calendarDateOfNow = new CalendarDate(dfOfDate.format(time));
        for (int i = 0; i < dateList.size(); i++) {
            if (calendarDateOfNow.equals(dateList.get(i))) {
                SimpleDateFormat dfOfTime = new SimpleDateFormat("HH:mm");
                Date nowStart = setTime(dfOfDate.format(time) + " " + dfOfTime.format(start));
                Date nowEnd = setTime(dfOfDate.format(time) + " " + dfOfTime.format(end));
                if ((nowStart.before(now) || nowStart.toString().equals(now.toString())) && (now.before(nowEnd) || now.toString().equals(nowEnd.toString()))) {
                    hasStart = true;
                }
            }
        }

        if ((now.before(end) || now.toString().equals(end.toString()))) {
            if (hasStart) {
                status = "进行中";
            } else {
                status = "未开始";
            }
        } else {
            status = "过期";
        }
        setStatus(status);
    }


    public Date getCourseStartTime() {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        return df.parse(dateStr, new ParsePosition(0));
    }


    @Override
    public String getMessage() {
        String start = new SimpleDateFormat("HH:mm").format(this.startTime);
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(this.startTime);
        String end = new SimpleDateFormat("HH:mm").format(this.endTime);
        return "从" + startDate + "开始，" + "上课时间：" + start + "~" + end + "，在" + place + "上" + name + ",内容为" + content + ",任课老师是" + teacher + ",持续周数为" + duration + "周,星期" + week + "上课。备注：" + remark;
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Course) {
            if ((this.getParentItem() == null && item.getParentItem() != null) || (this.getParentItem() != null && item.getParentItem() == null)) {
                return false;
            }
            int subItemSize = this.getSubItems().size();
            if (subItemSize != item.getSubItems().size()) {
                return false;
            }

            int[] sizeArray = new int[subItemSize];
            for (int i = 0; i < sizeArray.length; i++) {
                sizeArray[i] = 1;
            }

            int k = 0;
            for (int i = 0; i < subItemSize; i++) {
                for (int j = 0; j < subItemSize; j++) {
                    if (this.getSubItems().get(i).itemEqual(item.getSubItems().get(j)) && sizeArray[j] == 1) {
                        k++;
                        sizeArray[j] = 0;
                    }
                }
            }
            if (k == item.getSubItems().size() && this.getSubItems().size() == item.getSubItems().size()) {
                return this.startTime.toString().equals(((Course) item).startTime.toString()) && this.endTime.toString().equals(((Course) item).endTime.toString()) && this.name.equals(((Course) item).name)
                        && this.content.equals(((Course) item).content) && this.place.equals(((Course) item).place) && this.duration == ((Course) item).duration && this.teacher.equals(((Course) item).teacher)
                        && this.remark.equals(((Course) item).remark) && this.week == ((Course) item).week && item.getPriority().equals(this.getPriority()) && item.getStatus().equals(this.getStatus())
                        && item.getMinutes() == this.getMinutes() && item.getRemindAgain() == this.getRemindAgain() && item.getRemindType().equals(this.getRemindType())
                        && item.getEarliestReminder() == this.getEarliestReminder();
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public Date getStartTime() {
        return this.startTime;

    }

    public List<CalendarDate> getDateList() {
        return dateList;
    }

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getWeek() {
        return week;
    }

    @Override
    public String[] getUncontradictoryTypesItems() {
        return new String[]{"纪念日", "课程", "自定义", "旅程"};
    }

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Course";
        String name = this.name;

        String start = new SimpleDateFormat("HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("HH:mm").format(this.endTime);
        String startDate = this.startDate;
        String content = this.content;
        String place = this.place;
        String duration = "" + this.duration;
        String teacher = this.teacher;
        String remark = this.remark;
        String week = "" + this.week;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        return typeStr + "/" + name + "/" + start + "/" + end + "/" + startDate + "/" + content + "/" + place + "/" + duration + "/" + teacher + "/" + remark + "/" + week + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/"
                + this.getRemindType() + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }

}
