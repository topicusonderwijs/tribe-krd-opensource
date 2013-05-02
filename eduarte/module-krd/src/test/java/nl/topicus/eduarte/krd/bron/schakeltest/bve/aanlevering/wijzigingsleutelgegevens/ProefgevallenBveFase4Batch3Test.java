package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.wijzigingsleutelgegevens;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Test;

@SuppressWarnings("hiding")
public class ProefgevallenBveFase4Batch3Test extends ProefgevallenBveFase4
{
	@Test
	public void proefgeval17() throws Exception
	{
		createGeval17();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4001"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100320254"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910701")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(1));

		WijzigingSleutelgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeboortedatumGewijzigd(), is(Datum.valueOf(19871119)));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeslachtGewijzigd(), is((Geslacht) null));
		assertThat(record1.getPostcodeVolgensInstellingGewijzigd(), is((String) null));
		assertThat(record1.getLandGewijzigd(), is((String) null));
		assertThat(record1.getDatumIngangAdreswijziging(), is((Date) null));
	}

	private void createGeval17() throws Exception
	{
		getDeelnemer4001();

		Persoon persoon = deelnemer.getPersoon();
		persoon.setGeboortedatum(asDate(19871119));

		persoon.getFysiekAdres().getAdres().setPostcode(null);
		persoon.getFysiekAdres().getAdres().setLand(Land.getNederland());

		addChange(persoon, "geboortedatum", asDate(19910701), persoon.getGeboortedatum());
	}

	@Test
	public void proefgeval18() throws Exception
	{
		createGeval18();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4002"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100144750"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910702")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(1));

		WijzigingSleutelgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeboortedatumGewijzigd(), is((Datum) null));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeslachtGewijzigd(), is(Geslacht.Vrouw));
		assertThat(record1.getPostcodeVolgensInstellingGewijzigd(), is((String) null));
		assertThat(record1.getLandGewijzigd(), is((String) null));
		assertThat(record1.getDatumIngangAdreswijziging(), is((Date) null));
	}

	private void createGeval18() throws Exception
	{
		getDeelnemer4002();

		Persoon persoon = deelnemer.getPersoon();
		persoon.setGeslacht(Geslacht.Vrouw);

		persoon.getFysiekAdres().getAdres().setPostcode(null);
		persoon.getFysiekAdres().getAdres().setLand(Land.getNederland());

		addChange(deelnemer.getPersoon(), "geslacht", Geslacht.Man, persoon.getGeslacht());
	}

	@Test
	public void proefgeval19() throws Exception
	{
		createGeval19();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4003"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100069880"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910703")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(1));

		WijzigingSleutelgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeboortedatumGewijzigd(), is((Datum) null));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeslachtGewijzigd(), is((Geslacht) null));
		assertThat(record1.getPostcodeVolgensInstellingGewijzigd(), is("9722BT"));
		assertThat(record1.getLandGewijzigd(), is((String) null));
		assertThat(record1.getDatumIngangAdreswijziging(), is(asDate(20090101)));
	}

	private void createGeval19() throws Exception
	{
		getDeelnemer4003();

		Persoon persoon = deelnemer.getPersoon();

		PersoonAdres persoonAdres = persoon.getFysiekAdres();
		persoonAdres.setBegindatum(asDate(20090101));
		persoonAdres.getAdres().setPostcode("9722BT");
		addChange(persoonAdres.getAdres(), "postcode", null, "9722BT");
	}

	@Test
	public void proefgeval20() throws Exception
	{
		createGeval20();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4004"));
		assertThat(melding.getSofinummer(), is("210000041"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910504")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(1));

		WijzigingSleutelgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeboortedatumGewijzigd(), is((Datum) null));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeslachtGewijzigd(), is((Geslacht) null));
		assertThat(record1.getPostcodeVolgensInstellingGewijzigd(), is("9722BT"));
		assertThat(record1.getLandGewijzigd(), is((String) null));
		assertThat(record1.getDatumIngangAdreswijziging(), is(asDate(20090101)));
	}

	private void createGeval20() throws Exception
	{
		getDeelnemer4004();

		PersoonAdres persoonAdres = deelnemer.getPersoon().getFysiekAdres();
		persoonAdres.getAdres().setLand(Land.getNederland());

		persoonAdres.getAdres().setPostcode("9722BT");
		persoonAdres.setBegindatum(asDate(20090101));

		addChange(persoonAdres.getAdres(), "postcode", "9722TB", persoonAdres.getAdres()
			.getPostcode());
	}

	@Test
	public void proefgeval21() throws Exception
	{
		createGeval21();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("4005"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100509521"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910705")));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(1));

		WijzigingSleutelgegevensRecord record1 = melding.getMeldingen().get(0);
		assertThat(record1.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeboortedatumGewijzigd(), is((Datum) null));
		assertThat(record1.getSofinummerAchterhaald(), is((String) null));
		assertThat(record1.getGeslachtGewijzigd(), is((Geslacht) null));
		assertThat(record1.getPostcodeVolgensInstellingGewijzigd(), is("9722BT"));
		assertThat(record1.getLandGewijzigd(), is((String) null));
		assertThat(record1.getDatumIngangAdreswijziging(), is(asDate(20090101)));
	}

	private void createGeval21() throws Exception
	{
		getDeelnemer4005();

		PersoonAdres persoonAdres = deelnemer.getPersoon().getFysiekAdres();
		persoonAdres.setBegindatum(asDate(20090101));
		persoonAdres.getAdres().setPostcode("9722BT");
		addChange(persoonAdres.getAdres(), "postcode", null, "9722BT");
	}

	@Test
	public void bveBO1Batch3() throws Exception
	{
		createGeval17();
		createGeval18();
		createGeval19();
		createGeval20();
		createGeval21();

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(3);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getBrinNummer(), is("01AA"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Beroepsonderwijs));

		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(3));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));

		assertThat(batchBVE.getAantalMeldingen(), is(5));
		assertThat(batchBVE.getAantalRecords(), is(11));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}