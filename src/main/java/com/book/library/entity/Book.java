package com.book.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true, name = "isbn")
    private String isbn;
    @Column(nullable = false, name = "title")
    private String title;
    @Column(nullable = false, name = "author")
    private String author;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Genre genre;
    private String publisher;
    private LocalDate publishedDate;
    private String language;
    private Integer pages;
    private  String description;
    @Column(nullable = false)
    private Integer totalCopies;
    private BigDecimal price;
    private String coverImageUrl;
    @Column(nullable = false)
    private Boolean active=true;
    @Column(nullable = false)
    private Integer availableCopies;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Available copies can not exceed total copies")
    public boolean isAvailableCopiesValid(){
        if (totalCopies==null || availableCopies == null){
            return true;
        }
        return availableCopies<=totalCopies;
    }

}
