package nl.topicus.cobra.enforcement.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Met deze annotatie kan aangegeven worden dat het toegestaan is om
 * criteria-gerelateerde methodes te gebruiken die normaal gesproken niet
 * gebruikt zouden moeten worden.
 * 
 * @author papegaaij
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface CriteriaMethodsAllowed {
}
