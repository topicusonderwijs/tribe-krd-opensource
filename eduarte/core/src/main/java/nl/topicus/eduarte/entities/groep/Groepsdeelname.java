package nl.topicus.eduarte.entities.groep;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.search.browser.SearchResultsPanel.SearchBrowserEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Deelname aan een groep door een deelnemer.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
@Table(appliesTo = "Groepsdeelname", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class Groepsdeelname extends BeginEinddatumInstellingEntiteit implements DeelnemerProvider,
		GroepProvider, PersoonProvider, Comparable<Groepsdeelname>, SearchBrowserEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Nullable = true omdat er subclasses (persoonlijkeGroepDeelnemer) zijn waarbij
	 * deelnemer niet verplicht is.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_Groepsdeelname_deelnemer")
	private Deelnemer deelnemer;

	/**
	 * Nullable = true omdat er subclasses (Plaatsing) zijn waarbij groep niet verplicht
	 * is.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = true)
	@Index(name = "idx_Groepsdeelname_groep")
	@AutoForm(htmlClasses = "unit_max")
	protected Groep groep;

	public Groepsdeelname()
	{
	}

	@Exportable
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	@Exportable
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	@Override
	public int compareTo(Groepsdeelname o)
	{
		int res = getDeelnemer().compareTo(o.getDeelnemer());
		if (res == 0)
		{
			res = getBegindatum().compareTo(o.getBegindatum());
		}
		return res;
	}

	private static final String[] SEARCH_BROWSER_PROPERTIES =
		{"deelnemer.deelnemernummer", "deelnemer.persoon.volledigeNaam",
			"deelnemer.eersteInschrijvingOpPeildatum.opleiding.naam"};

	@Override
	public String[] getSearchBrowserProperties()
	{
		return SEARCH_BROWSER_PROPERTIES;
	}

	@Override
	public Persoon getPersoon()
	{
		return getDeelnemer().getPersoon();
	}

	@Override
	public String toString()
	{
		if (getDeelnemer() != null)
			return getDeelnemer().getPersoon().getVolledigeNaam();
		else if (getGroep() != null)
			return getGroep().getCode() + " - " + getGroep().getCode();
		return "";
	}
}
