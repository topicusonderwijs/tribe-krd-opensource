package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import java.util.Date;

import javax.persistence.Lob;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;

import org.apache.wicket.model.IModel;

public class CollectieveStatusovergangEditModel<T extends Enum<T>> implements
		IModel<CollectieveStatusovergangEditModel<T>>
{
	private static final long serialVersionUID = 1L;

	private T beginstatus;

	private T eindstatus;

	@AutoForm(description = "De uiteindelijk einddatum. In het selectiescherm worden alleen entiteiten getoond met begindatum voor deze einddatum.")
	private Date einddatum;

	private IModel<RedenUitschrijving> redenUitschrijvingModel;

	private IModel<Taxonomie> taxonomieModel;

	private IModel<DocumentTemplate> documentTemplateModel;

	@Lob
	@AutoForm(htmlClasses = "unit_max")
	private String toelichting;

	public CollectieveStatusovergangEditModel()
	{
	}

	public CollectieveStatusovergangEditModel(T beginstatus)
	{
		this.beginstatus = beginstatus;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

	public RedenUitschrijving getRedenUitschrijving()
	{
		if (redenUitschrijvingModel == null)
			redenUitschrijvingModel = ModelFactory.getModel(null);
		return redenUitschrijvingModel.getObject();
	}

	public void setRedenUitschrijving(RedenUitschrijving redenUitschrijving)
	{
		this.redenUitschrijvingModel = ModelFactory.getModel(redenUitschrijving);
	}

	public String getToelichting()
	{
		return toelichting;
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

	public T getBeginstatus()
	{
		return beginstatus;
	}

	public void setBeginstatus(T beginstatus)
	{
		this.beginstatus = beginstatus;
	}

	public T getEindstatus()
	{
		return eindstatus;
	}

	public void setEindstatus(T eindstatus)
	{
		this.eindstatus = eindstatus;
	}

	public Taxonomie getTaxonomie()
	{
		if (taxonomieModel == null)
			taxonomieModel = ModelFactory.getModel(null);
		return taxonomieModel.getObject();
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomieModel = ModelFactory.getModel(taxonomie);
	}

	public DocumentTemplate getDocumentTemplate()
	{
		if (documentTemplateModel == null)
			documentTemplateModel = ModelFactory.getModel(null);
		return documentTemplateModel.getObject();
	}

	public void setDocumentTemplate(DocumentTemplate documentTemplate)
	{
		this.documentTemplateModel = ModelFactory.getModel(documentTemplate);
	}

	public void setDocumentTemplateModel(IModel<DocumentTemplate> documentTemplateModel)
	{
		this.documentTemplateModel = documentTemplateModel;
	}

	@Override
	public CollectieveStatusovergangEditModel<T> getObject()
	{
		return this;
	}

	@Override
	public void setObject(CollectieveStatusovergangEditModel<T> object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(redenUitschrijvingModel);
		ComponentUtil.detachQuietly(taxonomieModel);
	}

}