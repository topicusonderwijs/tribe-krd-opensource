package nl.topicus.eduarte.entities;

import java.util.Date;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

/**
 * Generieke facade voor een debiteur. Zal in de praktijk een persoon of een externe
 * organisatie zijn.
 * 
 * @author hop
 */
@Exportable
public interface Debiteur extends IdObject
{
	/**
	 * @return Het unieke debiteurnummer van deze debiteur
	 */
	@Exportable
	public Long getDebiteurennummer();

	/**
	 * @param debiteurennummer
	 *            Het unieke debiteurnummer van deze debiteur
	 */
	public void setDebiteurennummer(Long debiteurennummer);

	/**
	 * @return Datum van de laatste export. Indien deze kleiner is dan de mutatiedatum,
	 *         dan moet deze debiteur de volgende keer geexporteerd worden.
	 */
	public Date getLaatsteExportDatum();

	/**
	 * @param laatsteExportDatum
	 *            Datum van de laatste export
	 */
	public void setLaatsteExportDatum(Date laatsteExportDatum);

	public Date getLastModifiedAt();

	@Exportable
	public String getBankrekeningnummer();

	@Exportable
	public String getBankrekeningGiro();

	@Exportable
	public String getBankrekeningBank();

	@Exportable
	public String getBankrekeningTenaamstelling();

	public void setBankrekeningnummer(String bankrekeningnummer);

	@Exportable
	public AdresEntiteit< ? > getPostAdres();

	@Exportable
	public AdresEntiteit< ? > getFysiekAdres();

	@Exportable
	public AdresEntiteit< ? > getFactuurAdres();

	@Exportable
	public IContactgegevenEntiteit getEersteTelefoon();

	@Exportable
	public IContactgegevenEntiteit getEersteEmailAdres();

	@Exportable
	public String getFormeleNaam();

	public AutomatischeIncasso getAutomatischeIncasso();

	public Date getAutomatischeIncassoEinddatum();

	@Exportable(omschrijving = "Bij personen: de volledige naam van de debiteur; bij organisaties: 'de' contactpersoon of \"Crediteurenadministratie\"")
	public String getContactpersoon();

	@Exportable(omschrijving = "I = incasso; A = overig")
	public char getBetaalwijze();

	public boolean isVerzamelfacturen();

	@Exportable
	public Integer getBetalingstermijn();

	public void setBetalingstermijn(Integer betalingstermijn);

	@Exportable
	public ExterneOrganisatieContactPersoon getContactpersoonMetRol(String rol);

	public Betaalwijze getFactuurBetaalwijze();

}
