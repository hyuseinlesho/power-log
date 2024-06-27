package com.hyuseinlesho.powerlog.mapper;

import com.hyuseinlesho.powerlog.dto.RegisterUserDto;
import com.hyuseinlesho.powerlog.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity mapToUserEntity(RegisterUserDto registerDto);
}