package Logic.instance;

import Logic.Item;

import java.text.SimpleDateFormat;
import java.util.Date;

import Exception.ItemCreateException;
import Exception.IsFormatException;

public class Appointment extends Item {
    private Date startTime;//约会的开始时间
    private Date endTime;//约会的结束时间
    private String participant;//约会的参与者
    private String content;//约会的内容
    private String place;//约会的地点

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Appointment(String startTime, String endTime, String participant,
                       String place, String content, String priority, Item parentItem) throws ItemCreateException, IsFormatException {
        super(priority, parentItem);

        Date start = setTime(startTime);
        Date end = setTime(endTime);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (content == null || start == null || end == null || start.after(end) || participant == null || place == null) {
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
        this.participant = participant;
        this.content = content;
        this.place = place;
        setStatus(status);
    }

    @Override
    public String getMessage() {
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        return start + "到" + end + "，在" + place + "与" + participant + "约会,内容为" + content + "。";
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Appointment) {
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
                return this.startTime.toString().equals(((Appointment) item).startTime.toString()) && this.endTime.toString().equals(((Appointment) item).endTime.toString()) && this.participant.equals(((Appointment) item).participant)
                        && this.content.equals(((Appointment) item).content) && this.place.equals(((Appointment) item).place) && item.getPriority().equals(this.getPriority()) && item.getStatus().equals(this.getStatus())
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

    @Override
    public Date getEndTime() {
        return this.endTime;
    }


    @Override
    public String[] getUncontradictoryTypesItems() {
        return new String[]{"纪念日", "约会", "自定义", "旅程"};
    }

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Appointment";
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        String participant = this.participant;
        String place = this.place;
        String content = this.content;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        return typeStr + "/" + start + "/" + end + "/" + participant + "/" + place + "/" + content + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/" + this.getRemindType()
                + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }

}
