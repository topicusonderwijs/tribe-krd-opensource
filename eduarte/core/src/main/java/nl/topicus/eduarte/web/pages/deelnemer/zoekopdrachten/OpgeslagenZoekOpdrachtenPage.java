package nl.topicus.eduarte.web.pages.deelnemer.zoekopdrachten;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemersZoeken;
import nl.topicus.eduarte.dao.helpers.DeelnemerZoekOpdrachtDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerZoekOpdrachtTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerZoekOpdrachtZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerUitgebreidZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerUitgebreidZoekenResultatenPage;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekOpdrachtZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

@PageInfo(title = "Opgeslagen zoekopdrachten", menu = "Deelnemer > Opgeslagen zoekopdrachten")
@InPrincipal(DeelnemersZoeken.class)
public class OpgeslagenZoekOpdrachtenPage extends SecurePage
{
	private class ZoekOpdrachtDeleteColumn extends AjaxDeleteColumn<DeelnemerZoekOpdracht>
	{
		private static final long serialVersionUID = 1L;

		private ZoekOpdrachtDeleteColumn()
		{
			super("Delete", "");
		}

		@Override
		protected String getConfirmationText()
		{
			return "Weet u zeker dat u deze opgeslagen zoekopdracht wilt verwijderen?";
		}

		@Override
		public void onClick(WebMarkupContainer item, IModel<DeelnemerZoekOpdracht> rowModel,
				AjaxRequestTarget target)
		{
			DeelnemerZoekOpdracht zoekopdracht = rowModel.getObject();
			for (DeelnemerZoekOpdrachtRecht curRecht : zoekopdracht.getRechten())
				curRecht.delete();
			zoekopdracht.delete();
			zoekopdracht.commit();
			target.addComponent(datapanel);
		}

		@Override
		public boolean isContentsVisible(IModel<DeelnemerZoekOpdracht> model)
		{
			return heeftEditEnDeleteRechten(model.getObject());
		}

		@Override
		public String getCssClass()
		{
			return "unit_20";
		}
	}

	private class ZoekOpdrachtBewerkenColumn extends ButtonColumn<DeelnemerZoekOpdracht>
	{
		private static final long serialVersionUID = 1L;

		private ZoekOpdrachtBewerkenColumn()
		{
			super("Bewerken", "", "ui-icon ui-icon-wrench", "ui-icon ui-icon-wrench");
		}

		@Override
		public void onClick(WebMarkupContainer item, IModel<DeelnemerZoekOpdracht> rowModel)
		{
			setResponsePage(new DeelnemerZoekOpdrachtEditPage(OpgeslagenZoekOpdrachtenPage.this,
				rowModel.getObject()));
		}

		@Override
		public boolean isContentsVisible(IModel<DeelnemerZoekOpdracht> model)
		{
			return heeftEditEnDeleteRechten(model.getObject());
		}

		@Override
		public String getCssClass()
		{
			return "unit_20";
		}
	}

	private EduArteDataPanel<DeelnemerZoekOpdracht> datapanel;

	private boolean publicerenRecht;

	public OpgeslagenZoekOpdrachtenPage()
	{
		super(CoreMainMenuItem.Deelnemer);

		publicerenRecht =
			new DataSecurityCheck(SecureComponentHelper.alias(DeelnemerZoekOpdrachtEditPage.class)
				+ DeelnemerZoekOpdrachtEditPage.PUBLICEREN).isActionAuthorized(Render.class);

		DeelnemerZoekOpdrachtZoekFilter filter = new DeelnemerZoekOpdrachtZoekFilter();
		filter.setPublicerenRecht(publicerenRecht);
		filter.setAccount(getIngelogdeAccount());
		filter.addOrderByProperty("omschrijving");

		IDataProvider<DeelnemerZoekOpdracht> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				DeelnemerZoekOpdrachtDataAccessHelper.class);

		DeelnemerZoekOpdrachtTable table = new DeelnemerZoekOpdrachtTable();
		if (new ClassSecurityCheck(DeelnemerUitgebreidZoekenPage.class)
			.isActionAuthorized(Render.class))
		{
			table.addColumn(new ZoekOpdrachtBewerkenColumn()
				.setPositioning(Positioning.FIXED_RIGHT));
			table.addColumn(new ZoekOpdrachtDeleteColumn().setPositioning(Positioning.FIXED_RIGHT));
		}

		datapanel = new EduArteDataPanel<DeelnemerZoekOpdracht>("datapanel", dataprovider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<DeelnemerZoekOpdracht>(
			DeelnemerUitgebreidZoekenResultatenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<DeelnemerZoekOpdracht> item)
			{
				DeelnemerZoekOpdracht zoekOpdracht = item.getModelObject();
				setResponsePage(new DeelnemerUitgebreidZoekenResultatenPage(zoekOpdracht));
			}
		});
		add(datapanel);

		DeelnemerZoekOpdrachtZoekFilterPanel filterPanel =
			new DeelnemerZoekOpdrachtZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	private boolean heeftEditEnDeleteRechten(DeelnemerZoekOpdracht zoekopdracht)
	{
		return (getIngelogdeAccount() != null && (getIngelogdeAccount().equals(
			zoekopdracht.getCreatedBy()) || getIngelogdeAccount().heeftRol(
			"Standaard applicatiebeheerder")));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerZoekOpdrachtEditPage.class;
			}

			@Override
			public Page getPage()
			{
				DeelnemerZoekOpdracht nieuweOpdracht = new DeelnemerZoekOpdracht();
				nieuweOpdracht.setPersoonlijk(true);
				return new DeelnemerZoekOpdrachtEditPage(OpgeslagenZoekOpdrachtenPage.this,
					nieuweOpdracht, DeelnemerUitgebreidZoekenPage.getDefaultFilter());
			}
		}));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.OpgeslagenZoekopdrachten);
	}
}
