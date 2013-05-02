package nl.topicus.cobra.comparators;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import nl.topicus.cobra.util.StringUtil;

import org.junit.Test;

public class NaturalOrderComparatorTest
{
	@Test
	public void testCompare()
	{
		String[] strings =
			new String[] {"1-2", "1-02", "1-20", "10-20", "fred", "jane", "pic01", "pic2", "pic02",
				"pic02a", "pic3", "pic4", "pic 4 else", "pic 5", "pic 5 something", "pic05",
				"pic 6", "pic   7", "pic100", "pic100a", "pic120", "pic121", "pic02000", "tom",
				"x2-g8", "x2-y7", "x2-y08", "x8-y8"};

		List<String> orig = Arrays.asList(strings.clone());

		List<String> scrambled = Arrays.asList(strings.clone());
		Collections.shuffle(scrambled);

		Collections.sort(scrambled, new NaturalOrderComparator());

		Assert.assertEquals(StringUtil.join(orig, ", "), StringUtil.join(scrambled, ", "));
	}

	@Test
	public void testCompare2()
	{
		String[] origStrings = new String[] {"1-2", "1-02"};

		String[] scrambledStrings = new String[] {"1-02", "1-2"};

		List<String> orig = Arrays.asList(origStrings);

		List<String> scrambled = Arrays.asList(scrambledStrings);

		Collections.sort(scrambled, new NaturalOrderComparator());

		Assert.assertEquals(StringUtil.join(orig, ", "), StringUtil.join(scrambled, ", "));
	}

	@Test
	public void testCompare3()
	{
		String[] origStrings = new String[] {"1.0", "1.0.1", "2.0", "2.0.1", "2.1"};

		String[] scrambledStrings = new String[] {"2.0", "1.0.1", "2.1", "2.0.1", "1.0"};

		List<String> orig = Arrays.asList(origStrings);

		List<String> scrambled = Arrays.asList(scrambledStrings);

		Collections.sort(scrambled, new NaturalOrderComparator());

		Assert.assertEquals(StringUtil.join(orig, ", "), StringUtil.join(scrambled, ", "));
	}
}
