package com.book.library.mappers;

import com.book.library.entity.Genre;
import com.book.library.payload.dto.GenreDTO;
import com.book.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    private  final GenreRepository genreRepository;
    public GenreDTO toDTo(Genre savedGenre) {
        if (savedGenre == null) return null;

        GenreDTO dto = GenreDTO.builder()
                .id(savedGenre.getId())
                .code(savedGenre.getCode())
                .name(savedGenre.getName())
                .description(savedGenre.getDescription())
                .displayOrder(savedGenre.getDisplayOrder())
                .active(savedGenre.getActive())
                .createdAt(savedGenre.getCreatedAt())
                .updatedAt(savedGenre.getUpdatedAt())
                .build();

        // Map parent genre if exists
        if (savedGenre.getParentGenre() != null) {
            dto.setParentGenreId(savedGenre.getParentGenre().getId());
            dto.setParentGenreName(savedGenre.getParentGenre().getName());
        }

        // Map sub-genres safely
        List<Genre> subGenres = savedGenre.getSubGenre();
        if (subGenres != null && !subGenres.isEmpty()) {
            dto.setSubGenre(
                    subGenres.stream()
                            .filter(Genre::getActive) // only active sub-genres
                            .map(this::toDTo)         // recursive mapping
                            .collect(Collectors.toList())
            );
        } else {
            dto.setSubGenre(List.of()); // empty list instead of null
        }

        return dto;
    }

    public  Genre toEntity(GenreDTO genreDTO){
        if (genreDTO == null){
            return null;
        }
        Genre genre = Genre.builder()
                .code(genreDTO.getCode())
                .name(genreDTO.getName())
                .description(genreDTO.getDescription())
                .displayOrder(genreDTO.getDisplayOrder())
                .active(true)
                .build();

        if (genreDTO.getParentGenreId() != null ){
             genreRepository.findById(genreDTO.getParentGenreId())
                    .ifPresent(
                            genre::setParentGenre
                    );
        }
        return genre;
    }

    public  void updateEntityFromDTO(GenreDTO dto, Genre existingGenre){
        if (dto == null || existingGenre == null){
            return;
        }
        existingGenre.setCode(dto.getCode());
        existingGenre.setName(dto.getName());
        existingGenre.setDescription(dto.getDescription());
        existingGenre.setDisplayOrder(dto.getDisplayOrder() !=null ? dto.getDisplayOrder() : 0);
        if (dto.getActive() != null){
            existingGenre.setActive(dto.getActive());
        }

        if (dto.getParentGenreId() != null){
            genreRepository.findById(dto.getParentGenreId()).ifPresent(existingGenre::setParentGenre);
        }
    }

    public List<GenreDTO> genreDTOList(List<Genre> genreList){
        return genreList.stream().map(genre -> toDTo(genre)).collect(Collectors.toList());
    }
}
