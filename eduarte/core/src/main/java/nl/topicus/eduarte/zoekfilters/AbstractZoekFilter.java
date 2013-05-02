package nl.topicus.eduarte.zoekfilters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.cobra.zoekfilters.QuickSearchZoekFilter;
import nl.topicus.eduarte.app.EduArteContext;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basis klasse voor alle wicket zoek filters. Deze klasse houdt vast wat het start item
 * is dat wordt opgehaald, en het maximum aantal resultaten dat opgehaald moet worden.
 * 
 * @param <T>
 *            Type dat het zoekfilter oplevert
 */
public abstract class AbstractZoekFilter<T> implements QuickSearchZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	public static enum GearchiveerdMode
	{
		Alles
		{
			@Override
			protected Boolean getGearchiveerd()
			{
				return null;
			}
		},
		NietGearchiveerd
		{
			@Override
			protected Boolean getGearchiveerd()
			{
				return Boolean.FALSE;
			}
		},
		AlleenGearchiveerd
		{
			@Override
			protected Boolean getGearchiveerd()
			{
				return Boolean.TRUE;
			}
		};

		protected abstract Boolean getGearchiveerd();

		private static GearchiveerdMode getMode(Boolean gearchiveerd)
		{
			for (GearchiveerdMode mode : values())
			{
				if (mode.getGearchiveerd() == gearchiveerd)
				{
					return mode;
				}
			}
			throw new IllegalStateException("Should not happen!");
		}
	}

	/** voor logging. */
	private static final Logger log = LoggerFactory.getLogger(AbstractZoekFilter.class);

	/**
	 * Properties waarop gesorteerd moet worden.
	 */
	private List<String> orderByList = new ArrayList<String>();

	/**
	 * Is de order by ascending?
	 */
	private boolean ascending = true;

	private boolean resultCacheable = true;

	private boolean unique = false;

	private String quickSearchQuery;

	/**
	 * Haal gearchiveerde gegevens op? Set op null om alles op te halen.
	 */
	private Boolean gearchiveerd = Boolean.FALSE;

	private IModel<Date> peildatumModel = EduArteContext.get().getPeildatumModel();

	/**
	 * Default constructor.
	 */
	public AbstractZoekFilter()
	{
	}

	/**
	 * Zorgt ervoor dat alle IModel afhankelijkheden een detach event ontvangen op het
	 * moment dat dit filter wordt ge-detached. Dit zorgt voor een optimaal sessie
	 * gebruik.
	 * 
	 * @see IModel#detach()
	 */
	public final void detach()
	{
		// detach alle models van elk subtype van AbstractZoekFilter
		Class< ? > clazz = getClass();
		while (AbstractZoekFilter.class.isAssignableFrom(clazz))
		{
			detachModels(clazz);
			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * Wraps a null check around getting the object from the model.
	 * 
	 * @param model
	 * @return the object from the model or null if the model is null.
	 */
	protected <Y> Y getModelObject(IModel<Y> model)
	{
		if (model != null)
			return model.getObject();
		return null;
	}

	/**
	 * Wraps a null check around building a model for a value.
	 * 
	 * @param object
	 * @return a new IModel or null if the object is null.
	 */
	protected <Y> IModel<Y> makeModelFor(Y object)
	{
		if (object != null)
			return ModelFactory.getModel(object);
		return null;
	}

	/**
	 * @param list
	 * @return a new IModel or null if the list is null
	 */
	protected <Y, Z extends Collection<Y>> IModel<Z> makeModelFor(Z list)
	{
		if (list != null)
			return ModelFactory.getListModel(list);
		return null;
	}

	/**
	 * Voegt het property toe aan het begin van de order by list. Indien het property al
	 * het eerste element in de lijst is, wordt de sorteervolgorde geflipt.
	 * 
	 * @param property
	 *            Het property dat aan het begin van de order by list moet komen.
	 */
	public void addOrderByProperty(String property)
	{
		String[] properties = property.split(",");
		if (properties.length == 1)
		{
			// Controleer of het meegegeven property het eerste item in deze lijst is.
			if (orderByList.size() > 0)
			{
				if (orderByList.get(0).equals(property))
				{
					ascending = !ascending;
					return;
				}
			}
			ascending = true;
			orderByList.remove(property);
			orderByList.add(0, property);
		}
		else
		{
			if (orderByList.size() > 0 && orderByList.get(0).equals(properties[0].trim()))
			{
				ascending = !ascending;
				return;
			}
			for (int i = properties.length - 1; i >= 0; i--)
			{
				String trim = properties[i].trim();
				orderByList.remove(trim);
				orderByList.add(0, trim);
			}
			ascending = true;
		}
	}

	/**
	 * @return Het eerste property in de order by list, of null indien de lijst leeg is.
	 */
	public String getOrderBy()
	{
		if (orderByList.size() > 0)
			return orderByList.get(0);
		return null;
	}

	public boolean isAscending()
	{
		return ascending;
	}

	public void setAscending(boolean ascending)
	{
		this.ascending = ascending;
	}

	public List<String> getOrderByList()
	{
		return orderByList;
	}

	public void setOrderByList(List<String> orderByList)
	{
		this.orderByList = orderByList;
	}

	/**
	 * Checkt of de {@link #getOrderByList()} een property bevat die begint met `alias`.
	 * 
	 * @param alias
	 * @return true als de {@link #getOrderByList()} een property bevat die begint met
	 *         `alias`
	 */
	public boolean orderByListContainsAlias(String alias)
	{
		return StringUtil.containsStringStartingWith(getOrderByList(), alias);
	}

	/**
	 * Roept detach aan op alle fields van deze class die IModel implementeren.
	 */
	protected void detachModels(Class< ? > clazz)
	{
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			if (IDetachable.class.isAssignableFrom(field.getType()))
			{
				// het is een IModel veld dus:
				boolean original = field.isAccessible();
				field.setAccessible(true);
				try
				{
					detachQuietly((IDetachable) field.get(this));
				}
				catch (Exception e)
				{
					log.error("Kan " + field.getName() + " niet detachen", e);
				}
				finally
				{
					field.setAccessible(original);
				}
			}
		}
	}

	/**
	 * Voert een detach uit op de detachable.
	 * 
	 * @param detachable
	 *            het model dat gedetached moet worden (mag <code>null</code> zijn).
	 */
	protected void detachQuietly(IDetachable detachable)
	{
		if (detachable != null)
		{
			detachable.detach();
		}
	}

	/**
	 * Voert een detach uit op het filter.
	 * 
	 * @param filter
	 *            filter dat gedetached moet worden (mag <code>null</code> zijn).
	 */
	protected void detachQuietly(DetachableZoekFilter< ? > filter)
	{
		if (filter != null)
		{
			filter.detach();
		}
	}

	/**
	 * Voert equals uit op elk veld van deze class.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (!(other.getClass().equals(getClass())))
			return false;
		AbstractZoekFilter< ? > otherFilter = (AbstractZoekFilter< ? >) other;

		// Vraag alle properties van dit object op.
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				Object thisValue = field.get(this);
				Object otherValue = field.get(otherFilter);
				if (thisValue == null && otherValue != null)
					return false;
				if (thisValue != null && otherValue == null)
					return false;
				if (thisValue != null && !(thisValue.equals(otherValue)))
					return false;
			}
			catch (IllegalAccessException e)
			{
				log.error(e.toString(), e);
				return false;
			}
		}
		return true;
	}

	/**
	 * @param searchString
	 *            originele opgegeven searchcriteria inclusief wildcard '*';
	 * @return string welke de wildcards in de searchcriteria aanpast.
	 */
	public String likeGeneration(String searchString)
	{
		String res = searchString.replace('*', '%');
		if (!res.endsWith("%"))
			res = res + "%";

		return res;
	}

	public final Date getPeildatum()
	{
		return getModelObject(peildatumModel);
	}

	public void setPeildatum(Date peildatum)
	{
		peildatumModel.setObject(peildatum);
	}

	public IModel<Date> getPeildatumModel()
	{
		return peildatumModel;
	}

	public void setCustomPeildatumModel(IModel<Date> peildatumModel)
	{
		this.peildatumModel = peildatumModel;
	}

	@Override
	public boolean isResultCacheable()
	{
		return resultCacheable;
	}

	@Override
	public void setResultCacheable(boolean resultCacheable)
	{
		this.resultCacheable = resultCacheable;
	}

	public String getQuickSearchQuery()
	{
		return quickSearchQuery;
	}

	public void setQuickSearchQuery(String quickSearchQuery)
	{
		this.quickSearchQuery = quickSearchQuery;
	}

	public Boolean getGearchiveerd()
	{
		return gearchiveerd;
	}

	public GearchiveerdMode getGearchiveerdMode()
	{
		return GearchiveerdMode.getMode(gearchiveerd);
	}

	public void setGearchiveerdMode(GearchiveerdMode mode)
	{
		Asserts.assertNotNull("mode", mode);
		this.gearchiveerd = mode.getGearchiveerd();
	}

	public void addQuickSearchCriteria(CriteriaBuilder builder, String... properties)
	{
		if (properties == null || properties.length == 0
			|| StringUtil.isEmpty(getQuickSearchQuery()))
			return;

		String[] searchValues = getQuickSearchQuery().split(" - ");

		if (searchValues.length == 0)
			return;

		List<Criterion> orsList = new ArrayList<Criterion>();

		for (String searchValue : searchValues)
		{
			for (String property : properties)
			{
				MatchMode mode = isMatchModeStart(property) ? MatchMode.START : MatchMode.ANYWHERE;
				orsList.add(Restrictions.ilike(property, builder.replaceRegEx(searchValue.trim()),
					mode));
			}
		}

		builder.addOrs(orsList);
	}

	private boolean isMatchModeStart(String property)
	{
		return property.toLowerCase().contains("code") || property.equals("afkorting");
	}

	public String getVrijVeldWaarde(@SuppressWarnings("unused") String naam)
	{
		return null;
	}

	public void setUnique(boolean unique)
	{
		this.unique = unique;
	}

	public boolean isUnique()
	{
		return unique;
	}
}
