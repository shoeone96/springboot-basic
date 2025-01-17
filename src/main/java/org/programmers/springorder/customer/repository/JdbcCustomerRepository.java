package org.programmers.springorder.customer.repository;

import org.programmers.springorder.customer.model.Customer;
import org.programmers.springorder.customer.model.CustomerType;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.*;

@Profile("default")
@Repository
public class JdbcCustomerRepository implements CustomerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String SELECT_ALL_CUSTOMER = "SELECT * FROM customers";
    private final String INSERT_CUSTOMER = "INSERT INTO customers(customer_id, customer_name, customer_type, created_at, updated_at) VALUES(UUID_TO_BIN(:customerId), :customerName, :customerType, :createdAt, :updatedAt)";
    private final String FIND_BLACKLIST = "SELECT * FROM customers where customer_type = 'BLACK'";
    private final String FIND_BY_CUSTOMER_ID = "SELECT * FROM customers where customer_id = UUID_TO_BIN(:customerId)";

    public JdbcCustomerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Customer> customerRowMapper = (resultSet, rowNum) -> {
        UUID customerId = toUUID(resultSet.getBytes("customer_id"));
        String customerName = resultSet.getString("customer_name");
        CustomerType customerType = CustomerType.valueOf(resultSet.getString("customer_type"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return Customer.fromDbCustomer(customerId, customerName, customerType, createdAt, updatedAt);
    };

    @Override
    public List<Customer> findAllBlackList() {
        return jdbcTemplate.query(FIND_BLACKLIST, customerRowMapper);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CUSTOMER, customerRowMapper);
    }

    @Override
    public Optional<Customer> findByID(UUID customerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    FIND_BY_CUSTOMER_ID,
                    findByIdMap(customerId),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Customer insert(Customer customer) {
        int update = jdbcTemplate.update(INSERT_CUSTOMER, toParamMap(customer));
        if (update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }
        return customer;
    }

    public void clear() {
        jdbcTemplate.getJdbcOperations().update("delete from customers");
    }

    private Map<String, Object> toParamMap(Customer customer) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customer.getCustomerId().toString().getBytes());
        map.put("customerName", customer.getName());
        map.put("customerType", customer.getCustomerType().name());
        map.put("createdAt", customer.getCreatedAt());
        map.put("updatedAt", customer.getCreatedAt());
        return map;
    }

    private Map<String, Object> findByIdMap(UUID customerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId.toString().getBytes());
        return map;
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}