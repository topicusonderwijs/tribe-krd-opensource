package nl.topicus.eduarte.krd.web.components.panels.examen;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DatumUitslagVaststellenPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DeelnemerKwalificatieSelecterenPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.KwalificatieModel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.TijdvakAangevenPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class OvergangLinkPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private IModel<ToegestaneExamenstatusOvergang> toegExOvergangModel;

	public OvergangLinkPanel(String id, IModel<ToegestaneExamenstatusOvergang> toegExOvergangModel)
	{
		super(id);
		this.toegExOvergangModel = toegExOvergangModel;
		WebMarkupContainer singleLink = new WebMarkupContainer("singleLink");
		singleLink.setRenderBodyOnly(true);
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				onLinkClick(true);
			}
		};
		link.add(new Label("linkLabel", getToegestaneExamenstatusOvergang().getActie()));
		singleLink.add(link);
		add(singleLink);

		WebMarkupContainer doubleLink = new WebMarkupContainer("doubleLink");
		doubleLink.setRenderBodyOnly(true);
		doubleLink.add(new Label("actie", getToegestaneExamenstatusOvergang().getActie()));
		Link<Void> linkGeslaagd = new Link<Void>("linkGeslaagd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				onLinkClick(true);
			}
		};
		linkGeslaagd.add(new Label("linkLabel", getToegestaneExamenstatusOvergang()
			.getNaarExamenstatus() == null ? "" : getToegestaneExamenstatusOvergang()
			.getNaarExamenstatus().getNaam()));
		doubleLink.add(linkGeslaagd);
		Link<Void> linkGezakt = new Link<Void>("linkGezakt")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				onLinkClick(false);
			}
		};
		linkGezakt.add(new Label("linkLabel", getToegestaneExamenstatusOvergang()
			.getAfgewezenExamenstatus() == null ? "" : getToegestaneExamenstatusOvergang()
			.getAfgewezenExamenstatus().getNaam()));
		doubleLink.add(linkGezakt);
		singleLink.setVisible(getToegestaneExamenstatusOvergang().getNaarExamenstatus() == null
			|| getToegestaneExamenstatusOvergang().getNaarExamenstatus().isCriteriumbankControle()
			|| getToegestaneExamenstatusOvergang().getAfgewezenExamenstatus() == null);
		doubleLink.setVisible(!singleLink.isVisible());
		add(doubleLink);
	}

	protected void onLinkClick(boolean geslaagd)
	{
		KwalificatieModel kwalificatieModel = new KwalificatieModel();
		kwalificatieModel.setToegestaneExamenstatusOvergang(getToegestaneExamenstatusOvergang());
		if (geslaagd)
			kwalificatieModel.setGeselecteerdeStatus(getToegestaneExamenstatusOvergang()
				.getNaarExamenstatus());
		else
			kwalificatieModel.setGeselecteerdeStatus(getToegestaneExamenstatusOvergang()
				.getAfgewezenExamenstatus());
		if (getToegestaneExamenstatusOvergang().isBepaaltDatumUitslag())
			setResponsePage(new DatumUitslagVaststellenPage(kwalificatieModel));
		else if (getToegestaneExamenstatusOvergang().isTijdvakAangeven())
			setResponsePage(new TijdvakAangevenPage(kwalificatieModel));
		else
			setResponsePage(new DeelnemerKwalificatieSelecterenPage(kwalificatieModel));

	}

	private ToegestaneExamenstatusOvergang getToegestaneExamenstatusOvergang()
	{
		return toegExOvergangModel.getObject();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(toegExOvergangModel);
		super.onDetach();
	}

}