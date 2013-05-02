package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord;

import org.junit.Test;

public class ProefgevallenBveBOSleutelgegevensTest extends ProefgevallenBveBO
{
	private long bsn = 210000004;

	@Test
	public void sofinummerWijzigingNietZonderOnderwijsnummer() throws Exception
	{
		getDeelnemer1005();
		deelnemer.setOnderwijsnummer(null);
		deelnemer.getPersoon().setBsn(bsn);
		verbintenis.setStatus(Definitief);

		addChange(deelnemer.getPersoon(), "bsn", null, bsn);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(0));
	}

	@Test
	public void sofinummerAlleenBijGeenGbaRelatie() throws Exception
	{
		getDeelnemer1005();
		deelnemer.setOnderwijsnummer(bsn);
		deelnemer.getPersoon().setBsn(bsn);
		verbintenis.setStatus(Definitief);

		addChange(deelnemer.getPersoon(), "bsn", null, bsn);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 306)));
		assertThat(melding.getSofinummer(), is(nullValue()));
		assertThat(melding.getOnderwijsnummer(), is(String.valueOf(bsn)));

		WijzigingSleutelgegevensRecord record =
			melding.getRecord(WijzigingSleutelgegevensRecord.class);
		assertThat(record.getSofinummerAchterhaald(), is(String.valueOf(bsn)));
	}

	@Test
	public void sofinummerGevuldBijOnderwijsnummer() throws Exception
	{
		getDeelnemer1005();
		deelnemer.setOnderwijsnummer(bsn);
		deelnemer.getPersoon().setBsn(bsn);

		addChange(verbintenis, "status", null, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320, 321)));
		assertThat(melding.getSofinummer(), is(String.valueOf(bsn)));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));
	}

	@Test
	public void sofinummerAchterhaaldPastMeldingNietAan() throws Exception
	{
		getDeelnemer1005();
		deelnemer.setOnderwijsnummer(bsn);
		deelnemer.getPersoon().setBsn(null);

		addChange(verbintenis, "status", null, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding inschrijving = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(Arrays.asList(insert(inschrijving))));

		assertThat(getRecordTypes(inschrijving), is(Arrays.asList(305, 320, 321)));
		assertThat(inschrijving.getSofinummer(), is(nullValue()));
		assertThat(inschrijving.getOnderwijsnummer(), is(String.valueOf(bsn)));

		controller = new BronController();

		deelnemer.getPersoon().setBsn(bsn);
		addChange(deelnemer.getPersoon(), "bsn", null, bsn);

		controller.save();

		aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(2));

		BronAanleverMelding sleutel = getTweedeMelding();

		assertThat(tester.getTransactionLog(), is(Arrays.asList(insert(inschrijving),
			insert(sleutel))));

		assertThat(getRecordTypes(inschrijving), is(Arrays.asList(305, 320, 321)));
		assertThat(inschrijving.getSofinummer(), is(nullValue()));
		assertThat(inschrijving.getOnderwijsnummer(), is(String.valueOf(bsn)));

		assertThat(getRecordTypes(sleutel), is(Arrays.asList(305, 306)));
		assertThat(sleutel.getSofinummer(), is(nullValue()));
		assertThat(sleutel.getOnderwijsnummer(), is(String.valueOf(bsn)));
	}

	@Test
	public void wijzigingAdresEnSofinummerAchterhaaldWordenVerdicht() throws Exception
	{
		Date gisteren = TimeUtil.getInstance().yesterday();
		BronEduArteModel model = new BronEduArteModel();
		getDeelnemer1005();

		deelnemer.setOnderwijsnummer(bsn);
		deelnemer.getPersoon().setBsn(null);

		deelnemer.getPersoon().setGeslacht(Geslacht.Vrouw);
		addChange(deelnemer.getPersoon(), "geslacht", Geslacht.Man, Geslacht.Vrouw);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding sleutel = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(Arrays.asList(insert(sleutel))));

		assertThat(getRecordTypes(sleutel), is(Arrays.asList(305, 306)));
		assertThat(sleutel.getSofinummer(), is(nullValue()));
		assertThat(sleutel.getOnderwijsnummer(), is(String.valueOf(bsn)));
		assertThat(sleutel.getGeslacht(), is(Geslacht.Man));
		assertThat(sleutel.getGeboortedatum(), is(model.getGeboortedatum(deelnemer)));
		assertThat(sleutel.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(sleutel.getLand(), is(nullValue()));

		WijzigingSleutelgegevensRecord record =
			sleutel.getRecord(WijzigingSleutelgegevensRecord.class);
		assertThat(record.getDatumIngangAdreswijziging(), is(nullValue()));
		assertThat(record.getPostcodeVolgensInstellingGewijzigd(), is(nullValue()));
		assertThat(record.getSofinummerAchterhaald(), is(nullValue()));
		assertThat(record.getGeboortedatumGewijzigd(), is(nullValue()));
		assertThat(record.getGeslachtGewijzigd(), is(Geslacht.Vrouw));

		controller = new BronController();

		deelnemer.getPersoon().getFysiekAdres().getAdres().setPostcode("7411HR");
		deelnemer.getPersoon().getFysiekAdres().setBegindatum(gisteren);
		addChange(deelnemer.getPersoon().getFysiekAdres(), "begindatum", null, gisteren);
		addChange(deelnemer.getPersoon().getFysiekAdres().getAdres(), "postcode", "9722TB",
			"7411HR");

		controller.save();

		assertThat(tester.getTransactionLog(), is(Arrays.asList(insert(sleutel), update(sleutel))));

		assertThat(getRecordTypes(sleutel), is(Arrays.asList(305, 306)));
		assertThat(sleutel.getOnderwijsnummer(), is(String.valueOf(bsn)));
		assertThat(sleutel.getSofinummer(), is(nullValue()));
		assertThat(sleutel.getGeslacht(), is(Geslacht.Man));
		assertThat(sleutel.getGeboortedatum(), is(model.getGeboortedatum(deelnemer)));
		assertThat(sleutel.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(sleutel.getLand(), is(nullValue()));

		assertThat(record.getDatumIngangAdreswijziging(), is(gisteren));
		assertThat(record.getPostcodeVolgensInstellingGewijzigd(), is("7411HR"));
		assertThat(record.getSofinummerAchterhaald(), is(nullValue()));
		assertThat(record.getGeboortedatumGewijzigd(), is(nullValue()));
		assertThat(record.getGeslachtGewijzigd(), is(Geslacht.Vrouw));

		controller = new BronController();

		deelnemer.getPersoon().setBsn(bsn);
		addChange(deelnemer.getPersoon(), "bsn", null, bsn);

		controller.save();

		assertThat(tester.getTransactionLog(), is(Arrays.asList(insert(sleutel), update(sleutel),
			update(sleutel))));

		assertThat(getRecordTypes(sleutel), is(Arrays.asList(305, 306)));
		assertThat(sleutel.getOnderwijsnummer(), is(String.valueOf(bsn)));
		assertThat(sleutel.getSofinummer(), is(nullValue()));
		assertThat(sleutel.getGeslacht(), is(Geslacht.Man));
		assertThat(sleutel.getGeboortedatum(), is(model.getGeboortedatum(deelnemer)));
		assertThat(sleutel.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(sleutel.getLand(), is(nullValue()));

		assertThat(record.getDatumIngangAdreswijziging(), is(gisteren));
		assertThat(record.getPostcodeVolgensInstellingGewijzigd(), is("7411HR"));
		assertThat(record.getSofinummerAchterhaald(), is(String.valueOf(bsn)));
		assertThat(record.getGeboortedatumGewijzigd(), is(nullValue()));
		assertThat(record.getGeslachtGewijzigd(), is(Geslacht.Vrouw));
	}
}
