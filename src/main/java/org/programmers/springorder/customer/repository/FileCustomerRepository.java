package org.programmers.springorder.customer.repository;

import org.programmers.springorder.console.Console;
import org.programmers.springorder.consts.ErrorMessage;
import org.programmers.springorder.customer.model.Customer;
import org.programmers.springorder.customer.model.CustomerType;
import org.programmers.springorder.voucher.model.Voucher;
import org.programmers.springorder.voucher.model.VoucherType;
import org.programmers.springorder.voucher.repository.FileVoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileCustomerRepository implements CustomerRepository{

    @Value(("${blacklistFilePath}"))
    private String filePath;
    private final Logger logger = LoggerFactory.getLogger(FileVoucherRepository.class);
    private final Console console;

    public FileCustomerRepository(Console console) {
        this.console = console;
    }

    @Override
    public List<Customer> findAllBlackList() {
        return findAll().stream()
                .filter(customer -> customer.getCustomerType() == CustomerType.BLACK)
                .toList();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                UUID customerId = UUID.fromString(data[0]);
                String name = data[1];
                CustomerType customerType = CustomerType.valueOf(data[2]);
                int age = Integer.parseInt(data[3]);
                String email = data[4];
                Customer customer = new Customer(customerId, name, customerType, age, email);
                customerList.add(customer);
            }
        } catch (FileNotFoundException e) {
            logger.error("errorMessage = {}", ErrorMessage.FILE_NOT_EXIST_MESSAGE);
            console.printMessage(ErrorMessage.FILE_NOT_EXIST_MESSAGE);
        } catch (IOException e) {
            logger.error("errorMessage = {}", ErrorMessage.FILE_FAIL_LOADING_MESSAGE);
            console.printMessage(ErrorMessage.FILE_FAIL_LOADING_MESSAGE);
        }

        return customerList;
    }
}