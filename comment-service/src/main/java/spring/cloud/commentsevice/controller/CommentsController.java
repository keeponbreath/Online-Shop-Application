package spring.cloud.commentsevice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.cloud.commentsevice.dto.CommentRequest;
import spring.cloud.commentsevice.entity.Comment;
import spring.cloud.commentsevice.entity.Response;
import spring.cloud.commentsevice.service.CommentsService;
import spring.cloud.commentsevice.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final CommentsService service;

    @Autowired
    public CommentsController(CommentsService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Response> leaveComment(@RequestBody @Valid CommentRequest request,
                                                 BindingResult result) throws IllegalAccessException {
        ValidationUtil.checkResult(result);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("added", service.saveComment(request)))
                        .message("Comment left")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<Response> deleteComment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", service.deleteComment(id)))
                        .message("Comment deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/item")
    public ResponseEntity<Response> getCommentsByItemId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", service.getCommentsByItemId(id)))
                        .message("Comments loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/author")
    public ResponseEntity<Response> getCommentsByAuthorId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("comments", service.getCommentsByAuthorId(id)))
                        .message("Comments loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}