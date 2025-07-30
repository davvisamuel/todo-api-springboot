package schneider.davi.to_do_app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import schneider.davi.to_do_app.commons.FileUtils;
import schneider.davi.to_do_app.response.TaskPostResponse;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerIT {
    private static final String URL = "/v1/tasks";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FileUtils fileUtils;

    @Test
    @Order(1)
    @DisplayName("POST v1/profiles creates a task")
    void save_ReturnsTaskPostResponse_WhenSuccessful() throws IOException {
        var request = fileUtils.readResourceLoader("task/post-request-task-200.json");

        var httpEntity = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntity, TaskPostResponse.class);

        Assertions.assertThat(responseEntity).isNotNull().hasNoNullFieldsOrProperties();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeaders);
    }

}