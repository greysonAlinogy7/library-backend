package com.book.library.service.impl;

import com.book.library.entity.Genre;
import com.book.library.exception.GenreException;
import com.book.library.payload.dto.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGenreService {
    GenreDTO createGenre(GenreDTO genre);
    List<GenreDTO> getAllGenres();
    GenreDTO getGenreById(Long genreId) throws GenreException;
    GenreDTO updateGenre(Long genreId, GenreDTO genreDTO) throws GenreException;
    void deleteGenre(Long genreId) throws GenreException;
    void hardDelete(Long genreId) throws GenreException;
    List<GenreDTO> getAllActiveGenresWithSubGenre();
    List<GenreDTO> getTopLevelGenres();
    Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);
    long getTotalActiveGenres();
    long getBookCountByGenre(Long genreId);
}
