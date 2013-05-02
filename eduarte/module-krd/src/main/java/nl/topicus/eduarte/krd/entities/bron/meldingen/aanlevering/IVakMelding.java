package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;

public interface IVakMelding
{
	public String getExamenvak();

	public Boolean getIndicatieDiplomavak();

	public ToepassingResultaat getToepassingResultaatOfBeoordelingExamenVak();

	public Boolean getIndicatieWerkstuk();

	public HogerNiveau getHogerNiveau();

	public BeoordelingSchoolExamen getBeoordelingSchoolExamen();

	public Integer getCijferSchoolExamen();

	public Integer getCijferCE1();

	public Integer getCijferCE2();

	public Integer getCijferCE3();

	public Integer getEersteEindcijfer();

	public Integer getTweedeEindcijfer();

	public Integer getDerdeEindcijfer();

	public Integer getCijferCijferlijst();

	public Boolean getVerwezenNaarVolgendeTijdvak();

	public Boolean getCertificaat();

	public Boolean getIndicatieCombinatieCijfer();

	public Integer getVakCodeHogerNiveau();
}