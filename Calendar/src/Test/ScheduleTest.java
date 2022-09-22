package Test;

import Logic.CalendarDate;
import Logic.DateUtil;
import Logic.Item;
import Logic.Schedule;
import Logic.instance.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Exception.*;

import static org.junit.Assert.*;

public class ScheduleTest {

    @Before
    public void setUp() throws Exception {
        File f = new File("src/Logic/schedule.txt");
        new FileWriter(f);
        System.out.println("Schedule part tests begin! Good luck!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Schedule part tests end! Are you satisfied?");
    }

    @Test
    public void testAddItemSuccess() throws ItemCreateException, IsFormatException {
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Appointment appointment = new Appointment("2018-6-20 18:00", "2018-6-20 20:00", "Mary",
                "上海饭店", "和Mary一起吃晚饭", "不紧急 & 重要", null);
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "15", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        Interview interview = new Interview("2018-6-12 13:00", "2018-6-12 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other = new Other("测试", "紧急 & 重要", null);
        Other other1 = new Other("2018-6-6 12:00", "2018-6-6 16:00", "测试", "不紧急 & 不重要", null);
        Trip trip = new Trip("2018-7-20 12:00", "2018-8-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);

        Schedule schedule = new Schedule();
        assertEquals("", schedule.addItem(anniversary));
        assertEquals("", schedule.addItem(appointment));
        assertEquals("", schedule.addItem(course));
        assertEquals("", schedule.addItem(interview));
        assertEquals("", schedule.addItem(meeting));
        assertEquals("", schedule.addItem(other));
        assertEquals("", schedule.addItem(other1));
        assertEquals("", schedule.addItem(trip));
        assertEquals(8, schedule.getItems().size());
    }

    @Test
    public void testAddItemWithOverlap() throws IsFormatException, ItemCreateException {
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-21",
                "艺术史", "邯郸校区三教", "5", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);

        Interview interview = new Interview("2018-5-28 18:00", "2018-5-28 19:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);

        Trip trip = new Trip("2018-5-20 12:00", "2018-5-25 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);

        Interview interviewOverlap = new Interview("2018-5-28 18:20", "2018-5-28 18:45", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);

        Other other = new Other("测试", "紧急 & 重要", null);

        Schedule schedule = new Schedule();
        assertEquals("", schedule.addItem(course));
        assertNotEquals("", schedule.addItem(interview));
        assertNotEquals("", schedule.addItem(trip));
        assertEquals("", schedule.addItem(other));
        assertEquals("", schedule.addItem(other));
        assertEquals(3, schedule.getItems().size());

        assertTrue(schedule.deleteItem(course));

        Meeting meeting = new Meeting("2018-5-28 12:00", "2018-5-28 21:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(interview));
        assertEquals(3, schedule.getItems().size());
        assertNotEquals("", schedule.addItem(meeting));
        assertEquals(3, schedule.getItems().size());

        Schedule schedule1 = new Schedule();
        assertEquals("", schedule1.addItem(interview));
        assertNotEquals("", schedule1.addItem(interviewOverlap));
        assertEquals("", schedule1.addItem(other));
        assertEquals(2, schedule1.getItems().size());
    }

    @Test
    public void testDelete() throws IsFormatException, ItemCreateException {
        Schedule schedule = new Schedule();
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);

        assertEquals("", schedule.addItem(anniversary));

        Other other = new Other("2018-5-20 13:15", "2018-5-20 14:00", "测试", "不紧急 & 不重要", null);
        Interview interview = new Interview("2018-5-20 13:00", "2018-5-20 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "不紧急 & 重要", null);
        Meeting meeting = new Meeting("2018-5-20 16:00", "2018-5-20 17:30", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);

        Trip trip = new Trip("2018-7-20 12:00", "2018-8-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);

        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));
        assertTrue(anniversary.addSubItem(interview));
        assertEquals("", schedule.addItem(interview));
        assertTrue(anniversary.addSubItem(meeting));
        assertEquals("", schedule.addItem(meeting));
        assertEquals(4, schedule.getItems().size());

        assertEquals("", schedule.addItem(trip));
        assertTrue(schedule.deleteItem(trip));
        assertEquals(4, schedule.getItems().size());

        assertFalse(schedule.deleteItem(anniversary));
        assertEquals(4, schedule.getItems().size());
        assertEquals(3, anniversary.getSubItems().size());

        assertTrue(schedule.deleteItem(interview));
        assertEquals(2, anniversary.getSubItems().size());
        assertEquals(3, schedule.getItems().size());

        assertFalse(schedule.deleteItem(anniversary));
        assertEquals(3, schedule.getItems().size());
        assertEquals(2, anniversary.getSubItems().size());

        assertTrue(schedule.deleteItem(meeting));
        assertEquals(1, anniversary.getSubItems().size());
        assertEquals(2, schedule.getItems().size());

        assertFalse(schedule.deleteItem(anniversary));
        assertEquals(1, anniversary.getSubItems().size());
        assertEquals(2, schedule.getItems().size());

        assertEquals("", schedule.addItem(trip));

        assertTrue(schedule.deleteItem(other));
        assertEquals(2, schedule.getItems().size());
        assertEquals(0, anniversary.getSubItems().size());
        assertTrue(schedule.deleteItem(anniversary));
        assertEquals(1, schedule.getItems().size());

        assertTrue(schedule.deleteItem(trip));
    }

    @Test
    public void testSearch() throws IsFormatException, ItemCreateException {
        Schedule schedule = new Schedule();
        Anniversary anniversary = new Anniversary("2017-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "30", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        Other other = new Other("2018-7-2 1:00", "2018-7-2 12:00", "测试", "紧急 & 重要", null);
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Trip trip = new Trip("2018-7-18 12:00", "2018-7-21 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        Appointment appointment = new Appointment("2018-6-20 18:00", "2018-6-20 20:00", "Mary",
                "上海饭店", "和Mary一起吃晚饭", "不紧急 & 重要", null);
        Interview interview = new Interview("2018-6-12 13:00", "2018-6-12 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);

        assertEquals("", schedule.addItem(anniversary));
        assertEquals("", schedule.addItem(course));
        assertEquals("", schedule.addItem(other));
        assertEquals("", schedule.addItem(meeting));
        assertEquals("", schedule.addItem(trip));
        assertEquals("", schedule.addItem(appointment));
        assertEquals("", schedule.addItem(interview));

        assertEquals(1, schedule.getTheListOfItem("2018-3-5").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-11").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-11 12:00", "2018-6-11 20:10").size());
        assertEquals(2, schedule.getTheListOfItem("2018-7-2").size());
        assertEquals(2, schedule.getTheListOfItem("2018-7-2 9:00", "2018-7-2 19:00").size());

        assertEquals(1, schedule.getTheListOfItem("2018-5-20").size());
        assertEquals(1, schedule.getTheListOfItem("2019-5-20").size());
        assertEquals(1, schedule.getTheListOfItem("2020-5-20").size());
        assertEquals(1, schedule.getTheListOfItem("2020-5-20 18:00", "2020-5-20 22:00").size());

        assertEquals(1, schedule.getTheListOfItem("2018-7-20").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-12 13:00", "2018-6-12 14:00").size());
        assertEquals(3, schedule.getTheListOfItem("2018-6-1 12:00", "2018-6-12 13:30").size());
    }

    @Test
    public void testSearchWithStatus() throws ItemCreateException, IsFormatException {
        Other other = new Other("测试", "不紧急 & 不重要", null);
        Schedule schedule = new Schedule();
        schedule.addItem(other);

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date date1 = calendar.getTime();
        String tomorrow = df.format(date1);

        assertEquals(1, schedule.getTheListOfItem(now).size());
        assertEquals(1, schedule.getTheListOfItem(now + " 12:00", now + " 12:30").size());
        assertEquals(0, schedule.getTheListOfItem(tomorrow).size());
        assertEquals(0, schedule.getTheListOfItem(tomorrow + " 12:00", tomorrow + " 12:30").size());

        schedule.itemFinished(other);
        assertTrue(other.getHasTime());
    }

    @Test
    public void testSearchSubItem() throws IsFormatException, ItemCreateException {
        Schedule schedule = new Schedule();
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "23", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);

        assertEquals("", schedule.addItem(anniversary));
        assertEquals("", schedule.addItem(course));

        Other other = new Other("2018-5-20 1:00", "2018-5-20 12:00", "测试", "紧急 & 重要", null);
        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));

        Other other1 = new Other("2018-5-28 19:00", "2018-5-28 20:00", "测试", "紧急 & 不重要", null);
        assertTrue(course.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Meeting other2 = new Meeting("2018-6-1 12:00", "2018-6-1 13:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Meeting other3 = new Meeting("2018-6-1 13:01", "2018-6-1 14:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);

        assertEquals("", schedule.addItem(meeting));
        assertTrue(meeting.addSubItem(other2));
        assertEquals("", schedule.addItem(other2));
        assertTrue(meeting.addSubItem(other3));
        assertEquals("", schedule.addItem(other3));

        assertEquals(2, schedule.getTheListOfItem("2018-5-20").size());
        assertEquals(2, schedule.getTheListOfItem("2018-5-20 11:00", "2018-5-20 19:00").size());
        assertEquals(1, schedule.getTheListOfItem("2019-5-20").size());
        assertEquals(1, schedule.getTheListOfItem("2019-5-20 11:00", "2019-5-20 19:00").size());

        assertEquals(2, schedule.getTheListOfItem("2018-5-28").size());
        assertEquals(2, schedule.getTheListOfItem("2018-5-28 11:00", "2018-5-28 19:45").size());
        assertEquals(1, schedule.getTheListOfItem("2018-7-2").size());
        assertEquals(1, schedule.getTheListOfItem("2018-7-2 18:30", "2018-7-2 19:00").size());

        assertEquals(2, schedule.getTheListOfItem("2018-6-1 13:01", "2018-6-1 14:00").size());
        assertEquals(3, schedule.getTheListOfItem("2018-6-1 12:01", "2018-6-1 15:0").size());
        assertEquals(3, schedule.getTheListOfItem("2018-6-1").size());
    }

    @Test
    public void testSearchFailure() throws IsFormatException, ItemCreateException {
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);

        Schedule schedule = new Schedule();
        schedule.addItem(meeting);

        assertNull(schedule.getTheListOfItem("2018-6-1 12:00", "2018-5-31 15:05"));
    }

    @Test
    public void testItemFinished() throws IsFormatException, ItemCreateException {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date date1 = calendar.getTime();
        String tomorrow = df.format(date1);

        Schedule schedule = new Schedule();

        Interview interview = new Interview(tomorrow + " 13:00", tomorrow + " 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);
        schedule.addItem(interview);
        assertEquals(Item.Status.NOSTART, interview.getStatus());
        assertFalse(schedule.itemFinished(interview));

        Meeting meeting = new Meeting(now + " 00:00", now + " 23:59", "会议",
                "...", "...", "不紧急 & 不重要", null);
        schedule.addItem(meeting);
        assertEquals(Item.Status.UNDERWAY, meeting.getStatus());
        assertTrue(schedule.itemFinished(meeting));
        assertEquals(Item.Status.FINISHED, meeting.getStatus());
        assertFalse(schedule.itemFinished(meeting));

        Trip trip = new Trip("2016-7-23 12:00", "2016-8-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        schedule.addItem(trip);
        assertEquals(Item.Status.OVERDUE, trip.getStatus());
        assertFalse(schedule.itemFinished(trip));
    }

    @Test
    public void testLoadAndSave() throws IsFormatException, ItemCreateException, IOException {
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Appointment appointment = new Appointment("2018-6-20 18:00", "2018-6-20 20:00", "Mary",
                "上海饭店", "和Mary一起吃晚饭", "不紧急 & 重要", null);
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "15", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        Interview interview = new Interview("2018-6-12 13:00", "2018-6-12 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other = new Other("测试", "紧急 & 重要", null);
        Other other1 = new Other("2018-6-6 12:00", "2018-6-6 16:00", "测试", "不紧急 & 不重要", null);
        Trip trip = new Trip("2018-7-20 12:00", "2018-8-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);

        Schedule schedule1 = new Schedule();
        schedule1.addItem(anniversary);
        schedule1.addItem(appointment);
        schedule1.addItem(course);
        schedule1.addItem(interview);
        schedule1.addItem(meeting);
        schedule1.addItem(other);
        schedule1.addItem(other1);
        schedule1.addItem(trip);
        assertEquals(8, schedule1.getItems().size());

        schedule1.save();

        Schedule schedule2 = new Schedule();
        assertEquals(8, schedule2.getItems().size());

        schedule2.save();

        Schedule schedule3 = new Schedule();
        assertEquals(8, schedule3.getItems().size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testLoadAndSaveWithSubItems() throws IsFormatException, ItemCreateException, IOException {
        Schedule schedule = new Schedule();
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Other other = new Other("2018-5-20 13:15", "2018-5-20 14:00", "测试", "不紧急 & 不重要", null);
        Interview interview = new Interview("2018-5-20 13:00", "2018-5-20 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "不紧急 & 重要", null);
        Meeting meeting = new Meeting("2018-5-20 16:00", "2018-5-20 17:30", "儿童节会议",
                "...", "...", "不紧急 & 重要", null);

        Trip trip = new Trip("2018-7-20 12:00", "2018-7-22 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);

        assertEquals("", schedule.addItem(anniversary));
        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));
        assertTrue(anniversary.addSubItem(interview));
        assertEquals("", schedule.addItem(interview));
        assertTrue(anniversary.addSubItem(meeting));
        assertEquals("", schedule.addItem(meeting));

        assertEquals("", schedule.addItem(trip));
        assertEquals(5, schedule.getItems().size());

        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "23", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(course));

        Other other1 = new Other("2018-5-28 19:00", "2018-5-28 20:00", "测试", "紧急 & 不重要", null);
        assertTrue(course.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        Meeting meeting1 = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other2 = new Other("2018-6-1 12:00", "2018-6-1 13:00", "测试", "紧急 & 不重要", null);
        Other other3 = new Other("2018-6-1 13:01", "2018-6-1 14:00", "测试", "紧急 & 不重要", null);

        assertEquals("", schedule.addItem(meeting1));
        assertTrue(meeting1.addSubItem(other2));
        assertEquals("", schedule.addItem(other2));
        assertTrue(meeting1.addSubItem(other3));
        assertEquals("", schedule.addItem(other3));

        assertEquals(10, schedule.getItems().size());

        schedule.save();

        Schedule schedule1 = new Schedule();
        assertEquals(10, schedule1.getItems().size());
        assertEquals(3, anniversary.getSubItems().size());
        assertEquals(1, course.getSubItems().size());
        assertEquals(0, trip.getSubItems().size());
        assertEquals(2, meeting1.getSubItems().size());

        schedule1.save();

        Schedule schedule2 = new Schedule();
        assertEquals(10, schedule2.getItems().size());
        assertEquals(3, anniversary.getSubItems().size());
        assertEquals(1, course.getSubItems().size());
        assertEquals(0, trip.getSubItems().size());
        assertEquals(2, meeting1.getSubItems().size());

        schedule2.save();

        Schedule schedule3 = new Schedule();
        assertEquals(10, schedule3.getItems().size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testLoadAndSaveWithSameSubItems() throws ItemCreateException, IsFormatException, IOException {
        Schedule schedule = new Schedule();
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Other other = new Other("2018-5-20 13:15", "2018-5-20 14:00", "测试", "不紧急 & 不重要", null);
        schedule.addItem(anniversary);
        anniversary.addSubItem(other);
        schedule.addItem(other);
        anniversary.addSubItem(other);
        schedule.addItem(other);
        anniversary.addSubItem(other);
        schedule.addItem(other);

        other = new Other("2018-5-20 13:25", "2018-5-20 14:00", "测试", "不紧急 & 不重要", null);
        anniversary.addSubItem(other);
        schedule.addItem(other);

        schedule.save();

        Schedule schedule1 = new Schedule();
        assertEquals(5, schedule1.getItems().size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testSaveWithStatus() throws IsFormatException, ItemCreateException, IOException {
        Schedule schedule = new Schedule();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

        Meeting meeting = new Meeting(now, "2222-2-2 2:2", "...",
                "...", "...", "不紧急 & 不重要", null);
        assertEquals("", schedule.addItem(meeting));

        assertEquals(Item.Status.UNDERWAY, meeting.getStatus());
        assertTrue(schedule.itemFinished(meeting));
        assertEquals(Item.Status.FINISHED, meeting.getStatus());

        schedule.save();

        Schedule schedule1 = new Schedule();
        assertEquals(1, schedule1.getItems().size());
        assertEquals(1, schedule1.getTheListOfItem("2222-2-2").size());
        assertEquals(Item.Status.FINISHED, schedule1.getTheListOfItem("2222-2-2").get(0).getStatus());

        assertTrue(schedule1.deleteItem(schedule1.getTheListOfItem("2221-2-1").get(0)));

        Meeting meeting1 = new Meeting("2017-4-5 12:00", now, "...",
                "...", "...", "紧急 & 不重要", null);
        assertEquals("", schedule1.addItem(meeting1));

        assertEquals(Item.Status.UNDERWAY, meeting1.getStatus());
        assertTrue(schedule1.itemFinished(meeting1));
        assertEquals(Item.Status.FINISHED, meeting1.getStatus());

        schedule1.save();

        Schedule schedule2 = new Schedule();
        assertEquals(1, schedule2.getItems().size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testAddDeleteSearch() throws ItemCreateException, IsFormatException {
        Schedule schedule = new Schedule();

        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        assertEquals("", schedule.addItem(anniversary));
        Other other = new Other("2018-5-20 1:00", "2018-5-20 12:00", "测试", "紧急 & 重要", null);
        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));

        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "23", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(course));
        Other other1 = new Other("2018-5-28 19:00", "2018-5-28 20:00", "测试", "紧急 & 不重要", null);
        assertTrue(course.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other2 = new Other("2018-6-1 12:00", "2018-6-1 13:00", "测试", "紧急 & 不重要", null);
        Other other3 = new Other("2018-6-1 13:01", "2018-6-1 14:00", "测试", "紧急 & 不重要", null);
        assertEquals("", schedule.addItem(meeting));
        assertTrue(meeting.addSubItem(other2));
        assertEquals("", schedule.addItem(other2));
        assertTrue(meeting.addSubItem(other3));
        assertEquals("", schedule.addItem(other3));

        Trip trip = new Trip("2018-6-2 11:00", "2018-6-4 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(trip));

        assertEquals(8, schedule.getItems().size());

        Trip trip1 = new Trip("2018-6-1 11:00", "2018-6-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        assertNotEquals("", schedule.addItem(trip1));
        assertEquals(8, schedule.getItems().size());

        assertEquals(2, schedule.getTheListOfItem("2018-5-28 19:00", "2018-5-28 20:00").size());
        assertEquals(4, schedule.getTheListOfItem("2018-6-1 1:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-1 19:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(3, schedule.getTheListOfItem("2018-6-1").size());

        assertTrue(schedule.deleteItem(other));
        assertTrue(schedule.deleteItem(other1));
        assertTrue(schedule.deleteItem(course));
        assertTrue(schedule.deleteItem(other2));
        assertFalse(schedule.deleteItem(meeting));
        assertTrue(schedule.deleteItem(trip));
        assertEquals(3, schedule.getItems().size());

        assertEquals(0, schedule.getTheListOfItem("2018-6-3").size());
        assertEquals(0, schedule.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(1, schedule.getTheListOfItem("2018-5-20 18:00", "2018-5-20 19:30").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-1 12:00", "2018-6-1 13:00").size());
        assertEquals(2, schedule.getTheListOfItem("2018-6-1 13:00", "2018-6-1 14:00").size());
        assertEquals(2, schedule.getTheListOfItem("2018-6-1").size());

        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));
        assertEquals("", schedule.addItem(course));
        assertTrue(course.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));
        assertTrue(meeting.addSubItem(other2));
        assertEquals("", schedule.addItem(other2));
        assertEquals("", schedule.addItem(trip));
        assertEquals(8, schedule.getItems().size());

        assertEquals(2, schedule.getTheListOfItem("2018-5-28 19:00", "2018-5-28 20:00").size());
        assertEquals(4, schedule.getTheListOfItem("2018-6-1 1:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-1 19:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(3, schedule.getTheListOfItem("2018-6-1").size());
    }

    @Test
    public void testAddDeleteSearchWithSave() throws ItemCreateException, IsFormatException, IOException {
        Schedule schedule = new Schedule();

        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        assertEquals("", schedule.addItem(anniversary));
        Other other = new Other("2018-5-20 1:00", "2018-5-20 12:00", "测试", "紧急 & 重要", null);
        assertTrue(anniversary.addSubItem(other));
        assertEquals("", schedule.addItem(other));

        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "23", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(course));
        Other other1 = new Other("2018-5-28 19:00", "2018-5-28 20:00", "测试", "紧急 & 不重要", null);
        assertTrue(course.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other2 = new Other("2018-6-1 12:00", "2018-6-1 13:00", "测试", "紧急 & 不重要", null);
        Other other3 = new Other("2018-6-1 13:01", "2018-6-1 14:00", "测试", "紧急 & 不重要", null);
        assertEquals("", schedule.addItem(meeting));
        assertTrue(meeting.addSubItem(other2));
        assertEquals("", schedule.addItem(other2));
        assertTrue(meeting.addSubItem(other3));
        assertEquals("", schedule.addItem(other3));

        Trip trip = new Trip("2018-6-2 11:00", "2018-6-4 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        assertEquals("", schedule.addItem(trip));

        assertEquals(8, schedule.getItems().size());

        schedule.save();
        Schedule schedule1 = new Schedule();

        Trip trip1 = new Trip("2018-6-1 11:00", "2018-6-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        assertNotEquals("", schedule1.addItem(trip1));
        assertEquals(8, schedule1.getItems().size());

        assertEquals(2, schedule1.getTheListOfItem("2018-5-28 19:00", "2018-5-28 20:00").size());
        assertEquals(4, schedule1.getTheListOfItem("2018-6-1 1:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule1.getTheListOfItem("2018-6-1 19:00", "2018-6-2 20:00").size());
        assertEquals(1, schedule1.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(3, schedule1.getTheListOfItem("2018-6-1").size());

        schedule1.save();
        Schedule schedule2 = new Schedule();

        assertEquals(8, schedule2.getItems().size());
        Item a = schedule2.getItems().get(1);
        Item b = schedule2.getItems().get(3);
        Item c = schedule2.getItems().get(2);
        Item d = schedule2.getItems().get(5);
        Item e = schedule2.getItems().get(4);
        Item f = schedule2.getItems().get(7);
        assertTrue(schedule2.deleteItem(a));
        assertTrue(schedule2.deleteItem(b));
        assertTrue(schedule2.deleteItem(c));
        assertTrue(schedule2.deleteItem(d));
        assertFalse(schedule2.deleteItem(e));
        assertTrue(schedule2.deleteItem(f));
        assertEquals(3, schedule2.getItems().size());

        schedule2.save();
        Schedule schedule3 = new Schedule();

        assertEquals(3, schedule3.getItems().size());
        assertEquals(0, schedule3.getTheListOfItem("2018-6-3").size());
        assertEquals(0, schedule3.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(1, schedule3.getTheListOfItem("2018-5-20 18:00", "2018-5-20 19:30").size());
        assertEquals(1, schedule3.getTheListOfItem("2018-6-1 12:00", "2018-6-1 13:00").size());
        assertEquals(2, schedule3.getTheListOfItem("2018-6-1 13:00", "2018-6-1 14:00").size());
        assertEquals(2, schedule3.getTheListOfItem("2018-6-1").size());

        schedule3.save();
        Schedule schedule4 = new Schedule();

        assertEquals(3, schedule4.getItems().size());
        assertEquals(0, schedule4.getTheListOfItem("2018-6-3").size());
        assertEquals(0, schedule4.getTheListOfItem("2018-6-2 11:00", "2018-6-4 13:00").size());
        assertEquals(1, schedule4.getTheListOfItem("2018-5-20 18:00", "2018-5-20 19:30").size());
        assertEquals(1, schedule4.getTheListOfItem("2018-6-1 12:00", "2018-6-1 13:00").size());
        assertEquals(2, schedule4.getTheListOfItem("2018-6-1 13:00", "2018-6-1 14:00").size());
        assertEquals(2, schedule4.getTheListOfItem("2018-6-1").size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testNotice() throws IsFormatException, ItemCreateException, InterruptedException {
        Schedule schedule = new Schedule();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date date1 = calendar.getTime();
        String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(date1);

        Meeting meeting = new Meeting(tomorrow + " 12:30", tomorrow + " 13:25", "会议",
                "...", "...", "紧急 & 不重要", null);
        meeting.setMinutes(60);
        meeting.setRemindAgain(15);
        meeting.setRemindType("以上两者");
        assertEquals("", schedule.addItem(meeting));

        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());

        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.MINUTE, 30);
        Date date2 = calendar.getTime();
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date2);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date date3 = calendar.getTime();
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date3);

        Trip trip = new Trip(startTime, endTime, "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        trip.setMinutes(60);
        trip.setRemindAgain(2);
        trip.setRemindType("弹窗");
        assertEquals("", schedule.addItem(trip));
        schedule.updateStatus();
        assertEquals(1, schedule.notice().size());
        assertEquals(0, schedule.notice().size());

        calendar.add(Calendar.HOUR_OF_DAY, -2);
        calendar.add(Calendar.MINUTE, -45);
        Date date4 = calendar.getTime();
        String startTime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date4);
        Other other = new Other(startTime1, endTime, "测试", "不紧急 & 不重要", null);
        other.setMinutes(30);
        other.setRemindAgain(5);
        other.setRemindType("以上两者");
        assertEquals("", schedule.addItem(other));
        assertEquals(Item.Status.UNDERWAY, other.getStatus());
        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());

        Date dateNow = new Date();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MINUTE, 2);
        dateNow = calendar.getTime();
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateNow);
        Other other1 = new Other(start, tomorrow + " 11:11", "message", "紧急 & 重要", null);
        other1.setMinutes(1);
        other1.setRemindAgain(5);
        other1.setRemindType("显示框");
        assertEquals("", schedule.addItem(other1));
        assertEquals(4, schedule.getItems().size());
        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());

        TimeUnit.MINUTES.sleep(1);
        schedule.updateStatus();
        List<String> result = schedule.notice();
        assertEquals(1, result.size());
        assertEquals("显示框", result.get(0).split("/")[0]);

        List<Item> items = schedule.getItems();
        for (Item item : items) {
            item.setRemindType("不用提醒");
        }
        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());

        schedule.deleteItem(trip);
        schedule.deleteItem(other);
        schedule.deleteItem(other1);
        schedule.deleteItem(meeting);

        dateNow = new Date();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MINUTE, 25);
        dateNow = calendar.getTime();
        start = new SimpleDateFormat("HH:mm").format(dateNow);
        calendar.add(Calendar.DATE, -7);
        Date startDate = calendar.getTime();
        String startDateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        int week = new CalendarDate(startDateStr).getDayOfWeek();
        Course course = new Course("城市公共艺术", start, "23:59", startDateStr,
                "艺术史", "邯郸校区三教", "23", "汤老师",
                "七模课", week + "", "不紧急 & 重要", null);
        course.setMinutes(30);
        course.setRemindAgain(5);
        course.setRemindType("以上两者");
        schedule.addItem(course);

        schedule.updateStatus();
        result = schedule.notice();
        assertEquals(1, result.size());
        assertEquals("以上两者", result.get(0).split("/")[0]);
    }

    @Test
    public void testNoticeWithStatusChanged() throws ItemCreateException, IsFormatException, InterruptedException {
        Schedule schedule = new Schedule();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 2);
        Date date1 = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String startTime = df.format(date1);
        calendar.add(Calendar.DATE, 2);
        Date date2 = calendar.getTime();
        String endTime = df.format(date2);

        Meeting meeting = new Meeting(startTime, endTime, "会议", "...", "...", "紧急 & 不重要", null);
        meeting.setMinutes(5);
        meeting.setRemindAgain(1);
        meeting.setRemindType("以上两者");
        schedule.addItem(meeting);

        assertEquals(Item.Status.NOSTART, meeting.getStatus());
        schedule.updateStatus();
        assertEquals(1, schedule.notice().size());

        TimeUnit.MINUTES.sleep(2);
        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());
        assertEquals(Item.Status.UNDERWAY, meeting.getStatus());

        assertTrue(schedule.itemFinished(meeting));
        assertEquals(Item.Status.FINISHED, meeting.getStatus());

        schedule.updateStatus();
        assertEquals(0, schedule.notice().size());
    }

    @Test
    public void testNoticeWithSave() throws IsFormatException, ItemCreateException, IOException, InterruptedException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date date1 = calendar.getTime();
        String tomorrow = new SimpleDateFormat("MM-dd").format(date1);

        Schedule schedule = new Schedule();

        Anniversary anniversary = new Anniversary("2016-" + tomorrow, "史密斯", "结婚纪念日",
                "和简的第N个结婚纪念日", "紧急 & 重要", null);
        anniversary.setMinutes(24 * 60 + 8);
        anniversary.setRemindType("以上两种");
        anniversary.setRemindAgain(1);
        anniversary.setEarliestReminder(24 * 60 + 8);

        schedule.addItem(anniversary);
        schedule.updateStatus();
        assertEquals(1, schedule.notice().size());

        schedule.save();

        TimeUnit.SECONDS.sleep(99);

        Schedule schedule1 = new Schedule();
        assertEquals(1, schedule.getItems().size());
        assertEquals(1, schedule1.notice().size());

        File f1 = new File("src/Logic/schedule.txt");
        new FileWriter(f1);
    }

    @Test
    public void testUpdateStatusWithDelete() throws IsFormatException, ItemCreateException {
        Schedule schedule = new Schedule();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayStr = df.format(yesterday);

        calendar.add(Calendar.DATE, 2);
        Date tomorrow = calendar.getTime();
        String tomorrowStr = df.format(tomorrow);

        Other other = new Other(yesterdayStr + " 12:15", tomorrowStr + " 14:00", "测试", "不紧急 & 不重要", null);
        Interview interview = new Interview(yesterdayStr + " 13:00", tomorrowStr + " 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "不紧急 & 重要", null);
        Other other1 = new Other(yesterdayStr + " 12:20", tomorrowStr + " 12:30", "...", "不紧急 & 不重要", null);

        assertEquals("", schedule.addItem(other));
        assertTrue(other.addSubItem(interview));
        assertEquals("", schedule.addItem(interview));
        assertTrue(other.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        schedule.itemFinished(other1);
        assertEquals(Item.Status.FINISHED, other1.getStatus());
        assertEquals(2, other.getSubItems().size());
        assertEquals(3, schedule.getItems().size());

        schedule.deleteItem(interview);
        assertEquals(2, schedule.getItems().size());
        assertEquals(1, other.getSubItems().size());

        assertEquals(Item.Status.FINISHED, other.getStatus());

        assertTrue(schedule.deleteItem(other1));
        assertTrue(schedule.deleteItem(other));
        assertEquals(0, schedule.getItems().size());
    }

    @Test
    public void testUpdateParentItemStatusAfterSubItemsFinished() throws ItemCreateException, IsFormatException {
        Schedule schedule = new Schedule();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayStr = df.format(yesterday);

        calendar.add(Calendar.DATE, 2);
        Date tomorrow = calendar.getTime();
        String tomorrowStr = df.format(tomorrow);

        Other other = new Other(yesterdayStr + " 12:15", tomorrowStr + " 14:00", "测试", "不紧急 & 不重要", null);
        Interview interview = new Interview(yesterdayStr + " 13:00", tomorrowStr + " 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "不紧急 & 重要", null);
        Other other1 = new Other(yesterdayStr + " 12:20", tomorrowStr + " 12:30", "...", "不紧急 & 不重要", null);

        assertEquals("", schedule.addItem(other));
        assertTrue(other.addSubItem(interview));
        assertEquals("", schedule.addItem(interview));
        assertTrue(other.addSubItem(other1));
        assertEquals("", schedule.addItem(other1));

        schedule.itemFinished(interview);
        schedule.itemFinished(other1);

        assertEquals(Item.Status.FINISHED, other.getStatus());
    }
}
