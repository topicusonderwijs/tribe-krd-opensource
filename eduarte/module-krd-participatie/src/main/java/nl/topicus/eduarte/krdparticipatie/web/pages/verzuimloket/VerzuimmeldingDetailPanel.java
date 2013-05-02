package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;

import org.apache.wicket.model.IModel;

public class VerzuimmeldingDetailPanel extends TypedPanel<IbgVerzuimmelding>
{
	private static final long serialVersionUID = 1L;

	public VerzuimmeldingDetailPanel(String id, IModel<IbgVerzuimmelding> model)
	{
		super(id, model);

		AutoFieldSet<IbgVerzuimmelding> detailsLinks =
			new AutoFieldSet<IbgVerzuimmelding>("detailsLinks", getModel());
		detailsLinks.setPropertyNames("aanduidingContactpseroon", "melddatum", "begindatum",
			"einddatum", "verzuimsoort", "aanduidingContactpersoon");
		detailsLinks.setSortAccordingToPropertyNames(true);
		detailsLinks.setRenderMode(RenderMode.DISPLAY);

		AutoFieldSet<IbgVerzuimmelding> detailsRechts =
			new AutoFieldSet<IbgVerzuimmelding>("detailsRechts", getModel());
		detailsRechts.setPropertyNames("vermoedelijkeReden", "toelichting",
			"toelichtingActieGewenst", "toelichtingOndernomenactie");
		detailsRechts.setRenderMode(RenderMode.DISPLAY);

		add(detailsLinks);
		add(detailsRechts);
	}

	@Override
	public boolean isVisible()
	{
		return getDefaultModelObject() != null;
	}

}
