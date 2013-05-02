package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * * Template voor eens samenvatting van een gesprek.
 * 
 * @author N. Henzen
 */

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GesprekSamenvattingTemplate extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 30)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Lob
	@Column(nullable = false)
	@AutoForm(editorClass = TextEditorPanel.class)
	private String template;

	public GesprekSamenvattingTemplate()
	{
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}

	public String getTemplate()
	{
		return template;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}
}
