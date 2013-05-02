package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensWijzigen;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.ExamendeelnameTable;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * 
 * 
 * @author loite
 */
@PageInfo(title = "Examens bewerken", menu = {"Deelnemer > [deelnemer] > Examens",
	"Groep > [groep] > [deelnemer] > Examens"})
@InPrincipal(DeelnemerExamensWijzigen.class)
public class DeelnemerExamenEditPage extends AbstractDeelnemerPage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private final Form<Void> form;

	private final ExamendeelnamePanel detailPanel;

	public DeelnemerExamenEditPage(Examendeelname examendeelname)
	{
		super(DeelnemerMenuItem.Examens, examendeelname.getVerbintenis().getDeelnemer(),
			examendeelname.getVerbintenis());
		ExamendeelnameZoekFilter filter =
			new ExamendeelnameZoekFilter(examendeelname.getVerbintenis().getDeelnemer());
		filter.setVerbintenisModel(getContextVerbintenisModel());
		filter.addOrderByProperty("datumLaatsteStatusovergang");
		filter.setAscending(false);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		GeneralFilteredSortableDataProvider<Examendeelname, ExamendeelnameZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				ExamendeelnameDataAccessHelper.class);

		form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				Examendeelname deelname = detailPanel.getExamendeelname();
				deelname.saveOrUpdate();
				deelname.commit();
				setResponsePage(new DeelnemerExamenPage(deelname.getVerbintenis().getDeelnemer(),
					deelname.getVerbintenis(), deelname));
			}

		};
		detailPanel = new ExamendeelnamePanel("detailPanel", examendeelname, true, form);
		detailPanel.setOutputMarkupId(true);
		form.add(detailPanel);
		add(form);

		EduArteDataPanel<Examendeelname> datapanel =
			new EduArteDataPanel<Examendeelname>("datapanel", provider, new ExamendeelnameTable());
		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				Examendeelname deelname = detailPanel.getExamendeelname();
				return new DeelnemerExamenPage(deelname.getVerbintenis().getDeelnemer(), deelname
					.getVerbintenis(), deelname);
			}

			@Override
			public Class< ? extends DeelnemerExamenPage> getPageIdentity()
			{
				return DeelnemerExamenPage.class;
			}

		}));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

}
