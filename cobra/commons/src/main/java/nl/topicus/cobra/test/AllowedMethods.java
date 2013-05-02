package nl.topicus.cobra.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Met deze annotatie kan aangegeven worden dat het vanuit de class toegestaan is om
 * bepaalde methodes te gebruiken die normaal gesproken niet gebruikt zouden moeten
 * worden. Dit wordt gebruikt vanuit de AbstractDiscouragedMethod
 * 
 * @author papegaaij
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface AllowedMethods
{
	String[] value();
}
