package kz.flurent.controller.rest;

import kz.flurent.model.response.UserResponse;
import kz.flurent.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
//@Tag(name = "Admin controller", description = "Контроллер управления пользователями")
public class AdminController {

    private final UserService userService;

    @GetMapping("/all")
//    @Operation(summary = "Получить всех пользователей", description = "Запрос для получения всех пользователей вне зависимости от их роли")
    public ResponseEntity<Page<UserResponse>> findAllUsers(@RequestParam Optional<Integer> page,
                                                           @RequestParam Optional<Integer> size,
                                                           @RequestParam Optional<String[]> sortBy,
                                                           @RequestParam Optional<String> search) {
        Page<UserResponse> all = userService.findAll(page,size,sortBy, search);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
//    @Operation(summary = "Получить пользователя по id")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/ids")
//    @Operation(summary = "Получить пользователей по id")
    public ResponseEntity<List<UserResponse>> findByIds(@RequestParam Set<UUID> ids) {
        return ResponseEntity.ok(userService.findByIds(ids));
    }

    @PostMapping("/block")
//    @Operation(summary = "Заблокировать пользователя по имени пользователя")
    public ResponseEntity<Void> blockUserByUsername(
            @RequestParam(name = "username") String username) {
        userService.blockUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unblock")
//    @Operation(summary = "Разблокировать пользователя по имени пользователя")
    public ResponseEntity<Void> unblockUserByUsername(
            @RequestParam(name = "username") String username) {
        userService.unblockUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user-details")
//    @Operation(summary = "Получить данные об этом юзере")
    public ResponseEntity<UserResponse> getUserDetails(){
        return  ResponseEntity.ok(userService.getUserDetails());
    }


}
