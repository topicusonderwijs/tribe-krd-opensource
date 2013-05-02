package nl.topicus.cobra.enforcement.method;

public aspect DiscouragedMethods
{
	public pointcut isDeprecated(): @withincode(Deprecated) || @within(Deprecated);

	declare error: call(* Throwable+.printStackTrace())
		&& !@withincode(PrintStackTraceAllowed) && !@within(PrintStackTraceAllowed)
		&& !isDeprecated():
		"Niet printStackTrace() gebruiken, maar een logger";

	declare error: call(* org.hibernate.Criteria+.list())
		&& !@withincode(CriteriaMethodsAllowed) && !@within(CriteriaMethodsAllowed)
		&& !isDeprecated():
		"Gebruik 1 van de list methodes van de DAH (bijv cachedList)";

	declare error: call(* org.hibernate.Criteria+.unique())
		&& !@withincode(CriteriaMethodsAllowed) && !@within(CriteriaMethodsAllowed)
		&& !isDeprecated():
		"Gebruik 1 van de unique methodes van de DAH (bijv cachedUnique)";

	declare error: call(* org.hibernate.Session.create*(..))
		&& !call(* org.hibernate.Session+.createSQLQuery(..))
		&& !@withincode(CriteriaMethodsAllowed) && !@within(CriteriaMethodsAllowed)
		&& !isDeprecated():
		"Gebruik create... op de DAH, niet op Session";

	declare error: call(* org.hibernate.Session+.forClass(..))
		&& !@withincode(CriteriaMethodsAllowed) && !@within(CriteriaMethodsAllowed)
		&& !isDeprecated():
		"Gebruik createCriteria op de DAH, niet forClass op de Session";

	declare error: call(* org.hibernate.Session+.forEntityName(..))
		&& !@withincode(CriteriaMethodsAllowed) && !@within(CriteriaMethodsAllowed)
		&& !isDeprecated():
		"Gebruik createCriteria op de DAH, niet forEntityName op de Session";

	declare error: call(org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.new(..))
		&& !isDeprecated():
		"Gebruik CobraModalWindow ipv ModalWindow";

	declare error: (call(* org.apache.log4j.Logger.getLogger(..))
		|| call(* org.apache.commons.logging.LogFactory.getLog(..)))
		&& !isDeprecated():
		"Gebruik SLF4J";
}
