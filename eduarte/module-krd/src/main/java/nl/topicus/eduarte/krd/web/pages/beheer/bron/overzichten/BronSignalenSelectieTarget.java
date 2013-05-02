package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.io.Serializable;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie.SignalenListModel;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class BronSignalenSelectieTarget extends AbstractSelectieTarget<Serializable, IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	public BronSignalenSelectieTarget()
	{
		super(BronSignalenPage.class, "Opslaan");
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Serializable, IBronSignaal> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				for (IBronSignaal signaal : SignalenListModel
					.getSignalen((BronSignaalZoekFilter) base.getFilter()))
				{
					boolean isGeaccordeerd =
						(signaal.getGeaccordeerd() != null ? signaal.getGeaccordeerd() : false);

					if (!base.getSelection().isSelected(signaal) == isGeaccordeerd)
					{
						if (signaal instanceof BronVoSignaal)
						{
							BronVoSignaal sign = (BronVoSignaal) signaal;
							sign.setGeaccordeerd(!isGeaccordeerd);
							sign.saveOrUpdate();
						}
						if (signaal instanceof BronBveTerugkoppelRecord)
						{
							BronBveTerugkoppelRecord sign = (BronBveTerugkoppelRecord) signaal;
							sign.setGeaccordeerd(!isGeaccordeerd);
							sign.saveOrUpdate();
						}
					}
				}
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			}
		};
	}
}
