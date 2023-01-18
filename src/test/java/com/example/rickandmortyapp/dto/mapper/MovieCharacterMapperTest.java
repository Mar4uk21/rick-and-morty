package com.example.rickandmortyapp.dto.mapper;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MovieCharacterMapperTest {

    @Test
    public void correctMappingApiCharacterDtoToMovieCharacter() {
        ApiCharacterDto apiCharacterDto = new ApiCharacterDto();
        apiCharacterDto.setId(1L);
        apiCharacterDto.setName("John");
        apiCharacterDto.setStatus(String.valueOf(Status.ALIVE));
        apiCharacterDto.setGender(String.valueOf(Gender.MALE));
        MovieCharacter movieCharacter = new MovieCharacterMapper()
                .parseApiCharacterResponseDto(apiCharacterDto);
        Assertions.assertEquals(1L,movieCharacter.getExternalId());
        Assertions.assertEquals("John",movieCharacter.getName());
        Assertions.assertEquals(Gender.MALE,movieCharacter.getGender());
        Assertions.assertEquals(Status.ALIVE, movieCharacter.getStatus());
    }

    @Test
    public void correctMappingMovieCharacterToCharacterResponseDto() {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setId(1L);
        movieCharacter.setExternalId(2L);
        movieCharacter.setName("John");
        movieCharacter.setStatus(Status.ALIVE);
        movieCharacter.setGender(Gender.MALE);
        CharacterResponseDto characterResponseDto = new MovieCharacterMapper()
                .toResponseDto(movieCharacter);
        Assertions.assertEquals(1L,characterResponseDto.getId());
        Assertions.assertEquals("John",characterResponseDto.getName());
        Assertions.assertEquals(Gender.MALE,characterResponseDto.getGender());
        Assertions.assertEquals(Status.ALIVE,characterResponseDto.getStatus());
        Assertions.assertEquals(2L,characterResponseDto.getExternalId())    ;
    }
}