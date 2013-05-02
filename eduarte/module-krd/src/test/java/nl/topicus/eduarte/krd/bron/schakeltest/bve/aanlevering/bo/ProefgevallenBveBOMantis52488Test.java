package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;

import org.junit.Test;

/**
 * Test voor het controleren van het aanpassen van de ingangsdatum van de verbintenis die
 * volledig bekostigd is (dus zonder bekostigingsperiodes).
 * 
 * @see <a href="http://bugs.topicus.nl/view.php?id=52488">Mantis 52488</a>
 */
public class ProefgevallenBveBOMantis52488Test extends ProefgevallenBveBO
{
	@Test
	public void aanpassingIngangsdatumVerwijdertEnVoegtBekostigingsperiodeToe() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);

		Date oorspronkelijkeDatum = huidigSchooljaar.getEenOktober();
		Date nieuweDatum = huidigSchooljaar.getBegindatum();

		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));

		PeriodegegevensInschrijvingRecord oorspronkelijkePeriodeRecord =
			melding.getPeriodeRecord(oorspronkelijkeDatum);
		assertThat(oorspronkelijkePeriodeRecord, is(not(nullValue())));

		assertThat(oorspronkelijkePeriodeRecord.getDatumIngangPeriodegegevensInschrijving(),
			is(oorspronkelijkeDatum));
		assertThat(oorspronkelijkePeriodeRecord.getSoortMutatie(), is(Verwijdering));

		PeriodegegevensInschrijvingRecord nieuwePeriodeRecord =
			melding.getPeriodeRecord(nieuweDatum);
		assertThat(nieuwePeriodeRecord, is(not(nullValue())));
		assertThat(nieuwePeriodeRecord.getDatumIngangPeriodegegevensInschrijving(), is(nieuweDatum));
		assertThat(nieuwePeriodeRecord.getIndicatieBekostigingInschrijving(), is(Boolean.TRUE));

		controller = new BronController();

		verbintenis.setBekostigd(Nee);
		addChange(verbintenis, "bekostigd", Ja, Nee);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));
		assertThat(nieuwePeriodeRecord.getIndicatieBekostigingInschrijving(), is(Boolean.FALSE));
	}

	@Test
	public void ongedaanmakenAanpassingIngangsdatum() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);

		Date oorspronkelijkeDatum = huidigSchooljaar.getEenOktober();
		Date nieuweDatum = huidigSchooljaar.getBegindatum();

		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));

		controller = new BronController();

		addChange(verbintenis, "begindatum", nieuweDatum, oorspronkelijkeDatum);
		verbintenis.setBegindatum(oorspronkelijkeDatum);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Aanpassing)));

		assertThat(melding.getPeriodeRecord(oorspronkelijkeDatum)
			.getIndicatieBekostigingInschrijving(), is(Boolean.TRUE));
	}

	@Test
	public void ongedaanmakenAanpassingIngangsdatumOngedaanmaken() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);

		Date oorspronkelijkeDatum = huidigSchooljaar.getEenOktober();
		Date nieuweDatum = huidigSchooljaar.getBegindatum();

		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));

		controller = new BronController();

		addChange(verbintenis, "begindatum", nieuweDatum, oorspronkelijkeDatum);
		verbintenis.setBegindatum(oorspronkelijkeDatum);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Aanpassing)));

		assertThat(melding.getPeriodeRecord(oorspronkelijkeDatum)
			.getIndicatieBekostigingInschrijving(), is(Boolean.TRUE));

		controller = new BronController();
		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));
	}

	@Test
	public void ongedaanmakenAanpassingIngangsdatumOngedaanmakenMetWijzigingTussendoor()
			throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);

		Date oorspronkelijkeDatum = huidigSchooljaar.getEenOktober();
		Date nieuweDatum = huidigSchooljaar.getBegindatum();

		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));

		assertThat(melding.getPeriodeRecord(verbintenis.getBegindatum())
			.getIndicatieBekostigingInschrijving(), is(Boolean.TRUE));

		controller = new BronController();

		addChange(verbintenis, "begindatum", nieuweDatum, oorspronkelijkeDatum);
		verbintenis.setBegindatum(oorspronkelijkeDatum);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Aanpassing)));

		assertThat(melding.getPeriodeRecord(verbintenis.getBegindatum())
			.getIndicatieBekostigingInschrijving(), is(Boolean.TRUE));

		controller = new BronController();

		addChange(verbintenis, "bekostigd", Ja, Nee);
		verbintenis.setBekostigd(Nee);

		controller.save();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Aanpassing)));

		assertThat(melding.getPeriodeRecord(verbintenis.getBegindatum())
			.getIndicatieBekostigingInschrijving(), is(Boolean.FALSE));

		controller = new BronController();
		addChange(verbintenis, "begindatum", oorspronkelijkeDatum, nieuweDatum);
		verbintenis.setBegindatum(nieuweDatum);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 320, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Aanpassing, Toevoeging, Verwijdering)));

		assertThat(melding.getPeriodeRecord(verbintenis.getBegindatum())
			.getIndicatieBekostigingInschrijving(), is(Boolean.FALSE));
	}

	/**
	 * Test of de meldingen die nog in de wachtrij staan goed verdicht worden met
	 * aanpassingen aan de verbintenis.
	 */
	@Test
	public void aanpassingIngangsdatumVerdichtOokBekostigingsperiode() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getEenOktober());
		verbintenis.setStatus(Definitief);
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding), is(asList(Toevoeging, Toevoeging)));

		assertThat(melding.getRecord(PeriodegegevensInschrijvingRecord.class)
			.getDatumIngangPeriodegegevensInschrijving(), is(verbintenis.getBegindatum()));

		controller = new BronController();

		addChange(verbintenis, "begindatum", verbintenis.getBegindatum(), huidigSchooljaar
			.getBegindatum());
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());

		controller.save();

		assertThat(melding.getRecord(PeriodegegevensInschrijvingRecord.class)
			.getDatumIngangPeriodegegevensInschrijving(), is(verbintenis.getBegindatum()));
	}

	@Test
	public void aanpassingBegindatumBekostigingsperiodeLevertVerwijderingEnToevoegingOp()
			throws Exception
	{
		getDeelnemer1002();

		verbintenis.setStatus(Definitief);
		Date eenOktober = huidigSchooljaar.getEenOktober();
		verbintenis.setBegindatum(eenOktober);

		Bekostigingsperiode periode1 = verbintenis.getBekostigingsperiodes().get(0);
		Bekostigingsperiode periode2 = verbintenis.getBekostigingsperiodes().get(1);

		periode1.setBegindatum(verbintenis.getBegindatum());
		periode1.setEinddatum(huidigSchooljaar.getEinddatum());
		periode1.setBekostigd(false);

		periode2.setBegindatum(volgendSchooljaar.getBegindatum());
		periode2.setEinddatum(null);
		periode2.setBekostigd(true);

		Date eenAugustus = huidigSchooljaar.getBegindatum();
		addChange(periode1, "begindatum", periode1.getBegindatum(), eenAugustus);
		periode1.setBegindatum(eenAugustus);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(asList(305, 321, 321)));
		assertThat(getMutaties(melding), is(asList(Toevoeging, Verwijdering)));

		assertThat(melding.getPeriodeRecord(eenOktober).getSoortMutatie(), is(Verwijdering));
		assertThat(melding.getPeriodeRecord(eenAugustus).getSoortMutatie(), is(Toevoeging));

		controller = new BronController();

		addChange(verbintenis, "begindatum", verbintenis.getBegindatum(), eenAugustus);
		verbintenis.setBegindatum(eenAugustus);

		controller.save();

		assertThat(melding.getRecord(PeriodegegevensInschrijvingRecord.class)
			.getDatumIngangPeriodegegevensInschrijving(), is(verbintenis.getBegindatum()));
	}
}
