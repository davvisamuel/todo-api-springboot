package schneider.davi.to_do_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import schneider.davi.to_do_app.domain.Tarefa;

@Repository
public interface ToDoRepository extends JpaRepository<Tarefa, Long> {
}
