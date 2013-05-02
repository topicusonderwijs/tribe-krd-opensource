package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.apache.wicket.model.IModel;

public class DeelnemerBijlageZoekFilter extends AbstractZoekFilter<DeelnemerBijlage>
{
	private static final long serialVersionUID = 1L;

	private IModel<DocumentCategorie> documentCategorie;

	private IModel<Deelnemer> deelnemer;

	private IModel<Deelnemer> nietDeelnemer;

	private String documentnummer;

	private Date einddatumVoor;

	private Date einddatumNa;

	public DeelnemerBijlageZoekFilter()
	{
	}

	public DocumentCategorie getDocumentCategorie()
	{
		return getModelObject(documentCategorie);
	}

	public void setDocumentCategorie(DocumentCategorie documentCategorie)
	{
		this.documentCategorie = makeModelFor(documentCategorie);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Deelnemer getNietDeelnemer()
	{
		return getModelObject(nietDeelnemer);
	}

	public void setNietDeelnemer(Deelnemer nietDeelnemer)
	{
		this.nietDeelnemer = makeModelFor(nietDeelnemer);
	}

	public String getDocumentnummer()
	{
		return documentnummer;
	}

	public void setDocumentnummer(String documentnummer)
	{
		this.documentnummer = documentnummer;
	}

	public Date getEinddatumVoor()
	{
		return einddatumVoor;
	}

	public void setEinddatumVoor(Date einddatumVoor)
	{
		this.einddatumVoor = einddatumVoor;
	}

	public Date getEinddatumNa()
	{
		return einddatumNa;
	}

	public void setEinddatumNa(Date einddatumNa)
	{
		this.einddatumNa = einddatumNa;
	}
}
