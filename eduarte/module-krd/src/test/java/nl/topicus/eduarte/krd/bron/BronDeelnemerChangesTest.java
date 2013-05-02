package nl.topicus.eduarte.krd.bron;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;
import org.junit.Test;

public class BronDeelnemerChangesTest extends EduArteTestCase
{
	private BronDeelnemerChanges bronDeelnemerChanges;

	private Deelnemer deelnemer = new Deelnemer();

	private List<BronStateChange> changes = new ArrayList<BronStateChange>();

	@Before
	public void setup()
	{
		bronDeelnemerChanges = new BronDeelnemerChanges(deelnemer, changes);
	}

	@Test
	public void isEnigeWijzigingTest()
	{
		Verbintenis gewijzigdeVerbintenis = new Verbintenis(deelnemer);

		BronStateChange statechange1 =
			new BronStateChange(gewijzigdeVerbintenis, "relevanteVooropleiding", null,
				"Vooropleiding 123");
		changes.add(statechange1);

		assertTrue(bronDeelnemerChanges.isEnigeWijziging(gewijzigdeVerbintenis,
			"relevanteVooropleiding"));

		BronStateChange statechange2 =
			new BronStateChange(gewijzigdeVerbintenis, "status", VerbintenisStatus.Voorlopig,
				VerbintenisStatus.Definitief);
		changes.add(statechange2);

		assertFalse(bronDeelnemerChanges.isEnigeWijziging(gewijzigdeVerbintenis,
			"relevanteVooropleiding"));
	}

}
