package GUI;

import Logic.CalendarDate;
import Logic.DateUtil;
import Logic.Item;
import Logic.Schedule;
import Logic.instance.*;

import Exception.InvalidTimeOfScheduleItem;
import Exception.MessageIsNull;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import Exception.ItemCreateException;
import Exception.IsFormatException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class SubItems extends Application implements Initializable {

    Stage stage = new Stage();
    private LayoutController layoutController;
    private ScheduleLayoutController scheduleLayoutController;
    private Schedule schedule;
    private Item parentItem1;
    private Item childItem;

    @FXML
    public ComboBox<String> typeOfThingToAddFiled1;
    @FXML
    public ComboBox<String> typeOfTransportsToAddFiled;
    @FXML
    public ComboBox<String> repeatWeek;
    @FXML
    public ComboBox wayOfRemind;
    @FXML
    public ComboBox timeOfRemind;
    @FXML
    public ComboBox remindAgain;
    @FXML
    public ComboBox<String> importanceOfSchedule;
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
    public TextField startDateOfCourse;
    @FXML
    public TextField placeOfCourse;
    @FXML
    public TextField lastTimeOfCourse;
    @FXML
    public TextField teacherOfCourse;
    @FXML
    public TextField remarksOfCourse;
    @FXML
    public TextField startTimeAddTextFieldOthers;
    @FXML
    public TextField endTimeAddTextFieldOthers;
    @FXML
    public TextField contentAddByTimeTextFieldOthers;
    @FXML
    public VBox addVbox;
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

    public SubItems() throws FileNotFoundException, InvalidTimeOfScheduleItem, MessageIsNull {

    }

    public SubItems(Item parentItem, Schedule schedule, LayoutController layoutController, ScheduleLayoutController scheduleLayoutController) throws FileNotFoundException, InvalidTimeOfScheduleItem, MessageIsNull {
        this.parentItem1 = parentItem;
        this.layoutController = layoutController;
        this.scheduleLayoutController = scheduleLayoutController;
        this.schedule = schedule;
    }

    public void typeChoose1() {
        typeChoose(parentItem1);
    }

    public void meetingItemByTime(ActionEvent actionEvent) throws ItemCreateException, IOException, IsFormatException {
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
            item = new Meeting(start, end, title, content, place, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                startTimeAddTextFieldMeeting.clear();
                endTimeAddTextFieldMeeting.clear();
                placeToAddByTimeTextFieldMeeting.clear();
                titleToAddByTimeTextFieldMeeting.clear();
                contentAddByTimeTextFieldMeeting.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }
        }  else {
            JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void appointmentAddItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException {
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
            item = new Appointment(start, end, person, place, content, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                startTimeAddTextFieldAppointment.clear();
                endTimeAddTextFieldAppointment.clear();
                placeToAddByTimeTextFieldAppointment.clear();
                personToAddByTimeTextFieldAppointment.clear();
                contentAddByTimeTextFieldAppointment.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            } else {
                JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    public void anniversaryAddItemByTime(ActionEvent actionEvent) throws IsFormatException, IOException {
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
            item = new Anniversary(date, name, typeOfIt, description, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                dayOfAnniversary.clear();
                typeOfAnniversary.clear();
                nameOfAnniversary.clear();
                descriptionOfAnniversary.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }  else {
                JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }


    }

    public void courseAddItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException {
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
            item = new Course(name, start, end, startDate, content, place, duration, teacher, remark, week, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
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
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }
        }  else {
            JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void interviewAddItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException {
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
            item = new Interview(start, end, place, company, position, remark, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }catch (IsFormatException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                startDayOfInterview.clear();
                endDayOfInterview.clear();
                companyOfInterview.clear();
                placeOfInterview.clear();
                positionOfInterview.clear();
                remarksOfInterview.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }
        } else {
            JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void tripAddItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException {
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
            item = new Trip(start, end, transports, place, num, remark, urgent, parentItem1);
            item.setMinutes(minutes);
            item.setRemindAgain(every);
            item.setRemindType(way);
            item.setEarliestReminder(minutes);
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                startTimeAddTextFieldTravel.clear();
                endTimeAddTextFieldTravel.clear();
                typeOfTransportsToAddFiled.setValue("选择方式");
                placeToAddByTimeTextFieldTravel.clear();
                numberOfTransportsToAddByTimeTextFieldTravel.clear();
                remarksToAddByTimeTextFieldTravel.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }
        }  else {
            JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
        }


    }

    public void othersAddItemByTime(ActionEvent actionEvent) throws IOException, IsFormatException {
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
            }else if (start.equals("") || end.equals("")){
                JOptionPane.showMessageDialog(null,"输入不完整，请输入完全", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ItemCreateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        childItem = item;
        if (parentItem1.addSubItem(item)) {
            String str = schedule.addItem(item);
            if (str.equals("添加的待办事项不能为空")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("时间有重复！！")) {
                JOptionPane.showMessageDialog(null, str, "提示", JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("")) {
                importanceOfSchedule.setValue("不紧急 & 不重要");
                startTimeAddTextFieldOthers.clear();
                endTimeAddTextFieldOthers.clear();
                contentAddByTimeTextFieldOthers.clear();
                JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                try {
                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                    String dateString = scheduleLayoutController.getDateString();
                    if (dateString != null)
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(dateString), layoutController);
                    else {
                        String startTimeString = scheduleLayoutController.getStartTimeString();
                        String endTimeString = scheduleLayoutController.getEndTimeString();
                        scheduleLayoutController.paintSchedule(layoutController.getSchedule().getTheListOfItem(startTimeString, endTimeString), layoutController);
                    }
                } catch (IsFormatException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (ItemCreateException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                try {
                    schedule.save();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                addVbox.getChildren().clear();
                typeOfThingToAddFiled1.setValue("选择类型");
                layoutController.setSchedule(schedule);
                stage.close();
            }
        } else {
            JOptionPane.showMessageDialog(null, "当前子待办事项有误，不能添加", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void setChoice(Item parentItem) {
        String choice = parentItem.getClass().getName().split("\\.")[parentItem.getClass().getName().split("\\.").length - 1];
        ObservableList<String> results = FXCollections.observableArrayList();
        String[] result1 = parentItem.getUncontradictoryTypesItems();
        results.addAll(Arrays.asList(result1));

        if (choice.equals("Meeting")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else if (choice.equals("Appointment")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else if (choice.equals("Trip")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else if (choice.equals("Anniversary")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else if (choice.equals("Interview")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else if (choice.equals("Course")) {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        } else {
            typeOfThingToAddFiled1.setItems(results);
            typeOfThingToAddFiled1.getSelectionModel().select(0);
        }
    }

    public void typeChoose(Item parentItem) {
        String choice = typeOfThingToAddFiled1.getSelectionModel().getSelectedItem().toString();
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

    public Item getChildItem() {
        return childItem;
    }

    public int changeForm(String minutes) {
        int result = 0;
        if (minutes.equals("1小时")) {
            result = 60;
        } else if (minutes.equals("5小时")) {
            result = 300;
        }else if (minutes.equals("1分钟")) {
            result = 1;
        }
        else if (minutes.equals("一天")) {
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
            remindAgain1.add("20分钟");
            remindAgain1.add("30分钟");
            remindAgain1.add("1小时");
        } else if (remindMinutes.equals("一天")) {
            remindAgain1.add("2小时");
            remindAgain1.add("4小时");
            remindAgain1.add("6小时");
        } else if (remindMinutes.equals("一周")) {
            remindAgain1.add("1天");
            remindAgain1.add("2天");
            remindAgain1.add("3天");
        }
        remindAgain.setItems(remindAgain1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addVbox.getChildren().clear();
        setChoice(parentItem1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(this);
        fxmlLoader.setLocation(getClass().getResource("SubItems.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1600, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SubItems");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("schedule.png"))));
        stage = primaryStage;
        primaryStage.show();
    }
}
