package nl.topicus.cobra.templates.annotations;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target( {TYPE, METHOD})
@Retention(RUNTIME)
@Inherited
public @interface Exportable
{
	String omschrijving() default "";
}
