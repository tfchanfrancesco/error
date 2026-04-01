package hk.edu.polyu.cpce.error.controller;

import hk.edu.polyu.cpce.error.dto.CreateUserRequest;
import hk.edu.polyu.cpce.error.dto.ValidationDemoRequest;
import hk.edu.polyu.cpce.error.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 异常演示说明（可自行用 curl / Postman 验证）：
 * <ul>
 *   <li>自定义 {@link hk.edu.polyu.cpce.error.exception.ResourceNotFoundException}：{@code GET /users/2}、{@code GET /users/students/2}</li>
 *   <li>自定义 {@link hk.edu.polyu.cpce.error.exception.BusinessViolationException}：{@code GET /users/demo/business-violation}</li>
 *   <li>{@code MethodArgumentNotValidException}：{@code POST /users} 或 {@code POST /users/demo/validation} 传入不合规 JSON</li>
 *   <li>{@code HttpMessageNotReadableException}：{@code POST /users/demo/validation}，{@code Content-Type: application/json}，body 為損壞的 JSON（例如半截括號）</li>
 *   <li>{@code MethodArgumentTypeMismatchException}：{@code GET /users/not-a-long}（路径变量无法转为 {@code Long}）</li>
 *   <li>{@code MissingServletRequestParameterException}：{@code GET /users/demo/required-query}（缺少必填 query {@code q}）</li>
 *   <li>兜底 {@code @ExceptionHandler(Exception.class)}：{@code GET /users/demo/unhandled-runtime}、{@code /users/demo/unhandled-illegal-state}、{@code /users/demo/unhandled-npe}</li>
 * </ul>
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/students/{studentId}")
    public Map<String, Object> getStudent(@PathVariable Long studentId) {
        return userService.getStudentRecordById(studentId);
    }

    @GetMapping("/demo/business-violation")
    public void demoBusinessViolation() {
        userService.triggerBusinessViolation();
    }

    @GetMapping("/demo/unhandled-runtime")
    public void demoUnhandledRuntime() {
        userService.triggerPlainRuntimeException();
    }

    @GetMapping("/demo/unhandled-illegal-state")
    public void demoUnhandledIllegalState() {
        userService.triggerIllegalStateException();
    }

    @GetMapping("/demo/unhandled-npe")
    public void demoUnhandledNpe() {
        userService.triggerNullPointerException();
    }

    /**
     * 缺少 {@code q} 时由 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler} 处理。
     */
    @GetMapping("/demo/required-query")
    public Map<String, Object> demoRequiredQuery(@RequestParam("q") String q) {
        return Map.of("q", q);
    }

    @PostMapping
    public Map<String, Object> createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    /**
     * 使用独立 DTO，便于专门测校验失败与非法 JSON 体。
     */
    @PostMapping("/demo/validation")
    public Map<String, Object> demoValidation(@Valid @RequestBody ValidationDemoRequest request) {
        return Map.of("title", request.getTitle(), "contactEmail", request.getContactEmail());
    }
}
