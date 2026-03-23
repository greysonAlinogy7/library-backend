package com.book.library.mappers;

import com.book.library.entity.Wishlist;
import com.book.library.payload.dto.WishlistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistMapper {
    private  final BookMapper bookMapper;
    public WishlistDTO toDTO(Wishlist wishlist){
        if (wishlist == null){
            return null;
        }

        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());

        if (wishlist.getUser() != null){
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserFullName(wishlist.getUser().getFullName());
        }

        if (wishlist.getBook() != null){
            dto.setBook(bookMapper.bookDTO(wishlist.getBook()));
        }
        dto.setAddedAt(wishlist.getAddedAt());
        dto.setNotes(wishlist.getNotes());
        return  dto;
    }
}
