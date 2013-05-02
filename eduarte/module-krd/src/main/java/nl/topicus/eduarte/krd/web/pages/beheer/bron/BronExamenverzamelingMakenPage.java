package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieEnum;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJob;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJobDataMap;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzamelingenAanmakenJobRun;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronExamenMeldingenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.quartz.JobDataMap;

@PageInfo(title = "Nieuwe Examenverzameling maken", menu = "Beheer > BRON > Verzameling maken")
@InPrincipal(BronOverzichtWrite.class)
public class BronExamenverzamelingMakenPage extends
		AbstractJobBeheerPage<BronExamenverzamelingenAanmakenJobRun>
{
	private BronExamenverzamelingZoekFilter examenVerzFilter;

	public BronExamenverzamelingMakenPage(BronExamenverzamelingZoekFilter filter)
	{
		super(CoreMainMenuItem.Beheer, BronExamenverzamelingenAanmakenJob.class, null);
		this.examenVerzFilter = filter;
		AutoFieldSet<JobDataMap> autoFieldSet = getJobPanel().getAutoFieldSet();
		autoFieldSet.addFieldModifier(new ConstructorArgModifier("schooljaar",
			new AbstractReadOnlyModel<List<Schooljaar>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<Schooljaar> getObject()
				{
					return examenVerzFilter.getSchooljaarList();
				}
			}));
		autoFieldSet.addFieldModifier(new ConstructorArgModifier("bronOnderwijssoort",
			new AbstractReadOnlyModel<List<BronOnderwijssoort>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<BronOnderwijssoort> getObject()
				{
					return Arrays.asList(BronOnderwijssoort.VOORTGEZETONDERWIJS,
						BronOnderwijssoort.VAVO);
				}
			}));
		autoFieldSet.addFieldModifier(new ConstructorArgModifier("soortOnderwijsTax", autoFieldSet
			.getModel()));
		autoFieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("bronOnderwijssoort")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				AutoFieldSet<JobDataMap> fieldset = getJobPanel().getAutoFieldSet();
				Component component = fieldset.findFieldComponent("soortOnderwijsTax");
				if (component != null)
					target.addComponent(component);
			}
		});
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new BronExamenverzamelingenAanmakenJobDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BronMenuItem.BRON);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Examenverzameling aanmaken";
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
		panel.addButton(new AbstractBottomRowButton(panel, "Naar laatst aangemaakte verzameling",
			CobraKeyAction.LINKKNOP1, ButtonAlignment.LEFT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				return new TargetBasedSecurePageLink<Void>(linkId, new IPageLink()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return BronExamenMeldingenPage.class;
					}

					@Override
					public Page getPage()
					{
						BronExamenmeldingZoekFilter filter = new BronExamenmeldingZoekFilter();
						filter.setExamenverzameling(DataAccessRegistry.getHelper(
							BronExamenverzamenlingDataAccessHelper.class)
							.getLaatstAangemaakteVerzameling());
						return new BronExamenMeldingenPage(filter);
					}
				});
			}
		});
		panel.addButton(new OpslaanButton(panel, getJobPanel().getForm(),
			"Selecteer examendeelnames")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				BronExamenverzamelingenAanmakenJobDataMap datamap =
					(BronExamenverzamelingenAanmakenJobDataMap) getJobPanel().getForm()
						.getModelObject();
				ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter();
				filter.setSchooljaar(datamap.getSchooljaar());
				filter.setBronOnderwijssoort(datamap.getBronOnderwijssoort());
				filter.setExamenworkflow(Taxonomie.getLandelijkeTaxonomie(
					TaxonomieEnum.VO.getCode()).getExamenWorkflow());
				filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
					this));

				setResponsePage(new BronExamendeelnameSelectiePage(
					BronExamenverzamelingMakenPage.this, new HibernateSelection<Examendeelname>(
						Examendeelname.class), new BronVerzamelingMakenSelectieTarget(
						BronExamendeelnameSelectiePage.class, "Maak verzameling aan", datamap,
						BronExamenverzamelingMakenPage.this), filter));

			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.GEEN;
			}
		});

		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronExamenverzamelingZoekFilter.class);
		ctorArgValues.add(examenVerzFilter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(examenVerzFilter);
		super.onDetach();
	}
}
