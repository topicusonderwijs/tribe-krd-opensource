package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.web.components.choice.IJkpuntCombobox;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * @author vandenbrink
 */
@Entity
public class IJkpunt extends CompetentieNiveauVerzameling
{
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "ijkpunt", cascade = javax.persistence.CascadeType.REMOVE)
	@Cascade( {CascadeType.REMOVE})
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<RapportageTemplateIJkpunt> rapportageTemplates;

	@Override
	@AutoForm(label = "Type ijkpunt", editorClass = IJkpuntCombobox.class, htmlClasses = "unit_max")
	public String getTypeNaam()
	{
		return "IJkpunt" + (getDeelnemer() == null ? "" : " (individueel)");
	}

	public List<RapportageTemplateIJkpunt> getRapportageTemplates()
	{
		return rapportageTemplates;
	}

	public void setRapportageTemplates(List<RapportageTemplateIJkpunt> rapportageTemplates)
	{
		this.rapportageTemplates = rapportageTemplates;
	}

	public boolean isBereikt(Map<Leerpunt, CompetentieNiveau> beoordelingMap)
	{
		for (Entry<Leerpunt, CompetentieNiveau> curEntry : getCompetentieNiveauAsMap().entrySet())
		{
			CompetentieNiveau behaaldeBeoordeling = beoordelingMap.get(curEntry.getKey());
			CompetentieNiveau ijkpuntNiveau = curEntry.getValue();
			if (behaaldeBeoordeling == null
				|| behaaldeBeoordeling.getScore().getWaarde() < ijkpuntNiveau.getScore()
					.getWaarde())
				return false;
		}
		return true;
	}
}
