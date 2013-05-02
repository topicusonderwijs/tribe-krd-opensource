/*
 * Copyright (c) 2008, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.collectief;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.eduarte.rapportage.leerplicht.SoortLeerplichtDeelnemer;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LeerplichtRapportageZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class LeerplichtRapportageZoekFilterPanel extends
		AutoZoekFilterPanel<LeerplichtRapportageZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private LeerplichtRapportageZoekFilter leerplichtFilter;

	public LeerplichtRapportageZoekFilterPanel(String id, LeerplichtRapportageZoekFilter filter,
			final IPageable pageable)
	{
		super(id, filter, pageable);
		this.leerplichtFilter = filter;

		setPropertyNames(Arrays.asList("peildatum", "organisatieEenheid", "locatie",
			"soortDeelnemer", "aantalklokuren", "aantalWekenAchtereenvolgendAfwezig",
			"aantalWeken", "ongeoorlooft"));

		addFieldModifier(new RequiredModifier(true, "soortDeelnemer"));

		addFieldModifier(new EduArteAjaxRefreshModifier("soortDeelnemer", "aantalklokuren",
			"aantalWekenAchtereenvolgendAfwezig", "aantalWeken", "ongeoorlooft")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				super.onUpdate(target);

				switch (leerplichtFilter.getSoortDeelnemer())
				{
					case KWALIFICATIE_PLICHTIG:
						leerplichtFilter.setAantalklokuren(16);
						leerplichtFilter.setAantalWeken(4);
						leerplichtFilter.setAantalWekenAchtereenvolgendAfwezig(0);
						leerplichtFilter.setOngeoorlooft(true);
						break;

					case LEERPLICHTIG:
						leerplichtFilter.setAantalklokuren(16);
						leerplichtFilter.setAantalWeken(4);
						leerplichtFilter.setAantalWekenAchtereenvolgendAfwezig(0);
						leerplichtFilter.setOngeoorlooft(true);
						break;

					case VSV:
						leerplichtFilter.setAantalklokuren(0);
						leerplichtFilter.setAantalWeken(4);
						leerplichtFilter.setAantalWekenAchtereenvolgendAfwezig(4);
						leerplichtFilter.setOngeoorlooft(true);
						break;

					case WTOS_WSF:
						leerplichtFilter.setAantalklokuren(0);
						leerplichtFilter.setAantalWekenAchtereenvolgendAfwezig(5);
						leerplichtFilter.setAantalWeken(5);
						leerplichtFilter.setOngeoorlooft(true);
						break;
				}
			}
		});

		addFieldModifier(new EnableModifier("aantalWekenAchtereenvolgendAfwezig",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return leerplichtFilter.getSoortDeelnemer() != null
						&& leerplichtFilter.getSoortDeelnemer() != SoortLeerplichtDeelnemer.KWALIFICATIE_PLICHTIG
						&& leerplichtFilter.getSoortDeelnemer() != SoortLeerplichtDeelnemer.LEERPLICHTIG;
				}
			}));

		addFieldModifier(new EnableModifier("aantalklokuren", new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return leerplichtFilter.getSoortDeelnemer() == null
					|| leerplichtFilter.getSoortDeelnemer() == SoortLeerplichtDeelnemer.KWALIFICATIE_PLICHTIG
					|| leerplichtFilter.getSoortDeelnemer() == SoortLeerplichtDeelnemer.LEERPLICHTIG;
			}
		}));

		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
