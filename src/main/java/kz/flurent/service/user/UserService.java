package kz.flurent.service.user;

import kz.flurent.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    Page<UserResponse> findAll(Optional<Integer> page,
                               Optional<Integer> size,
                               Optional<String[]> sortBy,
                               Optional<String> search);

    UserResponse findById(UUID id);

    List<UserResponse> findByIds(Set<UUID> id);

    void blockUserByUsername(String username);

    void unblockUserByUsername(String username);

    UserResponse getUserDetails();

}
