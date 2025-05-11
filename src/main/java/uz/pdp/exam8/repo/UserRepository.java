package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.exam8.dto.UserDto;

import uz.pdp.exam8.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = """
            select u.attachment_id as attachmentId,
                     (u.last_name || ' ' || u.first_name) as fullName,
                     count(t.id) as count
              from users u
                       join tasks_users tu on u.id = tu.users_id
                       join tasks t on tu.tasks_id = t.id and t.deadline < now()
                       join attachment a on t.attachment_id = a.id
                       join attachment_content ac on a.id = ac.attachment_id
              group by u.id, (u.last_name || ' ' || u.first_name)                                                                                          
            """, nativeQuery = true)
    List<UserDto> findAllUsersExpiredDeadline();

}