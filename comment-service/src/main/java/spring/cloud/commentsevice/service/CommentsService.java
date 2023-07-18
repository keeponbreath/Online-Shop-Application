package spring.cloud.commentsevice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.commentsevice.dto.CommentRequest;
import spring.cloud.commentsevice.feign.AccountClient;
import spring.cloud.commentsevice.feign.PurchaseClient;
import spring.cloud.commentsevice.entity.Comment;
import spring.cloud.commentsevice.repository.CommentsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentsService {
    private final CommentsRepository repository;
    private final AccountClient accountClient;
    private final PurchaseClient purchaseClient;

    @Autowired
    public CommentsService(CommentsRepository repository, AccountClient client, PurchaseClient purchaseClient) {
        this.repository = repository;
        this.accountClient = client;
        this.purchaseClient = purchaseClient;
    }

    public List<Comment> getCommentsByItemId(Long id) {
        List<Comment> comments = repository.getCommentsByItemId(id);
        if(!comments.isEmpty()) {
            return comments;
        } else {
            throw new NoSuchElementException("Comments with Item ID " + id + " not found");
        }
    }

    public List<Comment> getCommentsByAuthorId(Long id) {
        List<Comment> comments = repository.getCommentsByAuthorId(id);
        if(!comments.isEmpty()) {
            return comments;
        } else {
            throw new NoSuchElementException("Comments with Author ID " + id + " not found");
        }
    }

    public Boolean saveComment(CommentRequest commentRequest) throws IllegalAccessException {
        //todo add check for already existing comment and rating
        Long authorId = null;
        ResponseEntity<Long> accountRequest = accountClient.check();
        if(accountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
            authorId = accountRequest.getBody();
        } else {
            throw new IllegalAccessException("You can not adding comments or remote resource is inaccessible");
        }
        assert authorId != null;
        ResponseEntity<HttpStatus> purchaseRequest = purchaseClient
                .purchaseCheck(commentRequest.getItemId(), authorId);
        if(purchaseRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
            String authorName = accountClient.showFullName(authorId);
            Comment comment = Comment.builder()
                    .authorId(authorId)
                    .itemId(commentRequest.getItemId())
                    .authorName(authorName)
                    .rating(commentRequest.getRating())
                    .content(commentRequest.getContent())
                    .writtenAt(LocalDateTime.now())
                    .build();
            repository.save(comment);
            return Boolean.TRUE;
        } else {
            throw new IllegalAccessException("You did not buy the item");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean deleteComment(Long id) {
        repository.deleteById(id);
        return Boolean.TRUE;
    }
}