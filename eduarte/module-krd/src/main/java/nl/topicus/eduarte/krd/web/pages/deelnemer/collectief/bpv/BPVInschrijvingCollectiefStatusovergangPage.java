package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Pagina om status van meerdere verbintenissen tegelijk te wijzigen.
 * 
 * @author idserda
 */
@PageInfo(title = "BPV inschrijving collectief bewerken", menu = {"Deelnemer > Collectief > BPVs"})
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVInschrijvingCollectiefStatusovergangPage extends
		AbstractCollectieveStatusovergangPage<BPVStatus>
{
	private Form<Void> form;

	private EnumCombobox<BPVStatus> beginstatusCombobox;

	private EnumCombobox<BPVStatus> eindstatusCombobox;

	public BPVInschrijvingCollectiefStatusovergangPage()
	{
		this(new CollectieveStatusovergangEditModel<BPVStatus>(BPVStatus.Voorlopig));
	}

	public BPVInschrijvingCollectiefStatusovergangPage(
			CollectieveStatusovergangEditModel<BPVStatus> model)
	{
		super(model);

		form = new Form<Void>("form");

		beginstatusCombobox =
			new EnumCombobox<BPVStatus>("beginstatus", new PropertyModel<BPVStatus>(
				getDefaultModel(), "beginstatus"), getMogelijkeBeginstatussen());
		beginstatusCombobox.setRequired(true);
		beginstatusCombobox.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(eindstatusCombobox);
			}
		});

		eindstatusCombobox =
			new EnumCombobox<BPVStatus>("eindstatus", new PropertyModel<BPVStatus>(
				getDefaultModel(), "eindstatus"), new ToegestaneEindstatussenModel());
		eindstatusCombobox.setNullValid(false);
		eindstatusCombobox.setRequired(true);
		eindstatusCombobox.setAutoSelectOnlyOption(false);

		form.add(beginstatusCombobox);
		form.add(eindstatusCombobox);

		add(form);

		createComponents();
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
					BPVInschrijvingCollectiefStatusovergangPage.this));
			}

		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, BPVInschrijvingCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BPVs);
	}

	private class ToegestaneEindstatussenModel extends AbstractReadOnlyModel<List<BPVStatus>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public List<BPVStatus> getObject()
		{
			List<BPVStatus> temp = null;

			temp = Arrays.asList(getBeginstatus().getVervolgNormaal());

			List<BPVStatus> statussen = new ArrayList<BPVStatus>();
			for (BPVStatus status : temp)
			{
				if (!getBeginstatus().equals(status))
					statussen.add(status);
			}
			return statussen;
		}

	}

	private List<BPVStatus> getMogelijkeBeginstatussen()
	{
		return Arrays.asList(new BPVStatus[] {BPVStatus.Voorlopig, BPVStatus.OvereenkomstAfgedrukt,
			BPVStatus.Afgemeld, BPVStatus.Afgewezen});
	}

}
