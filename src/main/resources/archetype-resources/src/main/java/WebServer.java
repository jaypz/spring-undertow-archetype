import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ListenerInfo;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;

public class WebServer {

    /**
     * Initialize, configure and start a server implementation.
     *
     * @param contextPath
     * @param deploymentName
     * @param servletName
     * @param contextConfigLocation
     * @param mapping
     * @param host
     * @param port
     */
    public WebServer(final String contextPath, final String deploymentName, final String servletName, final String globalContextLocation, final String servletContextLocation, final String mapping, final String host, final Integer port) {


        try {

            final DeploymentInfo servletBuilder = deployment()
                    .setClassLoader(WebServer.class.getClassLoader())
                    .setContextPath(contextPath)
                    .setDeploymentName(deploymentName)
                    .setMajorVersion(3)
                    .setMinorVersion(1)
                    .addInitParameter("contextConfigLocation", globalContextLocation)
                    .addListener(new ListenerInfo(ContextLoaderListener.class))
                    .addFilter(new FilterInfo("springSecurityFilterChain", DelegatingFilterProxy.class))
                    .addFilterUrlMapping("springSecurityFilterChain", "/*", DispatcherType.REQUEST)
                    .addFilterUrlMapping("springSecurityFilterChain", "/*", DispatcherType.ASYNC)
                    .addServlet(
                            Servlets.servlet(servletName, DispatcherServlet.class)
                                    .addInitParam("contextConfigLocation", servletContextLocation)
                                    .addMapping(mapping)
                                    .setLoadOnStartup(1)
                                    .setAsyncSupported(true));

            final DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
            manager.deploy();

            final Undertow server = Undertow.builder()
                    .addHttpListener(port, host)
                    .setHandler(manager.start())
                    .build();

            server.start();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}

