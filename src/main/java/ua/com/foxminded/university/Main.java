package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/university");
        SpringApplication.run(Main.class, args);
    }

//    @Bean
//    public TomcatServletWebServerFactory tomcatFactory() {
//        return new TomcatServletWebServerFactory() {
//            @Override
//            protected TomcatWebServer getTomcatWebServer(org.apache.catalina.startup.Tomcat tomcat) {
//                tomcat.enableNaming();
//                return super.getTomcatWebServer(tomcat);
//            }
//
////            @Override
////            protected void postProcessContext(Context context) {
////                ContextResource resource = new ContextResource();
////                //resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
////                resource.setName("jdbc/myDatasourceName");
////                resource.setType(DataSource.class.getName());
////                resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
////                resource.setProperty("url", "db_url");
////                resource.setProperty("username", "db_username");
////                resource.setProperty("password", "db_password");
////                context.getNamingResources().addResource(resource);
////            }
//        };
//    }
}
