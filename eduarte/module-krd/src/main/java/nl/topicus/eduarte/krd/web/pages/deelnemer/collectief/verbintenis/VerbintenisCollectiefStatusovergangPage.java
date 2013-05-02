package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.AbstractCollectieveStatusovergangPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.CollectieveStatusovergangEditModel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * Pagina om status van meerdere verbintenissen tegelijk te wijzigen.
 * 
 * @author idserda
 */
@PageInfo(title = "Status van verbintenissen bewerken", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class VerbintenisCollectiefStatusovergangPage extends
		AbstractCollectieveStatusovergangPage<VerbintenisStatus>
{
	private Form<Void> form;

	private EnumCombobox<VerbintenisStatus> beginstatusCombobox;

	private EnumCombobox<VerbintenisStatus> eindstatusCombobox;

	public VerbintenisCollectiefStatusovergangPage()
	{
		this(new CollectieveStatusovergangEditModel<VerbintenisStatus>(VerbintenisStatus.Intake));
	}

	public VerbintenisCollectiefStatusovergangPage(
			CollectieveStatusovergangEditModel<VerbintenisStatus> model)
	{
		super(model);

		form = new Form<Void>("form");

		beginstatusCombobox =
			new EnumCombobox<VerbintenisStatus>("beginstatus",
				new PropertyModel<VerbintenisStatus>(getDefaultModel(), "beginstatus"),
				getMogelijkeBeginstatussen());
		beginstatusCombobox.setRequired(true);
		beginstatusCombobox.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(eindstatusCombobox);
				onUpdateBeginstatus(target);
			}

		});

		eindstatusCombobox =
			new EnumCombobox<VerbintenisStatus>("eindstatus", new PropertyModel<VerbintenisStatus>(
				getDefaultModel(), "eindstatus"), new ToegestaneEindstatussenModel());
		eindstatusCombobox.setNullValid(false);
		eindstatusCombobox.setRequired(true);
		eindstatusCombobox.add(new AjaxFormComponentUpdatingBehavior("onchange")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				onUpdateEindstatus(target);
			}

		});

		form.add(beginstatusCombobox);
		form.add(eindstatusCombobox);

		add(form);

		createComponents();
	}

	private void onUpdateBeginstatus(AjaxRequestTarget target)
	{
		if (VerbintenisStatus.Volledig.equals(getBeginstatus()))
			warn("Let op: Geselecteerde overgang alleen mogelijk bij VO verbintenissen");

		this.refreshFeedback(target);
	}

	private void onUpdateEindstatus(AjaxRequestTarget target)
	{
		if (VerbintenisStatus.Volledig.equals(getBeginstatus())
			&& VerbintenisStatus.Definitief.equals(getEindstatus()))
			warn("Let op: Geselecteerde overgang alleen mogelijk bij VO verbintenissen");

		this.refreshFeedback(target);
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
					VerbintenisCollectiefStatusovergangPage.this));
			}

		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel, VerbintenisCollectiefEditOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Verbintenissen);
	}

	private class ToegestaneEindstatussenModel extends
			LoadableDetachableModel<List<VerbintenisStatus>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<VerbintenisStatus> load()
		{
			return Arrays.asList(getBeginstatus().getVervolg(heeftExtraStatusovergangAutorisatie(),
				true));
		}
	}

	private List<VerbintenisStatus> getMogelijkeBeginstatussen()
	{
		return Arrays.asList(new VerbintenisStatus[] {VerbintenisStatus.Intake,
			VerbintenisStatus.Voorlopig, VerbintenisStatus.Volledig, VerbintenisStatus.Afgedrukt,
			VerbintenisStatus.Afgemeld, VerbintenisStatus.Afgewezen});
	}

	private boolean heeftExtraStatusovergangAutorisatie()
	{
		DataSecurityCheck dataCheck =
			new DataSecurityCheck(SecureComponentHelper.alias(EditVerbintenisPage.class)
				+ EditVerbintenisPage.EXTRA_STATUSOVERGANGEN);

		return dataCheck.isActionAuthorized(Enable.class);
	}
}
