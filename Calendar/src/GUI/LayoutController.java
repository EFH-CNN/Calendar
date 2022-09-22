package GUI;

import Logic.CalendarDate;
import Logic.DateUtil;
import Logic.Item;
import Logic.Schedule;
import Logic.instance.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Exception.InvalidTimeOfScheduleItem;
import Exception.MessageIsNull;
import Exception.ItemCreateException;
import Exception.IsFormatException;
import javafx.stage.WindowEvent;


import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

public class LayoutController extends Application implements Initializable {

    private Schedule schedule = new Schedule();
    public Item parentItem;
    private int nowadaysYear = DateUtil.getToday().getYear();
    private int nowadaysMonth = DateUtil.getToday().getMonth();

    @FXML
    public TextArea remindLabel;
    @FXML
    public ComboBox yearChoiceBox;
    @FXML
    public ComboBox monthChoiceBox;
    @FXML
    public GridPane calendarShow = new GridPane();
    @FXML
    public TextField searchTextField;
    @FXML
    public TextField startTimeSearchTextField;
    @FXML
    public TextField endTimeSearchTextField;
    @FXML
    public ComboBox typeOfThingToAddFiled;
    @FXML
    public ComboBox timeOfRemind;
    @FXML
    public ComboBox typeOfTransportsToAddFiled;
    @FXML
    public ComboBox repeatWeek;
    @FXML
    public ComboBox importanceOfSchedule;
    @FXML
    public ComboBox wayOfRemind;
    @FXML
    public ComboBox remindAgain;
    @FXML
    public TextField startTimeAddTextFieldMeeting;
    @FXML
    public TextField endTimeAddTextFieldMeeting;
    @FXML
    public TextField placeToAddByTimeTextFieldMeeting;
    @FXML
    public TextField titleToAddByTimeTextFieldMeeting;
    @FXML
    public TextField contentAddByTimeTextFieldMeeting;
    @FXML
    public TextField startTimeAddTextFieldAppointment;
    @FXML
    public TextField endTimeAddTextFieldAppointment;
    @FXML
    public TextField placeToAddByTimeTextFieldAppointment;
    @FXML
    public TextField personToAddByTimeTextFieldAppointment;
    @FXML
    public TextField contentAddByTimeTextFieldAppointment;
    @FXML
    public TextField startTimeAddTextFieldTravel;
    @FXML
    public TextField endTimeAddTextFieldTravel;
    @FXML
    public TextField placeToAddByTimeTextFieldTravel;
    @FXML
    public TextField numberOfTransportsToAddByTimeTextFieldTravel;
    @FXML
    public TextField remarksToAddByTimeTextFieldTravel;
    @FXML
    public TextField dayOfAnniversary;
    @FXML
    public TextField typeOfAnniversary;
    @FXML
    public TextField nameOfAnniversary;
    @FXML
    public TextField descriptionOfAnniversary;
    @FXML
    public TextField startDayOfInterview;
    @FXML
    public TextField endDayOfInterview;
    @FXML
    public TextField placeOfInterview;
    @FXML
    public TextField companyOfInterview;
    @FXML
    public TextField positionOfInterview;
    @FXML
    public TextField remarksOfInterview;
    @FXML
    public TextField nameOfCourse;
    @FXML
    public TextField startTimeOfCourse;
    @FXML
    public TextField endTimeOfCourse;
    @FXML
    public TextField contentOfCourse;
    @FXML
    public TextField placeOfCourse;
    @FXML
    public TextField lastTimeOfCourse;
    @FXML
    public TextField teacherOfCourse;
    @FXML
    public TextField remarksOfCourse;
    @FXML
    public TextField startDateOfCourse;
    @FXML
    public TextField startTimeAddTextFieldOthers;
    @FXML
    public TextField endTimeAddTextFieldOthers;
    @FXML
    public TextField contentAddByTimeTextFieldOthers;
    @FXML
    public TextArea remindContent;
    @FXML
    public TextArea clockContent;
    @FXML
    public VBox addVbox;
    @FXML
    public VBox childVbox;
    @FXML
    public HBox meetingHbox2;
    @FXML
    public HBox appointmentHbox2;
    @FXML
    public HBox travelHbox2;
    @FXML
    public HBox anniversaryHbox;
    @FXML
    public HBox interviewHbox;
    @FXML
    public HBox courseHbox1;
    @FXML
    public HBox courseHbox2;
    @FXML
    public HBox othersHbox2;

