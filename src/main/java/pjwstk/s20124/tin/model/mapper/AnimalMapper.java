package pjwstk.s20124.tin.model.mapper;

import lombok.Builder;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import pjwstk.s20124.tin.model.Animal;
import pjwstk.s20124.tin.model.User;
import pjwstk.s20124.tin.model.dto.AnimalDto;
import pjwstk.s20124.tin.model.dto.UserDto;
import pjwstk.s20124.tin.utils.SecurityUtils;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnimalMapper {

    AnimalDto animalToDto(Animal animal);
    @AfterMapping
    default void setOwnerFlag(Animal animal, @MappingTarget AnimalDto dto){
        boolean isOwner = Optional.of(animal)
            .map(Animal::getUser)
            .map(User::getUsername)
            .equals(SecurityUtils.getCurrentUserLogin());

        dto.setOwner(isOwner);

    }

    Animal dtoToAnimal(AnimalDto dto);

    void updateAnimal(@MappingTarget Animal entity, Animal data);
}
