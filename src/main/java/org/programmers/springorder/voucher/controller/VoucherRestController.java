package org.programmers.springorder.voucher.controller;

import org.programmers.springorder.voucher.dto.Response;
import org.programmers.springorder.voucher.dto.VoucherRequestDto;
import org.programmers.springorder.voucher.dto.VoucherResponseDto;
import org.programmers.springorder.voucher.service.VoucherService;
import org.springframework.context.annotation.Profile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Profile("default")
@RestController
public class VoucherRestController {
    private final VoucherService voucherService;

    public VoucherRestController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("api/v1/vouchers")
    public Response<List<VoucherResponseDto>> getVoucherList(
            @RequestParam(required = false) LocalDateTime startedAt,
            @RequestParam(required = false) LocalDateTime endedAt
            ) {
        if (startedAt != null && endedAt != null){
            Response.success(voucherService.getAllVoucherByTime(startedAt, endedAt));
        }
        return Response.success(voucherService.getAllVoucher());
    }

    @PostMapping("api/v1/vouchers")
    public Response<Void> createVoucher(VoucherRequestDto voucherRequestDto) {
        voucherService.saveNewVoucher(voucherRequestDto);
        return Response.success();
    }

    @GetMapping("api/v1/vouchers/{voucherId}")
    public Response<VoucherResponseDto> getVoucherDetail(@PathVariable UUID voucherId) {
        return Response.success(voucherService.getVoucherById(voucherId));
    }

    @DeleteMapping("api/v1/vouchers/{voucherId}")
    public Response<Void> deleteVoucher(@PathVariable UUID voucherId) {
        voucherService.deleteVoucher(voucherId);
        return Response.success();
    }

    @PostMapping("api/v1/voucher/{voucherId}/allocate")
    public Response<Void> giveVoucher(@PathVariable UUID voucherId, @RequestParam UUID customerId) {
        voucherService.allocateVoucher(voucherId, customerId);
        return Response.success();
    }

    @GetMapping("api/v1/vouchers/{customerId}/vouchers")
    public Response<List<VoucherResponseDto>> getCustomerOwnVoucherList(@PathVariable UUID customerId, Model model){
        return Response.success(voucherService.getCustomerOwnedVouchers(customerId));
    }
}

