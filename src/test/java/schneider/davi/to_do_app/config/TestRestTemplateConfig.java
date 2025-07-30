package schneider.davi.to_do_app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.DefaultUriBuilderFactory;

@TestConfiguration
public class TestRestTemplateConfig {
    @LocalServerPort
    private int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        var uri = new DefaultUriBuilderFactory("http://localhost:" + port);
        var testRestTemplate = new TestRestTemplate();
        testRestTemplate.setUriTemplateHandler(uri);
        return testRestTemplate;
    }
}
