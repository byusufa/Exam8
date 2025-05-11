package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.exam8.dto.DeveloperRes;
import uz.pdp.exam8.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {


    @Query(value = """
            select u.attachment_id,
                   u.first_name || ' ' || u.last_name                                            as fullName,
                   count(t.id)                                                                   as total_tasks,
                   sum(case when t.deadline < current_date and t.card_id <> 5 then 1 else 0 end) as expired_tasks,
                   sum(case
                           when t.deadline between current_date and current_date + interval '2 day'
                               and t.card_id <> 5 then 1
                           else 0 end)                                                           as approaching_tasks,
                   sum(case
                           when t.deadline > current_date + interval '2 day'
                               and t.card_id <> 5 then 1
                           else 0 end)                                                           as long_deadline_tasks,
                   sum(case
                           when t.deadline < current_date and t.card_id = 5 then 1
                           else 0 end)                                                           as completed_tasks
            from tasks t
                     join
                 tasks_users tu on t.id = tu.tasks_id
                     join
                 users u on tu.users_id = u.id
            group by u.attachment_id, u.first_name || ' ' || u.last_name
            order by fullName
            """, nativeQuery = true)
    List<DeveloperRes> findAllDeveloperAndTaskCount();

}