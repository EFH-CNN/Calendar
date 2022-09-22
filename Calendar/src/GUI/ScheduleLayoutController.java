package GUI;

import Logic.CalendarDate;
import Logic.Item;
import Logic.Schedule;
import Logic.instance.Anniversary;
import Logic.instance.Course;
import Logic.instance.Other;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

import Exception.IsFormatException;
import Exception.ItemCreateException;

public class ScheduleLayoutController extends Application {

    private String dateString = null;
    private String startTimeString = null;
    private String endTimeString = null;
    private ScheduleLayoutController scheduleLayoutController = this;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn idCol;
    @FXML
    public TableColumn typeCol;
    @FXML
    public TableColumn importanceCol;
    @FXML
    public TableColumn addChildCol;
    @FXML
    public TableColumn statusCol;
    @FXML
    public TableColumn isFinishedCol;
    @FXML
    public TableColumn startTimeCol;
    @FXML
    public TableColumn endTimeCol;
    @FXML
    public TableColumn cancelRemindCol;
    @FXML
    public TableColumn messageCol;
    @FXML
    public TableColumn deleteCol;

    ScheduleLayoutController() {

    }

    ScheduleLayoutController(String dateString) {
        dateString = dateString;
        this.dateString = dateString;
    }

    ScheduleLayoutController(String startTimeString, String endTimeString) {
        this.startTimeString = startTimeString;
        this.endTimeString = endTimeString;
    }

