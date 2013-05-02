package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter.TypeMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

/**
 * Panel voor het selecteren van onderwijssoort(Beroepsonderwijs, Educatie, VAVO, VO)
 * 
 * @author vandekamop
 */
public class SelecteerOnderwijssoortPanel extends ModalWindowBasePanel<BronOnderwijssoort>
{
	private static final long serialVersionUID = 1L;

	private class OnderwijsSoort implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private BronOnderwijssoort bronOnderwijssoort;

		private long aantalMeldingen;

		public OnderwijsSoort(BronOnderwijssoort bronOnderwijssoort, long aantalMeldingen)
		{
			this.bronOnderwijssoort = bronOnderwijssoort;
			this.aantalMeldingen = aantalMeldingen;
		}

		public BronOnderwijssoort getBronOnderwijssoort()
		{
			return bronOnderwijssoort;
		}

		public long getAantalMeldingen()
		{
			return aantalMeldingen;
		}
	}

	public SelecteerOnderwijssoortPanel(String id, SelecteerOnderwijssoortModalWindow modalWindow,
			final BronMeldingZoekFilter meldingFilter, TypeMelding typeMelding)
	{
		super(id, modalWindow);
		BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<OnderwijsSoort> soorten = new ArrayList<OnderwijsSoort>();
		meldingFilter.setTypeMelding(typeMelding);
		meldingFilter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		for (BronOnderwijssoort onderwijssoort : BronOnderwijssoort.values())
		{
			meldingFilter.setBronOnderwijssoort(onderwijssoort);
			if (BronOnderwijssoort.VOORTGEZETONDERWIJS == onderwijssoort)
				meldingFilter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
			if (BronOnderwijssoort.VAVO == onderwijssoort)
				meldingFilter.setBronMeldingOnderdeelNot(BronMeldingOnderdeel.Examen);
			soorten
				.add(new OnderwijsSoort(onderwijssoort, helper.getAantalMeldingen(meldingFilter)));
			meldingFilter.setBronMeldingOnderdeel(null);
			meldingFilter.setBronMeldingOnderdeelNot(null);
		}
		ListView<OnderwijsSoort> listView = new ListView<OnderwijsSoort>("listview", soorten)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<OnderwijsSoort> item)
			{

				AjaxLink<Void> link = new AjaxLink<Void>("link")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						OnderwijsSoort soortenMetAantalllen = item.getModelObject();
						getModalWindow().setModel(
							new Model<BronOnderwijssoort>(soortenMetAantalllen
								.getBronOnderwijssoort()));
						getModalWindow().close(target);
					}

				};
				item.add(link);
				OnderwijsSoort soortenMetAantalllen = item.getModelObject();
				link.add(new Label("soort", StringUtil.onlyFirstCharUppercase(soortenMetAantalllen
					.getBronOnderwijssoort().toString().toLowerCase())
					+ "(" + soortenMetAantalllen.getAantalMeldingen() + ")"));
			}
		};
		add(listView);
		createComponents();

	}
}
