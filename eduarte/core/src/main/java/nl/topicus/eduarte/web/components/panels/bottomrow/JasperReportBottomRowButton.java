package nl.topicus.eduarte.web.components.panels.bottomrow;

import java.io.IOException;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.wiquery.OpenInNewWindowAttributeModifier;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.link.SingleEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class voor bv een PDF knop op de deelnemer kaart page, deze genereert mbv de
 * opgegeven jasper reports file en de deelnemer een mooie pdf. in de current thread!
 * 
 * @author hoeve
 */
public abstract class JasperReportBottomRowButton<T extends IdObject> extends
		AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(JasperReportBottomRowButton.class);

	private String filename;

	private String modelName;

	private Class< ? extends SecurePage> clazz;

	/**
	 * @param bottomRow
	 * @param filename
	 *            naam van het jrxml bestand tov de pagina.
	 * @param clazz
	 *            de pagina class
	 * @param modelName
	 *            de variabele naam welke als ingang wordt gebruikt in de jrxml (bv
	 *            verbintenis)
	 */
	public JasperReportBottomRowButton(BottomRowPanel bottomRow, String filename,
			Class< ? extends SecurePage> clazz, String modelName)
	{
		super(bottomRow, "PDF", CobraKeyAction.GEEN, ButtonAlignment.LEFT);
		this.filename = filename;
		this.clazz = clazz;
		this.modelName = modelName;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		SingleEntityRapportageGenereerLink<T> link =
			new SingleEntityRapportageGenereerLink<T>(linkId, getTemplateModel(), modelName,
				getContextModel());
		link.add(new OpenInNewWindowAttributeModifier());

		return link;
	}

	private IModel<DocumentTemplate> getTemplateModel()
	{
		return new AbstractReadOnlyModel<DocumentTemplate>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public DocumentTemplate getObject()
			{
				DocumentTemplate template = new DocumentTemplate();
				template.setBestandsnaam(filename);
				template.setType(DocumentTemplateType.JRXML);
				template.setValid(true);

				try
				{
					template.setZzzBestand(ResourceUtil.readClassPathFileAsBytes(clazz, template
						.getBestandsnaam()));
				}
				catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}

				return template;
			}
		};
	}

	/**
	 * Overschrijf deze functie en geeft de Entiteit mee waarmee je een document wilt
	 * genereren.
	 */
	protected abstract IModel<T> getContextModel();

}
