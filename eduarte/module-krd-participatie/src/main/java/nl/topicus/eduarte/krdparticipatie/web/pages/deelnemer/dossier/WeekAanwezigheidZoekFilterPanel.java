/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.krdparticipatie.web.components.combobox.JaarCombobox;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Zoekbalk voor aanwezigheid per week.
 * 
 * @author marrink
 */
public class WeekAanwezigheidZoekFilterPanel extends TypedPanel<AanwezigheidsWeekFilter>
{
	private static final long serialVersionUID = 1L;

	public WeekAanwezigheidZoekFilterPanel(String id, AanwezigheidsWeekFilter filter)
	{
		super(id, new CompoundPropertyModel<AanwezigheidsWeekFilter>(filter));
		setRenderBodyOnly(true);
		Form<Void> form = new Form<Void>("form");
		add(form);
		form.add(new JaarCombobox("jaar")); // TODO ajax
		form.add(ComponentUtil.fixLength(new TextField<String>("beginWeek"), 2));
		form.add(ComponentUtil.fixLength(new TextField<String>("eindWeek"), 2));
	}

	public IModel<AanwezigheidsWeekFilter> getFilterModel()
	{
		return getModel();
	}
}
