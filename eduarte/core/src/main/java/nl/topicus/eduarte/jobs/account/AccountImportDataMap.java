package nl.topicus.eduarte.jobs.account;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.util.importeren.accounts.AccountsImporterenFile;
import nl.topicus.eduarte.app.util.importeren.accounts.IdentificerendeKolom;
import nl.topicus.eduarte.web.components.file.AccountImportBestandField;

public class AccountImportDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	@AutoForm(required = true, order = 1, label = "Identificatie medewerker", description = "De nieuwe accounts moeten gekoppeld kunnen worden aan een bestaande medewerker in het KRD. Meestal zult u hiervoor de roostercode van de medewerker gebruiken. In enkele gevallen zult u het database-nummer van de medewerker gebruiken.")
	public IdentificerendeKolom getIdentificerendeKolom()
	{
		return (IdentificerendeKolom) get("identificerendeKolom");
	}

	public void setIdentificerendeKolom(IdentificerendeKolom identificerendeKolom)
	{
		put("identificerendeKolom", identificerendeKolom);
	}

	@AutoForm(required = true, order = 2)
	public Boolean getWachtwoordGenereren()
	{
		return (Boolean) get("wachtwoordGenereren");
	}

	public void setWachtwoordGenereren(Boolean wachtwoordGenereren)
	{
		put("wachtwoordGenereren", wachtwoordGenereren);
	}

	@AutoForm(required = true, order = 3)
	public Boolean getEmailVersturen()
	{
		return (Boolean) get("emailVersturen");
	}

	public void setEmailVersturen(Boolean emailVersturen)
	{
		put("emailVersturen", emailVersturen);
	}

	@AutoForm(required = true, order = 4, editorClass = AccountImportBestandField.class)
	public AccountsImporterenFile getBestand()
	{
		return (AccountsImporterenFile) get("bestand");
	}

	public void setBestand(AccountsImporterenFile bestand)
	{
		put("bestand", bestand);
	}
}
