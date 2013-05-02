/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.NationaliteitDataAccessHelper;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Nationaliteittabel.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@IsViewWhenOnNoise
public class Nationaliteit extends BeginEinddatumLandelijkEntiteit implements ICodeNaamEntiteit
{
	private static final long serialVersionUID = 1L;

	private static final String GBA_CODE_NEDERLANDS = "0001";

	private static final String GBA_CODE_ZWITSERS = "0081";

	private static final String GBA_CODE_SURINAAMS = "0263";

	@Column(nullable = false)
	@Index(name = "idx_Nationaliteit_code")
	private String code;

	@Column(length = 100)
	@Index(name = "idx_Nationaliteit_naam")
	private String naam;

	/**
	 * Europese Economische Ruimte (België, Bulgarije, Cyprus, Denemarken, Duitsland,
	 * Estland, Finland, Frankrijk, Griekenland, Groot-Brittannië, Hongarije, Ierland,
	 * Italië, Letland, Liechtenstein, Litouwen, Luxemburg, Malta, Nederland, Noorwegen,
	 * Oostenrijk, Polen, Portugal, Roemenië, Slovenië, Slowakije, Spanje, Tsjechië,
	 * IJsland en Zweden)
	 */
	private boolean eer;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Nationaliteit()
	{
	}

	/**
	 * @return the naam
	 */
	@Exportable
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            the naam to set
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	private static NationaliteitDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(NationaliteitDataAccessHelper.class);
	}

	/**
	 * @return de nederlandse nationaliteit
	 */
	public static Nationaliteit getNederlands()
	{
		return getHelper().get(GBA_CODE_NEDERLANDS);
	}

	/**
	 * @param code
	 * @return De nationaliteit met de gegeven code
	 */
	public static Nationaliteit getNationaliteit(String code)
	{
		if (StringUtil.isEmpty(code))
			return null;
		return getHelper().get(code);
	}

	/**
	 * @return Returns the code.
	 */
	@Exportable
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @return Returns string representation.
	 */
	@Override
	public String toString()
	{
		String res = getCode() + " - " + getNaam();
		if (getEinddatum() != null
			|| getBegindatum().after(EduArteApp.MIN_BEGINDATUM_VOOR_TOSTRING))
		{
			res = res + " (";
			if (getBegindatum().after(EduArteApp.MIN_BEGINDATUM_VOOR_TOSTRING))
			{
				res = res + "vanaf " + TimeUtil.getInstance().formatDate(getBegindatum());
				if (getEinddatum() != null)
				{
					res = res + " ";
				}
			}
			if (getEinddatum() != null)
			{
				res = res + "tot " + TimeUtil.getInstance().formatDate(getEinddatum());
			}
			res = res + ")";
		}
		return res;
	}

	public void setEer(boolean eer)
	{
		this.eer = eer;
	}

	/**
	 * @return true als de nationaliteit deel uitmaakt van de landen van de Europese
	 *         Economische Ruimte (EU + IJsland, Noorwegen, Liechtenstein)
	 */
	public boolean isEer()
	{
		return eer;
	}

	/**
	 * @return true als deze nationaliteit in het HO bekostigbaar is en de student dus in
	 *         aanmerking komt voor wettelijk collegegeld ipv instellingscollegegeld. Dit
	 *         is het geval bij EER-nationaliteiten, de Surinaamse en de Zwitserse
	 *         nationaliteit.
	 */
	public boolean isHOBekostigbaar()
	{
		return isEer() || code.equals(GBA_CODE_SURINAAMS) || code.equals(GBA_CODE_ZWITSERS);
	}

}
