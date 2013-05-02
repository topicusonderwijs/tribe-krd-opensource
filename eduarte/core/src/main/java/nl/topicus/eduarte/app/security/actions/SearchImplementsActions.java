package nl.topicus.eduarte.app.security.actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.security.actions.WaspAction;

/**
 * Via deze annotatie kan aangegeven worden dat het een zoekenpagina betreft, welke de
 * zoekopdracht beperkt in de opgegeven actions. Zo zal bijv. de DeelnemerZoekenPage enkel
 * deelnemers van de organisatie-eenheid-locatie tonen waar de medewerker aan gekoppeld
 * is, als de medewerker op dat niveau geautoriseerd is.
 * 
 * De annotatie kan ook gebruikt worden als de pagina 'achter' een pagina zit die de
 * actions implementeert. Dit is bijvoorbeeld het geval in de intake wizard.
 * 
 * Een derde mogelijkheid is dat de pagina onderdeel is van een recht dat op meerdere
 * niveaus gegeven kan worden, maar dat een aantal van deze niveaus niet van toepassing
 * zijn voor de pagina, maar wel voor andere pagina's in het recht.
 * 
 * @author papegaaij
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SearchImplementsActions
{
	Class< ? extends WaspAction>[] value();
}
