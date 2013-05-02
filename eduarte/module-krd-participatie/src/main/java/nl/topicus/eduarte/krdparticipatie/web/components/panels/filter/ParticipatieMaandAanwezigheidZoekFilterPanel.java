/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.krdparticipatie.web.components.combobox.MaandCombobox;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author vandekamp
 */
public class ParticipatieMaandAanwezigheidZoekFilterPanel extends
		TypedPanel<ParticipatieAanwezigheidMaandZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ParticipatieMaandAanwezigheidZoekFilterPanel(String id,
			ParticipatieAanwezigheidMaandZoekFilter filter)
	{
		super(id, new CompoundPropertyModel<ParticipatieAanwezigheidMaandZoekFilter>(filter));
		setRenderBodyOnly(true);
		Form<Void> form = new Form<Void>("form");
		add(form);
		form.add(new MaandCombobox("vanafMaand"));
		form.add(new MaandCombobox("totMaand"));
		WebMarkupContainer afsprakenContainer = new WebMarkupContainer("afsprakenContainer");
		afsprakenContainer.setVisibilityAllowed(EduArteApp.get().isModuleActive(
			EduArteModuleKey.PARTICIPATIE));
		afsprakenContainer.setRenderBodyOnly(true);
		form.add(afsprakenContainer);
		afsprakenContainer.add(new JaNeeCombobox("alleenIIVOAfspraken"));
	}

}
