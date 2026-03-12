package com.book.library.controller;


import com.book.library.exception.GenreException;
import com.book.library.payload.dto.GenreDTO;
import com.book.library.payload.response.ApiResponse;
import com.book.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genre")
public class GenreController {
    private  final GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genre){
        GenreDTO createdGenre = genreService.createGenre(genre);
        return ResponseEntity.ok(createdGenre);

    }

    @GetMapping
    public ResponseEntity<?> getAllGenres(){
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);

    }

    @GetMapping("/{genreId}")
    public ResponseEntity<?> getGenreById(@PathVariable("genreId") Long genreId) throws GenreException {
        GenreDTO genres = genreService.getGenreById(genreId);
        return ResponseEntity.ok(genres);

    }

    @PutMapping("/{genreId}")
    public ResponseEntity<?> updateGenres(@PathVariable Long genreId, @RequestBody GenreDTO genre) throws GenreException {
        GenreDTO genres = genreService.updateGenre(genreId,genre);
        return ResponseEntity.ok(genres);

    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenres(@PathVariable("genreId") Long genreId) throws GenreException {
         genreService.deleteGenre(genreId);
        ApiResponse response = new ApiResponse("Genre Deleted successfully", true);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{genreId}/hard")
    public ResponseEntity<?> hardDeleteGenres(@PathVariable("genreId") Long genreId) throws GenreException {
        genreService.hardDelete(genreId);
        ApiResponse response = new ApiResponse("Genre Deleted successfully", true);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/top-level")
    public ResponseEntity<?> getTopLevelGenre() throws GenreException {
        List<GenreDTO> genre = genreService.getTopLevelGenres();
        return ResponseEntity.ok(genre);

    }

    @GetMapping("/count")
    public ResponseEntity<?> getTotalActiveGenre() throws GenreException {
        Long genre = genreService.getTotalActiveGenres();
        return ResponseEntity.ok(genre);

    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<?> getBookCountByGenre(@PathVariable Long id) throws GenreException {
        Long count = genreService.getBookCountByGenre(id);
        return ResponseEntity.ok(count);

    }
}
