/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.resolvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import nl.topicus.cobra.templates.FieldInfo;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hop
 */
public class XPathResolver implements FieldResolver
{
	public XPathResolver(Node document)
	{
		xpathMap = new HashMap<String, XPathExpression>();
		this.document = document;
	}

	private final HashMap<String, XPathExpression> xpathMap;

	private final Node document;

	/**
	 * @param xPathString
	 * @param document
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings( {"unused", "hiding"})
	private Element getElement(String xPathString, Node document) throws XPathExpressionException
	{
		if (document != null)
		{
			XPathExpression expr = getExpression(xPathString);

			Element node = (Element) expr.evaluate(document, XPathConstants.NODE);
			return node;
		}
		return null;
	}

	/**
	 * @param xPathString
	 * @param document
	 * @return
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("hiding")
	private NodeList getNodeList(String xPathString, Node document) throws XPathExpressionException
	{
		XPathExpression expr = getExpression(xPathString);

		NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		return nodeList;

	}

	private String getNodeValue(Node node)
	{
		String result = null;
		if (node != null && node.getFirstChild() != null)
		{
			result = node.getFirstChild().getTextContent();
		}
		return result;
	}

	/**
	 * @param xPathString
	 * @return
	 * @throws XPathExpressionException
	 */
	private XPathExpression getExpression(String xPathString) throws XPathExpressionException
	{
		XPathExpression expr = null;
		if (xpathMap.containsKey(xPathString))
		{
			expr = xpathMap.get(xPathString);
		}
		else
		{
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();

			// processing voor namespaces:
			// xPath.setNamespaceContext(new MeldingNameSpaceContext());

			expr = xPath.compile(xPathString.toString());
			xpathMap.put(xPathString, expr);
		}
		return expr;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#getInfo(java.lang.String)
	 */
	@Override
	public FieldInfo getInfo(String name)
	{
		FieldInfo result = new FieldInfo(name, null, false, "Veld kan niet gevonden worden");
		try
		{
			NodeList list = getNodeList(name, document);
			if (list.getLength() == 1)
			{
				result = new FieldInfo(name, String.class, true, null);
			}
			else if (list.getLength() > 1)
			{
				result = new FieldInfo(name, List.class, true, null);
			}
		}
		catch (XPathExpressionException e)
		{
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#next(java.lang.String)
	 */
	@Override
	public Object next(String name)
	{
		return null;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#resolve(java.lang.String)
	 */
	@Override
	public Object resolve(String name)
	{
		Object result = null;
		try
		{
			NodeList list = getNodeList(name, document);
			if (list.getLength() == 1)
			{
				result = getNodeValue(list.item(0));
			}
			else if (list.getLength() > 1)
			{
				List<String> stringList = new ArrayList<String>();
				for (int i = 0; i < list.getLength(); i++)
					stringList.add(getNodeValue(list.item(i)));
				result = stringList;
			}

		}
		catch (XPathExpressionException e)
		{
			// silently ignore
		}
		return result;
	}

}
