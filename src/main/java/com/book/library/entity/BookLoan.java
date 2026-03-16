package com.book.library.entity;


import com.book.library.domain.BookLoanStatus;
import com.book.library.domain.BookLoanType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private  User user;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookLoanType type;

    @Enumerated(EnumType.STRING)
    private BookLoanStatus status;

    @Column( nullable = false)
    private LocalDate checkoutDate;


    private LocalDate dueDate;


    private LocalDate returnDate;

    private Integer renewalCount=0;

    @Column(nullable = false)
    private Integer maxRenewals=2;

    @Column(length = 500)
    private  String notes;

    @Column(nullable = false)
    private Boolean isOverdue=false;

    @Column(nullable = false)
    private Integer overdueDays=0;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public  boolean isActive(){
        return status==BookLoanStatus.ACTIVE
                || status == BookLoanStatus.OVERDUE;
    }

    public boolean canRenew(){
        return status==BookLoanStatus.ACTIVE && !isOverdue && renewalCount<maxRenewals;
    }

}
