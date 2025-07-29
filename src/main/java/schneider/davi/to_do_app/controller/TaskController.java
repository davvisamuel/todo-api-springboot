package schneider.davi.to_do_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schneider.davi.to_do_app.mapper.TaskMapper;
import schneider.davi.to_do_app.request.TaskPostRequest;
import schneider.davi.to_do_app.request.TaskPutRequest;
import schneider.davi.to_do_app.response.TaskGetResponse;
import schneider.davi.to_do_app.response.TaskPostResponse;
import schneider.davi.to_do_app.response.TaskPutResponse;
import schneider.davi.to_do_app.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskMapper mapper;

    @PostMapping
    public ResponseEntity<TaskPostResponse> save(@RequestBody TaskPostRequest taskPostRequest) {
        var task = mapper.toTask(taskPostRequest);

        var savedTask = service.save(task);

        var taskPostResponse = mapper.toTaskPostResponse(savedTask);

        return ResponseEntity.ok(taskPostResponse);
    }

    @GetMapping
    public ResponseEntity<List<TaskGetResponse>> findAll() {
        var savedTasks = service.findAll();

        var taskGetResponseList = mapper.toTaskGetResponseList(savedTasks);

        return ResponseEntity.ok(taskGetResponseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<TaskPutResponse> update(@RequestBody TaskPutRequest taskPutRequest) {
        var task = mapper.toTask(taskPutRequest);

        var updatedTask = service.update(task);

        var taskPutResponse = mapper.toTaskPutResponse(updatedTask);

        return ResponseEntity.ok(taskPutResponse);
    }
}
