package nl.topicus.cobra.web.components.datapanel;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.model.IDetachable;

/**
 * Class waarmee aangegeven kan worden op welke properties een custom datapanel
 * gegroepeerd kan worden.
 * 
 * @author loite
 */
public class GroupProperty<T> implements IDetachable
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	private static final GroupProperty NIET_GROEPEREN =
		new GroupProperty(null, "<Niet groeperen>", null);

	private boolean ascending = true;

	private List<CustomColumn<T>> columns;

	/**
	 * Lijst met group properties. Bevat altijd het speciale '<Niet groeperen>' property.
	 * 
	 * @author loite
	 */
	public static final class GroupPropertyList<T> extends ArrayList<GroupProperty<T>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		public GroupPropertyList(int initialSize)
		{
			super(initialSize);
			add(NIET_GROEPEREN);
		}

		/**
		 * @param property
		 * @return Het group property met het gegeven property.
		 */
		@SuppressWarnings("unchecked")
		public GroupProperty<T> get(String property)
		{
			if (property == null)
				return NIET_GROEPEREN;
			for (GroupProperty<T> prop : this)
			{
				if (property.equals(prop.getProperty()))
				{
					return prop;
				}
			}
			return null;
		}

	}

	/**
	 * Het property dat aangeroepen moet worden (bijvoorbeeld 'persoon.achternaam'.
	 */
	private final String property;

	/**
	 * Een omschrijving (voor mensen begrijpbaar) van het property (bijvoorbeeld
	 * 'Achternaam').
	 */
	private final String omschrijving;

	/**
	 * Property waarop gesorteerd moet worden als dit property als groepering wordt
	 * geselecteerd.
	 */
	private final String sorteerProperty;

	/**
	 * Geeft aan of er voor elke ander value van de property een nieuwe pagina gemaakt
	 * moet worden
	 */
	private boolean startNewPage;

	public GroupProperty(String property, String omschrijving, String sorteerProperty)
	{
		this(property, omschrijving, sorteerProperty, false);
	}

	public GroupProperty(String property, String omschrijving, String sorteerProperty,
			boolean startNewPage)
	{
		this.property = property;
		this.omschrijving = omschrijving;
		this.sorteerProperty = sorteerProperty;
		this.startNewPage = startNewPage;
	}

	public String getProperty()
	{
		return property;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof GroupProperty< ? >))
			return false;
		return StringUtil.equalOrBothNull(getProperty(), ((GroupProperty<T>) other).getProperty());
	}

	public String getSorteerProperty()
	{
		return sorteerProperty;
	}

	public void setColumns(List<CustomColumn<T>> columns)
	{
		this.columns = columns;
	}

	public List<CustomColumn<T>> getColumns()
	{
		if (columns == null)
		{
			columns = new ArrayList<CustomColumn<T>>();
		}
		return columns;
	}

	public void addColumn(CustomColumn<T> column)
	{
		if (columns == null)
		{
			columns = new ArrayList<CustomColumn<T>>();
		}
		columns.add(column);
	}

	@Override
	public void detach()
	{
		for (CustomColumn<T> column : columns)
		{
			column.detach();
		}
	}

	public void setAscending(boolean ascending)
	{
		this.ascending = ascending;
	}

	public boolean isAscending()
	{
		return ascending;
	}

	public boolean getStartNewPage()
	{
		return startNewPage;
	}
}
