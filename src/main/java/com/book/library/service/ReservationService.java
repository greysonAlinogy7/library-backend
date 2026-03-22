package com.book.library.service;

import com.book.library.domain.BookLoanStatus;
import com.book.library.domain.ReservationStatus;
import com.book.library.domain.UserRole;
import com.book.library.entity.Book;
import com.book.library.entity.Reservation;
import com.book.library.entity.User;
import com.book.library.mappers.ReservationMapper;
import com.book.library.payload.dto.ReservationDTO;
import com.book.library.payload.request.CheckoutRequest;
import com.book.library.payload.request.ReservationRequest;
import com.book.library.payload.request.ReservatonSearchRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.repository.BookLoanRepository;
import com.book.library.repository.BookRepository;
import com.book.library.repository.ReservationResitory;
import com.book.library.service.impl.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {
    private  final ReservationResitory reservationResitory;
    private  final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private  final BookRepository bookRepository;
    private  final ReservationMapper reservationMapper;
    private  final BookLoanService bookLoanService;
    int MAX_RESERVATION = 5;

    @Override
    public ReservationDTO createReservation(ReservationRequest reservationRequest) {
        return null;
    }

    @Override
    public ReservationDTO createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception {
        boolean alreadyHasLoan = bookLoanRepository.existsByUserIdAndBookIdAndStatus(
                userId,reservationRequest.getBookId(), BookLoanStatus.ACTIVE
                );
        if (alreadyHasLoan){
            throw  new Exception("you have loan on this book");
        }

        // validate user exist
        User user = userService.getCurrentUser();
        //2. validate book existence
        Book book = bookRepository.findById(reservationRequest.getBookId()).orElseThrow(() -> new Exception("book not found"));
        if (reservationResitory.hasActiveReservation(userId, book.getId())){
            throw  new Exception("you have already reservation on this book");
        }

        // check if book is already available
        if (book.getAvailableCopies()>0){
            throw new Exception("book is already available");
        }

        // check users active reservation limit
        long activeReservations = reservationResitory.countActiveReservationByUser(userId);
        if (activeReservations>=MAX_RESERVATION){
            throw  new Exception("you have reserved"+MAX_RESERVATION+"times");
        }
        //create reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservation.setNotes(reservationRequest.getNotes());

        long pendingCount = reservationResitory.countPendingReservationsByBook(book.getId());
        reservation.setQueuePosition((int)pendingCount+1);
        Reservation savedReservation = reservationResitory.save(reservation);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationResitory.findById(reservationId).orElseThrow(() -> new Exception("Reservation not found with ID"));
        User currentUser = userService.getCurrentUser();
        if (!reservation.getUser().getId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ROLE_ADMIN){
            throw  new Exception("You can only cancel your own reservation");

        }

        if (reservation.canBeCancelled()){
            throw new Exception("reservation can not be cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        Reservation savedReservation = reservationResitory.save(reservation);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationResitory.findById(reservationId).orElseThrow(() -> new Exception("Reservation not found with ID"));
        if (reservation.getBook().getAvailableCopies()<=0){
            throw  new Exception("reservation is not available");
        }
        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setFulFilledAt(LocalDateTime.now());
        Reservation savedReservation = reservationResitory.save(reservation);

        CheckoutRequest request = new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        reservation.setNotes("Assign Booked by Admin");
        bookLoanService.checkoutBookForUser(reservation.getUser().getId(), request);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservatonSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest);

        Page<Reservation> reservationPage = reservationResitory.searchReservationWithFilters(searchRequest.getUserId(), searchRequest.getBookId(), searchRequest.getStatus(), pageable);
        return buildPageResponse(reservationPage);
    }

    private  PageResponse<ReservationDTO> buildPageResponse(Page<Reservation> reservationPage){
        List<ReservationDTO> dtos = reservationPage.getContent().stream().map(reservationMapper::toDTO).toList();
        PageResponse<ReservationDTO> response = new PageResponse<>();
        response.setContent(dtos);
        response.setPageNumber(reservationPage.getNumber());
        response.setPageSize(reservationPage.getSize());
        response.setTotalElement(reservationPage.getTotalElements());
        response.setTotalPages(reservationPage.getTotalPages());
        response.setLast(reservationPage.isLast());
        return buildPageResponse(reservationPage);
    }

    private  Pageable createPageable(ReservatonSearchRequest searchRequest){
        Sort sort = "ASC".equalsIgnoreCase(searchRequest.getSortDirection()) ? Sort.by(searchRequest.getSortBy()).ascending() : Sort.by(searchRequest.getSortBy()).descending();
        return PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservatonSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest);
        Page<Reservation> reservationPage = reservationResitory.searchReservationWithFilters(searchRequest.getUserId(), searchRequest.getBookId(), searchRequest.getStatus(), searchRequest.getActiveOnly() != null ? searchRequest.getActiveOnly() : false, pageable);
        return buildPageResponse(reservationPage);
    }
}
