package org.programmers.springorder.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class VoucherTest {

    @Test
    @DisplayName("Voucher 객체 생성 테스트")
    public void voucherCreateTest() throws Exception {
        //given
        UUID voucherId1 = UUID.randomUUID();
        long discountValue1 = 10;
        VoucherType voucherType1 = VoucherType.FIXED;
        long beforeDiscount = 10000;

        UUID voucherId2 = UUID.randomUUID();
        long discountValue2 = 10;
        VoucherType voucherType2 = VoucherType.PERCENT;

        //when
        Voucher voucher1 = new Voucher(voucherId1, discountValue1, voucherType1);
        Voucher voucher2 = new Voucher(voucherId2, discountValue2, voucherType2);

        //then
        assertThat(voucher1.getVoucherId()).isEqualTo(voucherId1);
        assertThat(voucher2.getVoucherId()).isEqualTo(voucherId2);

        /**
         * 생성된 Voucher 객체로 각각 discount 테스트 진행
         */
        assertThat(voucher1.getCalculate(beforeDiscount)).isEqualTo(9990);
        assertThat(voucher2.getCalculate(beforeDiscount)).isEqualTo(9000);

    }
}