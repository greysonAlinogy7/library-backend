package com.book.library.payload.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistDTO {
    private Long id;
    private Long userId;
    private  String userFullName;
    private BookDTO book;
    private LocalDateTime addedAt;
    private String notes;

}
