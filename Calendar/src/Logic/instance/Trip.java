package Logic.instance;

import Logic.Item;

import java.text.SimpleDateFormat;
import java.util.Date;

import Exception.ItemCreateException;

public class Trip extends Item {
    private Date startTime;//旅游的开始时间
    private Date endTime;//旅游的结束时间
    private String transportation;//旅游的交通方式
    private String place;//旅游地点
    private String transportationNum;//旅游车次
    private String remark;//旅游备注

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Trip(String startTime, String endTime, String transportation, String place
            , String transportationNum, String remark, String priority, Item parentItem) throws ItemCreateException {
        super(priority, parentItem);

        Date start = setTime(startTime);
        Date end = setTime(endTime);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (transportation == null || start == null || end == null || start.after(end) || transportationNum == null || place == null || remark == null) {
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
        this.transportation = transportation;
        this.transportationNum = transportationNum;
        this.place = place;
        this.remark = remark;
        setStatus(status);
    }

    @Override
    public String getMessage() {
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        return start + "到" + end + "去" + place + "旅行，交通方式为" + transportation + ",车次为" + transportationNum + "。备注：" + remark;
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Trip) {
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
                return this.startTime.toString().equals(((Trip) item).startTime.toString()) && this.endTime.toString().equals(((Trip) item).endTime.toString())
                        && this.transportation.equals(((Trip) item).transportation) && this.transportationNum.equals(((Trip) item).transportationNum)
                        && this.place.equals(((Trip) item).place) && this.remark.equals(((Trip) item).remark) && item.getPriority().equals(this.getPriority())
                        && item.getStatus().equals(this.getStatus()) && item.getMinutes() == this.getMinutes() && item.getRemindAgain() == this.getRemindAgain()
                        && item.getRemindType().equals(this.getRemindType()) && item.getEarliestReminder() == this.getEarliestReminder();
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

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Trip";
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        String transportation = this.transportation;
        String place = this.place;
        String transportationNum = this.transportationNum;
        String remark = this.remark;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        return typeStr + "/" + start + "/" + end + "/" + transportation + "/" + place + "/" + transportationNum + "/" + remark + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/" + this.getRemindType()
                + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }

}
