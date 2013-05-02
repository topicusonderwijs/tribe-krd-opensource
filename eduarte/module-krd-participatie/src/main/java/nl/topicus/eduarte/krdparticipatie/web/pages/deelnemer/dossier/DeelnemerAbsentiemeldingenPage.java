package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.GroupProperty.GroupPropertyList;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ImageColumn;
import nl.topicus.cobra.web.components.datapanel.columns.TimePropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerAbsentiemeldingenInzien;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.datapanel.CustomDataPanelModuleEditPageLinkRowFactory;
import nl.topicus.eduarte.web.components.factory.ParticipatieModuleComponentFactory;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerAbsentiemeldingenZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Pagina met alle absentiemeldingen van 1 deelnemer
 * 
 * @author loite
 */
@PageInfo(title = "Absentiemeldingen", menu = {
	"Deelnemer > [deelnemer] > Aanwezigheid > Absentiemeldingen",
	"Groep > [groep] > [deelnemer] > Aanwezigheid > Absentiemeldingen"})
@InPrincipal(DeelnemerAbsentiemeldingenInzien.class)
public class DeelnemerAbsentiemeldingenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private final Page returnToPage;

	private static final List<CustomColumn<AbsentieMelding>> getColumns()
	{
		List<CustomColumn<AbsentieMelding>> cols = new ArrayList<CustomColumn<AbsentieMelding>>(10);

		if (EduArteApp.get().isModuleActive(EduArteModuleKey.PARTICIPATIE))
		{
			cols.add(new ImageColumn<AbsentieMelding>("herhalend", "Her.",
				new ImageColumn.ImageFactory<AbsentieMelding>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Image getImage(String id, IModel<AbsentieMelding> data)
					{
						AbsentieMelding melding = data.getObject();
						Image img = new Image(id);
						if (melding.getHerhalendeAbsentieMelding() != null)
						{
							img.add(new SimpleAttributeModifier("src",
								"../assets/img/icons/repeat.png"));
							img.add(new SimpleAttributeModifier("title", "Herhalend"));
						}
						else
							img.setVisible(false);
						return img;
					}
				}));
		}
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Reden", "Reden",
			"absentieReden.omschrijving", "absentieReden.omschrijving"));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Datum/tijd", "Datum/tijd",
			"beginDatumTijd", "beginEind"));
		cols.add(new DatePropertyColumn<AbsentieMelding>("Begindatum", "Begindatum",
			"beginDatumTijd", "beginDatumTijd").setDefaultVisible(false));
		cols.add(new DatePropertyColumn<AbsentieMelding>("Einddatum", "Einddatum", "eindDatumTijd",
			"eindDatumTijd").setDefaultVisible(false));
		cols.add(new TimePropertyColumn<AbsentieMelding>("Begindtijd", "Begintijd",
			"beginDatumTijd", "beginDatumTijd").setDefaultVisible(false));
		cols.add(new TimePropertyColumn<AbsentieMelding>("Eindtijd", "Eindtijd", "eindDatumTijd",
			"eindDatumTijd").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Beginlesuur", "Beginlesuur",
			"beginLesuur", "beginLesuur").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Eindlesuur", "Eindlesuur",
			"eindLesuur", "eindLesuur").setDefaultVisible(false));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Afgehandeld", "Afgehandeld",
			"afgehandeld", "afgehandeldOmschrijving"));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Opmerkingen", "Opmerkingen",
			"opmerkingen", "opmerkingen"));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Laatst gewijzigd door",
			"Laatst gewijzigd door", "lastModifiedBy.eigenaar.volledigeNaam"));
		cols.add(new CustomPropertyColumn<AbsentieMelding>("Aangemaakt door", "Aangemaakt door",
			"createdBy.eigenaar.volledigeNaam").setDefaultVisible(false));
		cols.add(new DatePropertyColumn<AbsentieMelding>("Datum laatst gewijzigd",
			"Datum laatst gewijzigd", "lastModifiedAt").setDefaultVisible(false));
		cols.add(new DatePropertyColumn<AbsentieMelding>("Datum aangemaakt", "Datum aangemaakt",
			"createdAt").setDefaultVisible(false));

		return cols;
	}

	private static final GroupPropertyList<AbsentieMelding> getGroupProperties()
	{
		GroupPropertyList<AbsentieMelding> res = new GroupPropertyList<AbsentieMelding>(5);
		res.add(new GroupProperty<AbsentieMelding>("absentieReden.omschrijving", "Reden",
			"absentieReden.omschrijving"));
		res.add(new GroupProperty<AbsentieMelding>("groepeerDatumOmschrijving", "Datum/tijd",
			"beginDatumTijd"));
		res.add(new GroupProperty<AbsentieMelding>("afgehandeldOmschrijving", "Afgehandeld",
			"afgehandeld"));

		return res;
	}

	private static final AbsentieMeldingZoekFilter getDefaultFilter(Deelnemer deelnemer)
	{
		AbsentieMeldingZoekFilter filter = new AbsentieMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.addOrderByProperty("beginDatumTijd");
		filter.setAscending(false);
		return filter;
	}

	private final DeelnemerAbsentiemeldingenZoekFilterPanel filterPanel;

	public DeelnemerAbsentiemeldingenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer());
	}

	public DeelnemerAbsentiemeldingenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerAbsentiemeldingenPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public DeelnemerAbsentiemeldingenPage(Verbintenis verbintenis, Page returnToPage)
	{
		this(verbintenis.getDeelnemer(), verbintenis, getDefaultFilter(verbintenis.getDeelnemer()),
			returnToPage, null);
	}

	public DeelnemerAbsentiemeldingenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultFilter(deelnemer), null);
	}

	public DeelnemerAbsentiemeldingenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			AbsentieMeldingZoekFilter filter, String error)
	{
		this(deelnemer, verbintenis, filter, null, error);
	}

	public DeelnemerAbsentiemeldingenPage(Deelnemer deelnemer, Verbintenis verbintenis,
			AbsentieMeldingZoekFilter filter, Page returnToPage, String error)
	{
		super(ParticipatieDeelnemerMenuItem.Absentiemeldingen, deelnemer, verbintenis);
		this.returnToPage = returnToPage;
		if (StringUtil.isNotEmpty(error))
			Session.get().error(error);
		IDataProvider<AbsentieMelding> provider =
			GeneralDataProvider.of(filter, AbsentieMeldingDataAccessHelper.class);

		CustomDataPanelContentDescription<AbsentieMelding> contDesc =
			new CustomDataPanelContentDescription<AbsentieMelding>("Absentiemeldingen");
		contDesc.setGroupProperties(getGroupProperties());
		contDesc.setColumns(getColumns());
		EduArteDataPanel<AbsentieMelding> datapanel =
			new EduArteDataPanel<AbsentieMelding>("datapanel", provider, contDesc);
		datapanel.setRowFactory(new CustomDataPanelModuleEditPageLinkRowFactory<AbsentieMelding>(
			AbsentieMelding.class, ParticipatieDeelnemerMenuItem.Absentiemeldingen));
		add(datapanel);
		filterPanel = new DeelnemerAbsentiemeldingenZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	public String getWikiName()
	{
		return super.getWikiName() + " voor participatie";
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (returnToPage != null)
			panel.addButton(new TerugButton(panel, returnToPage));
		List<ParticipatieModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(ParticipatieModuleComponentFactory.class);
		for (ParticipatieModuleComponentFactory factory : factories)
		{
			factory.newHerhalendeAbsentieMeldingToevoegenKnop(panel, getContextDeelnemerModel(),
				getContextVerbintenisModel());
			factory.newAbsentieMeldingToevoegenKnop(panel, getContextDeelnemerModel(),
				getContextVerbintenisModel());
		}
	}

	public Page getReturnToPage()
	{
		return returnToPage;
	}
}