    public LayoutController() throws FileNotFoundException, InvalidTimeOfScheduleItem, MessageIsNull {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
        Scene scene = new Scene(root, 1800, 700);
        primaryStage.setTitle("Calendar");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("calendar.png"))));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    public void Remind() {
        schedule.updateStatus();
        final Date[] date = {new Date()};
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                schedule.updateStatus();
                List<String> results = schedule.notice();
                if (results.size() == 0){
                    remindContent.setText("");
                } else {
                    String[] way = new String[results.size()];
                    for (int i = 0; i < results.size(); i++) {
                        way[i] = results.get(i).split("/")[0];
                    }
                    for (int i = 0; i < way.length; i++) {
                        if (way[i].equals("弹窗")) {
                            JOptionPane.showMessageDialog(null, results.get(i).substring(way[i].length() + 1) + "\n", "提示", JOptionPane.INFORMATION_MESSAGE);
                        } else if (way[i].equals("显示框")) {
                            remindContent.setText(results.get(i).substring(way[i].length() + 1) + "\n");
                        } else if (way[i].equals("以上两者")) {
                            JOptionPane.showMessageDialog(null, results.get(i).substring(way[i].length() + 1) + "\n", "提示", JOptionPane.INFORMATION_MESSAGE);
                            remindContent.setText(results.get(i).substring(way[i].length() + 1) + "\n");
                        }
                    }
                }
            }
        }, date[0], 60 * 1000);

        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                clockContent.setText("Time：" + date[0].toString());
                date[0] = new Date();
            }
        }, date[0], 1000);
    }

    @FXML
    private void setYearChoiceBox() {
        ObservableList<String> yearList = FXCollections.observableArrayList();
        for (int i = 0; i < 501; i++) {
            yearList.add("" + (1800 + i));
        }
        yearChoiceBox.setItems(yearList);
        yearChoiceBox.getSelectionModel().select(DateUtil.getToday().getYear() - 1800);
    }

    @FXML
    private void setMonthChoiceBox() {
        ObservableList<String> monthList = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        monthChoiceBox.setItems(monthList);
        monthChoiceBox.getSelectionModel().select(DateUtil.getToday().getMonth() - 1);
    }

    @FXML
    private void setRemindAgain() {
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        ObservableList<String> remindAgain1 = FXCollections.observableArrayList();
        if (remindMinutes.equals("1小时")) {
            remindAgain1.add("1分钟");
            remindAgain1.add("5分钟");
            remindAgain1.add("10分钟");
            remindAgain1.add("20分钟");
        } else if (remindMinutes.equals("5小时")) {
            remindAgain1.add("5分钟");
            remindAgain1.add("20分钟");
            remindAgain1.add("30分钟");
            remindAgain1.add("1小时");
        } else if (remindMinutes.equals("一天")) {
            remindAgain1.add("5分钟");
            remindAgain1.add("2小时");
            remindAgain1.add("4小时");
            remindAgain1.add("6小时");
        } else if (remindMinutes.equals("一周")) {
            remindAgain1.add("5分钟");
            remindAgain1.add("1天");
            remindAgain1.add("2天");
            remindAgain1.add("3天");
        }
        remindAgain.setItems(remindAgain1);
        remindAgain.getSelectionModel().select(0);
    }

    @FXML
    private void searchDate() throws ItemCreateException, IsFormatException {
        schedule.updateStatus();
        String searchString = searchTextField.getText();

        if (DateUtil.isFormatted(searchString)) {

            String[] dates = searchString.split("-");
            int year = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int day = Integer.parseInt(dates[2]);
            CalendarDate searchDate = new CalendarDate(year, month, day);

            if (DateUtil.isValid(searchDate) && year >= 1800 && year <= 2300) {

                paintDays(searchDate);
                searchTextField.clear();

            } else {

                if (year < 1800 || year > 2300)
                    JOptionPane.showMessageDialog(null, "很抱歉，目前仅支持1800年-2300年的查询！", "提示", JOptionPane.WARNING_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "很抱歉，你输入的日期无法识别！", "提示", JOptionPane.WARNING_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(null, "输入格式不符合要求，标准格式为YYYY-MM-DD！", "提示", JOptionPane.WARNING_MESSAGE);
        }

    }

    @FXML
    private void paintDays() throws ItemCreateException, IsFormatException {
        if (yearChoiceBox.getValue() != null && monthChoiceBox.getValue() != null) {

            String yearString = yearChoiceBox.getValue().toString();
            int year = Integer.parseInt(yearString);
            String monthString = monthChoiceBox.getValue().toString();
            int month = Integer.parseInt(monthString);
            CalendarDate date = new CalendarDate(year, month, 1);
            paintDays(date);

        } else {

            JOptionPane.showMessageDialog(null, "你的筛选有缺漏！", "提示", JOptionPane.WARNING_MESSAGE);

        }
    }

    @FXML
    private void paintToday() throws ItemCreateException, IsFormatException {
        paintDays(DateUtil.getToday());
    }

    @FXML
    private void searchItem() throws ItemCreateException {
        schedule.updateStatus();
        String startTime = startTimeSearchTextField.getText();
        String endTime = endTimeSearchTextField.getText();
        List<Item> itemList = schedule.getTheListOfItem(startTime, endTime);

        if (itemList == null)
            JOptionPane.showMessageDialog(null, "时间设置不正确", "提示", JOptionPane.WARNING_MESSAGE);
        else {

            paintSchedule(itemList,startTime,endTime);
            startTimeSearchTextField.clear();
            endTimeSearchTextField.clear();

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addVbox.getChildren().clear();
        setYearChoiceBox();
        setMonthChoiceBox();
        Remind();
        try {
            paintDays();
        } catch (ItemCreateException e) {
            e.printStackTrace();
        } catch (IsFormatException e) {
            e.printStackTrace();
        }

    }

    void paintDays(CalendarDate date) throws IsFormatException, ItemCreateException {

        calendarShow.getChildren().clear();
        Button[][] days = new Button[7][7];
        Label[][] labels = new Label[7][7];
        Label[][] labelHolidays = new Label[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                days[i][j] = new Button("");
                labels[i][j] = new Label("");
                labelHolidays[i][j] = new Label("");
                calendarShow.add(labels[i][j], j, i);
                calendarShow.add(labelHolidays[i][j], j, i);
                calendarShow.add(days[i][j], j, i);
                labels[i][j].getStyleClass().add("labels");
                labelHolidays[i][j].getStyleClass().add("labelHoliday");
            }
        }
        days[0][0].setText("日");
        days[0][1].setText("一");
        days[0][2].setText("二");
        days[0][3].setText("三");
        days[0][4].setText("四");
        days[0][5].setText("五");
        days[0][6].setText("六");

        nowadaysYear = date.getYear();
        nowadaysMonth = date.getMonth();

        List<CalendarDate> calendarDateList = DateUtil.getDaysInMonth(date);
        int row = 1, col = 0;
        int endRow = 0;
        int startCol = 0;
        int endCol = 0;
        for (int i = 0; i < calendarDateList.size(); i++) {

            CalendarDate calendarDate = calendarDateList.get(i);
            int day = calendarDate.getDay();
            int week = calendarDate.getDayOfWeek();

            if (week != 7 && week != -1) {
                col = week;
            } else if (week == 7) col = 0;
            if (i == 0) {
                startCol = col;
            }

            writeButtonText(calendarDate, days[row][col], labels[row][col], labelHolidays[row][col]);
            String dateString = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDay();
            List<Item> itemList = schedule.getTheListOfItem(dateString);
            days[row][col].setOnAction(event -> {
                schedule.updateStatus();
                paintSchedule(itemList,dateString);
            });

            if (date.getMonth() == DateUtil.getToday().getMonth()
                    && date.getYear() == DateUtil.getToday().getYear()
                    && day == DateUtil.getToday().getDay()) {
                days[row][col].getStyleClass().add("today");
            }

            if (itemList != null && itemList.size() != 0) {
                days[row][col].setStyle("-fx-text-fill: #ff00ff;");
            }

            if (i == calendarDateList.size() - 1) {
                endRow = row;
                endCol = col;
            }
            if (week == 6) {
                row++;
            }


        }

        int nextMonth = nowadaysMonth + 1;
        int nextMonthYear = nowadaysYear;
        if (nextMonth == 13) {
            nextMonth = 1;
            nextMonthYear += 1;
        }

        int lastMonth = nowadaysMonth - 1;
        int lastMonthYear = nowadaysYear;
        if (lastMonth == 0) {
            lastMonth = 12;
            lastMonthYear -= 1;
        }

        CalendarDate lastMonthDate = new CalendarDate(lastMonthYear, lastMonth, 1);
        List<CalendarDate> lastMonthList = DateUtil.getDaysInMonth(lastMonthDate);
        int daysNum = lastMonthList.size();
        int j = 0;
        for (int i = startCol - 1; i >= 0; i--) {
            CalendarDate temp = lastMonthList.get(daysNum - j - 1);
            j += 1;
            writeButtonText(temp, days[1][i], labels[1][i], labelHolidays[1][i]);
            days[1][i].getStyleClass().add("otherMonth");
            labels[1][i].getStyleClass().add("otherMonth");
            labelHolidays[1][i].getStyleClass().add("otherMonth");
            days[1][i].setOnAction(event -> {
                try {
                    paintDays(lastMonthDate);
                } catch (IsFormatException e) {
                    e.printStackTrace();
                } catch (ItemCreateException e) {
                    e.printStackTrace();
                }
            });
        }

        CalendarDate nextMonthDate = new CalendarDate(nextMonthYear, nextMonth, 1);
        List<CalendarDate> nextMonthList = DateUtil.getDaysInMonth(nextMonthDate);
        daysNum = nextMonthList.size();
        for (int i = 0; i < daysNum; i++) {
            CalendarDate temp = nextMonthList.get(i);
            endCol += 1;
            if (endCol == 7) {
                endCol = 0;
                endRow += 1;
            }
            if (endRow == 7) {
                break;
            }
            writeButtonText(temp, days[endRow][endCol], labels[endRow][endCol], labelHolidays[endRow][endCol]);
            days[endRow][endCol].getStyleClass().add("otherMonth");
            labels[endRow][endCol].getStyleClass().add("otherMonth");
            labelHolidays[endRow][endCol].getStyleClass().add("otherMonth");
            days[endRow][endCol].setOnAction(event -> {
                try {
                    paintDays(nextMonthDate);
                } catch (IsFormatException e) {
                    e.printStackTrace();
                } catch (ItemCreateException e) {
                    e.printStackTrace();
                }
            });

        }
        yearChoiceBox.getSelectionModel().select(nowadaysYear - 1800);
        monthChoiceBox.getSelectionModel().select(nowadaysMonth - 1);
    }

    private void writeButtonText(CalendarDate calendarDate, Button button, Label label, Label labelHoliday) {

        int isHolidayNum = DateUtil.isHoliday(calendarDate);
        button.setText("" + calendarDate.getDay());
        if (isHolidayNum == 1 || isHolidayNum == 2) {

            String holiday = DateUtil.getHolidayName(calendarDate);
            label.setText("休");
            label.getStyleClass().add("rest");
            labelHoliday.setText(holiday);

        } else if (isHolidayNum == 3) {

            label.setText("休");
            label.getStyleClass().add("rest");

        } else if (isHolidayNum == 4) {

            label.setText("班");
            label.getStyleClass().add("work");

        }

    }

    private void paintSchedule(List<Item> itemList) {
        try {
            Stage stage = new Stage();
            new ScheduleLayoutController().start(stage, itemList, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintSchedule(List<Item> itemList, String dateString) {
        try {
            Stage stage = new Stage();
            new ScheduleLayoutController(dateString).start(stage, itemList, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintSchedule(List<Item> itemList, String startTimeString, String endTimeString) {
        try {
            Stage stage = new Stage();
            new ScheduleLayoutController(startTimeString, endTimeString).start(stage, itemList, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void meetingItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException, ItemCreateException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startTimeAddTextFieldMeeting.getText();
        String end = endTimeAddTextFieldMeeting.getText();
        String place = placeToAddByTimeTextFieldMeeting.getText();
        String title = titleToAddByTimeTextFieldMeeting.getText();
        String content = contentAddByTimeTextFieldMeeting.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Meeting item = null;
        try {
            item = new Meeting(start, end, title, content, place, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startTimeAddTextFieldMeeting.clear();
            endTimeAddTextFieldMeeting.clear();
            placeToAddByTimeTextFieldMeeting.clear();
            titleToAddByTimeTextFieldMeeting.clear();
            contentAddByTimeTextFieldMeeting.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }
    }

    public void appointmentAddItemByTime(ActionEvent actionEvent) throws ItemCreateException, IOException, IsFormatException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startTimeAddTextFieldAppointment.getText();
        String end = endTimeAddTextFieldAppointment.getText();
        String place = placeToAddByTimeTextFieldAppointment.getText();
        String person = personToAddByTimeTextFieldAppointment.getText();
        String content = contentAddByTimeTextFieldAppointment.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Appointment item = null;
        try {
            item = new Appointment(start, end, person, place, content, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startTimeAddTextFieldAppointment.clear();
            endTimeAddTextFieldAppointment.clear();
            placeToAddByTimeTextFieldAppointment.clear();
            personToAddByTimeTextFieldAppointment.clear();
            contentAddByTimeTextFieldAppointment.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }
    }

    public void anniversaryAddItemByTime(ActionEvent actionEvent) throws ItemCreateException, IsFormatException, IOException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String date = dayOfAnniversary.getText();
        String typeOfIt = typeOfAnniversary.getText();
        String name = nameOfAnniversary.getText();
        String description = descriptionOfAnniversary.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Anniversary item = null;
        try {
            item = new Anniversary(date, name, typeOfIt, description, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            dayOfAnniversary.clear();
            typeOfAnniversary.clear();
            nameOfAnniversary.clear();
            descriptionOfAnniversary.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }
    }

    public void courseAddItemByTime(ActionEvent actionEvent) throws ItemCreateException, IOException, IsFormatException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startTimeOfCourse.getText();
        String end = endTimeOfCourse.getText();
        String name = nameOfCourse.getText();
        String place = placeOfCourse.getText();
        String duration = lastTimeOfCourse.getText();
        String teacher = teacherOfCourse.getText();
        String remark = remarksOfCourse.getText();
        String week = repeatWeek.getSelectionModel().getSelectedItem().toString();
        String content = contentOfCourse.getText();
        String startDate = startDateOfCourse.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Course item = null;
        try {
            item = new Course(name, start, end, startDate, content, place, duration, teacher, remark, week, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }  catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startTimeOfCourse.clear();
            endTimeOfCourse.clear();
            nameOfCourse.clear();
            placeOfCourse.clear();
            lastTimeOfCourse.clear();
            teacherOfCourse.clear();
            remarksOfCourse.clear();
            repeatWeek.setValue("选择");
            contentOfCourse.clear();
            startDateOfCourse.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }
    }

    public void interviewAddItemByTime(ActionEvent actionEvent) throws ItemCreateException, IOException, IsFormatException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startDayOfInterview.getText();
        String end = endDayOfInterview.getText();
        String company = companyOfInterview.getText();
        String place = placeOfInterview.getText();
        String position = positionOfInterview.getText();
        String remark = remarksOfInterview.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Interview item = null;
        try {
            item = new Interview(start, end, place, company, position, remark, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startDayOfInterview.clear();
            endDayOfInterview.clear();
            companyOfInterview.clear();
            placeOfInterview.clear();
            positionOfInterview.clear();
            remarksOfInterview.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }

    }

    public void tripAddItemByTime(ActionEvent actionEvent) throws ItemCreateException, IOException, IsFormatException {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startTimeAddTextFieldTravel.getText();
        String end = endTimeAddTextFieldTravel.getText();
        String transports = typeOfTransportsToAddFiled.getSelectionModel().getSelectedItem().toString();
        String place = placeToAddByTimeTextFieldTravel.getText();
        String num = numberOfTransportsToAddByTimeTextFieldTravel.getText();
        String remark = remarksToAddByTimeTextFieldTravel.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Trip item = null;
        try {
            item = new Trip(start, end, transports, place, num, remark, urgent, null);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startTimeAddTextFieldTravel.clear();
            endTimeAddTextFieldTravel.clear();
            typeOfTransportsToAddFiled.setValue("选择方式");
            placeToAddByTimeTextFieldTravel.clear();
            numberOfTransportsToAddByTimeTextFieldTravel.clear();
            remarksToAddByTimeTextFieldTravel.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }
    }

    public void othersAddItemByTime(ActionEvent actionEvent) throws Exception {
        String urgent = importanceOfSchedule.getSelectionModel().getSelectedItem().toString();
        String start = startTimeAddTextFieldOthers.getText();
        String end = endTimeAddTextFieldOthers.getText();
        String content = contentAddByTimeTextFieldOthers.getText();
        String remindMinutes = timeOfRemind.getSelectionModel().getSelectedItem().toString();
        String everyRemind = remindAgain.getSelectionModel().getSelectedItem().toString();
        String way = wayOfRemind.getSelectionModel().getSelectedItem().toString();
        int minutes = changeForm(remindMinutes);
        int every = changeForm(everyRemind);
        Other item = null;
        try {
            if (start.equals("") && end.equals("")){
                item = new Other(content, urgent, null);
                item.setMinutes(minutes);
                item.setRemindAgain(every);
                item.setRemindType(way);
                item.setEarliestReminder(minutes);
            } else if ((!start.equals("")) && (!end.equals(""))){
                item = new Other(start, end, content, urgent, null);
                item.setMinutes(minutes);
                item.setRemindAgain(every);
                item.setRemindType(way);
                item.setEarliestReminder(minutes);
            } else if (start.equals("") || end.equals("")){
                JOptionPane.showMessageDialog(null,"输入不完整，请输入完全", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        String str = schedule.addItem(item);
        if (str.equals("添加的待办事项不能为空")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("时间有重复！！")) {
            JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (str.equals("")) {
            parentItem = item;
            importanceOfSchedule.setValue("不紧急 & 不重要");
            startTimeAddTextFieldOthers.clear();
            endTimeAddTextFieldOthers.clear();
            contentAddByTimeTextFieldOthers.clear();
            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            schedule.save();
            paintDays(new CalendarDate(nowadaysYear, nowadaysMonth, 1));
            addVbox.getChildren().clear();
            typeOfThingToAddFiled.setValue("选择类型");
        }

    }

    public void typeChoose() {
        String choice = typeOfThingToAddFiled.getSelectionModel().getSelectedItem().toString();
        addVbox.getChildren().clear();
        if (choice.equals("会议")) {
            addVbox.getChildren().add(meetingHbox2);
        } else if (choice.equals("约会")) {
            addVbox.getChildren().add(appointmentHbox2);
        } else if (choice.equals("旅程")) {
            addVbox.getChildren().add(travelHbox2);
        } else if (choice.equals("纪念日")) {
            addVbox.getChildren().add(anniversaryHbox);
        } else if (choice.equals("面试")) {
            addVbox.getChildren().add(interviewHbox);
        } else if (choice.equals("课程")) {
            addVbox.getChildren().add(courseHbox1);
            addVbox.getChildren().add(courseHbox2);
        } else {
            addVbox.getChildren().add(othersHbox2);
        }
    }

    public int changeForm(String minutes) {
        int result = 0;
        if (minutes.equals("1小时")) {
            result = 60;
        } else if (minutes.equals("1分钟")) {
            result = 1;
        } else if (minutes.equals("5小时")) {
            result = 300;
        } else if (minutes.equals("一天")) {
            result = 1440;
        } else if (minutes.equals("5分钟")) {
            result = 5;
        } else if (minutes.equals("10分钟")) {
            result = 10;
        } else if (minutes.equals("20分钟")) {
            result = 20;
        } else if (minutes.equals("30分钟")) {
            result = 30;
        } else if (minutes.equals("2小时")) {
            result = 120;
        } else if (minutes.equals("4小时")) {
            result = 240;
        } else if (minutes.equals("6小时")) {
            result = 360;
        } else if (minutes.equals("2天")) {
            result = 2880;
        } else if (minutes.equals("3天")) {
            result = 4320;
        }
        return result;
    }

    int getNowadaysYear() {
        return nowadaysYear;
    }

    int getNowadaysMonth() {
        return nowadaysMonth;
    }

    Schedule getSchedule() {
        return schedule;
    }

    Item getParentItem() {
        return parentItem;
    }

    void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
