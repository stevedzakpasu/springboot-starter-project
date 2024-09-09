package dev.stevedzakpasu.springboot_starter.mapper;

import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseAdminDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserResponseDTO toUserResponseDTO(User user);

  List<UserResponseAdminDTO> toUserResponseAdminDTOList(List<User> all);
}
