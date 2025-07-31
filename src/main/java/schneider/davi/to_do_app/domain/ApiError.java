package schneider.davi.to_do_app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ApiError {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}