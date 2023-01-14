package pjwstk.s20124.tin.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.UserDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDto userToDto(User user);
    User dtoToUser(UserDto dto);

}