    public void paintSchedule(List<Item> itemList, LayoutController layoutController) {
        Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ObservableList data = FXCollections.observableArrayList(itemList);
        idCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {

                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        Item item1 = this.getTableView().getItems().get(this.getIndex());
                        if (item1.getParentItem() == null) {

                            int i = 0;
                            for (int j = 0; j < this.getIndex(); j++) {
                                Item temp = this.getTableView().getItems().get(j);
                                if (temp.getParentItem() != null) {
                                    i++;
                                }
                            }

                            this.setText(String.valueOf(rowIndex - i));
                        } else {
                            this.setText("");

                        }
                    }
                }
            };
            return cell;
        });

        typeCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        String[] classMessage = this.getTableView().getItems().get(this.getIndex()).getClass().getName().split("\\.");
                        String string = classMessage[classMessage.length - 1];
                        String result = "";
                        switch (string) {
                            case "Anniversary":
                                result = "纪念日";
                                break;
                            case "Course":
                                result = "课程";
                                break;
                            case "Appointment":
                                result = "约会";
                                break;
                            case "Interview":
                                result = "面试";
                                break;
                            case "Meeting":
                                result = "会议";
                                break;
                            case "Trip":
                                result = "旅程";
                                break;
                            default:
                                result = "自定义";
                        }
                        this.setText(result);
                    }
                }
            };
            return cell;
        });

        importanceCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        String importance = Item.Priority.parsePriorityToString(this.getTableView().getItems().get(this.getIndex()).getPriority());
                        this.setText(importance);
                    }
                }
            };
            return cell;
        });

        addChildCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item parentItem = this.getTableView().getItems().get(index);
                        if (parentItem.getParentItem() == null && (Item.Status.parseStatusToString(parentItem.getStatus()).equals("进行中") ||
                                Item.Status.parseStatusToString(parentItem.getStatus()).equals("未开始")) && (!(parentItem instanceof Other && !((Other)parentItem).getHasTime()))) {
                            Button addBtn = new Button("添加");
                            this.setGraphic(addBtn);
                            addBtn.setOnMouseClicked((me) -> {
                                Schedule schedule = layoutController.getSchedule();
                                Stage stage = new Stage();
                                try {
                                    SubItems subItems = new SubItems(parentItem, schedule, layoutController, scheduleLayoutController);
                                    subItems.start(stage);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                        }
                    }
                }
            };
            return cell;
        });

        statusCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item item1 = this.getTableView().getItems().get(index);
                        String status = Item.Status.parseStatusToString(item1.getStatus());
                        this.setText(status);
                    }
                }
            };
            return cell;
        });

        isFinishedCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item item1 = this.getTableView().getItems().get(index);
                        Schedule schedule = layoutController.getSchedule();
                        if ((Item.Status.parseStatusToString(item1.getStatus()).equals("进行中")) &&
                                item1.getSubItems().size() == 0) {
                            Button finishBtn = new Button("已完成");
                            this.setGraphic(finishBtn);
                            finishBtn.setOnMouseClicked((me) -> {
                                schedule.itemFinished(item1);
                                item1.setStatus("已完成");
                                schedule.updateStatus();
                                paintSchedule(itemList, layoutController);
                                try {
                                    schedule.save();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                        }
                    }
                }
            };
            return cell;
        });

        startTimeCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item item1 = this.getTableView().getItems().get(index);
                        if (item1 instanceof Anniversary) {
                            CalendarDate startHour = ((Anniversary) item1).getStartDate();
                            int month = startHour.getMonth();
                            int day = startHour.getDay();
                            String startAnni = month + "-" + day;
                            this.setText(startAnni);
                        } else if (item1 instanceof Course) {
                            Date startHour = ((Course) item1).getStartTime();
                            Format f = new SimpleDateFormat("HH:mm");
                            String startCourse = f.format(startHour);
                            this.setText(startCourse);
                        } else {
                            this.setText(f.format(item1.getStartTime()));
                        }
                    }
                }
            };
            return cell;
        });

        endTimeCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item item1 = this.getTableView().getItems().get(index);
                        if (item1 instanceof Anniversary) {
                            this.setText(null);
                        } else if (item1 instanceof Course) {
                            Date endHour = item1.getEndTime();
                            Format f = new SimpleDateFormat("HH:mm");
                            String endCourse = f.format(endHour);
                            this.setText(endCourse);
                        } else {
                            this.setText(f.format(item1.getEndTime()));
                        }
                    }
                }
            };
            return cell;
        });

        cancelRemindCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item item1 = this.getTableView().getItems().get(index);
                        Schedule schedule = layoutController.getSchedule();
                        if (!item1.getRemindType().equals("不用提醒") && item1.getStatus() == Item.Status.NOSTART){
                            Button cancelRemind = new Button("取消提醒");
                            this.setGraphic(cancelRemind);
                            cancelRemind.setOnMouseClicked((me) -> {
                                item1.setRemindType("不用提醒");
                                paintSchedule(itemList, layoutController);
                                try {
                                    schedule.save();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                        }
                    }
                }
            };
            return cell;
        });

        messageCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        TextArea message = new TextArea();
                        message.setPrefRowCount(2);
                        message.setWrapText(true);
                        message.setEditable(false);
                        message.setText(this.getTableView().getItems().get(this.getIndex()).getMessage());
                        this.setGraphic(message);
                    }
                }
            };
            return cell;
        });


        deleteCol.setCellFactory((col) -> {
            TableCell<Item, String> cell = new TableCell<Item, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int index = this.getIndex();
                        Item del = this.getTableView().getItems().get(index);
                        if (del.getSubItems().size() == 0) {
                            Button delBtn = new Button("删除");
                            this.setGraphic(delBtn);
                            delBtn.setOnMouseClicked((me) -> {
                                Schedule schedule = layoutController.getSchedule();
                                schedule.deleteItem(del);
                                layoutController.setSchedule(schedule);
                                itemList.remove(del);
                                data.remove(index);
                                paintSchedule(itemList,layoutController);
                                try {
                                    layoutController.paintDays(new CalendarDate(layoutController.getNowadaysYear(), layoutController.getNowadaysMonth(), 1));
                                } catch (IsFormatException e) {
                                    e.printStackTrace();
                                } catch (ItemCreateException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    schedule.save();
                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, "找不到schedule.txt文件", "提示", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                        }
                    }
                }
            };
            return cell;
        });

        tableView.setItems(data);


    }

    void start(Stage stage, List<Item> itemList, LayoutController layoutController) throws Exception {
        start(stage);
        paintSchedule(itemList, layoutController);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(this);
        fxmlLoader.setLocation(getClass().getResource("ScheduleLayout.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Schedule");
        primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("schedule.png"))));
        primaryStage.show();
    }

    public String getDateString() {
        return dateString;
    }


    public String getStartTimeString() {
        return startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }
}
