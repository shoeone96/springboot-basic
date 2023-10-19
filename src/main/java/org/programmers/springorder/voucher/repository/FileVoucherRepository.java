package org.programmers.springorder.voucher.repository;

import org.programmers.springorder.console.Console;
import org.programmers.springorder.consts.ErrorMessage;
import org.programmers.springorder.voucher.model.Voucher;
import org.programmers.springorder.voucher.model.VoucherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
//@Profile("local")
public class FileVoucherRepository implements VoucherRepository{

    @Value(("${voucherFilePath}"))
    private String filePath;
    private final Logger logger = LoggerFactory.getLogger(FileVoucherRepository.class);
    private final Console console;

    public FileVoucherRepository(Console console) {
        this.console = console;
    }

    @Override
    public UUID save(Voucher voucher) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String data = String.valueOf(voucher.getVoucherId()) + "," +
                    String.valueOf(voucher.getDiscountValue()) + "," +
                    voucher.getVoucherType().name();
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            logger.error("errorMessage = {}", ErrorMessage.FILE_SAVE_ERROR_MESSAGE);
            console.printMessage(ErrorMessage.FILE_SAVE_ERROR_MESSAGE);
        }
        return voucher.getVoucherId();
    }

    @Override
    public List<Voucher> findAll() {
        List<Voucher> voucherList = new ArrayList<>(); // TODO: 동시성 처리

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                UUID voucherId = UUID.fromString(data[0]);
                long discountValue = Long.parseLong(data[1]);
                VoucherType voucherType = VoucherType.valueOf(data[2]);

                Voucher voucher = new Voucher(voucherId, discountValue, voucherType);
                voucherList.add(voucher);
            }
        } catch (FileNotFoundException e) {
            logger.error("errorMessage = {}", ErrorMessage.FILE_NOT_EXIST_MESSAGE);
            console.printMessage(ErrorMessage.FILE_NOT_EXIST_MESSAGE);
        } catch (IOException e) {
            logger.error("errorMessage = {}", ErrorMessage.FILE_FAIL_LOADING_MESSAGE);
            console.printMessage(ErrorMessage.FILE_FAIL_LOADING_MESSAGE);
        }

        return voucherList;
    }

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        return findAll().stream()
                .filter(voucher -> voucher.getVoucherId() == voucherId)
                .findFirst();
    }

}