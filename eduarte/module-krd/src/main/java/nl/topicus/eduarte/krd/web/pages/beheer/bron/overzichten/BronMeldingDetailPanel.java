package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.io.Serializable;

import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

public class BronMeldingDetailPanel extends Panel
{
	@InPrincipal(BronOverzichtWrite.class)
	private final class BronRecordVerwijderButton extends ConfirmationLink<Void>
	{
		private static final long serialVersionUID = 1L;

		private BronRecordVerwijderButton(String id, String confirmMessage)
		{
			super(id, confirmMessage);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BronRecordVerwijderButton.class));
		}

		@Override
		public void onClick()
		{
			if (aanleveringModel == null)
			{
				getSession()
					.info(
						"Kon record niet verwijderen, meldt dit A.U.B. met het betreffende deelnemernummer, meldingnummer en/of batchnummer");
				return;
			}
			Object record = aanleveringModel.getObject();
			if (record instanceof BronBveAanleverRecord)
			{
				BronBveAanleverRecord bveRecord = (BronBveAanleverRecord) record;
				bveRecord.getMelding().getMeldingen().remove(bveRecord);
				bveRecord.delete();
				bveRecord.getMelding().consolideerRecords();
				bveRecord.commit();
				getSession().info("Record is verwijderd");
			}
		}

		@Override
		public boolean isVisible()
		{
			// Terugkoppelrecords type 411 hebben geen corresponderend aanleverrecord
			if (aanleveringModel == null)
				return false;
			Object record = aanleveringModel.getObject();
			if (record instanceof BronBveAanleverRecord)
			{
				BronBveAanleverRecord bveRecord = (BronBveAanleverRecord) record;
				return bveRecord.getMelding().getMeldingen().size() > 1
					&& bveRecord.getMelding().getBronMeldingStatus() == BronMeldingStatus.WACHTRIJ;
			}

			// vooralsnog alleen voor BVE records mogelijk maken om de records te
			// verwijderen.
			return false;
		}
	}

	private static final long serialVersionUID = 1L;

	private IModel< ? > aanleveringModel;

	private IModel< ? > terugkoppelModel;

	private RepeatingView properties;

	public BronMeldingDetailPanel(String id, String caption, IModel< ? > recordModel)
	{
		this(id, caption, recordModel, null);
	}

	public BronMeldingDetailPanel(String id, String caption, IModel< ? > aanleveringModel,
			IModel< ? > terugkoppelModel)
	{
		super(id);
		this.aanleveringModel = aanleveringModel;
		this.terugkoppelModel = terugkoppelModel;
		add(new Label("caption", caption));
		properties = new RepeatingView("properties");
		add(properties);

		add(new BronRecordVerwijderButton("verwijder", "Het verwijderen van dit record uit deze "
			+ "melding betekent dat dit record niet meer naar BRON gestuurd wordt en "
			+ "kan niet ongedaan gemaakt worden. Hierdoor kan er een verschil tussen "
			+ "BRON en KRD ontstaan.\\n\\nWeet u zeker dat u het record wilt verwijderen?"));
	}

	public void addProperty(String label, String property)
	{
		WebMarkupContainer container = new WebMarkupContainer(properties.newChildId());
		container.add(new Label("label", label));
		if (aanleveringModel != null)
			addTextField(container, "aanlevering", aanleveringModel, property);
		else
			container.add(new TextField<String>("aanlevering", new Model<String>())
				.setVisible(false));

		if (terugkoppelModel != null)
			addTextField(container, "terugkoppeling", terugkoppelModel, property);
		else
			addTextField(container, "terugkoppeling", aanleveringModel, "terugkoppelrecord."
				+ property);
		properties.add(container);
	}

	private void addTextField(WebMarkupContainer container, String id, IModel< ? > baseModel,
			String property)
	{
		IModel<Object> model = new PropertyModel<Object>(baseModel, property);
		if (model.getObject() instanceof Boolean)
		{
			Boolean bool = (Boolean) model.getObject();
			String waarde = (bool.booleanValue() ? "Ja" : "Nee");
			container.add(new TextField<String>(id, Model.of(waarde)));
		}
		else
			container.add(new TextField<Object>(id, model));
	}

	public void addPropertyZonderTerugKoppelField(String label, String property)
	{
		WebMarkupContainer container = new WebMarkupContainer(properties.newChildId());
		container.add(new Label("label", label));
		if (aanleveringModel != null)
			container.add(new TextField<Object>("aanlevering", new PropertyModel<Object>(
				aanleveringModel, property)));
		else
			container.add(new TextField<Serializable>("aanlevering", new Model<Serializable>(null))
				.setVisible(false));
		container.add(new TextField<Serializable>("terugkoppeling", new Model<Serializable>(null))
			.setVisible(false));
		properties.add(container);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(aanleveringModel);
		ComponentUtil.detachQuietly(terugkoppelModel);
		super.onDetach();
	}
}
