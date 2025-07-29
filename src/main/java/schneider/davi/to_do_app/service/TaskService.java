package schneider.davi.to_do_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schneider.davi.to_do_app.domain.Task;
import schneider.davi.to_do_app.exception.NotFoundException;
import schneider.davi.to_do_app.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Task save(Task task) {
        return repository.save(task);
    }

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public void delete(Long id) {
        var taskToDelete = findById(id);
        repository.delete(taskToDelete);
    }

}
