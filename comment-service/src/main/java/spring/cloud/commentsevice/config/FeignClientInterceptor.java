package spring.cloud.commentsevice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    final HttpServletRequest request;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String authReqInput = request.getHeader("authorization");
        if (authReqInput != null) {
            requestTemplate.header("Authorization", authReqInput);
        }
    }
}