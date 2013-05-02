package nl.topicus.cobra.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.zoekfilters.NullFilter;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public abstract class AbstractCriteriaBuilder
{
	/**
	 * De aangemaakte aliases voor het joinen van tabellen tijdens het bouwen van de
	 * criteria.
	 */
	protected Set<String> aliases = new HashSet<String>();

	protected abstract void addCriterion(Criterion criterion);

	public abstract void createAlias(String associationPath, String alias)
			throws HibernateException;

	public abstract void createAlias(String associationPath, String alias, int joinType)
			throws HibernateException;

	/**
	 * Voegt een = of een in expressie toe, afhankelijk van of er een object geselecteerd
	 * is. Let op bij een return waarde van false kan er gestopt worden, er zijn dan geen
	 * resultaten mogelijk. indien er geen authorizationcontext is mag altijd doorgegaan
	 * worden.
	 * 
	 * @param <T>
	 *            type element
	 * @param expression
	 *            het veld dat gecontroleerd word
	 * @param selected
	 *            het geselecteerd item of null
	 * @param context
	 *            bepaald welke elementen wel geoorloofd zijn
	 * @return true als er niks aan de hand is, false als de beperking dermate is dat er
	 *         geen resultaten zijn die zullen voldoen aan de eisen.
	 */
	public <T> boolean addAllowed(String expression, T selected, AuthorizationContext<T> context)
	{
		if (selected != null)
			addCriterion(Restrictions.eq(expression, selected));
		else if (context == null)
			return true;
		else if (!context.getAllowedElements().isEmpty())
		{
			if (!context.isInstellingClearance())
				addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.in(
					expression, context.getAllowedElements())));
		}
		else
			return false;
		return true;
	}

	public <T> boolean addAllowed(String expression, T selected, Collection<T> allowed)
	{
		if (selected != null)
			addCriterion(Restrictions.eq(expression, selected));
		else if (allowed == null)
			return true;
		else if (!allowed.isEmpty())
		{
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.in(
				expression, allowed)));
		}
		else
			return false;
		return true;
	}

	/**
	 * Voegt een een in expressie toe. Hierbij worden of de geselecteerde items gebruikt
	 * of de toegestane items. Let op bij een return waarde van false kan er gestopt
	 * worden, er zijn dan geen resultaten mogelijk. indien er geen authorizationcontext
	 * is mag altijd doorgegaan worden.
	 * 
	 * @param <T>
	 *            type element
	 * @param expression
	 *            het veld dat gecontroleerd word
	 * @param selected
	 *            het geselecteerd item of null
	 * @param context
	 *            bepaald welke elementen wel geoorloofd zijn
	 * @return true als er niks aan de hand is, false als de beperking dermate is dat er
	 *         geen resultaten zijn die zullen voldoen aan de eisen.
	 */
	public <T> boolean addAllowed(String expression, Collection<T> selected,
			AuthorizationContext<T> context)
	{
		if (selected != null && !selected.isEmpty())
			addCriterion(Restrictions.in(expression, selected));
		else if (context == null)
			return true;
		else if (!context.getAllowedElements().isEmpty())
		{
			if (!context.isInstellingClearance())
				addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.in(
					expression, context.getAllowedElements())));
		}
		else
			return false;
		return true;
	}

	/**
	 * Voegt een een in expressie toe. Hierbij worden of de geselecteerde items gebruikt
	 * of de toegestane items. Let op bij een return waarde van false kan er gestopt
	 * worden, er zijn dan geen resultaten mogelijk.
	 * 
	 * @param <T>
	 *            type element
	 * @param expression
	 *            het veld dat gecontroleerd word
	 * @param selected
	 *            het geselecteerd item of null
	 * @param allowed
	 *            bepaald welke elementen wel geoorloofd zijn
	 * @return true als er niks aan de hand is, false als de beperking dermate is dat er
	 *         geen resultaten zijn die zullen voldoen aan de eisen.
	 */
	public <T> boolean addAllowed(String expression, Collection<T> selected, Collection<T> allowed)
	{
		if (selected != null && !selected.isEmpty())
			addCriterion(Restrictions.in(expression, selected));
		else if (allowed == null)
			return true;
		else if (!allowed.isEmpty())
		{
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.in(
				expression, allowed)));
		}
		else
			return false;
		return true;
	}

	/**
	 * Voegt een expressie toe waarbij 1 veld moet voldoen aan een minimum en het 2e veld
	 * aan een maximum. een waarde van null voor het minimum of maximum betekend dat deze
	 * niet gecontroleerd wordt. mimimum en maximum zijn inclusief.
	 * 
	 * @param expr1
	 *            eerste veldnaam
	 * @param expr2
	 *            tweede veldnaam
	 * @param min
	 *            minimum waarde (mag null zijn)
	 * @param max
	 *            maximum waarde (mag null zijn)
	 * @return builder
	 */
	public AbstractCriteriaBuilder addRange(String expr1, String expr2, Object min, Object max)
	{
		if (min == null)
		{
			if (max != null)
				addCriterion(Restrictions.le(expr2, max));
		}
		else if (max == null)
		{
			addCriterion(Restrictions.ge(expr1, min));
		}
		else
		{
			addCriterion(Restrictions.and(Restrictions.le(expr2, max), Restrictions.ge(expr1, min)));
		}
		return this;
	}

	/**
	 * Voegt een expressie toe die controleerd of het veld tussen 2 waardes ligt. een
	 * waarde van null voor het minimum of maximum betekend dat deze niet gecontroleerd
	 * wordt. mimimum en maximum zijn inclusief.
	 * 
	 * @param expr
	 * @param min
	 * @param max
	 * @return builder
	 */
	public AbstractCriteriaBuilder addBetween(String expr, Object min, Object max)
	{
		return addRange(expr, expr, min, max);

	}

	/**
	 * Voegt een equals expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.eq(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een equals expressie toe tussen 2 properties.
	 * 
	 * @param left
	 *            de linkerkant van het gelijk teken
	 * @param right
	 *            de rechterkant van het gelijk teken
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addPropertyEquals(String left, String right)
	{
		addCriterion(Restrictions.eqProperty(left, right));
		return this;
	}

	/**
	 * Voegt een 'equals ignoring case'expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addEqualsIgnoringCase(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.eq(expression, value).ignoreCase());
		}
		return this;
	}

	/**
	 * Voegt een 'not equals' expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van het ongelijk teken
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNotEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.ne(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een like expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van de like operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @param mode
	 *            de match mode
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addLike(String expression, String value, MatchMode mode)
	{
		if (value != null)
		{
			addCriterion(Restrictions.like(expression, replaceRegEx(value), mode));
		}
		return this;
	}

	/**
	 * Voegt een not like expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van de not like operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @param mode
	 *            de match mode
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNotLike(String expression, String value, MatchMode mode)
	{
		if (value != null)
		{
			addCriterion(Restrictions.not(Restrictions.like(expression, replaceRegEx(value), mode)));
		}
		return this;
	}

	/**
	 * @deprecated Use {@link #addILikeFixedMatchMode(String,String,MatchMode)} instead
	 */
	@Deprecated
	public AbstractCriteriaBuilder addILike(String expression, String value, MatchMode mode)
	{
		return addILikeFixedMatchMode(expression, value, mode);
	}

	/**
	 * Voegt een like expressie toe als de value niet null is, er wordt niet gekeken of
	 * value een wildcard bevat.
	 * 
	 * @param expression
	 *            de linkerkant van de ilike operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @param mode
	 *            de match mode
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addILikeFixedMatchMode(String expression, String value,
			MatchMode mode)
	{
		if (StringUtil.isNotEmpty(value))
		{
			addCriterion(Restrictions.ilike(expression, replaceRegEx(value), mode));
		}
		return this;
	}

	/**
	 * Voegt een not ilike expressie toe als de value niet null is.
	 * 
	 * @param expression
	 *            de linkerkant van de not ilike operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @param mode
	 *            de match mode
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNotILikeFixedMatchMode(String expression, String value,
			MatchMode mode)
	{
		if (value != null)
		{
			addCriterion(Restrictions
				.not(Restrictions.ilike(expression, replaceRegEx(value), mode)));
		}
		return this;
	}

	/**
	 * @deprecated Use {@link #addILikeFixedMatchMode(String,String,MatchMode)} instead
	 */
	@Deprecated
	public AbstractCriteriaBuilder addILike(String expression, String value)
	{
		addILikeCheckWildcard(expression, value, MatchMode.START);
		return this;
	}

	/**
	 * Voegt een ilike expressie toe als de value niet null is. Match mode is
	 * defaultMatchMode als de gegeven value geen wildcards bevat, en anders EXACT.
	 * 
	 * @param expression
	 *            de linkerkant van de ilike operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addILikeCheckWildcard(String expression, String value,
			MatchMode defaultMatchMode)
	{
		if (StringUtil.isNotEmpty(value))
		{
			addCriterion(createILike(expression, value, defaultMatchMode));
		}
		return this;
	}

	/**
	 * @param expression
	 * @param value
	 * @return Een criterion met een ilike-expression. Match mode is START als de gegeven
	 *         value geen wildcards bevat, en anders EXACT.
	 */
	public Criterion createILike(String expression, String value, MatchMode defaultMode)
	{
		Criterion res;
		String cValue = replaceRegEx(value);
		if (StringUtil.containsOracleWildcard(cValue))
		{
			res = Restrictions.ilike(expression, cValue, MatchMode.EXACT);
		}
		else
		{
			res = Restrictions.ilike(expression, cValue, defaultMode);
		}
		return res;
	}

	/**
	 * Voegt een ilike expressie toe als de value niet null is. Alle spaties worden
	 * vervangen voor '%' en er wordt een '%' aan het begin en het eind van de string
	 * geplakt. Hierdoor is het voor een gebruiker mogelijk om slechts een deel van een
	 * volledige naam, bestaande uit meerdere woorden, in te typen.
	 * 
	 * @param expression
	 *            de linkerkant van de ilike operand
	 * @param value
	 *            de waarde, indien <code>null</code>, dan wordt niets toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addVolledigeNaamILike(String expression, String value)
	{
		if (value != null)
		{
			String val = "%" + value.replace(" ", "%") + "%";
			addCriterion(Restrictions.ilike(expression, val, MatchMode.EXACT));
		}
		return this;
	}

	/**
	 * @param expression
	 * @param value
	 * @return Criterion dat hetzelfde doet als
	 *         {@link #addVolledigeNaamILike(String, String)}
	 */
	public Criterion getVolledigeNaamILike(String expression, String value)
	{
		if (value != null)
		{
			String val = "%" + value.replace(" ", "%") + "%";
			return Restrictions.ilike(expression, val, MatchMode.EXACT);
		}
		return null;
	}

	/**
	 * Voegt een in expressie toe als de lijst niet null of leeg (size==0) is.
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param list
	 *            de lijst, indien <code>null</code> of size==0, dan wordt niets
	 *            toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addIn(String expression, Collection< ? > list)
	{
		Criterion crit = getIn(expression, list);
		if (crit != null)
			addCriterion(crit);

		return this;
	}

	/**
	 * Voegt een in expressie toe als de lijst niet null of leeg (size==0) is.
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param list
	 *            de lijst, indien <code>null</code> of size==0, dan wordt niets
	 *            toegevoegd
	 * @return de builder
	 */
	public Criterion getIn(String expression, Collection< ? > list)
	{
		if (list != null && !list.isEmpty())
		{
			if (list.size() <= 1000)
			{
				return Restrictions.in(expression, list);
			}
			if (!(list instanceof List< ? >))
			{
				list = new ArrayList<Object>(list);
			}

			// Oracle heeft een max van 1000 elementen bij een in constructie.
			return getOrIn(expression, (List< ? >) list);
		}
		return null;
	}

	/**
	 * Voegt een in expressie toe voor het root-object als de lijst niet null of leeg
	 * (size==0) is.
	 * 
	 * @param list
	 *            de lijst, indien <code>null</code> of size==0, dan wordt niets
	 *            toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addThisIn(Collection< ? extends IdObject> list)
	{
		List<Serializable> ids = new ArrayList<Serializable>(list.size());
		for (IdObject curObject : list)
		{
			ids.add(curObject.getIdAsSerializable());
		}
		return addIn("id", ids);
	}

	private Criterion getOrIn(String expression, List< ? > list)
	{
		if (list.size() <= 2000)
		{
			return Restrictions.or(Restrictions.in(expression, list.subList(0, 1000)), Restrictions
				.in(expression, list.subList(1000, list.size())));
		}
		return Restrictions.or(Restrictions.in(expression, list.subList(0, 1000)), getOrIn(
			expression, list.subList(1000, list.size())));
	}

	/**
	 * Voegt de varargs lijst met criteria toe als (criterion1 or criterion2 or criterion3
	 * or ...)
	 * 
	 * @param list
	 * @return this
	 */
	public AbstractCriteriaBuilder addOrs(Criterion... list)
	{
		return addOrs(Arrays.asList(list));
	}

	/**
	 * Voegt de lijst met criteria toe als (criterion1 or criterion2 or criterion3 or ...)
	 * 
	 * @param list
	 * @return this
	 */
	public AbstractCriteriaBuilder addOrs(List<Criterion> list)
	{
		Asserts.assertNotEmpty("list", list);
		Disjunction ors = Restrictions.disjunction();
		for (Criterion curCrit : list)
		{
			ors.add(curCrit);
		}
		addCriterion(ors);
		return this;
	}

	/**
	 * Voegt een is null or in expressie toe als de lijst niet null of leeg (size==0) is.
	 * LET OP: een 'is null' filtering is voor een database een zware operatie omdat dit
	 * niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk is, of
	 * als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param list
	 *            de lijst, indien <code>null</code> of size==0, dan wordt niets
	 *            toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrIn(String expression, Collection< ? > list)
	{
		if (list != null && list.size() > 0)
		{
			if (list.size() <= 1000)
			{
				addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.in(
					expression, list)));
			}
			else if (list instanceof List< ? >)
			{
				// Oracle heeft een max van 1000 elementen bij een in constructie.
				addCriterion(Restrictions.or(Restrictions.isNull(expression), getOrIn(expression,
					(List< ? >) list)));
			}
			else
			{
				throw new IllegalArgumentException(
					"Collectie bevat meer dan 1000 elementen en is geen lijst.");
			}
		}
		return this;
	}

	/**
	 * Voegt een not in expressie toe als de lijst niet null of leeg (size==0) is.
	 * 
	 * @param expression
	 *            de linkerkant van het gelijk teken
	 * @param list
	 *            de lijst, indien <code>null</code> of size==0, dan wordt niets
	 *            toegevoegd
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNotIn(String expression, Collection< ? > list)
	{
		if (list != null && list.size() > 0)
		{
			if (list.size() <= 1000)
			{
				addCriterion(Restrictions.not(Restrictions.in(expression, list)));
			}
			else if (list instanceof List< ? >)
			{
				// Oracle heeft een max van 1000 elementen bij een in constructie.
				addCriterion(Restrictions.not(getOrIn(expression, (List< ? >) list)));
			}
			else
			{
				throw new IllegalArgumentException(
					"Collectie bevat meer dan 1000 elementen en is geen lijst.");
			}
		}
		return this;
	}

	/**
	 * Voegt een 'greater than or equals' ('>=') expressie toe als de waarde niet null is.
	 * 
	 * @param expression
	 * @param value
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addGreaterOrEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.ge(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een 'less than or equals' ('&lt;=') expressie toe als de waarde niet null is.
	 * 
	 * @param expression
	 * @param value
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addLessOrEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.le(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een 'greater than' ('&gt;') expressie toe als de waarde niet null is.
	 * 
	 * @param expression
	 * @param value
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addGreaterThan(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.gt(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een 'less than' ('&lt;') expressie toe als de waarde niet null is.
	 * 
	 * @param expression
	 * @param value
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addLessThan(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.lt(expression, value));
		}
		return this;
	}

	/**
	 * Voegt een 'expression is null or expression>=value' expressie toe als value niet
	 * null is. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null moet zijn, of anders gelijk aan de gegeven waarde.
	 * @param value
	 *            De expression moet of null zijn, of gelijk aan deze waarde. Mag null
	 *            zijn dan doet dit niks.
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrEquals(String expression, Object value)
	{
		if (value != null)
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.eq(
				expression, value)));
		return this;
	}

	/**
	 * Voegt een 'expression is null or not expression=value' expressie toe als value niet
	 * null is. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null moet zijn, of anders gelijk aan de gegeven waarde.
	 * @param value
	 *            De expression moet of null zijn, of gelijk aan deze waarde. Mag null
	 *            zijn dan doet dit niks.
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrNotEquals(String expression, Object value)
	{
		if (value != null)
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.ne(
				expression, value)));
		return this;
	}

	/**
	 * Voegt een 'expression is null or expression>=value' expressie toe als value niet
	 * null is. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null moet zijn, of anders groter/gelijk aan de gegeven
	 *            waarde.
	 * @param value
	 *            De expression moet of null zijn, of groter/gelijk aan deze waarde.
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrGreaterOrEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.ge(
				expression, value)));
		}
		return this;
	}

	/**
	 * Voegt een 'expression is null or expression>value' expressie toe als value niet
	 * null is. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null moet zijn, of anders groter aan de gegeven waarde.
	 * @param value
	 *            De expression moet of null zijn, of groter aan deze waarde.
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrGreaterThan(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.gt(
				expression, value)));
		}
		return this;
	}

	/**
	 * Voegt een 'expression is null or expression<=value' expressie toe als value niet
	 * null is. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null moet zijn, of anders kleiner/gelijk aan de gegeven
	 *            waarde.
	 * @param value
	 *            De expression moet of null zijn, of kleiner/gelijk aan deze waarde.
	 * @return de builder
	 */
	public AbstractCriteriaBuilder addNullOrLessOrEquals(String expression, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.or(Restrictions.isNull(expression), Restrictions.le(
				expression, value)));
		}
		return this;
	}

	/**
	 * Voegt een nullfilter toe voor de gegeven expression op basis van het gegeven
	 * nullfilter. LET OP: een 'is null' filtering is voor een database een zware operatie
	 * omdat dit niet te indexeren is. Gebruik dit daarom alleen als het echt noodzakelijk
	 * is, of als het om een kleine tabel gaat!
	 * 
	 * @param expression
	 *            De expressie die null of niet-null moet opleveren.
	 * @param nullFilter
	 *            Een waarde die aangeeft of en welk filter geplaatst moet worden. Als
	 *            nullFilter null of DontCare is, wordt geen filter geplaatst. Als
	 *            nullFilter gelijk aan IsNull is, wordt een 'is null' toegevoegd. Als
	 *            nullFilter gelijk aan IsNotNull is, wordt een 'is not null' toegevoegd.
	 * @return de builder.
	 */
	public AbstractCriteriaBuilder addNullFilterExpression(String expression, NullFilter nullFilter)
	{
		if (nullFilter != null)
		{
			if (nullFilter == NullFilter.IsNull)
			{
				addCriterion(Restrictions.isNull(expression));
			}
			else if (nullFilter == NullFilter.IsNotNull)
			{
				addCriterion(Restrictions.isNotNull(expression));
			}
		}
		return this;
	}

	/**
	 * Voegt een OR expressie toe obv ilike als de value niet null is. Match mode is START
	 * als de gegeven value geen wildcards bevat, en anders EXACT.
	 * 
	 * @param expression1
	 * @param expression2
	 * @param value
	 * @return Deze builder.
	 */
	public AbstractCriteriaBuilder addOrILike(String expression1, String expression2, String value)
	{
		if (value != null)
		{
			String cValue = replaceRegEx(value);
			MatchMode mode = MatchMode.START;
			if (StringUtil.containsOracleWildcard(cValue))
			{
				mode = MatchMode.EXACT;
			}
			addCriterion(Restrictions.or(Restrictions.ilike(expression1, cValue, mode),
				Restrictions.ilike(expression2, cValue, mode)));
		}
		return this;
	}

	/**
	 * Voegt een OR expressie toe obv ilike als de value niet null is.
	 * 
	 * @param expression1
	 * @param expression2
	 * @param value
	 * @param mode
	 * @return Deze builder.
	 */
	public AbstractCriteriaBuilder addOrILike(String expression1, String expression2, String value,
			MatchMode mode)
	{
		if (value != null)
		{
			String cValue = replaceRegEx(value);
			addCriterion(Restrictions.or(Restrictions.ilike(expression1, cValue, mode),
				Restrictions.ilike(expression2, cValue, mode)));
		}
		return this;
	}

	/**
	 * Voegt een OR expressie toe obv equals als de value niet null is.
	 * 
	 * @param expression1
	 * @param expression2
	 * @param value
	 * @return Deze builder.
	 */
	public AbstractCriteriaBuilder addOrEquals(String expression1, String expression2, Object value)
	{
		if (value != null)
		{
			addCriterion(Restrictions.or(Restrictions.eq(expression1, value), Restrictions.eq(
				expression2, value)));
		}
		return this;
	}

	/**
	 * Voegt een OR expressie toe obv in als de value niet null of leeg is.
	 * 
	 * @param expression1
	 * @param expression2
	 * @param value
	 * @return Deze builder.
	 */
	public AbstractCriteriaBuilder addOrIn(String expression1, String expression2,
			Collection< ? > value)
	{
		if (value != null)
		{
			Criterion crit1 = getIn(expression1, value);
			Criterion crit2 = getIn(expression2, value);
			if (crit1 != null && crit2 != null)
				addCriterion(Restrictions.or(crit1, crit2));
		}
		return this;
	}

	/**
	 * vervangt het '*' door '%'
	 * 
	 * @param value
	 * @return value
	 */
	public String replaceRegEx(String value)
	{
		return value.replace('*', '%');
	}

	/**
	 * Voegt een isNull of een IsNotNull restrictie toe afhankelijke van de waarde van
	 * isNull. Gebruik dit alleen als je geen andere opties hebt, omdat een isNull check
	 * voor een database altijd lastig is. Null-waarden worden namelijk niet meegenomen in
	 * indices...
	 * 
	 * @param expression
	 * @param isNull
	 *            True --> expression moet null zijn, False --> expression moet not null
	 *            zijn, <null> --> er wordt geen restrictie toegevoegd
	 * @return Deze builder
	 */
	public AbstractCriteriaBuilder addIsNull(String expression, Boolean isNull)
	{
		Criterion crit = getIsNull(expression, isNull);
		if (crit != null)
			addCriterion(crit);

		return this;
	}

	public Criterion getIsNull(String expression, Boolean isNull)
	{
		if (isNull != null)
		{
			if (isNull.booleanValue())
			{
				return Restrictions.isNull(expression);
			}
			else
			{
				return Restrictions.isNotNull(expression);
			}
		}
		return null;
	}

	/**
	 * Voegt een propertyIn toe aan deze criteria. Is gelijk aan
	 * {@link Subqueries#propertyIn(String, DetachedCriteria)}.
	 */
	public AbstractCriteriaBuilder propertyIn(String propertyName, DetachedCriteria dc)
	{
		addCriterion(Subqueries.propertyIn(propertyName, dc));

		return this;
	}

	/**
	 * Voegt een propertyNotIn toe aan deze criteria. Is gelijk aan
	 * {@link Subqueries#propertyNotIn(String, DetachedCriteria)}.
	 */
	public AbstractCriteriaBuilder propertyNotIn(String propertyName, DetachedCriteria dc)
	{
		addCriterion(Subqueries.propertyNotIn(propertyName, dc));

		return this;
	}
}
