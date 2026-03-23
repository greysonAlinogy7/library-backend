package com.book.library.service;

import com.book.library.entity.Book;
import com.book.library.entity.User;
import com.book.library.entity.Wishlist;
import com.book.library.mappers.WishlistMapper;
import com.book.library.payload.dto.WishlistDTO;
import com.book.library.payload.response.PageResponse;
import com.book.library.repository.BookLoanRepository;
import com.book.library.repository.BookRepository;
import com.book.library.repository.WishlistRepository;
import com.book.library.service.impl.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WishlistService implements IWishlistService {
    private  final WishlistRepository wishlistRepository;
    private final UserService userService;
    private  final BookRepository bookRepository;
    private  final WishlistMapper wishlistMapper;

    @Override
    public WishlistDTO addToWishlist(Long bookId, String notes) throws Exception {
        User user = userService.getCurrentUser();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new Exception("book not found"));
        //check if book is already in this list
        if (wishlistRepository.existsByUserIdAndBookId(user.getId(), bookId)){
            throw  new Exception("book in Wishlist already exist in your wishlist");
        }
        //create wishlist
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setBook(book);
        wishlist.setNotes(notes);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toDTO(savedWishlist);
    }

    @Override
    public void removeFromWishlist(Long bookId) throws Exception {
        User user = userService.getCurrentUser();
        Wishlist wishlist = wishlistRepository.findByUserIdAndBookId(user.getId(), bookId);
        if (wishlist==null){
            throw new Exception("book is not in your wishlist");

        }
        wishlistRepository.delete(wishlist);

    }

    @Override
    public PageResponse<WishlistDTO> getMyWishlist(int page, int size) throws Exception {
        Long userId = userService.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("addedAt").descending());
        Page<Wishlist> wishlistPage = wishlistRepository.findByUserId(userId, pageable);
        return converToPageResponse(wishlistPage);
    }

    private PageResponse<WishlistDTO> converToPageResponse(Page<Wishlist> wishlistPage){
        List<WishlistDTO> wishlistDTOs = wishlistPage.getContent().stream().map(wishlistMapper::toDTO).collect(Collectors.toList());
        return  new PageResponse<>(wishlistDTOs,
                wishlistPage.getNumber(),
                wishlistPage.getSize(),
                wishlistPage.getTotalElements(),
                wishlistPage.getTotalPages(),
                wishlistPage.isLast(),
                wishlistPage.isFirst(),
                wishlistPage.isEmpty()
                );
    }
}
