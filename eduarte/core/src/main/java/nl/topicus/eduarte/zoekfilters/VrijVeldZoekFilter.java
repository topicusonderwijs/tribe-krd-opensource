/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldType;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link VrijVeld}.
 * 
 * @author hoeve
 */
public class VrijVeldZoekFilter extends AbstractZoekFilter<VrijVeld>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private Boolean filterOpTaxonomie;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	private VrijVeldType type;

	private VrijVeldCategorie categorie;

	private Boolean intakeScherm;

	private Boolean uitgebreidZoekenScherm;

	private Boolean dossierScherm;

	private List<Long> excludeIds;

	private IModel<Opleiding> opleiding;

	public VrijVeldZoekFilter()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public VrijVeldType getType()
	{
		return type;
	}

	public void setType(VrijVeldType type)
	{
		this.type = type;
	}

	public VrijVeldCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(VrijVeldCategorie categorie)
	{
		this.categorie = categorie;
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public void setIntakeScherm(Boolean intakeScherm)
	{
		this.intakeScherm = intakeScherm;
	}

	public void setUitgebreidZoekenScherm(Boolean uitgebreidZoekenScherm)
	{
		this.uitgebreidZoekenScherm = uitgebreidZoekenScherm;
	}

	public void setDossierScherm(Boolean dossierScherm)
	{
		this.dossierScherm = dossierScherm;
	}

	public void setExclude(List<VrijVeld> vrijVelden)
	{
		excludeIds = new ArrayList<Long>();

		for (VrijVeld vv : vrijVelden)
		{
			excludeIds.add(vv.getId());
		}
	}

	public Boolean getIntakeScherm()
	{
		return intakeScherm;
	}

	public Boolean getUitgebreidZoekenScherm()
	{
		return uitgebreidZoekenScherm;
	}

	public Boolean getDossierScherm()
	{
		return dossierScherm;
	}

	public List<Long> getExcludeIds()
	{
		return excludeIds;
	}

	public void setOpleiding(IModel<Opleiding> opleiding)
	{
		this.opleiding = opleiding;
	}

	public IModel<Opleiding> getOpleiding()
	{
		return opleiding;
	}

	public void setFilterOpTaxonomie(Boolean filterOpTaxonomie)
	{
		this.filterOpTaxonomie = filterOpTaxonomie;
	}

	public Boolean getFilterOpTaxonomie()
	{
		return filterOpTaxonomie;
	}

}
