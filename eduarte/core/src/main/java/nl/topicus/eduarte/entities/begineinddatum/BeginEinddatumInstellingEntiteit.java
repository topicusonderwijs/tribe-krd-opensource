package nl.topicus.eduarte.entities.begineinddatum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.criho.annot.Criho;

import org.hibernate.annotations.Index;

/**
 * Basisentiteit voor alle instellingsentiteiten die een begin- en einddatum hebben.
 * Begindatum is not-nullable en einddatum is nullable.
 * 
 * @author loite
 */
@MappedSuperclass()
public abstract class BeginEinddatumInstellingEntiteit extends InstellingEntiteit implements
		IBeginEinddatumEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De eerste dag waarop de entiteit geldig wordt. Is Bron veld voor onder andere
	 * {@link nl.topicus.eduarte.entities.inschrijving.Verbintenis} en
	 * {@link nl.topicus.eduarte.entities.bpv.BPVInschrijving}.
	 */
	@Temporal(value = TemporalType.DATE)
	@Bron
	@Criho
	@Column(nullable = false)
	@Index(name = "GENERATED_NAME_begin")
	@AutoForm(htmlClasses = "unit_80")
	private Date begindatum = TimeUtil.getInstance().currentDate();

	/**
	 * De laatste dag waarop de entiteit geldig wordt. Is Bron veld voor onder andere
	 * {@link nl.topicus.eduarte.entities.inschrijving.Verbintenis}.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@Bron
	@Criho
	@AutoForm(htmlClasses = "unit_80")
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

	public BeginEinddatumInstellingEntiteit()
	{
	}

	@Override
	public Date getBegindatum()
	{
		return begindatum;
	}

	public String getBegindatumFormatted()
	{
		return TimeUtil.getInstance().formatDate(getBegindatum());
	}

	/**
	 * Zet de begindatum. Hoeft alleen gebruikt te worden als er een specifieke reden is
	 * om een andere datum te gebruiken dan vandaag, bijvoorbeeld de registratiedatum.
	 * 
	 * @param begindatum
	 *            The begindatum to set.
	 */
	@Override
	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	@Override
	public Date getEinddatum()
	{
		return einddatum;
	}

	public String getEinddatumFormatted()
	{
		return TimeUtil.getInstance().formatDate(getEinddatum());
	}

	@Override
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

	public boolean isActiefTijdens(Date startDate, Date endDate)
	{
		return BeginEinddatumUtil.isActiefTijdens(this, startDate, endDate);
	}

	public boolean isActiefTijdens(IBeginEinddatumEntiteit entiteit)
	{
		return isActiefTijdens(entiteit.getBegindatum(), entiteit.getEinddatum());
	}

	@Override
	public boolean isActief()
	{
		return isActief(TimeUtil.getInstance().currentDate());
	}

	public boolean isBeeindigd()
	{
		return getEinddatum() != null
			&& TimeUtil.getInstance().currentDate().getTime() >= getEinddatum().getTime();
	}

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

	@Override
	public Date getEinddatumNotNull()
	{
		return einddatumNotNull;
	}

}
