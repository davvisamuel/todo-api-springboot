package schneider.davi.to_do_app.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schneider.davi.to_do_app.domain.Task;
import schneider.davi.to_do_app.repository.TaskRepository;

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

}
