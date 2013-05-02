package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import java.util.Arrays;
import java.util.BitSet;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerToetsenBevriezen;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Bevriezen", menu = "Deelnemer > [Deelnemer] > Resultaten > Bevriezen")
@InPrincipal(DeelnemerToetsenBevriezen.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerAlleToetsenBevriezenPage extends AbstractDeelnemerPage
{
	private Form<Void> form;

	private HibernateSelection<Resultaatstructuur> selection;

	@SpringBean
	private ToetsDataAccessHelper toetsHelper;

	private ResultaatstructuurZoekFilter filter;

	public DeelnemerAlleToetsenBevriezenPage(PageParameters parameters)
	{
		this(getDeelnemerFromPageParameters(DeelnemerAlleToetsenBevriezenPage.class, parameters));
	}

	public DeelnemerAlleToetsenBevriezenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerAlleToetsenBevriezenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerAlleToetsenBevriezenPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerAlleToetsenBevriezenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Resultatenmatrix, deelnemer, verbintenis);

		add(form = new Form<Void>("form"));

		filter = new ResultaatstructuurZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.setDeelnemers(Arrays.asList(deelnemer));
		selection = new HibernateSelection<Resultaatstructuur>(Resultaatstructuur.class);
		initSelection();

		form.add(new ResultaatstructuurSelectiePanel("resultaatstructuren", filter, selection));
		createComponents();
	}

	private void initSelection()
	{
		for (DeelnemerToetsBevriezing curBevriezing : toetsHelper.getBevriezingen(null, Arrays
			.asList(getContextDeelnemer())))
		{
			if (curBevriezing.getToets().isEindresultaat())
			{
				BitSet bevroren = curBevriezing.getBevorenPogingenAsSet();
				if (bevroren.get(ResultatenModel.RESULTAAT_IDX))
				{
					selection.add(curBevriezing.getToets().getResultaatstructuur());
				}
			}
		}
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ResultaatstructuurDataAccessHelper structuurHelper;

			@Override
			protected void onSubmit()
			{
				for (Resultaatstructuur curStructuur : structuurHelper.list(filter))
				{
					if (selection.isSelected(curStructuur))
						bevriesStructuur(curStructuur);
					else
						ontdooiStructuur(curStructuur);
				}
				toetsHelper.batchExecute();
				DeelnemerAlleToetsenBevriezenPage ret =
					new DeelnemerAlleToetsenBevriezenPage(getContextVerbintenis());
				ret.info("De instellingen zijn opgeslagen.");
				setResponsePage(ret);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && structuurHelper.listCount(filter) > 0;
			}
		});
	}

	private DeelnemerToetsBevriezing findBevriezing(Resultaatstructuur structuur)
	{
		DeelnemerToetsBevriezing bevriezing =
			toetsHelper.getBevriezing(structuur.getEindresultaat(), getContextDeelnemer());
		if (bevriezing == null)
			bevriezing =
				new DeelnemerToetsBevriezing(getContextDeelnemer(), structuur.getEindresultaat());
		return bevriezing;
	}

	private void bevriesStructuur(Resultaatstructuur structuur)
	{
		DeelnemerToetsBevriezing bevriezing = findBevriezing(structuur);
		BitSet bevroren = bevriezing.getBevorenPogingenAsSet();
		bevroren.set(ResultatenModel.RESULTAAT_IDX);
		bevriezing.setBevorenPogingenAsSet(bevroren);
		bevriezing.saveOrUpdate();
	}

	private void ontdooiStructuur(Resultaatstructuur structuur)
	{
		DeelnemerToetsBevriezing bevriezing = findBevriezing(structuur);
		BitSet bevroren = bevriezing.getBevorenPogingenAsSet();
		bevroren.clear(ResultatenModel.RESULTAAT_IDX);
		bevriezing.setBevorenPogingenAsSet(bevroren);
		bevriezing.saveOrUpdate();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		selection.detach();
		filter.detach();
	}
}
