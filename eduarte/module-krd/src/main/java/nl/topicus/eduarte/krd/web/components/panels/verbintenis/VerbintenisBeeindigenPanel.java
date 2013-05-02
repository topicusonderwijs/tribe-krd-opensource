package nl.topicus.eduarte.krd.web.components.panels.verbintenis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronValidatingFormComponent;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class VerbintenisBeeindigenPanel extends TypedPanel<Verbintenis> implements
		VerbintenisProvider
{
	private static final long serialVersionUID = 1L;

	private Form< ? > form;

	private AutoFieldSet<Verbintenis> editAutoFieldSet;

	private List<Component> bpvPanels;

	private AutoFieldSet<Verbintenis> opleidingsGegevens;

	public VerbintenisBeeindigenPanel(String id, IModel<Verbintenis> verbintenisModel,
			Form< ? > form, List<BPVBeeindigenPanel> bpvPanels)
	{
		super(id, verbintenisModel);

		this.form = form;
		this.bpvPanels = new ArrayList<Component>(bpvPanels);

		form.add(new BronValidatingFormComponent("bronvalidator", this));

		opleidingsGegevens = createOpleidinggegevensTonen("velden");
		add(opleidingsGegevens);
		add(editAutoFieldSet = createUitschrijvinggegevensBewerken("veldenEditable"));

	}

	private AutoFieldSet<Verbintenis> createOpleidinggegevensTonen(String id)
	{
		AutoFieldSet<Verbintenis> ret = new AutoFieldSet<Verbintenis>(id, getVerbintenisModel());
		ret.setPropertyNames("opleiding.naam", "opleiding.leerweg", "intensiteit",
			"organisatieEenheid", "laatsteExamendeelname.datumUitslag",
			"laatsteExamendeelname.bekostigd", "begindatum", "geplandeEinddatum");
		ret.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getVerbintenis().getRedenUitschrijving() != null
					&& getVerbintenis().getRedenUitschrijving().isGeslaagd();
			}
		}, "laatsteExamendeelname.datumUitslag", "laatsteExamendeelname.bekostigd"));
		ret.setSortAccordingToPropertyNames(true);
		ret.setRenderMode(RenderMode.DISPLAY);
		ret.setOutputMarkupId(true);
		return ret;
	}

	private AutoFieldSet<Verbintenis> createUitschrijvinggegevensBewerken(String id)
	{
		final AutoFieldSet<Verbintenis> ret =
			new AutoFieldSet<Verbintenis>(id, getVerbintenisModel());
		ret.setPropertyNames("redenUitschrijving", "einddatum", "toelichting", "vertrekstatus");
		ret.addFieldModifier(new ConstructorArgModifier("redenUitschrijving",
			SoortRedenUitschrijvingTonen.Verbintenis));
		ret.addFieldModifier(new RequiredModifier(true, "redenUitschrijving", "einddatum"));
		ret.addFieldModifier(new VisibilityModifier("vertrekstatus", new Model<Boolean>(
			getVerbintenis().isVOVerbintenis())));
		EduArteAjaxRefreshModifier redenUitschrijvingRefresh =
			new EduArteAjaxRefreshModifier("redenUitschrijving", opleidingsGegevens)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					if (getVerbintenis().getRedenUitschrijving().isGeslaagd()
						&& getVerbintenis().getLaatsteExamendeelname() != null
						&& getVerbintenis().getLaatsteExamendeelname().getDatumUitslag() != null)
					{
						getVerbintenis().setEinddatum(
							getVerbintenis().getLaatsteExamendeelname().getDatumUitslag());
						target.addComponent(ret.findFieldComponent("einddatum"));
					}
				}
			};
		if (bpvPanels != null && !bpvPanels.isEmpty())
		{
			redenUitschrijvingRefresh.addComponents(bpvPanels);
			EduArteAjaxRefreshModifier eindatumRefresh =
				new EduArteAjaxRefreshModifier("einddatum");
			eindatumRefresh.addComponents(bpvPanels);
			ret.addFieldModifier(eindatumRefresh);
		}
		ret.addFieldModifier(redenUitschrijvingRefresh);
		ret.setSortAccordingToPropertyNames(true);
		ret.setRenderMode(RenderMode.EDIT);
		return ret;
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		DatumField einddatum = (DatumField) editAutoFieldSet.findFieldComponent("einddatum");
		Date begindatumVerbintenis = getVerbintenis().getBegindatum();

		form.add(new DatumGroterOfGelijkDatumValidator("einddatum", einddatum,
			begindatumVerbintenis));
	}

	public AutoFieldSet<Verbintenis> getEditAutoFieldSet()
	{
		return editAutoFieldSet;
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject();
	}

	public IModel<Verbintenis> getVerbintenisModel()
	{
		return getModel();
	}
}
