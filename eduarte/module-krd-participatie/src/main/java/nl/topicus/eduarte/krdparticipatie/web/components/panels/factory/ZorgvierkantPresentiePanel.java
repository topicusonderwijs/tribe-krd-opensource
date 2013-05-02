package nl.topicus.eduarte.krdparticipatie.web.components.panels.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.participatie.settings.AbsentiePresentie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieMaandOverzichtPage;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter.Maand;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.checks.ComponentSecurityCheck;
import org.apache.wicket.security.components.SecureComponentHelper;

public class ZorgvierkantPresentiePanel extends TypedPanel<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	public ZorgvierkantPresentiePanel(String id, IModel<Deelnemer> deelnemerModel)
	{
		super(id, deelnemerModel);

		createModuleSpecifiekePageLink("presentieLink");

		add(new PercentageAanwezigheidListView("aanwezigheid", getDeelnemer()));
	}

	private void createModuleSpecifiekePageLink(String id)
	{
		add(new TargetBasedSecurePageLink<Void>(id, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ParticipatieMaandOverzichtPage(getDeelnemer());
			}

			@Override
			public Class< ? extends AbstractDeelnemerPage> getPageIdentity()
			{
				return ParticipatieMaandOverzichtPage.class;
			}
		}));
	}

	private Deelnemer getDeelnemer()
	{
		return getModelObject();
	}

	private static final class PercentageAanwezigheidListView extends
			ListView<ParticipatieMaandOverzicht>
	{
		private static final long serialVersionUID = 1L;

		private PercentageAanwezigheidListView(String id, Deelnemer deelnemer)
		{
			super(id, new AanwezigheidModel(deelnemer));
			SecureComponentHelper.setSecurityCheck(this, new ComponentSecurityCheck(this, true));
			DeelnemerSecurityCheck.replaceSecurityCheck(this, deelnemer);
		}

		@Override
		protected void populateItem(ListItem<ParticipatieMaandOverzicht> item)
		{
			// 0-50% rood, 50-85% geel, >85% groen
			ParticipatieMaandOverzicht overzicht = item.getModelObject();
			String style = "red";
			BigDecimal percentage = overzicht.getPercentage();
			if (percentage == null)
			{
				style = "white";
				item.add(new Label("absentiePresentie", new Model<String>(
					"<Geen gegevens gevonden>")));
			}
			else
			{
				item.add(ComponentFactory.getDataLabel("absentiePresentie", overzicht
					.getPercentagePresent()));

				if (DecimalUtil.greaterThan(percentage, 50)
					&& DecimalUtil.lessOrEqual(percentage, 85))
					style = "yellow";
				else if (DecimalUtil.greaterThan(percentage, 85))
					style = "green";
			}
			item.add(ComponentFactory.getDataLabel("header", AbsentiePresentie.AbsentiePresentie
				.getHeader()));
			item.add(new WebMarkupContainer("indicator").add(new SimpleAttributeModifier("class",
				style)));
			item.add(ComponentFactory.getDataLabel("maand.langeNaam"));
		}

		@Override
		protected IModel<ParticipatieMaandOverzicht> getListItemModel(
				IModel< ? extends List<ParticipatieMaandOverzicht>> listViewModel, int index)
		{
			return new CompoundPropertyModel<ParticipatieMaandOverzicht>(super.getListItemModel(
				listViewModel, index));
		}
	}

	private static final class AanwezigheidModel extends
			LoadableDetachableModel<List<ParticipatieMaandOverzicht>>
	{
		private static final long serialVersionUID = 1L;

		private ParticipatieAanwezigheidMaandZoekFilter filter;

		public AanwezigheidModel(Deelnemer deelnemer)
		{
			super();
			filter = new ParticipatieAanwezigheidMaandZoekFilter(deelnemer);
			Maand maand = Maand.getHuidigeMaand();
			filter.setTotMaand(maand);
			maand = maand.vorigeMaand().vorigeMaand();
			filter.setVanafMaand(maand);
			filter.setAlleenIIVOAfspraken(false);
		}

		@Override
		protected List<ParticipatieMaandOverzicht> load()
		{
			List<ParticipatieMaandOverzicht> res = new ArrayList<ParticipatieMaandOverzicht>();
			Maand maand = filter.getTotMaand();
			while (maand != null)
			{
				ParticipatieMaandOverzicht overzicht = new ParticipatieMaandOverzicht();
				overzicht.setMaand(maand);
				DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).getMaandOverzicht(
					filter, overzicht);
				res.add(overzicht);
				if (maand.equals(filter.getVanafMaand()))
				{
					break;
				}
				maand = maand.vorigeMaand();
			}
			return res;
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(filter);
			super.onDetach();
		}
	}
}
