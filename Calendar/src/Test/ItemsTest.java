package Test;

import Logic.CalendarDate;
import Logic.Item;
import Logic.instance.*;
import Exception.ItemCreateException;
import Exception.IsFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class ItemsTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("Items part tests begin! Good luck!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Items part tests end! Are you satisfied?");
    }

    @Test
    public void testCreateAnniversaryItemSuccess() {
        try {
            Anniversary anniversary1 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                    "和简的第20个结婚纪念日", "紧急 & 重要", null);
            Anniversary anniversary2 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                    "和简的第20个结婚纪念日", "紧急 & 重要",
                    new Appointment("2018-5-20 18:00", "2018-5-20 20:00", "简",
                            "和平饭店", "和简一起吃晚饭，度过第20个结婚纪念日", "不紧急 & 重要", null));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test(expected = ItemCreateException.class)
    public void testCreateAnniversaryItemFailure1() throws IsFormatException, ItemCreateException {
        Anniversary anniversary = new Anniversary("2018-5-33", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
    }

    @Test(expected = IsFormatException.class)
    public void testCreateAnniversaryItemFailure2() throws IsFormatException, ItemCreateException {
        Anniversary anniversary = new Anniversary("2018-5-31 12:00", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
    }

    @Test(expected = Exception.class)
    public void testCreateOtherItemFailure() throws ItemCreateException {
        Other other1 = new Other("2018-6-6 12:00", "", "测试", "不紧急 & 不重要", null);
    }

    @Test
    public void testItemEqual() throws IsFormatException, ItemCreateException {
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Meeting meeting1 = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "紧急 & 重要", null);
        assertFalse(meeting.itemEqual(meeting1));
        assertTrue(meeting.itemEqual(meeting));
        assertFalse(meeting1.itemEqual(meeting));

        Meeting meeting2 = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "紧急 & 重要", null);
        assertTrue(meeting1.itemEqual(meeting2));
        assertTrue(meeting2.itemEqual(meeting1));
        meeting2.setMinutes(60);
        meeting1.setMinutes(30);
        assertFalse(meeting1.itemEqual(meeting2));
        assertFalse(meeting2.itemEqual(meeting1));
    }

    @Test
    public void testItemEqualCombinedWithSubItem() throws IsFormatException, ItemCreateException {
        Anniversary anniversary1 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);

        Anniversary anniversary2 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Appointment appointment = new Appointment("2018-5-20 16:00", "2018-5-20 20:00", "简",
                "和平饭店", "和简一起外出，度过第20个结婚纪念日", "不紧急 & 重要", null);
        Other other = new Other("2018-5-20 1:00", "2018-5-20 12:00", "测试", "紧急 & 重要", null);
        anniversary2.addSubItem(appointment);
        anniversary2.addSubItem(other);

        Anniversary anniversary3 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);

        assertTrue(anniversary1.itemEqual(anniversary3));
        assertFalse(anniversary1.itemEqual(anniversary2));
        assertFalse(anniversary1.itemEqual(meeting));

        Anniversary anniversary4 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        anniversary4.addSubItem(other);
        anniversary4.addSubItem(appointment);
        assertTrue(anniversary2.itemEqual(anniversary4));

        Anniversary anniversary5 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        anniversary4.addSubItem(other);
        anniversary4.addSubItem(new Meeting("2018-5-20 12:00", "2018-5-20 15:0", "会议",
                "内容", "地点", "不紧急 & 不重要", null));
        assertFalse(anniversary4.itemEqual(anniversary5));

        assertTrue(anniversary2.deleteSubItem(other));
        assertFalse(anniversary2.deleteSubItem(meeting));
        assertTrue(anniversary2.deleteSubItem(appointment));
        assertTrue(anniversary1.itemEqual(anniversary2));

        Meeting meeting1 = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other1 = new Other("2018-6-1 14:0", "2018-6-1 15:0", "测试", "紧急 & 重要", null);
        assertTrue(meeting1.addSubItem(other1));
        assertTrue(meeting1.addSubItem(other1));
        assertTrue(meeting1.addSubItem(other1));
        assertTrue(meeting1.addSubItem(other1));
        assertTrue(meeting1.addSubItem(other1));
        Meeting meeting2 = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Other other2 = new Other("2018-6-1 14:0", "2018-6-1 15:0", "测试", "紧急 & 重要", null);
        assertTrue(meeting2.addSubItem(other2));
        assertTrue(meeting2.addSubItem(other2));
        assertTrue(meeting2.addSubItem(other2));
        assertTrue(meeting2.addSubItem(other2));
        assertTrue(meeting2.addSubItem(other2));

        assertTrue(meeting1.itemEqual(meeting2));
        assertTrue(meeting2.itemEqual(meeting1));
    }

    @Test
    public void testGetUncontradictoryTypesItems() throws IsFormatException, ItemCreateException {
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        assertEquals(7, anniversary.getUncontradictoryTypesItems().length);

        Appointment appointment = new Appointment("2018-6-20 18:00", "2018-6-20 20:00", "Mary",
                "上海饭店", "和Mary一起吃晚饭", "不紧急 & 重要", null);
        String[] actualStrArray = appointment.getUncontradictoryTypesItems();
        assertEquals(4, actualStrArray.length);
        assertTrue(isInStrArray("自定义", actualStrArray));
        assertTrue(isInStrArray("纪念日", actualStrArray));
        assertTrue(isInStrArray("约会", actualStrArray));
        assertTrue(isInStrArray("旅程", actualStrArray));
        assertFalse(isInStrArray("面试", actualStrArray));

        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "16", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        actualStrArray = course.getUncontradictoryTypesItems();
        assertEquals(4, actualStrArray.length);
        assertTrue(isInStrArray("自定义", actualStrArray));
        assertTrue(isInStrArray("纪念日", actualStrArray));
        assertTrue(isInStrArray("课程", actualStrArray));
        assertTrue(isInStrArray("旅程", actualStrArray));
        assertFalse(isInStrArray("约会", actualStrArray));

        Interview interview = new Interview("2018-6-12 13:00", "2018-6-12 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);
        actualStrArray = interview.getUncontradictoryTypesItems();
        assertEquals(4, actualStrArray.length);
        assertTrue(isInStrArray("自定义", actualStrArray));
        assertTrue(isInStrArray("纪念日", actualStrArray));
        assertTrue(isInStrArray("面试", actualStrArray));
        assertTrue(isInStrArray("旅程", actualStrArray));
        assertFalse(isInStrArray("课程", actualStrArray));

        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-1 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        actualStrArray = meeting.getUncontradictoryTypesItems();
        assertEquals(4, actualStrArray.length);
        assertTrue(isInStrArray("自定义", actualStrArray));
        assertTrue(isInStrArray("纪念日", actualStrArray));
        assertTrue(isInStrArray("会议", actualStrArray));
        assertTrue(isInStrArray("旅程", actualStrArray));
        assertFalse(isInStrArray("面试", actualStrArray));

        Other other = new Other("测试", "紧急 & 重要", null);
        actualStrArray = other.getUncontradictoryTypesItems();
        assertEquals(7, actualStrArray.length);

        Trip trip = new Trip("2018-7-20 12:00", "2018-8-1 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        actualStrArray = trip.getUncontradictoryTypesItems();
        assertEquals(7, actualStrArray.length);
    }

    private boolean isInStrArray(String str, String[] strArray) {
        for (String s : strArray) {
            if (str.equals(s)) {
                return true;
            }
        }

        return false;
    }

    @Test(expected = Exception.class)
    public void testCreateCourseFailure1() throws ItemCreateException, IsFormatException {
        Course course = new Course("城市公共艺术", "23:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "15", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
    }

    @Test(expected = Exception.class)
    public void testCreateCourseFailure2() throws ItemCreateException, IsFormatException {
        Course course = new Course("城市公共艺术", "28:00", "20:10", "2018-3-5",
                "艺术史", "邯郸校区三教", "15", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
    }

    @Test(expected = Exception.class)
    public void testCreateCourseFailure3() throws ItemCreateException, IsFormatException {
        Course course = new Course("城市公共艺术", "18:00", "20:10", "www-qqq-rrr",
                "艺术史", "邯郸校区三教", "15", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
    }

    @Test
    public void testCourse() throws ItemCreateException, IsFormatException {
        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-12-19",
                "艺术史", "邯郸校区三教", "3", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        List<CalendarDate> actualList = course.getDateList();
        assertEquals(3, actualList.size());

        CalendarDate calendarDate1 = new CalendarDate(2018, 12, 24);
        CalendarDate calendarDate2 = new CalendarDate(2018, 12, 31);
        CalendarDate calendarDate3 = new CalendarDate(2019, 1, 7);

        assertEquals(calendarDate1, actualList.get(0));
        assertEquals(calendarDate2, actualList.get(1));
        assertEquals(calendarDate3, actualList.get(2));

        course = new Course("城市公共艺术", "18:00", "20:10", "2018-12-19",
                "艺术史", "邯郸校区三教", "3", "汤老师",
                "七模课", "5", "不紧急 & 重要", null);
        actualList = course.getDateList();
        assertEquals(3, actualList.size());

        calendarDate1 = new CalendarDate(2018, 12, 21);
        calendarDate2 = new CalendarDate(2018, 12, 28);
        calendarDate3 = new CalendarDate(2019, 1, 4);

        assertEquals(calendarDate1, actualList.get(0));
        assertEquals(calendarDate2, actualList.get(1));
        assertEquals(calendarDate3, actualList.get(2));
    }

    @Test
    public void testAddSubItem() throws ItemCreateException, IsFormatException {
        Other other = new Other("测试", "紧急 & 重要", null);
        Interview interview = new Interview("2018-6-12 13:00", "2018-6-12 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "紧急 & 不重要", null);
        assertFalse(other.addSubItem(interview));
        assertFalse(interview.addSubItem(other));

        Interview subInterview = new Interview("2018-6-12 13:10", "2018-6-12 13:20", "软件楼第二会议室",
                "Intel", "程序员", "热身", "紧急 & 不重要", null);
        Other subOther = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertTrue(interview.addSubItem(subInterview));
        assertTrue(interview.addSubItem(subOther));
        assertFalse(subInterview.addSubItem(interview));
        assertFalse(subInterview.addSubItem(subOther));

        Interview interviewOverlapping = new Interview("2018-6-12 13:15", "2018-6-12 13:30", "软件楼第二会议室",
                "Intel", "程序员", "热身overlap", "紧急 & 不重要", null);
        assertTrue(interview.addSubItem(interviewOverlapping));

        Anniversary anniversary = new Anniversary("2016-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Other other1 = new Other("2019-5-20 12:00", "2019-5-20 14:00", "测试", "紧急 & 重要", null);
        assertTrue(anniversary.addSubItem(other1));
        Other other2 = new Other("2018-5-20 12:00", "2018-5-20 14:00", "测试", "紧急 & 重要", null);
        assertTrue(anniversary.addSubItem(other2));
        Other other3 = new Other("2019-5-21 12:00", "2019-5-21 14:00", "测试", "紧急 & 重要", null);
        assertFalse(anniversary.addSubItem(other3));

        Course course = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "3", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        Other other4 = new Other("2018-6-4 19:00", "2018-6-4 20:00", "测试", "紧急 & 重要", null);
        assertTrue(course.addSubItem(other4));
        Other other5 = new Other("2018-5-28 19:00", "2018-5-28 20:01", "测试", "紧急 & 重要", null);
        assertTrue(course.addSubItem(other5));
        Other other6 = new Other("2018-5-28 21:00", "2018-5-28 21:30", "测试", "紧急 & 重要", null);
        assertFalse(course.addSubItem(other6));

        Meeting meeting = new Meeting("2018-5-20 19:00", "2018-5-20 20:01", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Trip trip = new Trip("2018-5-20 19:00", "2018-5-20 21:30", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        assertFalse(meeting.addSubItem(trip));
        assertTrue(trip.addSubItem(meeting));

        Other other7 = new Other("2018-5-19 19:00", "2018-5-20 1:00", "测试", "紧急 & 重要", null);
        assertFalse(meeting.addSubItem(other7));
        assertFalse(other7.addSubItem(meeting));

        Anniversary anniversary1 = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Course course1 = new Course("城市公共艺术", "18:00", "20:10", "2018-5-20",
                "艺术史", "邯郸校区三教", "1", "汤老师",
                "七模课", "7", "不紧急 & 重要", null);
        assertTrue(anniversary1.addSubItem(course1));

        Course course2 = new Course("城市公共艺术", "18:00", "20:10", "2018-5-20",
                "艺术史", "邯郸校区三教", "12", "汤老师",
                "七模课", "7", "不紧急 & 重要", null);
        Other other8 = new Other("2018-5-19 19:00", "2020-5-20 1:00", "测试", "紧急 & 重要", null);
        assertTrue(other8.addSubItem(course2));

        Course course3 = new Course("城市公共艺术", "18:00", "20:10", "2018-5-28",
                "艺术史", "邯郸校区三教", "13", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        Course course4 = new Course("城市公共艺术", "18:00", "20:10", "2018-5-29",
                "艺术史", "邯郸校区三教", "11", "汤老师",
                "七模课", "2", "不紧急 & 重要", null);
        assertFalse(course3.addSubItem(course4));
        assertFalse(course4.addSubItem(course3));
        assertTrue(course3.addSubItem(course3));

        Course course5 = new Course("城市公共艺术", "18:30", "19:15", "2018-5-25",
                "艺术史", "邯郸校区三教", "11", "汤老师",
                "七模课", "1", "不紧急 & 重要", null);
        assertFalse(course5.addSubItem(course3));
        assertTrue(course3.addSubItem(course5));
        assertFalse(course5.addSubItem(course3));

        Other other9 = new Other("2018-5-19 19:00", "2018-5-20 1:00", "测试", "紧急 & 重要", null);
        Other other10 = new Other("2018-5-19 19:30", "2018-5-20 00:00", "测试", "紧急 & 重要", null);
        assertTrue(other9.addSubItem(other10));
        assertTrue(other9.addSubItem(other10));
        assertTrue(other9.addSubItem(other10));
        assertEquals(3, other9.getSubItems().size());
    }

    @Test
    public void testAddDeleteAndGetSubItem() throws IsFormatException, ItemCreateException {
        Meeting meeting = new Meeting("2018-6-1 12:00", "2018-6-10 15:0", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);
        Trip trip = new Trip("2018-6-2 12:00", "2018-6-10 13:00", "飞机",
                "北京", "CZ3992", "玩耍", "不紧急 & 重要", null);
        Other other = new Other("2018-6-6 12:00", "2018-6-6 16:00", "测试", "不紧急 & 不重要", null);

        assertTrue(meeting.addSubItem(trip));
        assertEquals(1, meeting.getSubItems().size());
        assertTrue(meeting.addSubItem(other));
        assertEquals(2, meeting.getSubItems().size());
        assertTrue(meeting.deleteSubItem(other));
        assertEquals(1, meeting.getSubItems().size());
        assertFalse(meeting.deleteSubItem(other));
        assertEquals(1, meeting.getSubItems().size());
        assertTrue(meeting.deleteSubItem(trip));
        assertFalse(meeting.deleteSubItem(trip));
        assertEquals(0, meeting.getSubItems().size());

        assertTrue(meeting.addSubItem(trip));
        assertEquals(1, meeting.getSubItems().size());

        assertTrue(meeting.addSubItem(other));
        assertEquals(2, meeting.getSubItems().size());

        assertTrue(meeting.deleteSubItem(trip));
        assertEquals(1, meeting.getSubItems().size());
    }

    @Test
    public void testGetSubItems() throws ItemCreateException, IsFormatException {
        Anniversary anniversary = new Anniversary("2018-5-20", "史密斯", "结婚纪念日",
                "和简的第20个结婚纪念日", "紧急 & 重要", null);
        Other other = new Other("2018-5-20 13:15", "2018-5-20 14:00", "测试", "不紧急 & 不重要", null);
        Interview interview = new Interview("2018-5-20 13:00", "2018-5-20 14:00", "软件楼第二会议室",
                "Intel", "程序员", "没有备注", "不紧急 & 重要", null);
        Meeting meeting = new Meeting("2018-5-20 16:00", "2018-5-20 17:30", "儿童节会议",
                "如何度过儿童节", "篮球场", "不紧急 & 重要", null);

        assertTrue(anniversary.addSubItem(other));
        assertTrue(anniversary.addSubItem(interview));
        assertTrue(anniversary.addSubItem(meeting));

        List<Item> items = anniversary.getSubItems("2018-5-20");
        assertEquals(3, items.size());

        items = anniversary.getSubItems("2018-5-20 13:00", "2018-5-20 14:00");
        assertEquals(2, items.size());

        items = anniversary.getSubItems("2018-5-20 16:00", "2018-5-20 18:00");
        assertEquals(1, items.size());
        assertTrue(meeting.itemEqual(items.get(0)));
    }

    @Test
    public void testSetTimeNull() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertNull(other.setTime(null));
    }

    @Test
    public void testSetTimeFailure() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertNull(other.setTime("2018-1-1 20:60"));
        assertNull(other.setTime("2018-1-1 24:20"));
        assertNull(other.setTime("2018-1-32 20:20"));
        assertNull(other.setTime("201s-12-01 20:20"));
        assertNull(other.setTime("2018-s2-01 20:20"));
        assertNull(other.setTime("2018-12-s1 20:20"));
        assertNull(other.setTime("2018-12-01 2s:20"));
        assertNull(other.setTime("2018-12-01 20:2s"));
        assertNull(other.setTime("sss0-12-01 20:20"));
    }

    @Test
    public void testSetTimeSuccess() throws ItemCreateException {
        Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        String dateActualString = f.format(other.setTime("2018-01-01 20:20"));
        assertEquals("2018-01-01 20:20", dateActualString);
    }

    @Test
    public void testIsValidNull() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertFalse(other.isValid(null));
    }

    @Test
    public void testIsValidFalse() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertFalse(other.isValid("2018-01-01 28:00"));
        assertFalse(other.isValid("2018-01-01 20:61"));
        assertFalse(other.isValid("2018-01-32 20:50"));
        assertFalse(other.isValid("2018-13-01 20:50"));
        assertFalse(other.isValid("201s-01-01 20:20"));
        assertFalse(other.isValid("2018-s1-01 20:20"));
        assertFalse(other.isValid("2018-1-s1 20:20"));
        assertFalse(other.isValid("2018-1-01 2s:20"));
        assertFalse(other.isValid("2018-1-01 20:2s"));
    }

    @Test
    public void testIsValidTrue() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertTrue(other.isValid("2018-01-01 20:20"));
    }

    @Test
    public void testIsFormattedNull() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertFalse(other.isFormatted(null));
    }

    @Test
    public void testIsFormattedFalse() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);

        assertFalse(other.isFormatted("2018-222-1 20:20"));
        assertFalse(other.isFormatted("2018/1-1 20:20"));
        assertFalse(other.isFormatted("2018-01-111 20:20"));
        assertFalse(other.isFormatted("2018-01 20:20"));
        assertFalse(other.isFormatted("2018-01-ss 20:20"));
        assertFalse(other.isFormatted("sss8-01-01 20:20"));
        assertFalse(other.isFormatted("ss-ss-ss 20:20"));
        assertFalse(other.isFormatted("2018 20:20"));
        assertFalse(other.isFormatted("2018-01-120:20"));
        assertFalse(other.isFormatted("2018-01-01 20/20"));
        assertFalse(other.isFormatted("2018-01-01 ss:20"));
        assertFalse(other.isFormatted("2018-01-01 20:ss"));
        assertFalse(other.isFormatted("2018-01-01 222:20"));
        assertFalse(other.isFormatted("2018-01-01 20:222"));
    }

    @Test
    public void testIsFormattedTrue() throws ItemCreateException {
        Other other = new Other("2018-6-12 13:15", "2018-6-12 14:00", "测试", "紧急 & 重要", null);
        assertTrue(other.isFormatted("2018-01-01 20:20"));
    }
}