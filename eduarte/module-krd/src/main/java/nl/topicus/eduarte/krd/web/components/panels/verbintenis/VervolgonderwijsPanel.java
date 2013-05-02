package nl.topicus.eduarte.krd.web.components.panels.verbintenis;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.SearchEditorSelectBehavior;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs.SoortVervolgonderwijs;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class VervolgonderwijsPanel extends TypedPanel<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet<Vervolgonderwijs> autoFieldset;

	private EduArteAjaxRefreshModifier refreshModifier;

	public VervolgonderwijsPanel(String id, IModel<Verbintenis> verbintenisModel)
	{
		super(id, verbintenisModel);

		Form<Void> form = new Form<Void>("form");

		form.add(createEindeLeerplichtVeld("eindeLeerplicht"));
		form.add(createAutoFieldSet("autoFieldSet"));

		add(form);
	}

	private Label createEindeLeerplichtVeld(String id)
	{
		return new Label(id, TimeUtil.getInstance().formatDate(getDeelnemerEindeLeerplicht()));
	}

	private Component createAutoFieldSet(String id)
	{
		autoFieldset =
			new AutoFieldSet<Vervolgonderwijs>(id, new PropertyModel<Vervolgonderwijs>(
				getVerbintenisModel(), "vervolgonderwijs"));
		autoFieldset.setPropertyNames("soortVervolgonderwijs", "brincode", "naam", "plaats");
		autoFieldset.setSortAccordingToPropertyNames(true);
		autoFieldset.addFieldModifier(new RequiredModifier(true, "brincode"));
		autoFieldset.addFieldModifier(new EnableModifier(
			createIsSoortVervolgonderwijsBrinModel(false), "naam", "plaats"));
		autoFieldset.addFieldModifier(new EnableModifier(
			createIsSoortVervolgonderwijsBrinModel(true), "brincode"));
		autoFieldset.addFieldModifier(refreshModifier =
			new EduArteAjaxRefreshModifier("soortVervolgonderwijs", "naam", "plaats", "brincode")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					if (getSoortVervolgonderwijs() != null)
					{
						if (!getSoortVervolgonderwijs().equals(SoortVervolgonderwijs.BRIN))
						{
							getVervolgonderwijs().setBrincode(null);
							getVervolgonderwijs().setNaam(null);
							getVervolgonderwijs().setPlaats(null);
						}
					}
				}

			});

		autoFieldset.addFieldModifier(new BehaviorModifier(new SearchEditorSelectBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(AjaxRequestTarget target)
			{
				onSoortVervolgonderwijsUpdate(target);
			}
		}, "brincode"));

		autoFieldset.setRenderMode(RenderMode.EDIT);
		return autoFieldset;
	}

	private void onSoortVervolgonderwijsUpdate(AjaxRequestTarget target)
	{
		Brin brin = getBrin();

		if (brin != null)
		{
			getVervolgonderwijs().setBrincode(brin);
			getVervolgonderwijs().setNaam(getBrin().getNaam());
			getVervolgonderwijs().setPlaats(getBrinPlaats());
		}

		refreshModifier.refreshComponents(autoFieldset, target);
	}

	private String getBrinPlaats()
	{
		ExterneOrganisatieAdres eersteAdresOpPeilDatum = getBrin().getFysiekAdres();

		if (eersteAdresOpPeilDatum != null)
		{
			return eersteAdresOpPeilDatum.getAdres().getPlaats();
		}
		return null;
	}

	private IModel<Boolean> createIsSoortVervolgonderwijsBrinModel(final boolean keuze)
	{
		return new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				if (getVervolgonderwijs() != null)
				{
					SoortVervolgonderwijs soort = getVervolgonderwijs().getSoortVervolgonderwijs();

					return soort != null
						&& (soort.equals(SoortVervolgonderwijs.BRIN) || soort
							.equals(SoortVervolgonderwijs.Overig))
						&& soort.equals(SoortVervolgonderwijs.BRIN) == keuze;
				}
				return false;
			}
		};
	}

	private Date getDeelnemerEindeLeerplicht()
	{
		return getVerbintenis().getDeelnemer().getDatumEindeLeerplicht();
	}

	@SuppressWarnings("unchecked")
	public IModel<Verbintenis> getVerbintenisModel()
	{
		return (IModel<Verbintenis>) getDefaultModel();
	}

	private Verbintenis getVerbintenis()
	{
		return (Verbintenis) getDefaultModelObject();
	}

	private Vervolgonderwijs getVervolgonderwijs()
	{
		return getVerbintenis().getVervolgonderwijs();
	}

	private Brin getBrin()
	{
		return getVervolgonderwijs().getBrincode();
	}

	private SoortVervolgonderwijs getSoortVervolgonderwijs()
	{
		return getVervolgonderwijs().getSoortVervolgonderwijs();
	}
}
