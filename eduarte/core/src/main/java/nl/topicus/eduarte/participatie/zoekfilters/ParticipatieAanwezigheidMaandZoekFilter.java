/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class ParticipatieAanwezigheidMaandZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Waarneming> implements AanwezigheidMaandFilter
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemerModel;

	private Maand vanafMaand;

	private Maand totMaand;

	private Boolean alleenIIVOAfspraken;

	public ParticipatieAanwezigheidMaandZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public IModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemerModel;
	}

	public void setDeelnemerModel(IModel<Deelnemer> deelnemerModel)
	{
		this.deelnemerModel = deelnemerModel;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemerModel);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		deelnemerModel = makeModelFor(deelnemer);
	}

	public Maand getVanafMaand()
	{
		return vanafMaand;
	}

	public void setVanafMaand(Maand vanafMaand)
	{
		this.vanafMaand = vanafMaand;
	}

	public Maand getTotMaand()
	{
		return totMaand;
	}

	public void setTotMaand(Maand totMaand)
	{
		this.totMaand = totMaand;
	}

	public Boolean getAlleenIIVOAfspraken()
	{
		return alleenIIVOAfspraken;
	}

	public void setAlleenIIVOAfspraken(Boolean alleenIIVOAfspraken)
	{
		this.alleenIIVOAfspraken = alleenIIVOAfspraken;
	}
}
