package uz.pdp.exam8.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.exam8.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllCommentsByTaskId(Integer taskId);

}