package com.book.library.payload.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "ISBN is mandatory")
    @Size(max = 20, message = "ISBN must not exceed 20 characters")  // ISBN-13 with dashes is ~17–20 chars
    private String isbn;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    @NotNull(message = "Genre is mandatory")
    private Long genreId;

    private String genreName;
    private String genreCode;

    private String publisher;

    private LocalDate publicationDate;

    private String language;

    @Min(value = 1, message = "Pages must be at least 1")
    @Max(value = 2000, message = "Pages must not exceed 2000")
    private Integer pages;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Total copies is mandatory")
    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 fractional digits")
    private BigDecimal price;

    @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
    private String coverImageUrl;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}