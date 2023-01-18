package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.dto.external.ApiResponseDto;
import com.example.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.repository.MovieCharacterRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper movieCharacterMapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository,
                                     MovieCharacterMapper movieCharacterMapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.movieCharacterMapper = movieCharacterMapper;
    }

    @PostConstruct
    @Scheduled(cron = "* 0 8 * * ?")
    @Override
    public void syncExternalCharacters() {
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        saveDtosToDB(apiResponseDto);
        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtosToDB(apiResponseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        return movieCharacterRepository.getById(randomId);
    }

    @Override
    public List<MovieCharacter> findAllByNameContains(String namePart) {
        return movieCharacterRepository.findAllByNameContains(namePart);
    }

    void saveDtosToDB(ApiResponseDto apiResponseDto) {
        Map<Long, ApiCharacterDto> externalDtos = Arrays.stream(apiResponseDto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));

        Set<Long> externalIds = externalDtos.keySet();

        List<MovieCharacter> existingCharacters =
                movieCharacterRepository.findAllByExternalIdIn(externalIds);

        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        Set<Long> existingIds = existingCharactersWithIds.keySet();

        externalIds.removeAll(existingIds);

        List<MovieCharacter> charactersToSave = externalIds.stream()
                .map(i -> movieCharacterMapper.parseApiCharacterResponseDto(externalDtos.get(i)))
                .collect(Collectors.toList());

        movieCharacterRepository.saveAll(charactersToSave);
    }
}
