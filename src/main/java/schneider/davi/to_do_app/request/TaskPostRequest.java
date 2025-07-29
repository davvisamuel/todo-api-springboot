package schneider.davi.to_do_app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPostRequest {
    @NotBlank(message = "The field 'title' is required")
    private String title;
    @NotBlank(message = "The field 'description' is required")
    private String description;
}
