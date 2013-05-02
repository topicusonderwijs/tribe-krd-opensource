package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelToekenningsRegelDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.PeriodeIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelToekenningsRegelZoekFilter;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een PeriodeIndeling waaraan een lijst met periodes hangt
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PeriodeIndeling extends InstellingEntiteit implements OrganisatieEenheidProvider
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_200")
	private String omschrijving;

	/*
	 * bevat het jaar waarin het schoojaar start
	 */
	@Column(nullable = false)
	@AutoForm(include = false)
	private int schooljaar;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "periodeIndeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	@OrderBy("volgnummer")
	private List<Periode> periodeList = new ArrayList<Periode>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_PeriodeIndel_organi")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	public PeriodeIndeling()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	/**
	 * Voegt een nieuw periode toe aan de PeriodeIndeling. Initialiseert ook de lijst van
	 * {@link Periode} als deze nug null is
	 * 
	 * @param periode
	 *            De periode die moet worden toegevoegd.
	 */
	public void addPeriode(Periode periode)
	{

		getPeriodeList().add(periode);
	}

	/**
	 * Verwijderd de periode indeling uit de lijst van periode indelingen
	 * 
	 * @param periode
	 */
	public void removePeriode(Periode periode)
	{
		getPeriodeList().remove(periode);
	}

	public List<Periode> getPeriodeList()
	{
		return periodeList;
	}

	public void setPeriodeList(List<Periode> periodes)
	{
		this.periodeList = periodes;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	@AutoForm(editorClass = CohortCombobox.class, label = "Schooljaar", htmlClasses = "unit_200", required = true)
	public Cohort getSchooljaarObj()
	{
		return Cohort.getCohort(schooljaar);
	}

	public void setSchooljaarObj(Cohort beginjaar)
	{

		this.schooljaar = TimeUtil.getInstance().getYear(beginjaar.getBegindatum());
	}

	public Periode getPeriodesVanPeildatum(Date peilDatum)
	{
		for (Periode periode : getPeriodeList())
		{
			if (periode.getDatumBegin().before(peilDatum)
				&& periode.getDatumEind().after(peilDatum))
				return periode;
		}
		return null;
	}

	/**
	 * @return ingebruik
	 */
	public boolean inGebruik()
	{
		MaatregelToekenningsRegelZoekFilter filter = new MaatregelToekenningsRegelZoekFilter();
		filter.setPeriodeIndeling(this);
		MaatregelToekenningsRegelDataAccessHelper helper =
			DataAccessRegistry.getHelper(MaatregelToekenningsRegelDataAccessHelper.class);
		List<MaatregelToekenningsRegel> list = helper.list(filter);
		if (list != null && !list.isEmpty())
			return true;
		return false;
	}

	/**
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 */
	public boolean bestaatEnActief()
	{
		PeriodeIndelingDataAccessHelper helper =
			DataAccessRegistry.getHelper(PeriodeIndelingDataAccessHelper.class);
		return helper.bestaat(this);
	}

	/**
	 * @return Returns the schooljaar.
	 */
	public int getSchooljaar()
	{
		return schooljaar;
	}

	/**
	 * @param schooljaar
	 *            The schooljaar to set.
	 */
	public void setSchooljaar(int schooljaar)
	{
		this.schooljaar = schooljaar;
	}
}
