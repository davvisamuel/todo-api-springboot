package schneider.davi.to_do_app.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPutRequest {
    @NotNull(message = "The field 'id' is required")
    private Long id;
    @NotBlank(message = "The field 'title' is required")
    private String title;
    @NotBlank(message = "The field 'description' is required")
    private String description;
}
