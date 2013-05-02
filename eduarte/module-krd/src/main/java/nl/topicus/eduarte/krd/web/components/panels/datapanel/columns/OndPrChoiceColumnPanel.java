/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.krd.web.components.choice.OnderwijsproductComboBox;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Panel om te tonen in een customdatapanel, die een combobox toont voor
 * onderwijsproducten
 * 
 * @author loite
 */
public class OndPrChoiceColumnPanel extends TypedPanel<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public OndPrChoiceColumnPanel(String id, IModel<OnderwijsproductAfnameContext> model,
			Verbintenis verbintenis, boolean toonOokHogerNiveau)
	{
		super(id, model);
		OnderwijsproductAfnameContext context = model.getObject();
		if (context.getOnderwijsproductAfname() == null
			|| context.getOnderwijsproductAfname().getCohort().equals(verbintenis.getCohort()))
		{
			Productregel productregel = context.getProductregel();
			PropertyModel<Onderwijsproduct> onderwijsproductModel =
				new PropertyModel<Onderwijsproduct>(model,
					"onderwijsproductAfname.onderwijsproduct");

			List<Onderwijsproduct> choices = new ArrayList<Onderwijsproduct>();
			choices.addAll(productregel.getOnderwijsproducten(verbintenis.getOpleiding(),
				toonOokHogerNiveau, false));

			// Verwijder onderwijsproducten die de deelnemer niet kan volgen,
			// gezien de organisatie-eenheid en locatie van de verbintenis.
			List<Onderwijsproduct> filteredChoices = new ArrayList<Onderwijsproduct>();
			List<Verbintenis> verbintenisList = new ArrayList<Verbintenis>();
			verbintenisList.add(verbintenis);
			for (Onderwijsproduct op : choices)
			{
				if (OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(
					OrganisatieEenheidLocatieUtil.flatten(verbintenisList), op))
				{
					filteredChoices.add(op);
					continue;
				}
				for (OnderwijsproductAanbod opa : op.getOrganisatieEenheidLocatieKoppelingen())
				{
					if (opa.getOrganisatieEenheid().isParentOf(verbintenis.getOrganisatieEenheid()))
					{
						filteredChoices.add(op);
						break;
					}
				}

			}

			IModel<List<Onderwijsproduct>> choiceModel = ModelFactory.getListModel(filteredChoices);
			OnderwijsproductComboBox onderwijsproductComboBox =
				new OnderwijsproductComboBox("dropDownChoice", onderwijsproductModel, choiceModel,
					"codeTitelEnExterneCode");
			onderwijsproductComboBox.setForceAutoSelectOnlyOption(true);
			onderwijsproductComboBox.setNullValid(true);
			onderwijsproductComboBox
				.setVisible(productregel.getTypeProductregel() == TypeProductregel.Productregel
					&& productregel.getAlleOnderwijsproductenToestaanVan() == null);
			add(onderwijsproductComboBox);

			OnderwijsproductZoekFilter filter =
				new OnderwijsproductZoekFilter(productregel.getAlleOnderwijsproductenToestaanVan());
			filter.setStaOrganisatieEenheidAanpassingToe(false);
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
			OnderwijsproductSearchEditor onderwijsproductSearchEditor =
				new OnderwijsproductSearchEditor("searchEditor", onderwijsproductModel, filter);
			onderwijsproductSearchEditor
				.setVisible(productregel.getTypeProductregel() == TypeProductregel.Productregel
					&& productregel.getAlleOnderwijsproductenToestaanVan() != null);

			add(onderwijsproductSearchEditor);
			// Markeer het veld als verplicht, maar maak het veld niet echt verplicht
			// zodat de
			// gebruiker een onvolledig vakkenpakket wel kan opslaan.
			if (productregel.isVerplicht())
			{
				onderwijsproductComboBox.add(new AttributeAppender("class", true,
					new Model<String>("required"), " "));
				onderwijsproductSearchEditor.add(new AttributeAppender("class", true,
					new Model<String>("required"), " "));
			}

			add(new WebMarkupContainer("keuze").setVisible(false));
		}
		else
		{
			add(new WebMarkupContainer("dropDownChoice").setVisible(false));
			add(new WebMarkupContainer("searchEditor").setVisible(false));
			add(new Label("keuze", new PropertyModel<String>(model,
				"onderwijsproductAfname.codeTitelCohort")));
		}
		setRenderBodyOnly(false);
	}
}
