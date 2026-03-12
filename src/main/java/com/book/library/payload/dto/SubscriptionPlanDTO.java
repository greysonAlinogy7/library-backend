package com.book.library.payload.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDTO {
    private Long id;
    @NotNull(message = "plan code is mandatory")
    private String planCode;

    @NotNull(message = "plan name is mandatory")
    private String name;

    private String description;
    @NotNull(message = "plan code is mandatory")
    private Integer durationDays;

    @NotNull(message = "price mandatory")
    @Positive(message = "price must be positive")
    private Long price;

    private String currency="Tsh";

    @NotNull(message = "max book allowed is mandatory")
    @Positive(message = "max books must positive")
    private Integer maxBooksAllowed;

    @NotNull(message = "Max days must be positive")
    @Positive(message = "max days must be positive")
    private Integer maxDaysPerBook;

    private Integer displayOrder=0;
    private Boolean isActive=true;
    private Boolean isFeatured=false;
    private String badgeText;
    private String adminNotes;

    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private  String createdBy;
    private  String updatedBy;
}
