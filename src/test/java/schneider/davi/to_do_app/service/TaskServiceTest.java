package schneider.davi.to_do_app.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schneider.davi.to_do_app.commons.TaskUtils;
import schneider.davi.to_do_app.exception.NotFoundException;
import schneider.davi.to_do_app.repository.TaskRepository;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskServiceTest {
    @InjectMocks
    private TaskService service;
    @InjectMocks
    private TaskUtils taskUtils;
    @Mock
    private TaskRepository repository;

    @Test
    @Order(1)
    @DisplayName("save creates a task when successful")
    void save_ReturnsTask_WhenSuccessful() {
        var taskToSave = taskUtils.newTaskToSave();

        var expectedSavedTask = taskUtils.newSavedTask();

        BDDMockito.when(repository.save(taskToSave)).thenReturn(expectedSavedTask);

        var savedTask = service.save(taskToSave);

        Assertions.assertThat(savedTask)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(expectedSavedTask);
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns a list of all tasks")
    void findAll_ReturnsAllTasks_WhenSuccessful() {
        var expectedTaskList = taskUtils.newTaskList();

        BDDMockito.when(repository.findAll()).thenReturn(expectedTaskList);

        var taskList = service.findAll();

        Assertions.assertThat(taskList)
                .isNotNull()
                .hasSize(expectedTaskList.size())
                .hasSameElementsAs(expectedTaskList);
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns an empty list")
    void findAll_ReturnsEmptyList_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(Collections.emptyList());

        var taskList = service.findAll();

        Assertions.assertThat(taskList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById returns a task when task is found")
    void findById_ReturnsTaskById_WhenTaskIsFound() {
        var expectedTask = taskUtils.newTaskList().getFirst();

        var id = expectedTask.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(expectedTask));

        var task = service.findById(id);

        Assertions.assertThat(task)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isEqualTo(expectedTask);
    }

    @Test
    @Order(5)
    @DisplayName("findById returns a NotFoundException when task is not found")
    void findById_ReturnsNotFoundException_WhenTaskIsNotFound() {
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class);
    }
}