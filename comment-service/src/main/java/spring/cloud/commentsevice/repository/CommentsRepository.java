package spring.cloud.commentsevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.commentsevice.entity.Comment;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByItemId(Long id);
    List<Comment> getCommentsByAuthorId(Long id);
}
