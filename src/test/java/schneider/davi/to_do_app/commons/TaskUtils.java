package schneider.davi.to_do_app.commons;

import org.springframework.stereotype.Component;
import schneider.davi.to_do_app.domain.Task;

@Component
public class TaskUtils {

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
