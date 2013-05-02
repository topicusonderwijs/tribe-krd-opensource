package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GesprekSamenvattingZin extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 1024)
	@AutoForm(htmlClasses = "unit_640")
	private String zin;

	public GesprekSamenvattingZin()
	{
	}

	public String getZin()
	{
		return zin;
	}

	public void setZin(String zin)
	{
		this.zin = zin;
	}
}
