package nl.topicus.eduarte.web.components.panels.verbintenis;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.vrijevelden.BPVInschrijvingVrijVeld;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

public class DeelnemerBPVPanel extends TypedPanel<BPVInschrijving>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<BPVInschrijving> detailsLinks;

	private AutoFieldSet<BPVInschrijving> detailsRechts;

	public DeelnemerBPVPanel(String id, IModel<BPVInschrijving> model, SecurePage returnPage)
	{
		super(id, model);

		detailsLinks = new AutoFieldSet<BPVInschrijving>("detailsLinks", model);
		detailsLinks.setOutputMarkupId(true);
		detailsLinks.setPropertyNames("status", "opnemenInBron", "bronStatus", "bronDatum",
			"verbintenis.opleiding.naam", "volgnummer", "bpvBedrijf", "kenniscentrum",
			"codeLeerbedrijf", "contactPersoonBPVBedrijf", "praktijkopleiderBPVBedrijf",
			"naamPraktijkopleiderBPVBedrijf", "contractpartner", "contactPersoonContractpartner",
			"praktijkbegeleider", "neemtBetalingsplichtOver");
		detailsLinks.setSortAccordingToPropertyNames(true);
		detailsLinks.setRenderMode(RenderMode.DISPLAY);
		detailsLinks.addFieldModifier(new ConstructorArgModifier("bpvBedrijf", returnPage));
		detailsLinks.addFieldModifier(new ConstructorArgModifier("contractpartner", returnPage));

		detailsLinks
			.addFieldModifier(new LabelModifier("verbintenis.opleiding.naam", "Verbintenis"));
		add(detailsLinks);

		detailsRechts = new AutoFieldSet<BPVInschrijving>("detailsRechts", model);
		detailsRechts.setPropertyNames("begindatum", "verwachteEinddatum", "einddatum",
			"redenUitschrijving", "toelichtingBeeindiging", "totaleOmvang", "gerealiseerdeOmvang",
			"urenPerWeek", "dagenPerWeek", "afsluitdatum", "werkdagen", "locatiePOK",
			"overeenkomstnummer", "opmerkingen");
		detailsRechts.setSortAccordingToPropertyNames(true);
		detailsRechts.setRenderMode(RenderMode.DISPLAY);
		add(detailsRechts);

		add(new VrijVeldEntiteitPanel<BPVInschrijvingVrijVeld, BPVInschrijving>("vrijveldenPanel",
			getBPVModel()));
	}

	public AutoFieldSet<BPVInschrijving> getDetailsLinks()
	{
		return detailsLinks;
	}

	public IModel<BPVInschrijving> getBPVModel()
	{
		return getModel();
	}
}