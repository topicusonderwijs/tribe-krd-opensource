package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.Date;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

public class BronMeldingAlgemeenDetailPanel extends TypedPanel<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	public BronMeldingAlgemeenDetailPanel(String id, IModel<IBronMelding> meldingModel)
	{
		super(id, meldingModel);
		add(new Label("caption", "Algemeen"));
		add(new TextField<String>("gebruiker", new PropertyModel<String>(meldingModel,
			"createdBy.gebruikersnaam")));
		add(new TextField<Date>("datumMutatie", new PropertyModel<Date>(meldingModel, "createdAt")));
		add(new TextField<BronMeldingStatus>("status", new PropertyModel<BronMeldingStatus>(
			meldingModel, "bronMeldingStatus")));
		add(new TextField<String>("geblokkeerd", new Model<String>(isGeblokkeerd() ? "Ja" : "Nee")));

		CheckBox accountantsmutatieCheckBox =
			new CheckBox("accountantsmutatie", new PropertyModel<Boolean>(meldingModel,
				"bekostigingsRelevant"));
		accountantsmutatieCheckBox.setOutputMarkupId(true);
		add(accountantsmutatieCheckBox);

		if (magAccountatsmutatieAanpassen()
			&& getBronMelding().getBronMeldingStatus() == BronMeldingStatus.WACHTRIJ)
		{
			accountantsmutatieCheckBox.add(new AjaxEventBehavior("onclick")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target)
				{
					IBronMelding melding = getBronMelding();
					if (melding instanceof BronAanleverMelding)
					{
						BronAanleverMelding bveMelding = (BronAanleverMelding) melding;
						bveMelding.setBekostigingsRelevant(!bveMelding.isBekostigingsRelevant());
						bveMelding.saveOrUpdate();
						bveMelding.commit();
					}
					else if (melding instanceof AbstractBronVOMelding)
					{
						AbstractBronVOMelding voMelding = (AbstractBronVOMelding) melding;
						voMelding.setBekostigingsRelevant(!voMelding.isBekostigingsRelevant());
						voMelding.saveOrUpdate();
						voMelding.commit();
					}
					if (melding.isBekostigingsRelevant())
					{
						info("De melding is aangepast zodat de verantwoordelijke de accountant is");
					}
					else
					{
						info("De melding is aangepast zodat de verantwoordelijke de instelling is");
					}
					((SecurePage) getPage()).refreshFeedback(target);
					target.addComponent(getComponent());
				}
			});
		}
		else
		{
			accountantsmutatieCheckBox.setEnabled(false);
		}

		WebMarkupContainer redenGeblokkeerdContainer =
			new WebMarkupContainer("redenGeblokkeerdContainer");
		redenGeblokkeerdContainer.add(new TextArea<String>("redenGeblokkeerd",
			new PropertyModel<String>(meldingModel, "redenGeblokkeerd")));
		redenGeblokkeerdContainer.setVisible(isGeblokkeerd());
		add(redenGeblokkeerdContainer);

	}

	private boolean magAccountatsmutatieAanpassen()
	{
		DataSecurityCheck magAccountantsmutatieOngedaanMakenCheck =
			new DataSecurityCheck(BronBpvWijzigingToegestaanCheck.WIJZIGEN_NA_MUTATIEBEPERKING);
		boolean magAccountantsmutatieOngedaanMaken =
			magAccountantsmutatieOngedaanMakenCheck.isActionAuthorized(Enable.class);
		return magAccountantsmutatieOngedaanMaken;
	}

	private boolean isGeblokkeerd()
	{
		return getBronMelding().isGeblokkeerd();
	}

	private IBronMelding getBronMelding()
	{
		return getModelObject();
	}
}
