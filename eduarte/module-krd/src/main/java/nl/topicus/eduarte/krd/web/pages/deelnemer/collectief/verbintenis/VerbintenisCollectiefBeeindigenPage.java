package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.AbstractCollectieveStatusovergangPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.markup.html.form.Form;

/**
 * Pagina om meerdere verbintenissen tegelijk te beeindigen.
 * 
 * @author idserda
 */
@PageInfo(title = "Verbintenissen collectief beeindigen", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class VerbintenisCollectiefBeeindigenPage extends
		AbstractCollectieveStatusovergangPage<VerbintenisStatus>
{
	private Form<Void> form;

	public VerbintenisCollectiefBeeindigenPage()
	{
		this(new CollectieveStatusovergangEditModel<VerbintenisStatus>());

		CollectieveStatusovergangEditModel<VerbintenisStatus> model = getStatusovergangModel();
		model.setBeginstatus(VerbintenisStatus.Definitief);
		model.setEindstatus(VerbintenisStatus.Beeindigd);
	}

	@SuppressWarnings("unchecked")
	private CollectieveStatusovergangEditModel<VerbintenisStatus> getStatusovergangModel()
	{
		return (CollectieveStatusovergangEditModel<VerbintenisStatus>) getDefaultModel();
	}

	public VerbintenisCollectiefBeeindigenPage(
			CollectieveStatusovergangEditModel<VerbintenisStatus> model)
	{
		super(model);

		form = new Form<Void>("form");

		createVerbintenisBeeindigenPanel("beeindigenPanel");

		add(form);

		createComponents();
	}

	private void createVerbintenisBeeindigenPanel(String id)
	{
		AutoFieldSet<CollectieveStatusovergangEditModel<VerbintenisStatus>> verbintenisFieldSet =
			new AutoFieldSet<CollectieveStatusovergangEditModel<VerbintenisStatus>>(id,
				getStatusovergangModel(), "Verbintenis details");
		verbintenisFieldSet.setRenderMode(RenderMode.EDIT);
		verbintenisFieldSet.setPropertyNames("einddatum", "redenUitschrijving", "toelichting");
		verbintenisFieldSet.setSortAccordingToPropertyNames(true);
		verbintenisFieldSet.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.Verbintenis));

		verbintenisFieldSet.addFieldModifier(new RequiredModifier(true, "einddatum",
			"redenUitschrijving"));

		form.add(verbintenisFieldSet);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new VerbintenisCollectiefSelectiePage(
					getCollectieveStatusovergangEditModel(),
					VerbintenisCollectiefBeeindigenPage.this));
			}

		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, VerbintenisCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Verbintenissen);
	}
}
