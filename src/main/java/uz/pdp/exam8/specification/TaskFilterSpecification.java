package uz.pdp.exam8.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import uz.pdp.exam8.entity.Task;
import uz.pdp.exam8.entity.User;
import uz.pdp.exam8.payload.TaskFilter;

import java.time.LocalDate;

public class TaskFilterSpecification {

    public static Specification<Task> taskFilterSpecification(TaskFilter taskFilter) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (taskFilter.getTitle() != null) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), '%' + taskFilter.getTitle().toLowerCase() + '%'));
            }

            if (taskFilter.getUserId() != null) {
                Join<Task, User> userJoin = root.join("users");
                predicate = cb.and(predicate, cb.equal(userJoin.get("id"), taskFilter.getUserId()));
            }

            if (taskFilter.getDeadline() != null) {
                switch (taskFilter.getDeadline()) {
                    case "expired":
                        predicate = cb.and(predicate, cb.lessThan(root.get("deadline"), LocalDate.now()));
                        break;
                    case "danger":
                        predicate = cb.and(predicate, cb.equal(root.get("deadline"), LocalDate.now()));
                        break;
                    case "recent":
                        predicate = cb.and(predicate, cb.between(root.get("deadline"), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
                        break;
                    case "nodeadline":
                        predicate = cb.and(predicate, cb.isNull(root.get("deadline")));
                        break;
                    default:
                        new IllegalArgumentException("Invalid deadline: " + taskFilter.getDeadline());
                }
            }
            return predicate;

        };

    }
}
