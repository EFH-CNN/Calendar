package Logic.instance;

import Logic.CalendarDate;
import Logic.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Exception.ItemCreateException;
import Exception.IsFormatException;

public class Anniversary extends Item {
    private CalendarDate startDate;//创建的第一年的CalenderDate
    private String type;//纪念日的类型
    private String name;//纪念日的名字
    private String description;//纪念日的描述

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Anniversary(String date, String name, String type, String description,
                       String priority, Item parentItem) throws ItemCreateException, IsFormatException {
        super(priority, parentItem);

        if (date == null || name == null || type == null || description == null) {
            throw new ItemCreateException("输入错误，无法构造待办事项！！");
        }
        CalendarDate calendarDate = new CalendarDate(date);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        Date start = setTime(date + " 0:0");
        if (start == null) {
            throw new ItemCreateException("时间输入错误！！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(start);
        String status;
        if (calendar.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH) &&
                calendar1.get(Calendar.YEAR) <= calendar.get(Calendar.YEAR)) {
            status = "进行中";
        } else {
            status = "未开始";
        }

        this.startDate = calendarDate;
        this.type = type;
        this.name = name;
        this.description = description;
        setStatus(status);
    }

    @Override
    public String getMessage() {
        return "从" + startDate.getYear() + "-" + startDate.getMonth() + "-" + startDate.getDay() + "开始" + "是" + type + "类型的纪念日，是" + name + "纪念日，描述为" + description + "。";
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Anniversary) {
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
                    if (this.getSubItems().get(i).itemEqual(item.getSubItems().get(j)) && sizeArray[j] == 1){
                        k++;
                        sizeArray[j] = 0;
                    }
                }
            }

            if ((this.getParentItem() == null && item.getParentItem() != null) || (this.getParentItem() != null && item.getParentItem() == null)) {
                return false;
            }
            if (k == item.getSubItems().size() && this.getSubItems().size() == item.getSubItems().size()) {
                return ((Anniversary) item).description.equals(this.description) && ((Anniversary) item).type.equals(type) && ((Anniversary) item).name.equals(this.name)
                        && ((Anniversary) item).startDate.equals(this.startDate) && item.getPriority().equals(this.getPriority()) && item.getStatus().equals(this.getStatus())
                        && item.getMinutes() == this.getMinutes() && item.getRemindAgain() == this.getRemindAgain() && item.getRemindType().equals(this.getRemindType())
                        && item.getEarliestReminder() == this.getEarliestReminder();
            } else {
                return false;
            }

        } else
            return false;
    }

    @Override
    public Date getStartTime() {
        return setTime(this.startDate.getYear() + "-" + this.startDate.getMonth() + "-" + this.startDate.getDay() + " 0:0");
    }

    @Override
    public Date getEndTime() {
        return setTime(this.startDate.getYear() + "-" + this.startDate.getMonth() + "-" + this.startDate.getDay() + " 23:59");
    }

    public CalendarDate getStartDate() {
        return startDate;
    }

    @Override
    public String[] getUncontradictoryTypesItems() {
        return new String[]{"纪念日", "约会", "课程", "面试", "会议", "自定义", "旅程"};
    }

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Anniversary";
        String date = "" + this.startDate.getYear() + "-" + this.startDate.getMonth() + "-" + this.startDate.getDay();
        String name = this.name;
        String type = this.type;
        String description = this.description;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        return typeStr + "/" + date + "/" + name + "/" + type + "/" + description + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/" + this.getRemindType()
                + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }
}
