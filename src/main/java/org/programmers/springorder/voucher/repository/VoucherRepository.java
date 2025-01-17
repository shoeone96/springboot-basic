package org.programmers.springorder.voucher.repository;

import org.programmers.springorder.customer.model.Customer;
import org.programmers.springorder.voucher.model.Voucher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    Voucher save(Voucher voucher);
    List<Voucher> findAll();
    Optional<Voucher> findById(UUID voucherId);
    Voucher updateVoucherOwner(Voucher voucher, Customer customer);
    List<Voucher> findAllByCustomerId(Customer customer);
    void deleteVoucher(Voucher voucher);
    List<Voucher> findAllByTimeLimit(LocalDateTime startedAt, LocalDateTime endedAt);
}
