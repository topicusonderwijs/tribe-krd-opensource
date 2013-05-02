package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IVakMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronVakmeldingTable;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "BRON Examenmelding details", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronExamenmeldingDetailsPage extends AbstractBronPage
{
	private SecurePage returnPage;

	public BronExamenmeldingDetailsPage(IBronExamenMelding examenmelding, SecurePage returnPage)
	{
		setDefaultModel(ModelFactory.getCompoundModelForObject(examenmelding));
		this.returnPage = returnPage;
		Link<Void> deelnemerLink = new Link<Void>("deelnemerLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				setResponsePage(new DeelnemerkaartPage(
					((IBronExamenMelding) BronExamenmeldingDetailsPage.this.getDefaultModelObject())
						.getDeelnemer()));
			}

		};
		String label = "" + examenmelding.getDeelnemer().getDeelnemernummer();
		label += " " + examenmelding.getDeelnemer().getPersoon().getVolledigeNaam();
		deelnemerLink.add(new Label("deelnemer", label));
		add(deelnemerLink);
		add(new Label("examenCode"));
		add(new Label("examenJaar"));
		add(new Label("datumUitslagExamen"));
		add(new Label("uitslagExamen"));
		add(new Label("titelOfThemaWerkstuk"));
		add(new Label("beoordelingWerkstuk"));
		add(new Label("cijferWerkstuk"));

		IDataProvider<IVakMelding> provider =
			new IModelDataProvider<IVakMelding>(new PropertyModel<List<IVakMelding>>(
				getDefaultModel(), "vakMeldingen"));
		EduArteDataPanel<IVakMelding> datapanel =
			new EduArteDataPanel<IVakMelding>("datapanel", provider, new BronVakmeldingTable());
		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, returnPage));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(IBronExamenMelding.class);
		ctorArgTypes.add(SecurePage.class);
		ctorArgValues.add(BronExamenmeldingDetailsPage.this.getDefaultModelObject());
		ctorArgValues.add(returnPage);
	}
}