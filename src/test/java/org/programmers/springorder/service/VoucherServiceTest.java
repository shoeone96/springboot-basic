package org.programmers.springorder.service;


import org.junit.jupiter.api.*;

import org.programmers.springorder.voucher.dto.VoucherRequestDto;
import org.programmers.springorder.voucher.dto.VoucherResponseDto;
import org.programmers.springorder.voucher.model.Voucher;
import org.programmers.springorder.voucher.model.VoucherType;
import org.programmers.springorder.voucher.repository.MemoryVoucherRepository;
import org.programmers.springorder.voucher.repository.VoucherRepository;
import org.programmers.springorder.voucher.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VoucherServiceTest {

    private static final VoucherRepository voucherRepository = new MemoryVoucherRepository();
    private static final VoucherService voucherService = new VoucherService(voucherRepository);

    private static final Logger log = LoggerFactory.getLogger(VoucherServiceTest.class);


    @Test
    @Order(2)
    @DisplayName("모든 Voucher 리스트를 가져오는 Service 로직")
    void getAllVoucher() {
        List<UUID> uuids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        voucherRepository.save(new Voucher(uuids.get(0),10, VoucherType.PERCENT));
        voucherRepository.save(new Voucher(uuids.get(1),5, VoucherType.PERCENT));
        voucherRepository.save(new Voucher(uuids.get(2),1000, VoucherType.FIXED));
        voucherRepository.save(new Voucher(uuids.get(3), 2000, VoucherType.FIXED));

        List<VoucherResponseDto> allVoucher = voucherService.getAllVoucher();
        allVoucher.forEach(voucher -> log.info("voucher.toString = {}",voucher));
        List<UUID> rs = allVoucher.stream().map(VoucherResponseDto::getVoucherId).toList();

        assertThat(allVoucher).hasSize(5);
        assertThat(uuids.stream()
                .allMatch(rs::contains))
                .isTrue();
    }

    @Test
    @Order(1)
    @DisplayName("Voucher를 저장하는 Service 로직")
    void saveNewVoucher() {
        //given
        VoucherRequestDto requestDto = new VoucherRequestDto(100, VoucherType.FIXED);
        List<VoucherResponseDto> beforeSaveVoucher = voucherService.getAllVoucher();
        assertThat(beforeSaveVoucher).hasSize(0);

        //when
        voucherService.save(requestDto);
        List<VoucherResponseDto> allVoucher = voucherService.getAllVoucher();

        //then
        assertThat(allVoucher).hasSize(1);
    }
}