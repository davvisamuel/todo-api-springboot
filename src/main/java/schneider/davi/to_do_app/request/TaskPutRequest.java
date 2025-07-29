package schneider.davi.to_do_app.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPutRequest {
    private Long id;
    private String title;
    private String description;
}
