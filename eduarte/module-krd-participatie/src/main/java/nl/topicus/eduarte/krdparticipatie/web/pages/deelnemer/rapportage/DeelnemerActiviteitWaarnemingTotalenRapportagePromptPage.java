package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerTotalenPerOnderwijsproductRapportage;
import nl.topicus.eduarte.participatie.zoekfilters.DeelnemerActiviteitTotalenZoekFilter;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het instellen van de activiteit totalen rapportage
 * 
 * @author vandekamp
 */
@PageInfo(title = "Rapportage-instellingen", menu = "Deelnemer > Rapportage > Activiteittotalen > [Deelnemer] > Activiteittotalen genereren")
@InPrincipal(DeelnemerTotalenPerOnderwijsproductRapportage.class)
public class DeelnemerActiviteitWaarnemingTotalenRapportagePromptPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private final Form<DeelnemerActiviteitTotalenZoekFilter> form;

	private IModel<List<Verbintenis>> verbintenissenModel;

	private MenuItemKey selectedMenuItem;

	private DeelnemerActiviteitTotalenZoekFilter filter;

	private static DeelnemerActiviteitTotalenZoekFilter getDefaultFilter(Verbintenis verbintenis)
	{
		DeelnemerActiviteitTotalenZoekFilter filter = new DeelnemerActiviteitTotalenZoekFilter();
		filter.setOrganisatieEenheid(verbintenis.getOrganisatieEenheid());
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setActiviteitenTonen(true);
		List<AfspraakTypeCategory> afspraakTypeCategoryList = new ArrayList<AfspraakTypeCategory>();
		afspraakTypeCategoryList.add(AfspraakTypeCategory.INDIVIDUEEL);
		afspraakTypeCategoryList.add(AfspraakTypeCategory.ROOSTER);
		filter.setAfspraakTypeCategories(afspraakTypeCategoryList);
		return filter;

	}

	public DeelnemerActiviteitWaarnemingTotalenRapportagePromptPage(
			List<Verbintenis> verbintenissen, CoreMainMenuItem coreMainMenuItem)
	{
		this(verbintenissen, getDefaultFilter(verbintenissen.get(0)), coreMainMenuItem);
	}

	public DeelnemerActiviteitWaarnemingTotalenRapportagePromptPage(
			List<Verbintenis> verbintenissen, DeelnemerActiviteitTotalenZoekFilter filter,
			final CoreMainMenuItem coreMainMenuItem)
	{
		super(coreMainMenuItem);
		this.filter = filter;
		if (coreMainMenuItem.equals(CoreMainMenuItem.Groep))
			this.selectedMenuItem = GroepCollectiefMenuItem.Rapportages;
		else
			this.selectedMenuItem = DeelnemerCollectiefMenuItem.Rapportages;
		verbintenissenModel = ModelFactory.getListModel(verbintenissen);
		form =
			new Form<DeelnemerActiviteitTotalenZoekFilter>("form",
				new CompoundPropertyModel<DeelnemerActiviteitTotalenZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{

					@SuppressWarnings("hiding")
					DeelnemerActiviteitTotalenZoekFilter filter = getModelObject();
					if (!filter.getBeginDatum().before(filter.getEindDatum()))
					{
						error("De 'vanaf datum' dient voor de 't/m datum' te zijn.");
						return;
					}
					List<Verbintenis> verbintenissenList = verbintenissenModel.getObject();
					setResponsePage(new DeelnemerActiviteitWaarnemingTotalenRapportagePage(
						verbintenissenList, getModelObject(), coreMainMenuItem));
				}
			};
		add(form);
		form.setOutputMarkupId(true);
		form.add(new DatumField("beginDatum"));
		form.add(new DatumField("eindDatum"));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "Maak rapport"));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (DeelnemerCollectiefMenuItem.Rapportages.equals(selectedMenuItem))
		{
			return new DeelnemerCollectiefMenu(id, selectedMenuItem);
		}
		return new GroepCollectiefMenu(id, selectedMenuItem);

	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, "Deelnemerwaarnemingenoverzicht");
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(verbintenissenModel);
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}

}
