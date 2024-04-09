package webserver.servletbasic;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

import java.io.File;

@Slf4j
public class WebServerLauncher {
    public static void main(String[] args) throws LifecycleException {
        String webappLocation = "webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.addWebapp("/", new File(webappLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
