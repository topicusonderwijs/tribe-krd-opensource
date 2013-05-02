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
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Landentabel uit de basisadministratie Persoonsgegevens en Reisdocumenten van het
 * Ministerie van Binnenlandse Zaken en Koninkrijksrelaties. Dit betreft tabel 34 van het
 * GBA te vinden op <a href="http://www.bprbzk.nl/GBA/Informatiebank/Procedures/Landelijke_tabellen/Landelijke_Tabellen_32_t_m_56_excl_35"
 * >bprbzk.nl</a>
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@IsViewWhenOnNoise
public class Land extends BeginEinddatumLandelijkEntiteit implements ICodeNaamEntiteit
{
	private static final long serialVersionUID = 1L;

	public static final String CODE_NEDERLAND = "6030";

	public static final String CODE_BELGIE = "5010";

	public static final String CODE_LUXEMBURG = "6018";

	public static final String CODE_DUITSLAND = "9089";

	private static final String CODE_ONBEKEND = "0000";

	@Column(length = 4, nullable = false)
	@Index(name = "idx_Land_code")
	private String code;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Land_naam")
	private String naam;

	@Column(length = 2, nullable = true)
	@Index(name = "idx_Land_isocode")
	private String isoCode;

	public Land()
	{
	}

	@AutoForm(label = "Land")
	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

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

	/**
	 * Is dit Nederland? Niet exportable, want dit interfereert met de static methode
	 * getNederland().
	 * 
	 * @return true als dit land gelijk is aan Nederland
	 */
	public boolean isNederland()
	{
		return CODE_NEDERLAND.equals(getCode());
	}

	public boolean isBenelux()
	{
		return CODE_NEDERLAND.equals(getCode()) || CODE_BELGIE.equals(getCode())
			|| CODE_LUXEMBURG.equals(getCode());
	}

	public boolean isDuitsland()
	{
		return CODE_DUITSLAND.equals(getCode());
	}

	private static LandDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(LandDataAccessHelper.class);
	}

	public static Land getNederland()
	{
		return getHelper().get(CODE_NEDERLAND);
	}

	public static Land getOnbekend()
	{
		return getHelper().get(CODE_ONBEKEND);
	}

	/**
	 * @param code
	 * @return het land met de gegeven code
	 */
	public static Land getLand(String code)
	{
		if (StringUtil.isEmpty(code))
			return null;
		return getHelper().get(code);
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setIsoCode(String isoCode)
	{
		this.isoCode = isoCode;
	}

	@Exportable
	public String getIsoCode()
	{
		return isoCode;
	}

}
