package Logic.instance;

import Logic.Item;

import java.text.SimpleDateFormat;
import java.util.Date;

import Exception.ItemCreateException;
import Exception.IsFormatException;

public class Interview extends Item {
    private Date startTime;//面试的开始时间
    private Date endTime;//面试的结束时间
    private String company;//面试的公司
    private String remark;//面试的备注
    private String position;//面试的职位
    private String place;//面试的地点

    //Status不需要传，根据时间来判断初始状态，写的时候从构造方法中除去
    public Interview(String startTime, String endTime, String place, String Company,
                     String position, String remark, String priority, Item parentItem) throws ItemCreateException, IsFormatException {
        super(priority, parentItem);

        Date start = setTime(startTime);
        Date end = setTime(endTime);
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = setTime(df.format(time));
        if (Company == null || start == null || end == null || start.after(end) || position == null || place == null || remark == null) {
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
        this.company = Company;
        this.position = position;
        this.place = place;
        this.remark = remark;
        setStatus(status);
    }

    @Override
    public String getMessage() {
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.endTime);
        return start + "到" + end + "，去" + place + "面试，面试公司为" + this.company + ",面试岗位为" + position + "。备注：" + remark;
    }

    @Override
    public boolean itemEqual(Item item) {
        if (item == null) {
            return false;
        }
        if (item instanceof Interview) {
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
                return this.startTime.toString().equals(((Interview) item).startTime.toString()) && this.endTime.toString().equals(((Interview) item).endTime.toString())
                        && this.company.equals(((Interview) item).company) && this.position.equals(((Interview) item).position) && this.place.equals(((Interview) item).place)
                        && this.remark.equals(((Interview) item).remark) && item.getPriority().equals(this.getPriority()) && item.getStatus().equals(this.getStatus())
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
        return new String[]{"纪念日", "面试", "自定义", "旅程"};
    }

    @Override
    public String getItemSaveInfo() {
        String typeStr = "Interview";
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.startTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this.endTime);
        String place = this.place;
        String company = "" + this.company;
        String position = this.position;
        String remark = this.remark;
        String priority = Item.Priority.parsePriorityToString(this.getPriority());
        return typeStr + "/" + start + "/" + end + "/" + place + "/" + company + "/" + position + "/" + remark + "/" + priority + "/" + Item.Status.parseStatusToString(this.getStatus()) + "/" + this.getRemindType()
                + "/" + this.getMinutes() + "/" + this.getRemindAgain() + "/" + this.getEarliestReminder() + "/" + "\n";
    }

}
