package schneider.davi.to_do_app.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskPutResponse {

    private Long id;
    private String title;
    private String description;
}
