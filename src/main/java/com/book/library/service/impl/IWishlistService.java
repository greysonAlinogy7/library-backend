package com.book.library.service.impl;

import com.book.library.payload.dto.WishlistDTO;
import com.book.library.payload.response.PageResponse;

public interface IWishlistService {
    WishlistDTO addToWishlist(Long bookId, String notes) throws Exception;
    void removeFromWishlist(Long bookId) throws Exception;
    PageResponse<WishlistDTO> getMyWishlist(int page, int size) throws Exception;
}
