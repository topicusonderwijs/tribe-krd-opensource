package nl.topicus.eduarte.resultaten.web.components.structuur;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.DisableSecurityCheckMarker;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.resultaten.web.components.security.ResultaatstructuurSecurityCheck;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultaatstructurenKopierenStap1Page;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.ResultatenHerberekenOverzichtPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.HerberekenJobHerstartenButton;
import nl.topicus.eduarte.resultaten.web.pages.shared.ResultaatstructuurEditPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.ResultaatstructuurReturnPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.ToetsenBevriezenPage;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.util.time.Duration;

public class ResultaatstructuurPageUIFactory implements IDetachable
{
	private class HerberekenButton extends HerberekenJobHerstartenButton
	{
		private static final long serialVersionUID = 1L;

		private HerberekenButton(BottomRowPanel bottomRow, String label,
				IModel<Resultaatstructuur> structuurModel)
		{
			super(bottomRow, label, structuurModel);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				ResultatenHerberekenOverzichtPage.class));
		}

		@Override
		protected void postOnClick()
		{
			setResponsePage(new ResultatenHerberekenOverzichtPage(getResultaatstructuur()
				.getOnderwijsproduct()));
		}
	}

	private class StatusUpdateButton extends AbstractLinkButton
	{
		private static final long serialVersionUID = 1L;

		private Status status;

		public StatusUpdateButton(BottomRowPanel bottomRow, String label, Status status)
		{
			super(bottomRow, label, CobraKeyAction.GEEN, ButtonAlignment.LEFT);
			ResultaatstructuurSecurityCheck check =
				new ResultaatstructuurSecurityCheck(new ClassSecurityCheck(
					ResultatenHerberekenOverzichtPage.class), getResultaatstructuur());
			check.setEditTarget(true);
			ComponentUtil.setSecurityCheck(this, check);
			this.status = status;
		}

		@Override
		protected void onClick()
		{
			Resultaatstructuur structuur = getResultaatstructuur();
			structuur.setStatus(status);
			structuur.update();
			structuur.commit();
		}

		@Override
		public boolean isVisible()
		{
			return super.isVisible() && getResultaatstructuur() != null
				&& !getResultaatstructuur().getStatus().equals(status)
				&& !getResultaatstructuur().getStatus().equals(Status.IN_HERBEREKENING)
				&& !getResultaatstructuur().getStatus().equals(Status.FOUTIEF);
		}
	}

	private static final long serialVersionUID = 1L;

	private IModel<Resultaatstructuur> resultaatstructuurModel;

	private ResultaatstructuurReturnPage page;

	private ToetsZoekFilter filter;

	public ResultaatstructuurPageUIFactory(final ResultaatstructuurReturnPage page,
			IModel<Resultaatstructuur> resultaatstructuurModel, ToetsZoekFilter filter)
	{
		this.page = page;
		getPage().add(new AbstractAjaxTimerBehavior(Duration.seconds(5))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTimer(AjaxRequestTarget target)
			{
				target.addComponent(getPage().getBottomRow());
			}
		});
		this.resultaatstructuurModel = resultaatstructuurModel;
		this.filter = filter;
	}

	private SecurePage getPage()
	{
		return (SecurePage) page;
	}

	public IModel<Resultaatstructuur> getResultaatstructuurModel()
	{
		return resultaatstructuurModel;
	}

	public ToetsZoekFilter getFilter()
	{
		return filter;
	}

	public EduArteDataPanel<Toets> createDataPanel()
	{
		GeneralFilteredSortableDataProvider<Toets, ToetsZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter, ToetsDataAccessHelper.class);
		EduArteDataPanel<Toets> datapanel =
			new EduArteDataPanel<Toets>("datapanel", provider, new ToetsTable(
				new AbstractReadOnlyModel<Integer>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Integer getObject()
					{
						List<Toets> toetsen =
							DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(filter);

						if (toetsen.isEmpty())
							return 1;
						return toetsen.get(0).getResultaatstructuur().getDepth();
					}
				}));
		return datapanel;
	}

	public void setMessages()
	{
		Resultaatstructuur structuur = getResultaatstructuur();
		if (structuur != null)
		{
			if (structuur.getStatus().equals(Status.FOUTIEF))
				getPage().error(
					"Er is een fout opgetreden tijdens een herberekening. "
						+ "U kunt de herberekening opnieuw starten onder 'Herberekeningen'. "
						+ "Mocht dit niet helpen, neem dan contact op met de beheerder.");
			else if (structuur.getStatus().equals(Status.IN_HERBEREKENING))
				getPage().info(
					"Er vindt momenteel een herberekening plaats voor deze resultaatstructuur. "
						+ "U kunt de resultaatstruur bewerken zodra de herbereking is afgerond.");
		}
	}

	public void createBottomRow(BottomRowPanel panel)
	{
		panel.setOutputMarkupId(true);
		panel.setDefaultModel(resultaatstructuurModel);
		if (getResultaatstructuur() == null)
		{
			final ToevoegenButton toevoegenButton = new ToevoegenButton(panel, new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return ResultaatstructuurEditPage.class;
				}

				@Override
				public Page getPage()
				{
					return new ResultaatstructuurEditPage(createEmptyStructuur(), page);
				}
			});
			DisableSecurityCheckMarker
				.place(toevoegenButton, ResultaatstructuurSecurityCheck.class);
			panel.addButton(toevoegenButton);
			return;
		}

		final BewerkenButton<Void> bewerkenButton = new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ResultaatstructuurEditPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ResultaatstructuurEditPage(getResultaatstructuur(), page);
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getResultaatstructuur() != null
					&& getResultaatstructuur().isBewerkbaar();
			}
		};
		panel.addButton(bewerkenButton);
		panel.addButton(new PageLinkButton(panel, "Toetsen bevriezen", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ToetsenBevriezenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new ToetsenBevriezenPage(getResultaatstructuur(), page.getReturnPage());
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getResultaatstructuur() != null;
			}
		});

		panel.addButton(new PageLinkButton(panel, "Kopiëren", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return ResultaatstructurenKopierenStap1Page.class;
			}

			@Override
			public Page getPage()
			{
				return new ResultaatstructurenKopierenStap1Page(getResultaatstructuur(), page
					.getReturnPage());
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getResultaatstructuur() != null;
			}
		}.setAlignment(ButtonAlignment.LEFT).setAction(CobraKeyAction.LINKKNOP1));

		panel.addButton(new StatusUpdateButton(panel, "Beschikbaar maken", Status.BESCHIKBAAR));
		panel.addButton(new StatusUpdateButton(panel, "In onderhoud plaatsen", Status.IN_ONDERHOUD)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				super.onClick();
				bewerkenButton.followLink();
			}
		});
		panel.addButton(new HerberekenButton(panel, "Herberekening herstarten",
			new AbstractReadOnlyModel<Resultaatstructuur>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Resultaatstructuur getObject()
				{
					return getResultaatstructuur();
				}
			}));
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return resultaatstructuurModel.getObject();
	}

	public Resultaatstructuur createEmptyStructuur()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		resultaatstructuurModel.detach();
	}
}
