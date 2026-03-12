package com.book.library.payload.dto;


import com.book.library.entity.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDTO {
    private Long id;
    @NotBlank(message = "Genre code is mandatory")
    private String code;
    @NotBlank(message = "genre name is mandatory")
    private String name;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @Min(value = 0, message = "display order can not be negative")
    private Integer displayOrder=0;
    private  Boolean active;
    private Long parentGenreId;
    private String parentGenreName;
    private List<GenreDTO> subGenre;
    private Long bookCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
