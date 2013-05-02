package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;

public interface IBronExamenMelding
{
	public Deelnemer getDeelnemer();

	// iltcode
	public String getExamenCode();

	public Date getDatumUitslagExamen();

	public ExamenUitslag getUitslagExamen();

	public int getAantalVakken();

	public int getExamenJaar();

	public String getTitelOfThemaWerkstuk();

	public BeoordelingWerkstuk getBeoordelingWerkstuk();

	public List<IVakMelding> getVakMeldingen();

	public Integer getCijferWerkstuk();
}