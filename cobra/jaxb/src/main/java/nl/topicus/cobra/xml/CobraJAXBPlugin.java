package nl.topicus.cobra.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;

import org.xml.sax.ErrorHandler;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.outline.PackageOutline;

public class CobraJAXBPlugin extends Plugin
{
	public static final String JAVA_TYPE = "javaType";

	public static final String INLINE_LIST = "inlineList";

	public CobraJAXBPlugin()
	{
	}

	@Override
	public String getOptionName()
	{
		return "cobra";
	}

	@Override
	public String getUsage()
	{
		return "  -cobra           :  enable Topicus Cobra extensions";
	}

	@Override
	public List<String> getCustomizationURIs()
	{
		return Collections.singletonList(Namespace.COBRA_JAXB);
	}

	@Override
	public boolean isCustomizationTagName(String nsUri, String localName)
	{
		if (!Namespace.COBRA_JAXB.equals(nsUri))
			return false;

		return JAVA_TYPE.equals(localName) || INLINE_LIST.equals(localName);
	}

	@Override
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler)
	{
		handleJavaType(outline);
		handleListElement(outline);
		return true;
	}

	private void handleListElement(Outline outline)
	{
		for (ClassOutline ci : outline.getClasses())
		{
			for (FieldOutline curField : ci.getDeclaredFields())
			{
				CPluginCustomization customization =
					curField.getPropertyInfo().getCustomizations().find(Namespace.COBRA_JAXB,
						INLINE_LIST);
				if (customization == null)
					continue;

				customization.markAsAcknowledged();
				inlineList(outline, (JDefinedClass) curField.getRawType());
			}
		}
	}

	private void inlineList(Outline outline, JDefinedClass typeToInline)
	{
		JFieldVar innerField = getInnerField(typeToInline);
		JFieldVar outerField = null;
		JMethod getter = null;
		JMethod setter = null;
		JDefinedClass fieldOwner = null;
		for (ClassOutline ci : outline.getClasses())
		{
			JDefinedClass definedClass = ci.implClass;
			for (JFieldVar curField : definedClass.fields().values())
			{
				if (curField.type().equals(typeToInline))
				{
					outerField = curField;
					fieldOwner = definedClass;
					break;
				}
			}
			for (JMethod curMethod : definedClass.methods())
			{
				if (curMethod.type().equals(typeToInline))
					getter = curMethod;
				if (curMethod.listParams().length == 1
					&& curMethod.listParams()[0].type().equals(typeToInline))
					setter = curMethod;
			}
		}
		inlineList(outline, fieldOwner, innerField, outerField, getter, setter);
	}

	private void inlineList(Outline outline, JDefinedClass parentClass, JFieldVar innerField,
			JFieldVar outerField, JMethod getter, JMethod setter)
	{
		JType outerType = outerField.type();
		Iterator<JDefinedClass> nestedClassesIterator = parentClass.classes();
		while (nestedClassesIterator.hasNext())
		{
			if (nestedClassesIterator.next().equals(outerType))
			{
				nestedClassesIterator.remove();
				break;
			}
		}
		JClass innerType = (JClass) innerField.type();
		updateXmlElementAnnotation(innerField, outerField);
		copyXmlAdapterAnnotation(innerField, outerField);
		outerField.type(innerField.type());
		getter.type(innerField.type());
		getter.body().pos(0);
		JConditional cond = getter.body()._if(outerField.eq(JExpr._null()));
		cond._then().assign(
			outerField,
			JExpr._new(outline.getCodeModel().ref(ArrayList.class).narrow(
				innerType.getTypeParameters().get(0))));
		outerField.annotate(XmlElementWrapper.class).param("name", outerField.name());
		for (PackageOutline curPackage : outline.getAllPackageContexts())
		{
			Iterator<JMethod> it = curPackage.objectFactory().methods().iterator();
			while (it.hasNext())
			{
				if (it.next().type().equals(outerType))
					it.remove();
			}
		}
		Iterator<JMethod> it = parentClass.methods().iterator();
		while (it.hasNext())
		{
			if (it.next().equals(setter))
				it.remove();
		}
		updateJavadoc(getter, outerField.name(), innerType);
	}

	private void updateXmlElementAnnotation(JFieldVar innerField, JFieldVar outerField)
	{
		JAnnotationUse annotation = null;
		for (JAnnotationUse curAnnotation : getAnnotations(outerField))
			if (getAnnotationType(curAnnotation).name().equals("XmlElement"))
				annotation = curAnnotation;

		if (annotation == null)
			annotation = outerField.annotate(XmlElement.class);
		annotation.param("name", innerField.name());
	}

	private void copyXmlAdapterAnnotation(JFieldVar innerField, JFieldVar outerField)
	{
		for (JAnnotationUse curAnnotation : getAnnotations(innerField))
		{
			if (getAnnotationType(curAnnotation).name().equals("XmlJavaTypeAdapter"))
			{
				JAnnotationUse annotation = outerField.annotate(XmlJavaTypeAdapter.class);
				annotation.param("type", String.class);
				Map<String, JAnnotationValue> params = getAnnotationParams(annotation);
				params.clear();
				params.putAll(getAnnotationParams(curAnnotation));
			}
		}
	}

	private void updateJavadoc(JMethod getter, String pname, JClass innerType)
	{
		getter.javadoc().clear();
		getter.javadoc().addReturn().clear();
		getter.javadoc().append(
			"Gets the value of the " + pname + " property.\n\n" + "<p>\n"
				+ "This accessor method returns a reference to the live list,\n"
				+ "not a snapshot. Therefore any modification you make to the\n"
				+ "returned list will be present inside the JAXB object.\n"
				+ "This is why there is not a <CODE>set</CODE> method for the " + pname
				+ " property.\n" + "\n" + "<p>\n"
				+ "For example, to add a new item, do as follows:\n" + "<pre>\n" + "   "
				+ getter.name() + "().add(newItem);\n" + "</pre>\n" + "\n\n");

		getter.javadoc().append(
			"<p>\n" + "Objects of the following type(s) are allowed in the list\n").append(
			innerType.getTypeParameters().get(0));
	}

	@SuppressWarnings("unchecked")
	private List<JAnnotationUse> getAnnotations(JFieldVar field)
	{
		List<JAnnotationUse> ret =
			(List<JAnnotationUse>) ReflectionUtil.findProperty(JVar.class, "annotations").getValue(
				field);
		if (ret == null)
			ret = Collections.emptyList();
		return ret;
	}

	private JClass getAnnotationType(JAnnotationUse annotation)
	{
		try
		{
			Property<JAnnotationUse, JAnnotationUse, JClass> property =
				new Property<JAnnotationUse, JAnnotationUse, JClass>(JAnnotationUse.class
					.getDeclaredField("clazz"));
			return property.getValue(annotation);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, JAnnotationValue> getAnnotationParams(JAnnotationUse annotation)
	{
		return (Map<String, JAnnotationValue>) ReflectionUtil.findProperty(JAnnotationUse.class,
			"memberValues").getValue(annotation);
	}

	private JFieldVar getInnerField(JDefinedClass typeToInline)
	{
		return typeToInline.fields().values().iterator().next();
	}

	private void handleJavaType(Outline outline)
	{
		Map<JDefinedClass, CPluginCustomization> customizations =
			new HashMap<JDefinedClass, CPluginCustomization>();

		for (ClassOutline ci : outline.getClasses())
		{
			CPluginCustomization customization =
				ci.target.getCustomizations().find(Namespace.COBRA_JAXB, JAVA_TYPE);
			if (customization == null)
				continue;

			customization.markAsAcknowledged();
			customizations.put(ci.implClass, customization);
		}
		for (ClassOutline ci : outline.getClasses())
		{
			JDefinedClass definedClass = ci.implClass;
			for (JFieldVar curField : definedClass.fields().values())
			{
				CPluginCustomization customization = customizations.get(curField.type());
				if (customization != null)
				{
					JClass javaType = getJavaType(outline.getCodeModel(), customization);
					JClass adapter = getAdapter(outline.getCodeModel(), customization);
					curField.annotate(XmlJavaTypeAdapter.class).param("value", adapter).param(
						"type", curField.type());
					curField.type(javaType);
				}
			}
			for (JMethod curMethod : definedClass.methods())
			{
				CPluginCustomization customization = customizations.get(curMethod.type());
				if (customization != null)
				{
					JClass javaType = getJavaType(outline.getCodeModel(), customization);
					curMethod.type(javaType);
				}
				for (JVar curParam : curMethod.listParams())
				{
					customization = customizations.get(curParam.type());
					if (customization != null)
					{
						JClass javaType = getJavaType(outline.getCodeModel(), customization);
						curParam.type(javaType);
					}
				}
			}
		}
	}

	private JClass getJavaType(JCodeModel cm, CPluginCustomization customization)
	{
		String type = customization.element.getAttribute("name");
		return cm.ref(type);
	}

	private JClass getAdapter(JCodeModel cm, CPluginCustomization customization)
	{
		String adapter = customization.element.getAttribute("adapter");
		return cm.ref(adapter);
	}
}
