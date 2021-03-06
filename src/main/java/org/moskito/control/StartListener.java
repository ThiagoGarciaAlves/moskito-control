package org.moskito.control;

import net.anotheria.moskito.webui.util.VersionUtil;
import net.anotheria.util.maven.MavenVersion;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.history.StatusUpdateHistoryRepository;
import org.moskito.control.core.updater.ApplicationStatusUpdater;
import org.moskito.control.core.updater.ChartDataUpdater;
import org.moskito.control.data.DataRepository;
import org.moskito.control.plugins.PluginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContextListener that ensures that MoSKito-Control components are started in proper order.
 *
 * @author lrosenberg
 * @since 28.05.13 21:15
 */
public class StartListener implements ServletContextListener{

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(StartListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("Starting up MoSKito Control...");

		//first force configuration to load
		MoskitoControlConfiguration.getConfiguration();

		ApplicationRepository.getInstance();
		log.info("ApplicationRepository loaded.");

		//initialize history
		StatusUpdateHistoryRepository.getInstance();
		log.info("StatusUpdateHistoryRepository loaded.");

		ApplicationStatusUpdater.getInstance();
		log.info("Application StatusResource Updater loaded.");

		ChartDataUpdater.getInstance();
		log.info("ChartData Updater loaded.");

		log.info("Initializing PluginRepository ...");
		PluginRepository.getInstance();
		log.info("PluginRepository initialized.");

		log.info("Initializing DataRepository");
		DataRepository.getInstance();
		log.info("DataRepository initialized");


		String versionString = "unknown";
		try{
			MavenVersion appVersion = VersionUtil.getWebappVersion(servletContextEvent.getServletContext());
			versionString = appVersion.getVersion();
		}catch(Exception e){
			log.warn("couldn't read version.");
		}
		servletContextEvent.getServletContext().setAttribute("moskito.control.version", versionString);
		log.info("MoSKito Control "+versionString+" started.");

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ApplicationStatusUpdater.getInstance().terminate();
		ChartDataUpdater.getInstance().terminate();
	}
}
