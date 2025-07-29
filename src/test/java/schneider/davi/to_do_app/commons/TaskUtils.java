package schneider.davi.to_do_app.commons;

import org.springframework.stereotype.Component;
import schneider.davi.to_do_app.domain.Task;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskUtils {

    public List<Task> newTaskList() {
        var market = Task.builder()
                .id(1L)
                .title("Go to the market")
                .description("Milk, eggs")
                .build();

        var study = Task.builder()
                .id(1L)
                .title("To study")
                .description("History, Science")
                .build();

        return new ArrayList<>(List.of(market, study));
    }

    public Task newTaskToSave() {
        return Task.builder()
                .title("Ir ao mercado")
                .description("Milk, eggs")
                .build();
    }

    public Task newSavedTask() {
        return Task.builder()
                .id(1L)
                .title("Ir ao mercado")
                .description("Milk, eggs")
                .build();
    }
}
