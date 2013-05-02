/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.jrxml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * Read-only Map representatie van een FieldResolver. Alleen de methodes
 * <code>containsKey</code> en <code>get</code> zijn zinvol geimplementeerd.
 * Schrijf-acties <code>clear</code>- en <code>put</code>-methodes resulteren in een
 * UnsupportedOperationException.
 * 
 * @author Laurens Hop
 */
public class FieldResolverMap implements Map<String, Object>
{
	private final FieldResolver resolver;

	private final Map<String, Object> internalMap = new HashMap<String, Object>();

	/**
	 * Maakt van een gegeven FieldResolver een map-representatie
	 * 
	 * @param resolver
	 */
	public FieldResolverMap(FieldResolver resolver)
	{
		this.resolver = resolver;

		internalMap.put(JRParameter.REPORT_CLASS_LOADER, DocumentTemplate.class.getClassLoader());
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Geeft true als de internal map of de FieldResolver resultaat oplevert bij de
	 * gegeven key (geen null)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key)
	{
		if (internalMap.containsKey(key))
			return true;

		return resolver.resolve((String) key) != null;
	}

	@Override
	public boolean containsValue(Object value)
	{
		return internalMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet()
	{
		return internalMap.entrySet();
	}

	/**
	 * Gebruikt eerst de internal map en daarna de resolver om de waarde van de gegeven
	 * key te resolven.
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key)
	{
		if (internalMap.containsKey(key))
			return internalMap.get(key);

		// speciaal voor jasper reports gezien deze null moet terug krijgen en niet "".
		if (isJasperReportsKey(key.toString()))
			return null;

		return resolver.resolve((String) key);
	}

	@Override
	public boolean isEmpty()
	{
		return internalMap.isEmpty();
	}

	@Override
	public Set<String> keySet()
	{
		return internalMap.keySet();
	}

	@Override
	public Object put(String key, Object value)
	{
		return internalMap.put(key, value);
	}

	@Override
	public void putAll(Map< ? extends String, ? extends Object> m)
	{
		internalMap.putAll(m);
	}

	@Override
	public Object remove(Object key)
	{
		return internalMap.remove(key);
	}

	@Override
	public int size()
	{
		return internalMap.size();
	}

	@Override
	public Collection<Object> values()
	{
		return internalMap.values();
	}

	public void setMonitor(JRExportProgressMonitor monitor)
	{
		this.internalMap.put(JRExporterParameter.PROGRESS_MONITOR.toString(), monitor);
	}

	private boolean isJasperReportsKey(String key)
	{
		return key.contains(JRParameter.REPORT_PARAMETERS_MAP)
			|| key.contains(JRParameter.JASPER_REPORT)
			|| key.contains(JRParameter.REPORT_CONNECTION)
			|| key.contains(JRParameter.REPORT_MAX_COUNT)
			|| key.contains(JRParameter.REPORT_DATA_SOURCE)
			|| key.contains(JRParameter.REPORT_SCRIPTLET)
			|| key.contains(JRParameter.REPORT_LOCALE)
			|| key.contains(JRParameter.REPORT_RESOURCE_BUNDLE)
			|| key.contains(JRParameter.REPORT_TIME_ZONE)
			|| key.contains(JRParameter.REPORT_VIRTUALIZER)
			|| key.contains(JRParameter.REPORT_CLASS_LOADER)
			|| key.contains(JRParameter.REPORT_URL_HANDLER_FACTORY)
			|| key.contains(JRParameter.REPORT_FILE_RESOLVER)
			|| key.contains(JRParameter.REPORT_FORMAT_FACTORY)
			|| key.contains(JRParameter.IS_IGNORE_PAGINATION)
			|| key.contains(JRParameter.REPORT_TEMPLATES);
	}
}
