package schneider.davi.to_do_app.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import schneider.davi.to_do_app.domain.Task;
import schneider.davi.to_do_app.request.TaskPostRequest;
import schneider.davi.to_do_app.response.TaskGetResponse;
import schneider.davi.to_do_app.response.TaskPostResponse;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TaskMapper {

  Task toTask(TaskPostRequest taskPostRequest);

  TaskPostResponse toTaskPostResponse(Task task);

  TaskGetResponse toTaskGetResponse(Task task);

  List<TaskGetResponse> toTaskGetResponseList(List<Task> tasks);
}
