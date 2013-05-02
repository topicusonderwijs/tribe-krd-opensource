package nl.topicus.cobra.templates.documents.docx;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.Field;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateFormattingException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.StringUtil;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author hop
 */
public class Word2007Field implements Field
{
	private String type;

	private String name;

	private String format;

	private Object mergedResult;

	private final Node node;

	private final XPath xpath;

	Pattern INSTRUCTION_PATTERN =
		Pattern
			.compile("\\s*(\\S*)\\s*([^\\(\\)\\s]*(?:\\([^\\(\\)]*\\)\\S*)*)\\s*(\\\\@\\s*\"(.*)\")?(\\\\#\\s*\"(.*)\")?.*");

	/**
	 * @param node
	 * @throws XPathExpressionException
	 */
	public Word2007Field(Node node, XPath xpath) throws XPathExpressionException
	{
		this.node = node;
		this.xpath = xpath;
		Node instructionsNode = null;
		StringBuilder instructionText = new StringBuilder();
		NamedNodeMap attributes = node.getAttributes();
		if (node.getLocalName().equals("fldChar") && attributes != null
			&& attributes.getNamedItem("w:fldCharType").getTextContent().equals("begin"))
		{
			// Zoek alle siblingelementen van de parent van de fldChar met een
			// 'w:instrText' als child, totaan de volgende fldChar met fldCharType=end
			Node current = node.getParentNode();
			XPathExpression instr = xpath.compile("./w:instrText");
			XPathExpression end = xpath.compile("./w:fldChar[@w:fldCharType=\"end\"]");
			Node endNode = null;
			while (endNode == null && current.getNextSibling() != null)
			{
				current = current.getNextSibling();
				endNode = (Node) end.evaluate(current, XPathConstants.NODE);
				instructionsNode = (Node) instr.evaluate(current, XPathConstants.NODE);
				if (instructionsNode != null)
					instructionText.append(instructionsNode.getTextContent());
			}
		}
		else
		{
			if (attributes != null)
			{
				instructionsNode = attributes.getNamedItem("w:instr");
				instructionText.append(instructionsNode.getTextContent());
			}
		}

		String instructions = instructionText.toString();
		Matcher matcher = INSTRUCTION_PATTERN.matcher(instructions);
		if (matcher.matches())
		{
			type = matcher.group(1);
			name = matcher.group(2);
			format = matcher.group(4);
			if (StringUtil.isEmpty(format))
				format = matcher.group(6);
		}
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.Field#getType()
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.Field#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 * @throws XPathExpressionException
	 */
	public void setName(String name) throws XPathExpressionException
	{
		NamedNodeMap attributes = node.getAttributes();
		Node instructionsNode = null;
		if (node.getLocalName().equals("fldChar") && attributes != null
			&& attributes.getNamedItem("w:fldCharType").getTextContent().equals("begin"))
		{
			// Zoek alle siblingelementen van de parent van de fldChar met een
			// 'w:instrText' als child, totaan de volgende fldChar met fldCharType=end
			Node current = node.getParentNode();
			XPathExpression instr = xpath.compile("./w:instrText");
			XPathExpression end = xpath.compile("./w:fldChar[@w:fldCharType=\"end\"]");
			Node endNode = null;
			while (endNode == null && current.getNextSibling() != null)
			{
				current = current.getNextSibling();
				endNode = (Node) end.evaluate(current, XPathConstants.NODE);
				instructionsNode = (Node) instr.evaluate(current, XPathConstants.NODE);
				if (instructionsNode != null)
				{

					String text = instructionsNode.getTextContent();
					if (text != null && text.contains(this.name))
					{
						instructionsNode.getFirstChild()
							.setNodeValue(text.replace(this.name, name));
					}
				}
			}
		}
		else
		{
			if (attributes != null)
			{
				instructionsNode = attributes.getNamedItem("w:instr");
				String text = instructionsNode.getTextContent();
				if (text != null && text.contains(this.name))
					instructionsNode.setNodeValue(text.replace(this.name, name));
			}
		}
		this.name = name;

	}

	public void merge(FieldResolver resolver) throws TemplateException
	{
		if ("MERGEFIELD".equals(getType()))
		{
			mergedResult = resolver.resolve(getName());
		}
		else
		{
			// constructie om fields als AUTHOR, DATE etc. uit de context te
			// kunnen halen
			// indien dit null oplevert, wordt het veld niet gesubstitueerd
			mergedResult = resolver.resolve(getType());
		}

		if (mergedResult != null)
			mergedResult = formatResult(mergedResult);
	}

	public Object getMergedResult()
	{
		return mergedResult;
	}

	public void setMergedResult(Object mergedResult)
	{
		this.mergedResult = mergedResult;
	}

	public Node getNode()
	{
		return node;
	}

	/**
	 * Converteert een merge result als een string. Voorlopig wordt alleen Date omgezet
	 * naar een dd-MM-yyyy en een Boolean omgezet naar J of N. Misschien later nog
	 * uitbreiden naar een systeem waarin formatters voor objecten kunnen worden
	 * geregistreerd.
	 */
	@SuppressWarnings("unchecked")
	protected String formatResult(Object mergeResult) throws TemplateException
	{
		if (mergeResult instanceof Iterable)
		{
			StringBuilder builder = new StringBuilder();
			Iterator<Object> it = ((Iterable<Object>) mergeResult).iterator();
			while (it.hasNext())
				if (builder.length() == 0)
					builder.append(formatResult(it.next()));
				else
					builder.append(", ").append(formatResult(it.next()));
			return builder.toString();
		}
		else if (mergeResult instanceof Date)
		{
			Date date = (Date) mergeResult;

			String dateFormat = format;
			if (StringUtil.isEmpty(dateFormat))
				dateFormat = "dd-MM-yyyy";

			Locale loc = new Locale("nl", "NL");
			try
			{
				DateFormat formatter = new SimpleDateFormat(dateFormat, loc);
				return formatter.format(date);
			}
			catch (IllegalArgumentException e)
			{
				throw new TemplateFormattingException("Ongeldige format optie '" + format
					+ "' voor een datum veld.", e);
			}

		}
		else if (mergeResult instanceof Boolean)
		{
			Pattern pattern = Pattern.compile("(.*)\\|(.*)");

			String booleanFormat = format;
			if (StringUtil.isEmpty(booleanFormat))
				booleanFormat = "J|N";
			Matcher matcher = pattern.matcher(booleanFormat);

			if (!matcher.matches())
				throw new TemplateFormattingException("Ongeldige format optie '" + format
					+ "' voor een boolean veld.");

			String formattedValue;
			if (((Boolean) mergeResult))
				formattedValue = matcher.group(1);
			else
				formattedValue = matcher.group(2);

			return formattedValue;
		}
		else if (mergeResult instanceof Number && StringUtil.isNotEmpty(format))
		{
			Locale loc = new Locale("nl", "NL");
			try
			{
				DecimalFormat formatter = new DecimalFormat(format, new DecimalFormatSymbols(loc));
				return formatter.format(mergeResult);
			}
			catch (IllegalArgumentException e)
			{
				throw new TemplateFormattingException("Ongeldige format optie '" + format
					+ "' voor een numeriek veld.", e);
			}
		}
		return mergeResult.toString();
	}

	@Override
	public FieldInfo getFieldInfo(FieldResolver resolver)
	{
		FieldInfo info = null;
		if ("MERGEFIELD".equals(getType()))
		{
			info = resolver.getInfo(getName());
		}
		else if (!"NEXT".equals(getType()))
		{
			// constructie om fields als AUTHOR, DATE etc. uit de context te
			// kunnen halen
			info = resolver.getInfo(getType());
		}
		if (info != null && StringUtil.isNotEmpty(format))
			info.setFormat(format);

		return info;
	}

	public void clear()
	{
		setMergedResult("");
	}
}
