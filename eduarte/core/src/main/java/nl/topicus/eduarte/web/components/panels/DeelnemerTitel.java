/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels;

import java.util.List;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.InschrijvingCombobox;
import nl.topicus.eduarte.web.components.factory.BegeleidingModuleComponentFactory;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

/**
 * Header voor deelnemerpagina's. Hierin wordt de naam van de deelnemer getoond samen met
 * het nummer en de opleiding van de deelnemer.
 * 
 * @author loite
 */
public class DeelnemerTitel extends TypedPanel<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Maakt een titel voor de gegeven deelnemer. De opleiding wordt opgehaald uit de
	 * gegeven inschrijving. Inschrijving mag null zijn.
	 * 
	 * @param id
	 *            wicket-id
	 * @param deelnemerModel
	 *            De deelnemer die getoond moet worden
	 * @param verbintenisModel
	 *            De inschrijving waarvan de opleiding getoond moet worden, getObject()
	 *            mag null zijn indien geen inschrijving gevonden is maar het model zelf
	 *            niet.
	 */
	public DeelnemerTitel(String id, IModel<Deelnemer> deelnemerModel,
			IModel<Verbintenis> verbintenisModel)
	{
		super(id, deelnemerModel);
		setOutputMarkupId(true);
		Asserts.assertNotNull("deelnemerModel", deelnemerModel);
		Asserts.assertNotNull("inschrijvingModel", verbintenisModel);

		String meerderjarigDesc;
		String meerderjarigUrl;

		boolean meerderjarig = deelnemerModel.getObject().getPersoon().isMeerderjarig();
		if (meerderjarig)
		{
			meerderjarigUrl = "assets/img/icons/person18.png";
			meerderjarigDesc = "Deelnemer is meerderjarig";
		}
		else
		{
			meerderjarigUrl = "assets/img/icons/person17.png";
			meerderjarigDesc = "Deelnemer is minderjarig";
		}

		ContextImage ageImage = new ContextImage("age", new Model<String>(meerderjarigUrl));
		ageImage.add(new SimpleAttributeModifier("title", meerderjarigDesc));
		ageImage.add(new SimpleAttributeModifier("alt", meerderjarigDesc));
		add(ageImage);

		add(new Label("label", new LabelModel(deelnemerModel, verbintenisModel)));

		add(createCombobox(deelnemerModel, verbintenisModel));
		Image ddImage = new Image("dropdown");
		ddImage.add(new SimpleAttributeModifier("src", getRequest()
			.getRelativePathPrefixToContextRoot()
			+ "assets/img/icons/titlebardown.png"));
		add(ddImage.setVisible(false));

		ContextImage overledenImage =
			new ContextImage("overleden", new Model<String>("assets/img/icons/error.png"));
		overledenImage.setVisible(((Deelnemer) DeelnemerTitel.this.getDefaultModelObject())
			.getPersoon().getDatumOverlijden() != null);
		overledenImage.add(new SimpleAttributeModifier("title", "Deze deelnemer is overleden op "
			+ TimeUtil.getInstance().formatDate(
				(deelnemerModel.getObject()).getPersoon().getDatumOverlijden())));
		overledenImage.add(new SimpleAttributeModifier("alt", "Deze deelnemer is overleden op "
			+ TimeUtil.getInstance().formatDate(
				(deelnemerModel.getObject()).getPersoon().getDatumOverlijden())));
		add(overledenImage);

		ContextImage nietVerstrekkenAanDerdenImage =
			new ContextImage("nietVerstrekkenAanDerden", new Model<String>(
				"assets/img/icons/error.png"));
		nietVerstrekkenAanDerdenImage.setVisible(((Deelnemer) DeelnemerTitel.this
			.getDefaultModelObject()).getPersoon().isNietVerstrekkenAanDerden());
		add(nietVerstrekkenAanDerdenImage);

		addBijzonderhedenImage(deelnemerModel);
	}

	private void addBijzonderhedenImage(IModel<Deelnemer> deelnemerModel)
	{
		List<BegeleidingModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BegeleidingModuleComponentFactory.class);
		if (factories.size() > 0)
			add(factories.get(0).getDeelnemerTitelImage("bijzonderheden", deelnemerModel));
		else
			add(new WebMarkupContainer("bijzonderheden").setVisible(false));
	}

	/**
	 * Callback om wat te doen op het moment dat de selectie gewijzigd is.
	 * 
	 * @param inschrijving
	 *            de nieuwe selectie
	 */
	protected void onSelectionChanged(Verbintenis inschrijving)
	{
		// noop
	}

	/**
	 * Model om dynamisch de titel op te bouwen.
	 * 
	 * @author marrink
	 */
	private static final class LabelModel extends LoadableDetachableModel<String>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Deelnemer> deelnemerModel;

		private IModel<Verbintenis> verbintenisModel;

		public LabelModel(IModel<Deelnemer> deelnemerModel, IModel<Verbintenis> verbintenisModel)
		{
			super();
			this.deelnemerModel = deelnemerModel;
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected String load()
		{
			Deelnemer deelnemer = deelnemerModel.getObject();
			Asserts.assertNotNull("deelnemerModel.getObject()", deelnemerModel);
			StringBuilder label = new StringBuilder(40);
			label.append(deelnemer.getDeelnemernummer()).append(" ").append(
				deelnemer.getPersoon().getVolledigeNaam());
			return label.toString();
		}

		@Override
		protected void onDetach()
		{
			deelnemerModel.detach();
			verbintenisModel.detach();
		}
	}

	private InschrijvingCombobox createCombobox(IModel<Deelnemer> deelnemerModel,
			IModel<Verbintenis> verbintenisModel)
	{
		return new InschrijvingCombobox("combo", verbintenisModel, deelnemerModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && !((SecurePage) getPage()).isEditable();
			}

			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}

			@Override
			protected void onSelectionChanged(Verbintenis newSelection)
			{
				DeelnemerTitel.this.onSelectionChanged(newSelection);
			}

			@Override
			protected boolean showVerbintenis(Verbintenis verbintenis)
			{
				return DeelnemerTitel.this.showVerbintenis(verbintenis);
			}
		};
	}

	/**
	 * deze methode haalt een verbintenis uit de lijst van weer te geven verbintenissen
	 * als er false wordt terug gegeven
	 */
	@SuppressWarnings("unused")
	protected boolean showVerbintenis(Verbintenis verbintenis)
	{
		return true;
	}
}
