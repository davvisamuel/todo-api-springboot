package schneider.davi.to_do_app.controller;

import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import schneider.davi.to_do_app.commons.FileUtils;
import schneider.davi.to_do_app.config.TestContainersConfiguration;
import schneider.davi.to_do_app.repository.TaskRepository;
import schneider.davi.to_do_app.response.TaskGetResponse;
import schneider.davi.to_do_app.response.TaskPostResponse;
import schneider.davi.to_do_app.response.TaskPutResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestContainersConfiguration.class)
@ActiveProfiles("itest")
class TaskControllerIT {
    private static final String URL = "/v1/tasks";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private TaskRepository repository;

    @Test
    @Order(1)
    @DisplayName("POST /v1/tasks creates a task")
    @Sql(value = "/sql/clean_tasks_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        var typeReference = new ParameterizedTypeReference<List<TaskGetResponse>>() {
        };

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();

        responseEntity
                .getBody()
                .forEach(taskGetResponse -> Assertions.assertThat(taskGetResponse).hasNoNullFieldsOrProperties());

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .whenIgnoringPaths("[*].id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/tasks returns a empty list")
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        var typeReference = new ParameterizedTypeReference<List<TaskGetResponse>>() {
        };

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().isEmpty();

    }

    @Test
    @Order(4)
    @DisplayName("DELETE /v1/tasks/{1} removes task")
    @Sql(value = "/sql/init_two_tasks_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_tasks_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_RemovesTask_WhenTaskIsFound() {

        var taskToDelete = repository.findAll().getFirst();

        var taskToDeleteId = taskToDelete.getId();

        var response = testRestTemplate.exchange(
                URL + "/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                taskToDeleteId
        );

        var savedTasks = repository.findAll();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(savedTasks).doesNotContain(taskToDelete);
    }

    @Test
    @Order(5)
    @DisplayName("DELETE /v1/tasks/{1} Throws NotFoundException")
    void delete_ThrowsNotFoundException_WhenTaskIsNotFound() throws IOException {
        var expectedResponse = fileUtils.readResourceLoader("/task/delete-response-task-404.json");

        var taskToDeleteId = 99L;

        var response = testRestTemplate.exchange(
                URL + "/{id}",
                HttpMethod.DELETE,
                null,
                String.class,
                taskToDeleteId
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    @Order(6)
    @DisplayName("PUT /v1/tasks Returns an updated task")
    @Sql(value = "/sql/init_two_tasks_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/clean_tasks_db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ReturnsUpdatedTask_WhenTaskIsFound() throws IOException {
        var savedTask = repository.findAll().getFirst();

        var request = fileUtils
                .readResourceLoader("/task/put-request-task-200.json")
                .replace("1", savedTask.getId().toString());

        var expectedResponse = fileUtils
                .readResourceLoader("/task/put-response-task-200.json")
                .replace("1", savedTask.getId().toString());

        var httpEntity = buildHttpEntity(request);

        var response = testRestTemplate.exchange(
                URL,
                HttpMethod.PUT,
                httpEntity,
                TaskPutResponse.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonAssertions
                .assertThatJson(response.getBody())
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Order(7)
    @DisplayName("PUT /v1/tasks Throws NotFoundException when task is not found")
    void update_ThrowsNotFoundException_WhenTaskIsNotFound() throws IOException {
        var request = fileUtils.readResourceLoader("/task/put-request-task-404.json");

        var expectedResponse = fileUtils.readResourceLoader("/task/put-response-task-404.json");

        var httpEntity = buildHttpEntity(request);

        var response = testRestTemplate.exchange(
                URL,
                HttpMethod.PUT,
                httpEntity,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        JsonAssertions.assertThatJson(response.getBody()).isNotNull().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("providePutArguments")
    @Order(8)
    @DisplayName("PUT /v1/tasks returns bad request when fields are invalid")
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws IOException {
        var request = fileUtils.readResourceLoader("%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceLoader("%s".formatted(responseFile));

        var httpEntity = buildHttpEntity(request);

        var response = testRestTemplate.exchange(
                URL,
                HttpMethod.PUT,
                httpEntity,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(response.getBody()).isNotNull();

        JsonAssertions.assertThatJson(response.getBody())
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);

    }

    @ParameterizedTest
    @MethodSource("providePostArguments")
    @Order(9)
    @DisplayName("POST /v1/tasks returns bad request when fields are invalid")
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String requestFile, String responseFile) throws IOException {
        var request = fileUtils.readResourceLoader("%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceLoader("%s".formatted(responseFile));

        var httpEntity = buildHttpEntity(request);

        var response = testRestTemplate.exchange(
                URL,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(response.getBody()).isNotNull();

        JsonAssertions.assertThatJson(response.getBody())
                .whenIgnoringPaths("timestamp")
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> providePostArguments() {
        return Stream.of(
                Arguments.of("/task/post-request-task-blank-fields-400.json", "/task/post-response-task-blank-fields-400.json"),
                Arguments.of("/task/post-request-task-empty-fields-400.json", "/task/post-response-task-empty-fields-400.json"),
                Arguments.of("/task/post-request-task-null-fields-400.json", "/task/post-response-task-null-fields-400.json")
        );
    }

    private static Stream<Arguments> providePutArguments() {
        return Stream.of(
                Arguments.of("/task/put-request-task-blank-fields-400.json", "/task/put-response-task-blank-fields-400.json"),
                Arguments.of("/task/put-request-task-empty-fields-400.json", "/task/put-response-task-empty-fields-400.json"),
                Arguments.of("/task/put-request-task-null-fields-400.json", "/task/put-response-task-null-fields-400.json")
        );
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeaders);
    }

}