package nl.topicus.eduarte.entities.ibgverzuimloket;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.ibgverzuimloket.model.ISSLCertificaat;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class SSLCertificaat extends InstellingEntiteit implements ISSLCertificaat
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	@Lob()
	private byte[] certificaat;

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public byte[] getCertificaat()
	{
		return certificaat;
	}

	@Override
	public void setCertificaat(byte[] value)
	{
		certificaat = value;

	}
}
