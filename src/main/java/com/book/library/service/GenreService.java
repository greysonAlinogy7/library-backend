package com.book.library.service;

import com.book.library.entity.Genre;
import com.book.library.exception.GenreException;
import com.book.library.mappers.GenreMapper;
import com.book.library.payload.dto.GenreDTO;
import com.book.library.repository.GenreRepository;
import com.book.library.service.impl.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService implements IGenreService {
    private  final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {
        Genre genre = genreMapper.toEntity(genreDTO);
        Genre savedGenre = genreRepository.save(genre);
        return  genreMapper.toDTo(savedGenre);

    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream().map(genreMapper::toDTo).collect(Collectors.toList());
    }

    @Override
    public GenreDTO getGenreById(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new GenreException("genre not found"));
        return genreMapper.toDTo(genre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genreDTO) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(()-> new GenreException("Genre not found"));
        genreMapper.updateEntityFromDTO(genreDTO, existingGenre);
      Genre updatedGenre =  genreRepository.save(existingGenre);
        return genreMapper.toDTo(updatedGenre);
    }

    @Override
    public void deleteGenre(Long genreId) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(()-> new GenreException("Genre not found"));
        existingGenre.setActive(false);
        genreRepository.save(existingGenre);


    }

    @Override
    public void hardDelete(Long genreId) throws GenreException {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(()-> new GenreException("Genre not found"));
        genreRepository.delete(existingGenre);
    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenre() {
        List<Genre> topLevelGenre = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.genreDTOList(topLevelGenre);
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        List<Genre> topGenre = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.genreDTOList(topGenre);
    }

    @Override
    public Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenre(Long genreId) {
        return 0;
    }
}
