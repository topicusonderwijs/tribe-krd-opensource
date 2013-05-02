package nl.topicus.eduarte.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.cxf.jaxb.NamespaceMapper;
import org.xml.sax.SAXException;

public final class JAXBContextFactory
{
	private JAXBContextFactory()
	{
	}

	private static JAXBContext context;

	public static synchronized JAXBContext getContext() throws JAXBException
	{
		if (context == null)
			context =
				JAXBContext.newInstance("nl.topicus.eduarte.xml.types.v10"
					+ ":nl.topicus.eduarte.xml.onderwijscatalogus.v10"
					+ ":nl.topicus.eduarte.xml.resultaatstructuur.v10");
		return context;
	}

	public static Marshaller createMarshaller() throws JAXBException
	{
		Map<String, String> namespaces = new HashMap<String, String>();
		namespaces.put("http://eduarte.topicus.nl/types/1.0", "eat");
		namespaces.put("http://eduarte.topicus.nl/onderwijscatalogus/1.0", "oc");
		namespaces.put("http://eduarte.topicus.nl/resultaatstructuur/1.0", "rs");

		Marshaller m = getContext().createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespaceMapper(namespaces));
		return m;
	}

	public static Unmarshaller createUnmarshaller() throws JAXBException
	{
		Schema schema;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try
		{
			schema =
				sf.newSchema(new Source[] {createSource("/nl/topicus/eduarte/xsd/basistypes.xsd"),
					createSource("/nl/topicus/eduarte/xsd/onderwijscatalogus.xsd"),
					createSource("/nl/topicus/eduarte/xsd/resultaatstructuur.xsd")});
		}
		catch (SAXException saxe)
		{
			throw new JAXBException(saxe);
		}

		Unmarshaller ret = getContext().createUnmarshaller();
		ret.setSchema(schema);
		return ret;
	}

	private static StreamSource createSource(String location)
	{
		return new StreamSource(JAXBContextFactory.class.getResourceAsStream(location));
	}

}
