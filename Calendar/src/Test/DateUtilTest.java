package Test;

import Logic.CalendarDate;
import Logic.DateUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("Class Logic.DateUtil tests begin! Good Luck!");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Class Logic.DateUtil tests end! Are you satisfied?");
    }

    @Test
    public void testGetDaysInMonthNull() {
        assertNull(DateUtil.getDaysInMonth(null));
    }

    @Test
    public void testGetDaysInMonthNotNull() {
        CalendarDate date = new CalendarDate(2018, 4, 2);
        List<CalendarDate> actualList = DateUtil.getDaysInMonth(date);

        List<CalendarDate> expectedList = new ArrayList<>();
        CalendarDate temp = null;
        for (int i = 0; i < 30; i++) {
            temp = new CalendarDate(2018, 4, i + 1);
            expectedList.add(temp);
        }

        assertEquals(expectedList.size(), actualList.size());

        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(expectedList.get(i), actualList.get(i));
        }
    }

    @Test
    public void testGetDaysInMonthIllegal() {
        CalendarDate date1 = new CalendarDate(2018, 2, 29);
        List<CalendarDate> actualList1 = DateUtil.getDaysInMonth(date1);
        assertNull(actualList1);

        CalendarDate date2 = new CalendarDate(2018, 22, 3);
        List<CalendarDate> actualList2 = DateUtil.getDaysInMonth(date2);
        assertNull(actualList2);

        CalendarDate date3 = new CalendarDate(2018, 3, 100);
        List<CalendarDate> actualList3 = DateUtil.getDaysInMonth(date3);
        assertNull(actualList3);

        CalendarDate date4 = new CalendarDate(2019, 33, 42);
        List<CalendarDate> actualList4 = DateUtil.getDaysInMonth(date4);
        assertNull(actualList4);
    }

    @Test
    public void testIntegration() {
        List<List<CalendarDate>> generatedCalendar = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            List<CalendarDate> monthCalendar = new ArrayList<>();
            for (int j = 1; j <= 31; j++) {
                CalendarDate temp = new CalendarDate(2018, i, j);
                monthCalendar.add(temp);
            }
            generatedCalendar.add(monthCalendar);
        }

        for (int i = 1; i <= 20; i++) {
            List<CalendarDate> tempCalendar = generatedCalendar.get(i - 1);
            int length = tempCalendar.size();
            CalendarDate temp = tempCalendar.get(length - 1);
            if (DateUtil.isValid(temp)) {
                for (int j = 0; j < length; j++) {
                    Assert.assertEquals(tempCalendar.get(j), DateUtil.getDaysInMonth(temp).get(j));
                }
            } else {
                assertNull(DateUtil.getDaysInMonth(temp));
            }
        }
    }

    @Test
    public void testIsValidNull() {
        assertFalse(DateUtil.isValid(null));
    }

    @Test
    public void testIsValidTrue() {
        CalendarDate date = new CalendarDate(2018, 4, 1);
        assertTrue(DateUtil.isValid(date));
    }

    @Test
    public void testIsValidFalse() {
        CalendarDate date = new CalendarDate(1900, 2, 29);
        assertFalse(DateUtil.isValid(date));
    }

    @Test
    public void testIsFormattedNull() {
        assertFalse(DateUtil.isFormatted(null));
    }

    @Test
    public void testIsFormattedTrue() {
        String dateStr = "2018-22-1";
        assertTrue(DateUtil.isFormatted(dateStr));
    }

    @Test
    public void testIsFormattedFalse() {
        String dateStr1 = "2018-222-111";
        assertFalse(DateUtil.isFormatted(dateStr1));

        String dateStr2 = "2018/1-1";
        assertFalse(DateUtil.isFormatted(dateStr2));

        String dateStr3 = "2018-1";
        assertFalse(DateUtil.isFormatted(dateStr3));

        String dateStr4 = "2018-1-1sss";
        assertFalse(DateUtil.isFormatted(dateStr4));

        String dateStr5 = "sss2018-1-1";
        assertFalse(DateUtil.isFormatted(dateStr5));
    }

    @Test
    public void testIsLeapYearTrue() {
        assertTrue(DateUtil.isLeapYear(2000));

        assertTrue(DateUtil.isLeapYear(2012));
    }

    @Test
    public void testIsLeapYearFalse() {
        assertFalse(DateUtil.isLeapYear(1999));

        assertFalse(DateUtil.isLeapYear(1900));
    }

    @Test
    public void testIsHoliday(){
        CalendarDate ordinary1 = new CalendarDate(2018,2,1);
        CalendarDate ordinary2 = new CalendarDate(2018,6,30);
        CalendarDate holiday1 = new CalendarDate(2018,10,1);
        CalendarDate holiday2 = new CalendarDate(2018,5,1);
        CalendarDate changeDay1 = new CalendarDate(2018,9,30);
        CalendarDate changeDay2 = new CalendarDate(2018,2,11);
        CalendarDate duringHoliday1 = new CalendarDate(2018,10,2);
        CalendarDate duringHoliday2 = new CalendarDate(2018,4,29);

        assertEquals(0, DateUtil.isHoliday(ordinary1));
        assertEquals(0, DateUtil.isHoliday(ordinary2));
        assertEquals(2, DateUtil.isHoliday(holiday1));
        assertEquals(2, DateUtil.isHoliday(holiday1));
        assertEquals(4, DateUtil.isHoliday(changeDay1));
        assertEquals(4, DateUtil.isHoliday(changeDay2));
        assertEquals(3, DateUtil.isHoliday(duringHoliday1));
        assertEquals(3, DateUtil.isHoliday(duringHoliday2));
    }

    @Test
    public void testGetHolidayName(){
        CalendarDate holiday = new CalendarDate(2018,10,1);
        String name1 = DateUtil.getHolidayName(holiday);
        CalendarDate holiday1 = new CalendarDate(2018,5,1);
        String name2 = DateUtil.getHolidayName(holiday1);
        CalendarDate holiday2 = new CalendarDate(2018,6,18);
        String name3 = DateUtil.getHolidayName(holiday2);

        assertEquals("国庆节", name1);
        assertEquals("劳动节", name2);
        assertEquals("端午节", name3);
    }

}