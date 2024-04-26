package kz.flurent.mpper;

import kz.flurent.model.entity.User;
import kz.flurent.model.response.UserResponse;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@NoArgsConstructor
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract List<UserResponse> toResponse(List<User> users);

    @Mapping(source = "user.role.roleName",target = "role")
    public abstract UserResponse toResponse(User user);


}
