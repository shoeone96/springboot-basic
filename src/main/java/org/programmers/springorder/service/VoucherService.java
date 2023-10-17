package org.programmers.springorder.service;

import org.programmers.springorder.dto.VoucherRequestDto;
import org.programmers.springorder.dto.VoucherResponseDto;
import org.programmers.springorder.model.Voucher;
import org.programmers.springorder.repository.VoucherRepository;

import java.util.List;
import java.util.UUID;

public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public List<VoucherResponseDto> getAllVoucher(){
        return voucherRepository.findAll()
                .stream()
                .map(VoucherResponseDto::of)
                .toList();
    }

    public void save(VoucherRequestDto voucherDto) {
        Voucher voucher = Voucher.of(UUID.randomUUID(), voucherDto);
        voucherRepository.save(voucher);
    }
}