package nl.topicus.cobra.util;

import java.util.List;

import junit.framework.TestCase;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;

public class ReflectionUtilTest extends TestCase
{

	// public void testFindConstructor()
	// {
	// ReflectionUtil.findConstructor(clazz, args);
	// }
	//
	// public void testFindMethod()
	// {
	// ReflectionUtil.findMethod(staticMethod, clazz, name, args);
	// }

	/**
	 * TestClass1 heeft weliswaar maar 2 get functies maar getClass() wordt ook altijd
	 * meegenomen.
	 */
	public void testFindPropertiesByClass1()
	{
		Asserts.assertEquals("properties", ReflectionUtil.findProperties(TestClass1.class).size(),
			3);
	}

	/**
	 * TestClass2 heeft weliswaar maar 3 get functies maar getClass() wordt ook altijd
	 * meegenomen.
	 */
	public void testFindPropertiesByClass2()
	{
		List<Property<TestClass2, ? , ? >> properties =
			ReflectionUtil.findProperties(TestClass2.class);

		Asserts.assertNotEmpty("parentText", properties);
		Asserts.assertEquals("properties", properties.size(), 4);
	}

	public void testFindProperty()
	{
		Asserts.assertNotEmpty("parentText", ReflectionUtil.findProperty(TestClass2.class,
			"parentText"));
	}

	public void testFindPropertiesByProperty()
	{
		Property<TestClass2, ? , ? > parentProperty =
			ReflectionUtil.findProperty(TestClass2.class, "parentText");
		List<Property<TestClass2, ? , ? >> properties =
			ReflectionUtil.findProperties(parentProperty);

		Asserts.assertNotEmpty("parentText", properties);
		Asserts.assertEquals("properties", properties.size(), 3);
	}

	// public void testGetPrimitiveClass()
	// {
	// ReflectionUtil.getPrimitiveClass(wrapper);
	// }
	//
	// public void testGetWrapperClass()
	// {
	// ReflectionUtil.getWrapperClass(primitive);
	// }

	public class TestClass1
	{
		private String text;

		private String extendedText;

		@SuppressWarnings("unused")
		private TestClass1()
		{

		}

		public TestClass1(String text)
		{
			setText(text);
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}

		public String getExtendedText()
		{
			return extendedText;
		}

		public void setExtendedText(String extendedText)
		{
			this.extendedText = extendedText;
		}
	}

	public class TestClass2
	{
		private String text;

		private String extendedText;

		private TestClass1 parentText;

		@SuppressWarnings("unused")
		private TestClass2()
		{

		}

		public TestClass2(String text)
		{
			setText(text);
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}

		public String getExtendedText()
		{
			return extendedText;
		}

		public void setExtendedText(String extendedText)
		{
			this.extendedText = extendedText;
		}

		public TestClass1 getParentText()
		{
			return parentText;
		}

		public void setParentText(TestClass1 parentText)
		{
			this.parentText = parentText;
		}
	}
}
