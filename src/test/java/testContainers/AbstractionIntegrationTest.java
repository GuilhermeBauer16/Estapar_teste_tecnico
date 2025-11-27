package testContainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractionIntegrationTest.Initializer.class)
public class AbstractionIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:8.0.28");


        private static void startContainers() {
            Startables.deepStart(Stream.of(mySQL)).join();


        }


        private Map<String, Object> createConnectionConfiguration() {
            Map<String, Object> config = new HashMap<>();


            config.put("spring.datasource.url", mySQL.getJdbcUrl());
            config.put("spring.datasource.username", mySQL.getUsername());
            config.put("spring.datasource.password", mySQL.getPassword());
            config.put("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");


            return config;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            startContainers();


            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testContainers);
        }
    }
}
