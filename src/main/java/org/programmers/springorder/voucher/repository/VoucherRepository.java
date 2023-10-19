package org.programmers.springorder.voucher.repository;

import org.programmers.springorder.voucher.model.Voucher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    UUID save(Voucher voucher);
    List<Voucher> findAll();
    Optional<Voucher> findById(UUID voucherId);
}