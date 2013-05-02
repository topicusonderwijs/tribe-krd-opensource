package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.LinkColumn;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.datapanel.PostcodeWoonplaatsColumn;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.bpv.DeelnemerBPVPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class BPVInschrijvingTable extends CustomDataPanelContentDescription<BPVInschrijving>
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingTable()
	{
		this(null, false);
	}

	public BPVInschrijvingTable(final IModel<ExterneOrganisatie> externeOrganisatieModel,
			final boolean deelnemerClickable)
	{
		super("Beroepspraktijkvorming");
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "verbintenis.deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Naam", "Naam",
			"verbintenis.deelnemer.persoon.volledigeNaam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !deelnemerClickable;
			}
		}.setDefaultVisible(isNaamDefaultVisible()));
		addColumn(new LinkColumn<BPVInschrijving>("Naam", "Naam", null,
			"Klik om details overeenkomst weer te geven", null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<BPVInschrijving> rowModel)
			{
				EduArteRequestCycle.get().setResponsePage(
					new DeelnemerBPVPage(rowModel.getObject()));
			}

			@Override
			protected String getLabelTekst(IModel<BPVInschrijving> rowModel)
			{
				BPVInschrijving inschrijving = rowModel.getObject();
				return inschrijving.getDeelnemer().getPersoon().toString();
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && deelnemerClickable;
			}
		});
		addColumn(new DatePropertyColumn<BPVInschrijving>("Begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Einddatum", "Einddatum", "einddatum",
			"tot"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Status", "Status", "status", "status"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Reden beëindiging",
			"Reden beëindiging", "redenUitschrijving", "redenUitschrijving")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Praktijkopleider", "Praktijkopleider",
			"praktijkopleiderBPVBedrijf.naam"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Praktijkbegeleider",
			"Praktijkbegeleider", "praktijkbegeleider.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Totale omvang", "Totale omvang",
			"totaleOmvang", "totaleOmvang"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Opleiding", "Opleiding",
			"verbintenis.opleiding.naam"));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("BPV-bedrijf", "BPV-bedrijf",
			"bpvBedrijf.naam", "bpvBedrijf.naam"));

		addColumn(new AbstractCustomColumn<BPVInschrijving>("Relatie", "Relatie")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(WebMarkupContainer cell, String componentId,
					WebMarkupContainer row, IModel<BPVInschrijving> rowModel, int span)
			{
				BPVInschrijving inschrijving = rowModel.getObject();
				String label = null;

				ExterneOrganisatie externeOrganisatie =
					(externeOrganisatieModel != null ? (ExterneOrganisatie) externeOrganisatieModel
						.getObject() : null);

				if (externeOrganisatie != null)
				{
					if (externeOrganisatie.equals(inschrijving.getBpvBedrijf()))
					{
						label = "BPV-bedrijf";
					}
					else if (externeOrganisatie.equals(inschrijving.getContractpartner()))
					{
						label = "Contractpartner";
					}
					else if (externeOrganisatie.equals(inschrijving.getBpvBedrijf()
						.getOndertekeningBPVOdoor())
						|| (inschrijving.getContractpartner() != null && externeOrganisatie
							.equals(inschrijving.getContractpartner().getOndertekeningBPVOdoor())))
					{
						label = "Ondertekening BPVO";
					}
					else
					{
						label = "Onbekend";
					}

				}
				cell.add(new Label(componentId, label).setRenderBodyOnly(true));
			}
		}.setVisible(externeOrganisatieModel != null));

		addColumn(new CustomPropertyColumn<BPVInschrijving>("Stamgroep", "Stamgroep",
			"verbintenis.plaatsingOpPeildatum.groep.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Organisatie-eenheid", "Organisatie",
			"verbintenis.organisatieEenheid.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Straat en huisnummer (woonadres)",
			"Adres", "verbintenis.deelnemer.persoon.fysiekAdres.adres.straatHuisnummer", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<BPVInschrijving>("Postcode en plaats (woonadres)",
			"Postcode en plaats", "verbintenis.deelnemer.persoon.fysiekAdres.adres.postcodePlaats",
			false).setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<BPVInschrijving>("Straat en huisnummer (postadres)",
			"Adres", "verbintenis.deelnemer.persoon.postAdres.adres.straatHuisnummer", false)
			.setDefaultVisible(false));
		addColumn(new PostcodeWoonplaatsColumn<BPVInschrijving>("Postcode en plaats (postadres)",
			"Postcode en plaats", "verbintenis.deelnemer.persoon.postAdres.adres.postcodePlaats",
			false).setDefaultVisible(false));
	}

	protected boolean isNaamDefaultVisible()
	{
		return false;
	}
}