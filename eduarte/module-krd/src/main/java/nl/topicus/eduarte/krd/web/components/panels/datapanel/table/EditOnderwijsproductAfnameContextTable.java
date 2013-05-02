package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CheckboxColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.PanelColumn;
import nl.topicus.cobra.web.components.labels.TableHeaderDescriptionLabel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.OndPrChoiceColumnPanel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ToepassingResultaatExamenPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Columns voor tabellen van OnderwijsproductAfnameContexten
 * 
 * @author loite
 */
public class EditOnderwijsproductAfnameContextTable extends
		CustomDataPanelContentDescription<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public EditOnderwijsproductAfnameContextTable(final IModel<Verbintenis> verbintenisModel,
			final IModel<Boolean> toonOokHogerNiveauModel)
	{
		super("Onderwijsproducten");
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Soort", "Soort",
			"productregel.soortProductregel"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Afkorting", "Afkorting",
			"productregel.afkorting").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Naam", "Naam",
			"productregel.naam"));
		addColumn(new PanelColumn<OnderwijsproductAfnameContext>("Keuze", "Keuze")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<OnderwijsproductAfnameContext> model)
			{
				return new OndPrChoiceColumnPanel(componentId, model, verbintenisModel.getObject(),
					toonOokHogerNiveauModel.getObject());
			}

		});

		Verbintenis verbintenis = verbintenisModel.getObject();
		if (verbintenis.isVAVOVerbintenis() || verbintenis.isVOVerbintenis())
		{
			addColumn(new PanelColumn<OnderwijsproductAfnameContext>("toepassingResultaat",
				"Toepassing")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Panel getPanel(String componentId, WebMarkupContainer row,
						IModel<OnderwijsproductAfnameContext> model)
				{
					return new ToepassingResultaatExamenPanel<OnderwijsproductAfnameContext>(
						componentId, model, verbintenisModel.getObject());
				}

				@Override
				public Component getHeader(String componentId)
				{
					TableHeaderDescriptionLabel header =
						new TableHeaderDescriptionLabel(
							componentId,
							"Toepassing",
							"Toepassing - De aanduiding die aangeeft of het resultaat voor het betreffende vak is behaald in het examenjaar dan wel de reden waarom dit niet het geval is.");
					return header;
				}

			});
			addColumn(new CheckboxColumn<OnderwijsproductAfnameContext>("volgendeTijdvak",
				"Volg. tijdvak")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<Boolean> getCheckBoxModel(
						IModel<OnderwijsproductAfnameContext> rowModel)
				{
					return new PropertyModel<Boolean>(rowModel, "verwezenNaarVolgendTijdvak");
				}
			});
			addColumn(new CheckboxColumn<OnderwijsproductAfnameContext>("diplomaVak", "Diploma vak")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<Boolean> getCheckBoxModel(
						IModel<OnderwijsproductAfnameContext> rowModel)
				{
					return new PropertyModel<Boolean>(rowModel, "diplomavak");
				}
			});

			addColumn(new CheckboxColumn<OnderwijsproductAfnameContext>("toonOpCijferlijst",
				"Cijferlijst")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<Boolean> getCheckBoxModel(
						IModel<OnderwijsproductAfnameContext> rowModel)
				{
					return new PropertyModel<Boolean>(rowModel, "toonOpCijferlijst");
				}
			});

			if (verbintenis.isHavoVwoVerbintenis())
			{
				addColumn(new CheckboxColumn<OnderwijsproductAfnameContext>("indWerkstuk",
					"Ind. werkstuk")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<Boolean> getCheckBoxModel(
							IModel<OnderwijsproductAfnameContext> rowModel)
					{
						return new PropertyModel<Boolean>(rowModel, "werkstukHoortBijProduct");
					}
				});
			}
		}

	}
}
