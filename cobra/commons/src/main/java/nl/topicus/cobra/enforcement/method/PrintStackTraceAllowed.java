package nl.topicus.cobra.enforcement.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Met deze annotatie kan aangegeven worden dat het toegestaan is om
 * printStackTrace te gebruiken.
 * 
 * @author papegaaij
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface PrintStackTraceAllowed {
}
