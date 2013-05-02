package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;
import org.junit.Test;

public class BronEduArteModelTest extends EduArteTestCase
{
	private BronEduArteModel model;

	@Before
	public void setup()
	{
		model = new BronEduArteModel();
	}

	@Test
	public void medewerkerNietInBronScope()
	{
		Medewerker medewerker = new Medewerker();
		medewerker.setPersoon(new Persoon());

		assertFalse(model.isInBronScope(medewerker.getPersoon()));
	}

	@Test
	public void ongekoppeldPersoonNietInScope()
	{
		assertFalse(model.isInBronScope(new Persoon()));
	}

	@Test
	public void ongekoppeldAdresNietInScope()
	{
		assertFalse(model.isInBronScope(new Adres()));
	}

	@Test
	public void adresControleertPersoon()
	{
		class MockBronEduArteModel extends BronEduArteModel
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isInBronScope(Persoon p)
			{
				// als deze methode wordt aangeroepen klopt de logica niet
				return true;
			}
		}
		model = new MockBronEduArteModel();
		Persoon persoon = new Persoon();
		PersoonAdres persoonAdres = persoon.newAdres();
		persoonAdres.setFysiekadres(true);
		persoon.getAdressen().add(persoonAdres);
		assertTrue(model.isInBronScope(persoonAdres.getAdres()));
	}

	@Test
	public void medewerkerPersoonControleertNietOfDeelnemerInBronScopeIs()
	{
		class MockBronEduArteModel extends BronEduArteModel
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isInBronScope(Deelnemer deelnemer)
			{
				// als deze methode wordt aangeroepen klopt de logica niet
				return true;
			}
		}
		model = new MockBronEduArteModel();
		Medewerker medewerker = new Medewerker();
		medewerker.setPersoon(new Persoon());

		assertFalse(model.isInBronScope(medewerker.getPersoon()));
	}

	@Test
	public void deelnemerPersoonControleertOfDeelnemerInBronScopeIs()
	{
		class MockBronEduArteModel extends BronEduArteModel
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isInBronScope(Deelnemer deelnemer)
			{
				return true;
			}
		}
		model = new MockBronEduArteModel();
		Deelnemer deelnemer = createDeelnemerMetPersoon();

		assertTrue(model.isInBronScope(deelnemer.getPersoon()));
	}

	private Deelnemer createDeelnemerMetPersoon()
	{
		TimeUtil timeUtil = TimeUtil.getInstance();

		Deelnemer deelnemer = new Deelnemer();
		Persoon persoon = new Persoon();
		deelnemer.setPersoon(persoon);
		persoon.setDeelnemer(deelnemer);
		PersoonAdres persoonAdres = persoon.newAdres();
		persoonAdres.setFysiekadres(true);
		persoonAdres.setBegindatum(timeUtil.yesterday());
		persoon.getAdressen().add(persoonAdres);
		return deelnemer;
	}

	@Test
	public void deelnemerMetIntakeVerbintenisNietInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Intake);
		assertFalse(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetVoorlopigeVerbintenisNietInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Voorlopig);
		assertFalse(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetVolledigeVerbintenisInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Volledig);
		assertTrue(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetAfgedrukteVerbintenisInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Afgedrukt);
		assertTrue(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetDefinitieveVerbintenisInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Definitief);
		assertTrue(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetAfgemeldeVerbintenisNietInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Afgemeld);
		assertFalse(model.isInBronScope(deelnemer));
	}

	@Test
	public void deelnemerMetAfgewezenVerbintenisNietInScope()
	{
		Deelnemer deelnemer = maakDeelnemerMetVerbintenis(Afgewezen);
		assertFalse(model.isInBronScope(deelnemer));
	}

	private Deelnemer maakDeelnemerMetVerbintenis(VerbintenisStatus status)
	{
		Deelnemer deelnemer = createDeelnemerMetPersoon();

		Verbintenis verbintenis = new Verbintenis(deelnemer);
		verbintenis.setStatus(status);
		deelnemer.getVerbintenissen().add(verbintenis);
		return deelnemer;
	}

	@Test
	public void huisnummer9()
	{
		Deelnemer deelnemer = createDeelnemerMetPersoon();
		Adres adres = deelnemer.getPersoon().getFysiekAdres().getAdres();
		adres.setHuisnummer("9");

		assertThat(model.getHuisnummer(deelnemer), is(9));
	}

	@Test
	public void huisnummerToevoeging9()
	{
		Deelnemer deelnemer = createDeelnemerMetPersoon();
		Adres adres = deelnemer.getPersoon().getFysiekAdres().getAdres();
		adres.setHuisnummer("9");

		assertThat(model.getHuisnummerToevoeging(deelnemer), nullValue());
	}

}
