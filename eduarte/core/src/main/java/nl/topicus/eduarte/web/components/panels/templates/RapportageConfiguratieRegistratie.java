package nl.topicus.eduarte.web.components.panels.templates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RapportageConfiguratieRegistratie
{
	String naam();

	Class< ? extends RapportageConfiguratieFactory< ? >> factoryType();

	Class< ? > configuratieType();
}
