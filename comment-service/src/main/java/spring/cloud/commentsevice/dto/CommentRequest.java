package spring.cloud.commentsevice.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommentRequest {
    private Long itemId;
    @Min(1)
    @Max(5)
    private Short rating;
    @Column(length = 200)
    @NotEmpty(message = "Comment can not be empty or more than 200 symbols")
    private String content;
}
