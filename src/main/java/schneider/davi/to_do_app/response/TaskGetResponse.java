package schneider.davi.to_do_app.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskGetResponse {

  private Long id;
  private String title;
  private String description;
}
