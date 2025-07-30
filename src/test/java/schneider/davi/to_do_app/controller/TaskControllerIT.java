package schneider.davi.to_do_app.controller;

import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import schneider.davi.to_do_app.commons.FileUtils;
import schneider.davi.to_do_app.response.TaskGetResponse;
import schneider.davi.to_do_app.response.TaskPostResponse;

import java.io.IOException;
import java.util.List;

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
    @DisplayName("POST /v1/tasks creates a task")
    void save_ReturnsTaskPostResponse_WhenSuccessful() throws IOException {
        var request = fileUtils.readResourceLoader("task/post-request-task-200.json");

        var httpEntity = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntity, TaskPostResponse.class);

        Assertions.assertThat(responseEntity).isNotNull().hasNoNullFieldsOrProperties();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/tasks returns a list of all tasks")
    @Sql(value = "/sql/init_two_tasks_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_tasks_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAllTasks_WhenSuccessful() throws IOException {
        var expectedResponse = fileUtils.readResourceLoader("task/get-response-task-200.json");

        var typeReference = new ParameterizedTypeReference<List<TaskGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();

        responseEntity
                .getBody()
                .forEach(taskGetResponse -> Assertions.assertThat(taskGetResponse).hasNoNullFieldsOrProperties());

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/tasks returns a empty list")
    void findAll_ReturnsEmptyList_WhenSuccessful() throws IOException {
        var typeReference = new ParameterizedTypeReference<List<TaskGetResponse>>() {};

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();

    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeaders);
    }

}