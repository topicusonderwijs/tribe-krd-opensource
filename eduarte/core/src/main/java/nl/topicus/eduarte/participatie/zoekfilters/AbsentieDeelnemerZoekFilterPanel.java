/*
 * Copyright (c) 2008, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.VerbintenisSearchEditor;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author vandenbrink, Henzen
 * 
 */
public class AbsentieDeelnemerZoekFilterPanel extends TypedPanel<VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisSearchEditor seditor;

	/**
	 * @param id
	 * @param filter
	 * @param pageClass
	 */
	public AbsentieDeelnemerZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			ISelectListener listner, Class< ? extends Page> pageClass)
	{
		super(id, new CompoundPropertyModel<VerbintenisZoekFilter>(filter));
		setRenderBodyOnly(true);
		Form<Void> form = new Form<Void>("form");
		add(form);

		seditor =
			new VerbintenisSearchEditor("deelnemer", ModelFactory.getModel(filter.getVerbintenis()));
		seditor.addListener(listner);
		form.add(seditor);

	}

	/**
	 * Geeft het filter dat gekoppeld is aan dit panel
	 * 
	 * @return Het filter
	 */
	public VerbintenisZoekFilter getFilter()
	{
		return getModelObject();
	}

	/**
	 * @return De inschrijving die op dit moment geselecteerd is op deze pagina.
	 */
	public Verbintenis getGeselecteerdeVerbintenis()
	{
		return seditor.getModelObject();
	}
}
