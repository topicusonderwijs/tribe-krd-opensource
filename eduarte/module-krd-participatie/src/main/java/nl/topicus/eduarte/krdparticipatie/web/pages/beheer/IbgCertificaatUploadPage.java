package nl.topicus.eduarte.krdparticipatie.web.pages.beheer;

import java.io.InputStream;
import java.io.Serializable;

import javax.persistence.Transient;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.dao.helpers.SSLCertificaatDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.SSLCertificaat;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.BeheerAbsentieredenen;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.file.SSLCertificaatUploadField;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.onderwijs.ibgverzuimloket.logic.IbgCertificaatModule;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgVerzuimException;
import nl.topicus.onderwijs.ibgverzuimloket.model.SSLCertificateProperties;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

@PageInfo(title = "Verzuimloket", menu = "Beheer > Participatie > Verzuimloket")
@InPrincipal(BeheerAbsentieredenen.class)
public class IbgCertificaatUploadPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	Form<Void> form;

	private IModel<PasswordObject> passwordObject;

	public IbgCertificaatUploadPage()
	{
		super(ParticipatieBeheerMenuItem.Verzuimloket);

		SSLCertificaatDataAccessHelper helper =
			DataAccessRegistry.getHelper(SSLCertificaatDataAccessHelper.class);

		SSLCertificaat cert = helper.findCertificaatOfInstelling();

		if (cert == null)
		{
			cert = new SSLCertificaat();
		}

		setDefaultModel(ModelFactory.getModel(cert, new DefaultModelManager(SSLCertificaat.class)));
		form = new Form<Void>("datapanel");

		passwordObject = ModelFactory.getModel(new PasswordObject());
		AutoFieldSet<PasswordObject> fieldSetCertificaat =
			new AutoFieldSet<PasswordObject>("certificaat", passwordObject, "Certificaat");
		fieldSetCertificaat.setPropertyNames("inputstream", "wachtwoord");
		fieldSetCertificaat.setRenderMode(RenderMode.EDIT);

		form.add(fieldSetCertificaat);
		setCertificaatInfo(form, cert);
		add(form);

		createComponents();
	}

	private void setCertificaatInfo(Form<Void> form, SSLCertificaat certificaat)
	{
		SSLCertificateProperties properties = new SSLCertificateProperties();
		if (certificaat.getCertificaat() != null)
		{
			try
			{
				properties = IbgCertificaatModule.getCertificaatProperties(properties, certificaat);

			}
			catch (IbgVerzuimException e)
			{
				error(e.getMessage());
			}

		}

		AutoFieldSet<SSLCertificateProperties> fieldSetCertificaat =
			new AutoFieldSet<SSLCertificateProperties>("properties",
				new Model<SSLCertificateProperties>(properties),
				"Eigenschappen ingelezen certificaat");
		fieldSetCertificaat.setPropertyNames("begindatum", "einddatum", "uitgever", "algoritme",
			"serienummer", "versie");
		form.add(fieldSetCertificaat);
	}

	@Override
	public void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{

				SSLCertificaat certificaat =
					(SSLCertificaat) IbgCertificaatUploadPage.this.getDefaultModelObject();
				PasswordObject pwObj = passwordObject.getObject();

				if (pwObj.getInputstream() != null)
				{

					try
					{
						IbgCertificaatModule.createCertificateFromFile(certificaat, pwObj
							.getInputstream(), pwObj.getWachtwoord());
					}
					catch (IbgVerzuimException e)
					{
						error("Fout bij het inlezen van het certificaat.\n"
							+ e.getLocalizedMessage());
						throw new RestartResponseException(getPage());
					}
					finally
					{
						pwObj.inputstream = null;
						pwObj.wachtwoord = "";
					}
					certificaat.saveOrUpdate();
					certificaat.commit();
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private class PasswordObject implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public PasswordObject()
		{

		}

		@Transient
		@AutoForm(label = "Wachtwoord", editorClass = PasswordTextField.class)
		private String wachtwoord;

		@Transient
		@AutoForm(label = "Bestand", editorClass = SSLCertificaatUploadField.class)
		private InputStream inputstream;

		public String getWachtwoord()
		{
			return wachtwoord;
		}

		public void setWachtwoord(String wachtwoord)
		{
			this.wachtwoord = wachtwoord;
		}

		public InputStream getInputstream()
		{
			return inputstream;
		}

		public void setInputstream(InputStream inputstream)
		{
			this.inputstream = inputstream;
		}
	}

	@Override
	public void detachModel()
	{
		super.detachModel();
		passwordObject.detach();
	}
}
