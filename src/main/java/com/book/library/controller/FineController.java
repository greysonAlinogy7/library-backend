package com.book.library.controller;


import com.book.library.domain.FineStatus;
import com.book.library.domain.FineType;
import com.book.library.payload.dto.FineDTO;
import com.book.library.payload.request.CreateFineRequest;
import com.book.library.payload.request.WaiveFineRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.payload.response.PaymentInitiatedResponse;
import com.book.library.service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fines")
public class FineController {
    private  final FineService fineService;

    @PostMapping
    public ResponseEntity<?> createFine(@Valid @RequestBody CreateFineRequest fineRequest){
        FineDTO fineDTO = fineService.createFine(fineRequest);
        return  ResponseEntity.ok(fineDTO);

    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id, @RequestParam(required = false) String transactionId) throws Exception {
        PaymentInitiatedResponse response = fineService.payFine(id,transactionId);
        return  ResponseEntity.ok(response);

    }

    @PostMapping("/wave")
    public ResponseEntity<?> waiveFine(@Valid @RequestBody WaiveFineRequest waiveFineRequest)  throws Exception {
        FineDTO fineDTO = fineService.waiveFine(waiveFineRequest);
        return  ResponseEntity.ok(fineDTO);

    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFine(@RequestParam(required = false)FineStatus status, @RequestParam(required = false)FineType type) throws Exception {
        List<FineDTO> fines = fineService.getMyFines(status, type);
        return  ResponseEntity.ok(fines);

    }

    @GetMapping
    public ResponseEntity<?> getAllFines(@RequestParam(required = false) FineStatus status, @RequestParam(required = false) FineType type, @RequestParam(required = false) Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        PageResponse<FineDTO> fines = fineService.getAllFines(status, type, userId, page,size);
        return ResponseEntity.ok(fines);
    }


}
