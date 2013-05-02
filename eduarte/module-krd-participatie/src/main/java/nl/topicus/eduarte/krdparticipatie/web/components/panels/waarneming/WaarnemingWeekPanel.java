package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.WaarnemingOverzichtInterface;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.web.components.factory.ParticipatieModuleComponentFactory;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * @author vanderkamp
 */
public class WaarnemingWeekPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private WaarnemingOverzichtZoekFilter filter;

	private boolean gekleurd = false;

	private IModel<Deelnemer> deelnemerModel;

	private IModel<Date> datumModel;

	/**
	 * IE7 heeft een tabel nodig met vaste breedte Om ervoor te zorgen dat de cellen 20px
	 * x 20px zijn, moeten alle kolommen geteld worden om de totale breedte te bepalen
	 * 
	 * @author Chris Gunnink
	 * @since SoundScape 1.14 - 27-05-2009
	 */
	private int columnCount = 0;

	public WaarnemingWeekPanel(String id, Deelnemer deelnemer, Date datum,
			WaarnemingOverzichtInterface page)
	{
		super(id);
		this.deelnemerModel = ModelFactory.getModel(deelnemer);
		this.datumModel = new Model<Date>(datum);
		filter = page.getFilterModel().getObject();
		WeekWaarnemingen weekWaarnemingen = new WeekWaarnemingen(datum, deelnemer, filter);
		if (filter.getDeelnemer() != null)
			add(new Label("week/deelnemer", "Week-" + TimeUtil.getInstance().getWeekOfYear(datum)));
		else
		{
			add(new Label("week/deelnemer", deelnemer.getDeelnemernummer() + " - "
				+ deelnemer.getPersoon().getVolledigeNaam()));
		}

		RepeatingView uren = new RepeatingView("uren");
		boolean heeftLabel = false;
		if (page.getLesweekIndelingModel().getObject() != null)
		{
			for (LesdagIndeling lesdag : (page.getLesweekIndelingModel().getObject())
				.getLesdagIndelingenOrderedByDay())
			{
				int dagVanWeek = lesdag.getDagNummer();
				for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
				{
					int lesuur = lestijd.getLesuur();
					int totLesuur = page.getTotLesuur(dagVanWeek);
					if (lesuur >= page.getVanLesuur(dagVanWeek) && lesuur <= totLesuur)
					{
						String label = weekWaarnemingen.getLabel(dagVanWeek, lestijd);
						Label waarnemingLabel = new Label("" + dagVanWeek + "-" + lesuur, label);
						if (StringUtil.isNotEmpty(label))
						{
							heeftLabel = true;
							final List<ParticipatieModuleComponentFactory> factories =
								EduArteApp.get().getPanelFactories(
									ParticipatieModuleComponentFactory.class);
							if (factories.size() == 1)
							{
								DeelnemerSecurityCheck check =
									new DeelnemerSecurityCheck(new ClassSecurityCheck(factories
										.get(0).getDeelnemerAgendaPageClass()), deelnemerModel
										.getObject());
								if (check.isActionAuthorized(Enable.class))
								{
									waarnemingLabel.add(new AjaxEventBehavior("onclick")
									{
										private static final long serialVersionUID = 1L;

										@Override
										protected void onEvent(AjaxRequestTarget target)
										{
											int week =
												TimeUtil.getInstance().getWeekOfYear(
													datumModel.getObject());
											int year =
												TimeUtil.getInstance().getYear(
													datumModel.getObject());
											Date startDate =
												TimeUtil.getInstance().getWeekBeginEnEindDatum(
													year, week)[0];
											Deelnemer deelnemer1 = deelnemerModel.getObject();
											setResponsePage(factories.get(0)
												.newDeelnemerAgendaPage(deelnemer1,
													deelnemer1.getEersteInschrijvingOpPeildatum(),
													startDate));
										}
									});
									waarnemingLabel.add(new AttributeAppender("class",
										new Model<String>("hand"), " "));
								}
							}
						}
						uren.add(waarnemingLabel);
						columnCount++;

						if (lesuur == totLesuur)
						{
							waarnemingLabel.add(new AttributeAppender("class", new Model<String>(
								"tdRightBorder"), " "));
						}
						if (isGekleurd())
							waarnemingLabel.add(new AttributeAppender("class", new Model<String>(
								"tdYellow"), " "));
						String color = weekWaarnemingen.getLastLabelColor();
						if (color != null)
						{
							heeftLabel = true;
							waarnemingLabel.add(new AttributeAppender("class", new Model<String>(
								"td" + color), " "));
						}
						String title = weekWaarnemingen.getLastWaarnemingTitle();
						if (title != null)
							waarnemingLabel.add(new AttributeAppender("title", new Model<String>(
								title), " "));
					}
				}
			}
		}
		add(uren);
		WebMarkupContainer totalenContainer = new WebMarkupContainer("totalenContainer");
		totalenContainer.setRenderBodyOnly(true);
		if (filter.isToonTotalenKolommen())
		{
			totalenContainer.add(new Label("totaalAanwezig", ""
				+ weekWaarnemingen.getTotaalAanwezig()));
			totalenContainer.add(new Label("totaalGeoorloofdAbsent", ""
				+ weekWaarnemingen.getTotaalGeoorloofdAbsent()));
			totalenContainer.add(new Label("totaalOngeoorloofdAbsent", ""
				+ weekWaarnemingen.getTotaalOngeoorloofdAbsent()));
		}
		else
			totalenContainer.setVisible(false);
		add(totalenContainer);

		if (!filter.isToonLegeRegels() && !heeftLabel)
			this.setVisible(false);
	}

	/**
	 * IE7 heeft een tabel nodig met vaste breedte Om ervoor te zorgen dat de cellen 20px
	 * x 20px zijn, moeten alle kolommen geteld worden om de totale breedte te bepalen
	 * 
	 * @author Chris Gunnink
	 * @since SoundScape 1.14 - 27-05-2009
	 */
	public int getColumnCount()
	{
		return columnCount;
	}

	private boolean isGekleurd()
	{
		gekleurd = !gekleurd;
		return gekleurd;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(deelnemerModel);
		ComponentUtil.detachQuietly(datumModel);
	}

}
