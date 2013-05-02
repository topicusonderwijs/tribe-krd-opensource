package nl.topicus.cobra.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TimeUtilCorrectTest extends TestCase
{
	private String dateFormat;

	private Calendar calendar = Calendar.getInstance();

	Date testDate;

	public TimeUtilCorrectTest(String dateFormat)
	{
		this.dateFormat = dateFormat;

		calendar.set(1999, 4, 13); // Thu May 13 00:00:00 CEST 1999
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// - het formaat bevat geen jaar item, stel het jaar op dit jaar in.
		if (!dateFormat.contains("y"))
			calendar.set(Calendar.YEAR, 2009);

		// - length = 2, dan kan er geen jaartal in zitten.
		// - length = 3, maar wel met een min, dan moet het formaat wel iets zijn als
		// 'd-m' of "ddm" of "dmm" ...
		if (dateFormat.length() == 2 || (dateFormat.length() == 3 && dateFormat.contains("-")))
			calendar.set(Calendar.DAY_OF_MONTH, 9);

		testDate = calendar.getTime();
	}

	@Test
	public void TestParseDateString()
	{
		String formattedDate = new SimpleDateFormat(dateFormat).format(testDate);
		Date parsedDate = TimeUtil.getInstance().parseDateString(formattedDate);

		Assert.assertNotNull(parsedDate);
		Assert.assertEquals(testDate, parsedDate);
	}

	/**
	 * De lijst met 52 formatting mogelijkheden. Niet alle mogelijkheden gaan goed!
	 * 
	 * @see TimeUtil#parseDateString(String)
	 */
	@Parameters
	public static List<Object[]> data()
	{

		ArrayList<Object[]> list = new ArrayList<Object[]>();
		list.add(new Object[] {"dd-MM-yyyy"}); // 8+2
		list.add(new Object[] {"ddMM-yyyy"}); // 8+1
		list.add(new Object[] {"dd-MMyyyy"}); // 8+1
		list.add(new Object[] {"ddMMyyyy"}); // 8+0
		list.add(new Object[] {"yyyyMMdd"});// 8+0, english
		list.add(new Object[] {"yyyy-MM-dd"});// 8+2, english
		list.add(new Object[] {"d-MM-yyyy"}); // 7+2
		list.add(new Object[] {"dd-M-yyyy"}); // 7+2
		list.add(new Object[] {"dMM-yyyy"}); // 7+1
		list.add(new Object[] {"d-MMyyyy"}); // 7+1
		// list.add(new Object[] {"ddM-yyyy"}); // 7+1
		// list.add(new Object[] {"dd-Myyyy"}); // 7+1
		list.add(new Object[] {"dMMyyyy"}); // 7+0
		// list.add(new Object[] {"ddMyyyy"}); // 7+0
		list.add(new Object[] {"d-M-yyyy"});// 6+2, year = 4
		// list.add(new Object[] {"dM-yyyy"});// 6+1, year = 4
		// list.add(new Object[] {"d-Myyyy"});// 6+1, year = 4
		// list.add(new Object[] {"dMyyyy"}); // 6+0, year = 4
		// list.add(new Object[] {"dd-MM-yy"});// 6+2, year= 2
		// list.add(new Object[] {"dd-MMyy"});// 6+1, year = 2
		// list.add(new Object[] {"ddMM-yy"});// 6+1, year = 2
		// list.add(new Object[] {"ddMMyy"});// 6+0, year = 2
		list.add(new Object[] {"d-MM-yy"});// 5+2, year = 2
		list.add(new Object[] {"dd-M-yy"});// 5+2, year = 2
		list.add(new Object[] {"dd-MM-y"});// 5+2, year = 1
		// list.add(new Object[] {"dMM-yy"});// 5+1, year = 2
		// list.add(new Object[] {"d-MMyy"});// 5+1, year = 2
		// list.add(new Object[] {"ddM-yy"});// 5+1, year = 2
		// list.add(new Object[] {"dd-Myy"});// 5+1, year = 2
		// list.add(new Object[] {"ddMM-y"});// 5+1, year = 1
		// list.add(new Object[] {"dd-MMy"});// 5+1, year = 1
		list.add(new Object[] {"dMMyy"});// 5+0, year = 2
		list.add(new Object[] {"ddMyy"});// 5+0, year = 2
		list.add(new Object[] {"ddMMy"});// 5+0, year = 1
		list.add(new Object[] {"d-M-yy"});// 4+2, year = 2
		list.add(new Object[] {"d-MM-y"});// 4+2, year = 1
		list.add(new Object[] {"dd-M-y"});// 4+2, year = 1
		// list.add(new Object[] {"dM-yy"});// 4+1, year = 2
		// list.add(new Object[] {"d-Myy"});// 4+1, year = 2
		// list.add(new Object[] {"d-MMy"});// 4+1, year = 1
		// list.add(new Object[] {"dMM-y"});// 4+1, year = 1
		// list.add(new Object[] {"ddM-y"});// 4+1, year = 1
		// list.add(new Object[] {"dd-My"});// 4+1, year = 1
		list.add(new Object[] {"dMyy"});// 4+0, year = 2
		list.add(new Object[] {"dMMy"});// 4+0
		list.add(new Object[] {"ddMy"});// 4+0
		list.add(new Object[] {"d-M-y"});// 3+2
		// list.add(new Object[] {"dM-y"});// 3+1
		// list.add(new Object[] {"d-My"});// 3+1
		list.add(new Object[] {"dMy"});// 3+0
		// list.add(new Object[] {"d-M"});// 2+1
		// list.add(new Object[] {"dM"});// 2+0

		return list;
	}
}
