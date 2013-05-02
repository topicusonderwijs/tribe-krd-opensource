package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CollectiefPlaatsingAanmakenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private IModel<Plaatsing> plaatsingModel;

	private Form<Void> form;

	private AutoFieldSet<Plaatsing> autoFieldSet;

	public CollectiefPlaatsingAanmakenPanel(String id, IModel<Plaatsing> plaatsingModel,
			Form<Void> form)
	{
		super(id);

		this.plaatsingModel = plaatsingModel;
		setDefaultModel(plaatsingModel);

		this.form = form;

		createAutoFieldSet("autofieldset");
	}

	private void createAutoFieldSet(String string)
	{
		autoFieldSet = new AutoFieldSet<Plaatsing>(string, plaatsingModel);
		autoFieldSet.setSortAccordingToPropertyNames(true);
		autoFieldSet.setRenderMode(RenderMode.EDIT);
		autoFieldSet.setPropertyNames("begindatum", "einddatum", "groep", "leerjaar",
			"jarenPraktijkonderwijs");
		autoFieldSet.addFieldModifier(new LabelModifier("jarenPraktijkonderwijs", "Praktijkjaar"));

		GroepZoekFilter filter = GroepZoekFilter.createDefaultFilter();
		filter.setPlaatsingsgroep(Boolean.TRUE);
		autoFieldSet.addFieldModifier(new ConstructorArgModifier("groep", filter));

		add(autoFieldSet);
	}

	@Override
	public void detachModels()
	{
		ComponentUtil.detachQuietly(plaatsingModel);
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		DatumField begindatumVeld = (DatumField) autoFieldSet.findFieldComponent("begindatum");
		DatumField einddatumVeld = (DatumField) autoFieldSet.findFieldComponent("einddatum");

		form.add(new BegindatumVoorEinddatumValidator(begindatumVeld, einddatumVeld));
	}

}
