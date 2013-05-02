package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.NummerGeneratorHibernateDataAccessHelper.Sequences;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingSequence;

public interface NummerGeneratorDataAccessHelper extends DataAccessHelper<InstellingEntiteit>
{
	/**
	 * Geeft de InstellingSequence entiteit met de naam die opgegeven is, deze is
	 * verbonden aan een sequence-generator met dezelfde naam.
	 */
	public InstellingSequence getCurrentInstellingSequence(Sequences sequence);

	/**
	 * Als een sequence verandert wordt, zal dit door gecascade moeten worden in de
	 * database, daarvoor is deze functie bedoelt. Deze handelt dit automatisch af.
	 */
	public void updateInstellingSequence(InstellingSequence sequence);

	/**
	 * Geeft de huidige waarde van een sequence terug.
	 */
	public long getCurrentValue(Sequences sequence);

	/**
	 * Genereert een nieuw deelnemernummer dat 1 hoger is dan het laatst uitgegeven
	 * deelnemernummer binnen de instelling.
	 */
	public int newDeelnemernummer();

	/**
	 * Genereert een nieuw debiteurnummer dat 1 hoger is dan het laatst uitgegeven
	 * debiteurnummer binnen de instelling.
	 */
	public long newDebiteurnummer();

	/**
	 * Genereert een nieuw debiteurnummer voor externe organisatie (kan in een aparte
	 * range zijn afhankelijk van configuratie instelling).
	 */
	public long newOrganisatieDebiteurnummer();

	public String newTaxonomiecode(String type);

	/**
	 * Genereert een nieuw verzuimmeldingsnummer dat 1 hoger is dan het laatst uitgegeven
	 * Verzuimmeldingsnummer binnen de instelling.
	 */
	public long newVerzuimmeldingsnummer();

	/**
	 * Genereert een nieuw nummer dat 1 hoger is dan het laatst uitgegeven
	 * overeenkomstnummer binnen de instelling. Wordt gebruikt voor
	 * onderwijsovereenkomsten en praktijkovereenkomsten (verbintenissen en
	 * bpv-inschrijvingen).
	 */
	public long newOvereenkomstnummer();

	/**
	 * Genereert een nieuw factuurnummer.
	 */
	public long newFactuurnummer();
}
