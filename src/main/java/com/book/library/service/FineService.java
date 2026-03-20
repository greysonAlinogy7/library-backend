package com.book.library.service;

import com.book.library.domain.FineStatus;
import com.book.library.domain.FineType;
import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentType;
import com.book.library.entity.BookLoan;
import com.book.library.entity.Fine;
import com.book.library.entity.User;
import com.book.library.mappers.FineMapper;
import com.book.library.payload.dto.FineDTO;
import com.book.library.payload.request.CreateFineRequest;
import com.book.library.payload.request.PaymentInitiatedRequest;
import com.book.library.payload.request.WaiveFineRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.payload.response.PaymentInitiatedResponse;
import com.book.library.repository.BookLoanRepository;
import com.book.library.repository.FineRepository;
import com.book.library.service.impl.IFineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService implements IFineService {
    private  final FineRepository fineRepository;
    private  final BookLoanRepository bookLoanRepository;
    private  final FineMapper fineMapper;
    private  final UserService userService;
    private  final PaymentService paymentService;

    @Override
    public FineDTO createFine(CreateFineRequest fineRequest) {
        //1. validate book loan exist
        BookLoan bookLoan = bookLoanRepository.findById(fineRequest.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan does not exist"));
        //2. create fine
        Fine fine = Fine.builder()
                .bookLoan(bookLoan)
                .user(bookLoan.getUser())
                .type(fineRequest.getType())
                .amount(fineRequest.getAmount())
                .status(FineStatus.PENDING)
                .reason(fineRequest.getReason())
                .note(fineRequest.getNote())
                .build();

        Fine saveFine = fineRepository.save(fine);
        return fineMapper.toDTO(saveFine);
    }

    @Override
    public PaymentInitiatedResponse payFine(Long fineId, String transactionId) throws Exception {
        //validate fine exist
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new Exception("Fine does not exist"));
        //2. check if already paid
        if (fine.getStatus().equals(FineStatus.PAID)){
            throw  new Exception("fine already paid");
        }
        if (fine.getStatus().equals(FineStatus.WAIVED)){
            throw  new Exception("fine waived");
        }
        //3. initiate payment
        User user = userService.getCurrentUser();
        PaymentInitiatedRequest request =PaymentInitiatedRequest
                .builder()
                .userId(user.getId())
                .fineId(fine.getId())
                .paymentType(PaymentType.FINE)
                .gateway(PaymentGateway.MIX_BY_YAS)
                .amount(fine.getAmount())
                .description("library fine payment")
                .build();
        return paymentService.initialPayment(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new Exception("fine not found with id" + fineId));
        fine.applyPayment(amount);
        fine.setTransactionId(transactionId);
        fine.setStatus(FineStatus.PAID);
        fine.setUpdateAt(LocalDateTime.now());
        fineRepository.save(fine);

    }

    @Override
    public FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception {
        Fine fine = fineRepository.findById(waiveFineRequest.getFineId()).orElseThrow(() -> new Exception("fine not found eith " + waiveFineRequest.getFineId()));
        if (fine.getStatus() == FineStatus.WAIVED){
            throw  new Exception("fine has already been waived");
        }

        if (fine.getStatus() == FineStatus.PAID){
            throw  new Exception("Fine has already been paid and can not be waived");
        }
        User currentAdmin = userService.getCurrentUser();
        fine.waive(currentAdmin, waiveFineRequest.getReason());

        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toDTO(savedFine);
    }

    @Override
    public List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception {
        User currentUser = userService.getCurrentUser();
        List<Fine> fines;

        if (status != null && type != null){
            fines = fineRepository.findByUserId(currentUser.getId()).stream().filter(f -> f.getStatus() == status && f.getType() == type).collect(Collectors.toList());
        } else if (type != null){
            fines = fineRepository.findByUserIdAndType(currentUser.getId(), type);
        } else {
            fines = fineRepository.findByUserId(currentUser.getId());
        }
        return fines.stream().map(fineMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<FineDTO> getAllFines(FineStatus status, FineType type, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Fine> finePage = fineRepository.findAllWithFilters(userId, status, type, pageable);
        return convertToPageResponse(finePage);
    }

    private PageResponse<FineDTO>  convertToPageResponse(Page<Fine> finePage){
        List<FineDTO> fineDTOS = finePage.getContent()
                .stream().map(fineMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                fineDTOS,
                finePage.getNumber(),
                finePage.getSize(),
                finePage.getTotalElements(),
                finePage.getTotalPages(),
                finePage.isLast(),
                finePage.isFirst(),
                finePage.isEmpty());

    }
}
