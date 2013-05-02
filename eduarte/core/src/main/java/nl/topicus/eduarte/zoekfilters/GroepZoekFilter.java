/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.GroepVrijVeld;
import nl.topicus.eduarte.web.components.choice.GroepstypeCombobox;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;

import org.apache.wicket.model.IModel;

public class GroepZoekFilter extends
		AbstractOrganisatieEenheidLocatieVrijVeldableZoekFilter<GroepVrijVeld, Groep>
{
	private static final long serialVersionUID = 1L;

	private String code;

	private String naam;

	@AutoForm(editorClass = GroepstypeCombobox.class)
	private IModel<Groepstype> type;

	private IModel<Medewerker> mentorOrDocent;

	private boolean mentorOrDocentRequired;

	private String snelZoekenString;

	private Boolean plaatsingsgroep;

	@AutoForm(editorClass = OpleidingSearchEditor.class)
	private IModel<Opleiding> opleiding;

	private String codeExactCaseInsensitiveMatch;

	public GroepZoekFilter()
	{
		super(GroepVrijVeld.class);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Groepstype getType()
	{
		return getModelObject(type);
	}

	public void setType(Groepstype type)
	{
		this.type = makeModelFor(type);
	}

	/**
	 * Stelt de medewerker in die wordt gebruikt als filter voor groepen welke deze
	 * medewerker als mentor of docent hebben.
	 * 
	 * @param medewerker
	 */
	public void setMentorOrDocent(Medewerker medewerker)
	{
		this.mentorOrDocent = makeModelFor(medewerker);
	}

	public Medewerker getMentorOrDocent()
	{
		return getModelObject(mentorOrDocent);
	}

	public boolean isMentorOrDocentRequired()
	{
		return mentorOrDocentRequired;
	}

	public void setMentorOrDocentRequired(boolean required)
	{
		this.mentorOrDocentRequired = required;
	}

	public String getSnelZoekenString()
	{
		return snelZoekenString;
	}

	public void setSnelZoekenString(String snelZoekenString)
	{
		this.snelZoekenString = snelZoekenString;
	}

	public static GroepZoekFilter createDefaultFilter()
	{
		GroepZoekFilter ret = new GroepZoekFilter();
		ret.addOrderByProperty("einddatum");
		ret.addOrderByProperty("begindatum");
		ret.addOrderByProperty("naam");
		ret.addOrderByProperty("code");
		return ret;
	}

	public Boolean getPlaatsingsgroep()
	{
		return plaatsingsgroep;
	}

	public void setPlaatsingsgroep(Boolean plaatsingsgroep)
	{
		this.plaatsingsgroep = plaatsingsgroep;
	}

	public void setTypeModel(IModel<Groepstype> typeModel)
	{
		type = typeModel;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public String getExactCaseInsensitiveMatch()
	{
		return codeExactCaseInsensitiveMatch;
	}

	public void setExactCaseInsensitiveMatch(String codeExactCaseInsensitiveMatch)
	{
		this.codeExactCaseInsensitiveMatch = codeExactCaseInsensitiveMatch;
	}

}
