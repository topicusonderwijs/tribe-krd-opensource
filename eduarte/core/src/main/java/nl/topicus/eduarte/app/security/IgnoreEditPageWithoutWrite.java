package nl.topicus.eduarte.app.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Met deze annotatie kun je aangeven dat een pagina een IEditPage is, maar in een
 * principal moet vallen waar geen @Write aan zit. Normaal gesproken zou dit zorgen voor
 * een testcase failure.
 * 
 * @author papegaaij
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IgnoreEditPageWithoutWrite
{

}
