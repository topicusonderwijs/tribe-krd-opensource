package nl.topicus.eduarte.app.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Niveau
{
	AuthorisatieNiveau value();
}
