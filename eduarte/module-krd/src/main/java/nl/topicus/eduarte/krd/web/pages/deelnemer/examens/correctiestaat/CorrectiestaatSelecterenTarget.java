package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.correctiestaat;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.wiquery.OpenInNewWindowAttributeModifier;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DeelnemerKwalificatieGeselecteerdPage;
import nl.topicus.eduarte.web.components.link.SingleEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrectiestaatSelecterenTarget extends
		AbstractSelectieTarget<Verbintenis, Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CorrectiestaatSelecterenTarget.class);

	private Correctiestaat correctiestaat;

	public CorrectiestaatSelecterenTarget(Correctiestaat correctiestaat)
	{
		super(DeelnemerKwalificatieGeselecteerdPage.class, "Genereer PDF");
		this.correctiestaat = correctiestaat;
	}

	@Override
	public Link<DocumentTemplate> createLink(String linkId,
			final ISelectionComponent<Verbintenis, Verbintenis> base)
	{
		SingleEntityRapportageGenereerLink<Correctiestaat> link =
			new SingleEntityRapportageGenereerLink<Correctiestaat>(linkId, getTemplateModel(),
				"correctiestaat", new AbstractReadOnlyModel<Correctiestaat>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Correctiestaat getObject()
					{
						correctiestaat.setVerbintenissen(base.getSelectedElements());
						correctiestaat.haalResultatenOp();
						return correctiestaat;
					}
				});
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
				template.setBestandsnaam("correctiestaat.jrxml");
				template.setType(DocumentTemplateType.JRXML);
				template.setValid(true);
				try
				{
					template.setZzzBestand(ResourceUtil.readClassPathFileAsBytes(
						CorrectiestaatDeelnemersSelecterenPage.class, template.getBestandsnaam()));
				}
				catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}
				return template;
			}
		};
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(correctiestaat);
	}
}
