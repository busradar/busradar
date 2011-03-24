package busradar.madison;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

public class HolidayChecker {

private static void
shift_to_nearest_weekday(Calendar day)
{
	int day_of_week = day.get(DAY_OF_WEEK);
	
	if (day_of_week == SATURDAY)
		day.add(DAY_OF_MONTH, -1);
	else if (day_of_week == SUNDAY)
		day.add(DAY_OF_MONTH, 1);
	
}


private static boolean
same_date(Calendar c1, Calendar c2)
{
	return (c1.get(YEAR) == c2.get(YEAR) && 
		    c1.get(DAY_OF_YEAR) == c2.get(DAY_OF_YEAR));
}


private static void
shift_to_nth_day_of_month(GregorianCalendar cal, int month, int day_of_week, int n)
{
	cal.set(MONTH, month);
	cal.set(DAY_OF_MONTH, 1);
	
	int now = cal.get(DAY_OF_WEEK);
	
	int shift = 0;
	
	if (now > day_of_week)
		shift = 7 - (now - day_of_week);
	else if (now < day_of_week)
		shift = day_of_week - now;
	
	shift += 7 * n;
	
	cal.add(DAY_OF_MONTH, shift);
}

private static void
shift_to_date(GregorianCalendar cal, int month, int day)
{
	cal.set(MONTH, month);
	cal.set(DAY_OF_MONTH, day);
}

private static boolean
check_numbered_date(GregorianCalendar date, int month, int day_of_month, boolean shift_to_weekday)
{
	GregorianCalendar holiday = (GregorianCalendar)(date.clone());
	
	shift_to_date(holiday, month, day_of_month);
	
	if (shift_to_weekday)
		shift_to_nearest_weekday(holiday);
	
	return same_date(date, holiday);
}

private static boolean
check_named_date(GregorianCalendar date, int month, int day_of_week, int num_weeks)
{
	GregorianCalendar holiday = (GregorianCalendar)(date.clone());
	
	shift_to_nth_day_of_month(holiday, month, day_of_week, num_weeks);
	
	System.out.printf("named date %s\n",holiday.getTime());
	
	return same_date(date, holiday);
}

// New Year's Day
// January 1
public static boolean
New_Years_Day_Observed(GregorianCalendar day)
{	
	GregorianCalendar holiday1 = (GregorianCalendar)(day.clone());
	GregorianCalendar holiday2 = (GregorianCalendar)(day.clone());
	
	holiday1.set(DAY_OF_YEAR, 1);
	holiday2.set(DAY_OF_YEAR, 1);
	
	holiday2.add(YEAR, 1);
	
	shift_to_nearest_weekday(holiday1);
	shift_to_nearest_weekday(holiday2);
	
	return (same_date(day, holiday1) || same_date(day, holiday2));
}


// Martin Luther King, Jr. Day
// Third Monday in January
public static boolean
Martin_Luther_King_Day(GregorianCalendar day)
{
	return check_named_date(day, JANUARY, MONDAY, 2);
}

// Memorial Day
// Last Monday in May
public static boolean
Memorial_Day(GregorianCalendar day)
{
	return check_named_date(day, MAY+1, MONDAY, -1);
}


// Independence Day
// July 4
public static boolean
Independence_Day_Observed(GregorianCalendar day)
{
	return check_numbered_date(day, JULY, 4, true);
}

// Labor Day
// First Monday in September
public static boolean
Labor_Day(GregorianCalendar day)
{	
	return check_named_date(day, SEPTEMBER, MONDAY, 0);
}

// Thanksgiving Day
// Fourth Thursday in November
public static boolean
Thanksgiving_Day(GregorianCalendar day)
{
	return check_named_date(day, NOVEMBER, THURSDAY, 3);
}

//Thanksgiving Day
//Fourth Thursday in November
public static boolean
Thanksgiving_Day_After(GregorianCalendar day)
{
	GregorianCalendar holiday = (GregorianCalendar)(day.clone());
	shift_to_nth_day_of_month(holiday, NOVEMBER, THURSDAY, 3);
	holiday.add(DAY_OF_MONTH, 1);
	
	return same_date(day, holiday);
}

// Christmas Day
// December 25
public static boolean
Christmas_Day_Observed(GregorianCalendar day)
{
	return check_numbered_date(day, DECEMBER, 25, true);
}

}
