package nl.topicus.eduarte.entities.adres;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkOfInstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Entiteit welke een enkel adres bevat.
 * 
 * @author hoeve
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "AdresEntiteit", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public abstract class AdresEntiteit<T extends AdresEntiteit<T>> extends
		BeginEinddatumLandelijkOfInstellingEntiteit implements TransientIdObject
{
	private static final long serialVersionUID = 1L;

	/**
	 * Eager omdat vanuit persoon een lijst met alle adressen opgevraagd wordt. De
	 * adressen zijn dus in het algemeen niet beschikbaar in de cache.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "adres", nullable = false)
	@Index(name = "idx_AdresE_adres")
	@AutoFormEmbedded
	@Bron
	private Adres adres = new Adres(this);

	@Column(nullable = false)
	@Bron
	private boolean postadres;

	@Column(nullable = false)
	@Bron
	private boolean fysiekadres;

	@Column(nullable = false)
	private boolean factuuradres;

	/**
	 * Gebruik newAdres() van bijbehorende Adresseerbaar entiteit.
	 */
	protected AdresEntiteit()
	{
	}

	@Exportable
	public Adres getAdres()
	{
		return adres;
	}

	public void setAdres(Adres adres)
	{
		this.adres = adres;
	}

	public boolean isPostadres()
	{
		return postadres;
	}

	public void setPostadres(boolean postadres)
	{
		this.postadres = postadres;
	}

	public boolean isFysiekadres()
	{
		return fysiekadres;
	}

	public void setFysiekadres(boolean fysiekadres)
	{
		this.fysiekadres = fysiekadres;
	}

	public boolean isFactuuradres()
	{
		return factuuradres;
	}

	public void setFactuuradres(boolean factuuradres)
	{
		this.factuuradres = factuuradres;
	}

	public final String getFysiekLabel()
	{
		return getEntiteit().getFysiekAdresOmschrijving();
	}

	public boolean isPostEnFysiekAdres()
	{
		return isPostadres() && isFysiekadres();
	}

	@Override
	public String toString()
	{
		return getAdres().toString();
	}

	public String getType()
	{
		StringBuilder sb = new StringBuilder();

		if (isPostadres())
		{
			sb.append("P");
		}
		if (isFysiekadres())
		{
			sb.append(getFysiekLabel().charAt(0));
		}
		if (isFactuuradres())
		{
			sb.append("F");
		}

		return sb.toString();
	}

	public abstract Adresseerbaar<T> getEntiteit();

	public abstract void setEntiteit(Adresseerbaar<T> entiteit);

}
