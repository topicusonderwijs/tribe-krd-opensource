/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link IBronSignaal} .
 * 
 * @author vandekamp
 */
public class BronSignaalZoekFilter extends AbstractBronZoekFilter<IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = JaNeeCombobox.class, htmlClasses = "unit_80")
	private Boolean geaccordeerd;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_120")
	private BronOnderwijssoort bronOnderwijssoort;

	@AutoForm(htmlClasses = "unit_30", label = "Batchnr")
	private Integer batchNummer;

	@AutoForm(htmlClasses = "unit_30", label = "Terugkopnr")
	private Integer terugkoppelingNummer;

	@AutoForm(htmlClasses = "unit_40", label = "Deelnnr")
	private Integer deelnemernNummer;

	@AutoForm(htmlClasses = "unit_40", label = "Code")
	private Integer signaalcode;

	@AutoForm(htmlClasses = "unit_60")
	private Ernst ernst;

	@AutoForm(htmlClasses = "unit_120")
	private IModel<Locatie> locatie;

	private IModel<IBronTerugkoppeling> terugkoppelbestand;

	public BronSignaalZoekFilter()
	{
	}

	public BronSignaalZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatusModel)
	{
		super(bronSchooljaarStatusModel);
	}

	public Boolean getGeaccordeerd()
	{
		return geaccordeerd;
	}

	public void setGeaccordeerd(Boolean geaccordeerd)
	{
		this.geaccordeerd = geaccordeerd;
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public Integer getBatchNummer()
	{
		return batchNummer;
	}

	public void setBatchNummer(Integer batchNummer)
	{
		this.batchNummer = batchNummer;
	}

	public Integer getTerugkoppelingNummer()
	{
		return terugkoppelingNummer;
	}

	public void setTerugkoppelingNummer(Integer terugkoppelingNummer)
	{
		this.terugkoppelingNummer = terugkoppelingNummer;
	}

	public Integer getDeelnemernNummer()
	{
		return deelnemernNummer;
	}

	public void setDeelnemernNummer(Integer deelnemernNummer)
	{
		this.deelnemernNummer = deelnemernNummer;
	}

	public Integer getSignaalcode()
	{
		return signaalcode;
	}

	public void setSignaalcode(Integer signaalcode)
	{
		this.signaalcode = signaalcode;
	}

	public Ernst getErnst()
	{
		return ernst;
	}

	public void setErnst(Ernst ernst)
	{
		this.ernst = ernst;
	}

	public Locatie getLocatie()
	{
		return getModelObject(locatie);
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = makeModelFor(locatie);
	}

	public void setTerugkoppelbestand(IBronTerugkoppeling terugkoppelbestand)
	{
		this.terugkoppelbestand = makeModelFor(terugkoppelbestand);
	}

	public IBronTerugkoppeling getTerugkoppelbestand()
	{
		return getModelObject(terugkoppelbestand);
	}
}
