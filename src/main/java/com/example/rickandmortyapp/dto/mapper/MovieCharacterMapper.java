package com.example.rickandmortyapp.dto.mapper;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import org.springframework.stereotype.Component;

@Component
public class MovieCharacterMapper {

    public MovieCharacter parseApiCharacterResponseDto(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setName(dto.getName());
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setExternalId(dto.getId());
        return movieCharacter;
    }

    public CharacterResponseDto toResponseDto(MovieCharacter movieCharacter) {
        CharacterResponseDto dto = new CharacterResponseDto();
        dto.setExternalId(movieCharacter.getExternalId());
        dto.setName(movieCharacter.getName());
        dto.setGender(movieCharacter.getGender());
        dto.setStatus(movieCharacter.getStatus());
        dto.setId(movieCharacter.getId());
        return dto;
    }
}
