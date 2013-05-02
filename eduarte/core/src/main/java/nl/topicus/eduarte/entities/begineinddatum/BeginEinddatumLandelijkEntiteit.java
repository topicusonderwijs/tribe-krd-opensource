package nl.topicus.eduarte.entities.begineinddatum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Index;

/**
 * Base entiteit voor landelijke entiteiten met een begin- en einddatum.
 * 
 * @author loite
 */
@MappedSuperclass()
public abstract class BeginEinddatumLandelijkEntiteit extends LandelijkEntiteit implements
		IBeginEinddatumEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	@Index(name = "GENERATED_NAME_begin")
	private Date begindatum = TimeUtil.getInstance().currentDate();

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date einddatum;

	/**
	 * Dit property wordt automatisch bijgehouden bij het setten van de echte einddatum,
	 * en wordt gebruikt bij zoekacties. Het is namelijk veel sneller om te zoeken op een
	 * not null datum dan op een mogelijke null-waarde.
	 */
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	@AutoForm(include = false)
	@Index(name = "GENERATED_NAME_eind")
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private Date einddatumNotNull = MAX_DATE;

	public BeginEinddatumLandelijkEntiteit()
	{
	}

	public Date getBegindatum()
	{
		return begindatum;
	}

	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
		this.einddatumNotNull = (einddatum == null ? MAX_DATE : einddatum);
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return BeginEinddatumUtil.isActief(this, peildatum);
	}

	@Override
	public boolean isActief()
	{
		return BeginEinddatumUtil.isActief(this, TimeUtil.getInstance().currentDate());
	}

	public Date getEinddatumNotNull()
	{
		return einddatumNotNull;
	}

	@Exportable
	public String getGeldigVanTotBeschrijving()
	{
		String geldigVanTotBeschrijving = null;

		if (getBegindatum() != null)
		{
			geldigVanTotBeschrijving = TimeUtil.getInstance().formatDate(getBegindatum()) + " t/m ";
			if (getEinddatum() != null)
			{
				geldigVanTotBeschrijving += TimeUtil.getInstance().formatDate(getEinddatum());
			}
			else
			{
				geldigVanTotBeschrijving += "...";
			}
		}
		else
		{
			geldigVanTotBeschrijving = "Onbekend";
		}

		return geldigVanTotBeschrijving;
	}
}
