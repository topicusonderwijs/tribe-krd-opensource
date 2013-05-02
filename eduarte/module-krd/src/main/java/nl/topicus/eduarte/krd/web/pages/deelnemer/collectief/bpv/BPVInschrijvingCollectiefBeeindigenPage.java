package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

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
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.AbstractCollectieveStatusovergangPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.markup.html.form.Form;

/**
 * Pagina om meerdere BPV's tegelijk te beeindigen.
 * 
 * @author idserda
 */
@PageInfo(title = "BPV inschrijving collectief beeindigen", menu = {"Deelnemer > Collectief > BPV's"})
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVInschrijvingCollectiefBeeindigenPage extends
		AbstractCollectieveStatusovergangPage<BPVStatus>
{
	private Form<Void> form;

	public BPVInschrijvingCollectiefBeeindigenPage()
	{
		this(new CollectieveStatusovergangEditModel<BPVStatus>());

		CollectieveStatusovergangEditModel<BPVStatus> model = getStatusovergangModel();
		model.setBeginstatus(BPVStatus.Definitief);
		model.setEindstatus(BPVStatus.Beëindigd);
	}

	@SuppressWarnings("unchecked")
	private CollectieveStatusovergangEditModel<BPVStatus> getStatusovergangModel()
	{
		return (CollectieveStatusovergangEditModel<BPVStatus>) getDefaultModel();
	}

	public BPVInschrijvingCollectiefBeeindigenPage(
			CollectieveStatusovergangEditModel<BPVStatus> model)
	{
		super(model);

		form = new Form<Void>("form");

		createVerbintenisBeeindigenPanel("beeindigenPanel");

		add(form);

		createComponents();
	}

	private void createVerbintenisBeeindigenPanel(String id)
	{
		AutoFieldSet<CollectieveStatusovergangEditModel<BPVStatus>> verbintenisFieldSet =
			new AutoFieldSet<CollectieveStatusovergangEditModel<BPVStatus>>(id,
				getStatusovergangModel(), "BPV Inschrijving details");
		verbintenisFieldSet.setRenderMode(RenderMode.EDIT);
		verbintenisFieldSet.setPropertyNames("einddatum", "redenUitschrijving", "toelichting");
		verbintenisFieldSet.setSortAccordingToPropertyNames(true);
		verbintenisFieldSet.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.BPV));

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
				setResponsePage(new BPVInschrijvingCollectiefSelectiePage(
					getCollectieveStatusovergangEditModel(),
					BPVInschrijvingCollectiefBeeindigenPage.this));
			}

		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, BPVInschrijvingCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BPVs);
	}
}