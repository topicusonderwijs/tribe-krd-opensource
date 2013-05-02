package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Nieuwe BRON-batches aanmaken", menu = "Deelnemer")
@InPrincipal(BronOverzichtWrite.class)
public class BronBatchesAanmakenPage extends AbstractBronPage
{
	private ListView<IBronBatch> listView;

	public BronBatchesAanmakenPage(BronBatchModel bronBatchModel)
	{
		listView = new ListView<IBronBatch>("listview", bronBatchModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<IBronBatch> item)
			{
				item.add(new Label("batchNummerAsString"));
				item.add(new Label("aanleverPuntNummer"));
				BronBatchModel model = (BronBatchModel) listView.getModel();
				item.add(new Label("aantalMeldingen", model.getAantalMeldingen(item.getModel(),
					null)));
				item.add(new Label("aantalAanpassingen", model.getAantalMeldingen(item.getModel(),
					SoortMutatie.Aanpassing)));

				item.add(new Label("aantalToevoegingen", model.getAantalMeldingen(item.getModel(),
					SoortMutatie.Toevoeging)));
				item.add(new Label("aantalUitschrijvingen", model.getAantalMeldingen(item
					.getModel(), SoortMutatie.Uitschrijving)));
				item.add(new Label("aantalVerwijderingen", model.getAantalMeldingen(
					item.getModel(), SoortMutatie.Verwijdering)));
				item.add(new TargetBasedSecurePageLink<Void>("details", new IPageLink()
				{

					private static final long serialVersionUID = 1L;

					@Override
					public Page getPage()
					{
						BronBatchModel batchModel = (BronBatchModel) listView.getModel();
						return new BronBatchAanmakenMeldingenPage(BronBatchesAanmakenPage.this,
							batchModel.getSelection(item.getModel()),
							new AbstractSelectieTarget<Serializable, IBronMelding>(
								BronBatchesAanmakenPage.class, "Opslaan")
							{
								private static final long serialVersionUID = 1L;

								@Override
								public Link<Void> createLink(String linkId,
										ISelectionComponent<Serializable, IBronMelding> base)
								{
									return new Link<Void>(linkId)
									{
										private static final long serialVersionUID = 1L;

										@Override
										public void onClick()
										{
											setResponsePage(BronBatchesAanmakenPage.this);
										}
									};
								}
							}, new PropertyModel<List<IBronMelding>>(item.getModel(), "meldingen"));
					}

					@Override
					public Class< ? extends Page> getPageIdentity()
					{
						return BronBatchAanmakenMeldingenPage.class;
					}
				}));
			}

			@Override
			protected IModel<IBronBatch> getListItemModel(
					IModel< ? extends List<IBronBatch>> listViewModel, int index)
			{
				BronBatchModel batchModel = (BronBatchModel) listViewModel;
				return batchModel.getItem(index);
			}
		};
		add(listView);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractLinkButton(panel, "Bevestigen", CobraKeyAction.OPSLAAN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				BronBatchModel model = (BronBatchModel) listView.getModel();
				model.save();
				setResponsePage(new BronBatchesAangemaaktPage(model));
			}
		});
		panel.addButton(new AnnulerenButton(panel, BronAlgemeenPage.class));
		super.fillBottomRow(panel);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
