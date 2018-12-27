package com.device.api;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.device.api.vo.DeviceVO;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {
	public static void main(String[] args) {

		Server server = new Server(8080);

		ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		ctx.setAttribute("db", new Hashtable<String,DeviceVO>());

		ctx.setContextPath("/DeviceApi");
		server.setHandler(ctx);

		ServletHolder serHol = ctx.addServlet(ServletContainer.class, "/api/*");
		serHol.setInitOrder(1);
		serHol.setInitParameter("com.sun.jersey.config.property.packages", "com.device.api");
		serHol.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		try {
			server.start();
			server.join();
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} finally {

			server.destroy();
		}
	}
}
