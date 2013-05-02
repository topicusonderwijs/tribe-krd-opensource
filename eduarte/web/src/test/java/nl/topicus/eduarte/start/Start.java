package nl.topicus.eduarte.start;

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import nl.topicus.cobra.update.dbupdate.DBVersionLauncher;

import org.mortbay.component.Container;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.plus.naming.EnvEntry;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * @author
 */
public class Start
{
	private static Container container;

	public static void main(String[] args) throws Exception
	{
		System.out.println(" _____     _        ___       _        ");
		System.out.println("|  ___|   | |      / _ \\     | |       ");
		System.out.println("| |__   __| |_   _/ /_\\ \\_ __| |_  ___ ");
		System.out.println("|  __| / _` | | | |  _  | '__| __|/ _ \\");
		System.out.println("| |___| (_| | |_| | | | | |  | |_|  __/");
		System.out.println("\\____/ \\__,_|\\__,_\\_| |_/_|   \\__|\\___|");
		System.out.println();
		System.out.println("EduArte web applicatie");
		System.out.println();

		System.setProperty("java.version", "1.6.0");

		DBVersionLauncher.checkDatabaseConnectionBeforeC3p0KillsAKitten();

		Server server = new Server();
		SocketConnector connector = new SocketConnector();
		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(8080);
		server.setConnectors(new Connector[] {connector});

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		server.setHandler(contexts);

		WebAppContext webapp = new WebAppContext();
		webapp.setServer(server);
		webapp.setContextPath("/");
		webapp.setWar("src/webapp");

		webapp.setConfigurationClasses(new String[] {
			"org.mortbay.jetty.webapp.WebInfConfiguration",
			"org.mortbay.jetty.plus.webapp.EnvConfiguration",
			"org.mortbay.jetty.plus.webapp.Configuration",
			"org.mortbay.jetty.webapp.JettyWebXmlConfiguration"});

		container = server.getContainer();
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "baas2");
		Session session = Session.getDefaultInstance(props);
		configure("mail/EduArte", session);

		// KOL: Eigen ooservlet is niet meer nodig, app maakt standaard gebruik van
		// eduarte-demo.
		// Context root = new Context(contexts, "/ooservlet", Context.SESSIONS);
		// root.addServlet(new ServletHolder(OOServlet.class), "/*");

		server.addHandler(webapp);

		System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
		server.start();
		while (System.in.available() == 0)
		{
			Thread.sleep(5000);
		}
		server.stop();
		server.join();

		// -Dhibernate.connection.username=dashorst
		// -Dhibernate.connection.password=dashorst
		// -Dhibernate.connection.url=jdbc:oracle:thin:@mrfreeze:1522:eduarte
		// -Dhibernate.default_schema=dashorst
	}

	static void configure(String key, Object value) throws NamingException
	{
		EnvEntry entry = null;
		entry = new EnvEntry(key, value);
		container.addBean(entry);
	}

}
