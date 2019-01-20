package top.happing.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RelativeDateFormat {
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final String SECOND_AGO = "刚刚";
	private static final String MINUTE_AGO = "分钟前";
	private static final String HOUR_AGO = "小时前";
	private static final String YESTERDAY = "昨天";
	private static final String BEFORE_YESTERDAY = "前天";


	public static String format(Date date) {
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Date now = new Date();
			Date today = format4.parse(format4.format(now));
			long delta = now.getTime() - date.getTime();
			long delta2 = today.getTime() - date.getTime();
			
			if (delta < 1L * ONE_MINUTE) {
				return SECOND_AGO;
			}
			if (delta < 45L * ONE_MINUTE) {
				long minutes = toMinutes(delta);
				return (minutes <= 0 ? 1 : minutes) + MINUTE_AGO;
			}
			if (delta < 24L * ONE_HOUR) {
				long hours = toHours(delta);
				return (hours <= 0 ? 1 : hours) + HOUR_AGO;
			}
			if (delta < 48L * ONE_HOUR) {
				if(delta2 > 0 && delta2 < 24L * ONE_HOUR) {
					return YESTERDAY + format1.format(date);
				} else {
					return BEFORE_YESTERDAY + format1.format(date);
				}
			}
			if (delta < 72L * ONE_HOUR) {
				if(delta2 > 0 && delta2 < 48L * ONE_HOUR) {
					return BEFORE_YESTERDAY + format1.format(date);
				} else {
					return format2.format(date);
				}
			}
			if (delta < 365L * 24L * ONE_HOUR) {
				return format2.format(date);
			}
			return format3.format(date);
		} catch (ParseException e) {
			return "";
		}
	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}
}
