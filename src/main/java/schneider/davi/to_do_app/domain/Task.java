package schneider.davi.to_do_app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @NotBlank(message = "The field 'id' is required")
    private Long id;
    @NotBlank(message = "The field 'title' is required")
    private String title;
    @NotBlank(message = "The field 'description' is required")
    private String description;
}
