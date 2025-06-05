package com.example.memecommerceback.domain.payment.repository;

import com.example.memecommerceback.domain.payment.entity.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

  Optional<Payment> findByPaymentKey(String paymentKey);
}
