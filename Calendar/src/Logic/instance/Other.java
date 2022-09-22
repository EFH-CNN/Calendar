package Logic.instance;

import Logic.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Exception.ItemCreateException;

public class Other extends Item {

    private Date startTime;//自定义事件的开始时间，如果是没有的，此处为今天的0点
    private Date endTime;//自定义事件的结束时间，如果没有，则为今天的23点59分
    private String message;//内容
    private boolean hasTime;//是否设置了时间，用于区别自定义类型

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Other(String startTime, String endTime, String message, String priority, Item parentItem) throws ItemCreateException {
        super(priority, parentItem);

        Date start = setTime(startTime);
        Date end = setTime(endTime);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (start == null || end == null || message == null) {
            throw new ItemCreateException("输入错误，无法构造待办事项！！");
        }
        String status;
        if (start.after(now)) {
            status = "未开始";
        } else if ((now.after(start) || now.toString().equals(start.toString())) && (end.after(now) || end.toString().equals(now.toString()))) {
            status = "进行中";
        } else {
            status = "过期";
        }
        this.startTime = start;
        this.endTime = end;
        this.message = message;
        this.hasTime = true;
        setStatus(status);
    }

    public Other(String message, String priority, Item parentItem) throws ItemCreateException {
        super(priority, parentItem);

        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (message == null) {
            throw new ItemCreateException("输入错误，无法构造待办事项！！");
        }
        String status = "进行中";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Date startTime = setTime(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " 0:0");
        Date endTime = setTime(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " 23:59");
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.hasTime = false;
        setStatus(status);
    }

    @Override
    public String getMessage() {
        if (hasTime) {
            String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
            String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
            return start + "到" + end + "，" + this.message;
        } else {
            return this.message;
        }
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Other) {
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
                    if (this.getSubItems().get(i).itemEqual(item.getSubItems().get(j)) && sizeArray[j] == 1){
                        k++;
                        sizeArray[j] = 0;
                    }
                }
            }
            if (k == item.getSubItems().size() && this.getSubItems().size() == item.getSubItems().size()) {
                return this.startTime.toString().equals(((Other) item).startTime.toString()) && this.endTime.toString().equals(((Other) item).endTime.toString())
                        && this.message.equals(((Other) item).message) && item.getPriority().equals(this.getPriority()) && item.getStatus().equals(this.getStatus())
                        && item.getMinutes() == this.getMinutes() && item.getRemindAgain() == this.getRemindAgain() && item.getRemindType().equals(this.getRemindType())
                        && item.getEarliestReminder() == this.getEarliestReminder() && ((Other) item).hasTime == this.hasTime;
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

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    @Override
    public String[] getUncontradictoryTypesItems() {
        return new String[]{"纪念日", "约会", "课程", "面试", "会议", "自定义", "旅程"};
    }

    public boolean getHasTime() {
        return hasTime;
    }

    public void setHasTime(boolean hasTime) {
        this.hasTime = hasTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Other2";
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        String message = this.message;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        if (hasTime) {
            typeStr = "Other1";
        }
        return typeStr + "/" + start + "/" + end + "/" + message + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/" + this.getRemindType()
                + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }

}
