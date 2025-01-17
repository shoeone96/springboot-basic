package org.programmers.springorder.config.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.programmers.springorder.customer.repository.CustomerRepository;
import org.programmers.springorder.customer.repository.JdbcCustomerRepository;
import org.programmers.springorder.customer.service.CustomerService;
import org.programmers.springorder.voucher.repository.JdbcVoucherRepository;
import org.programmers.springorder.voucher.repository.VoucherRepository;
import org.programmers.springorder.voucher.service.VoucherService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    public DataSource dataSource() {
        var dataSource = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3300/vouchers_test")
                .username("root")
                .password("1234")
                .type(HikariDataSource.class)
                .build();
        dataSource.setMaximumPoolSize(15);
        dataSource.setMinimumIdle(10);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public VoucherRepository voucherRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new JdbcVoucherRepository(namedParameterJdbcTemplate);
    }

    @Bean
    public CustomerRepository customerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new JdbcCustomerRepository(namedParameterJdbcTemplate);
    }

    @Bean
    public CustomerService customerService( CustomerRepository customerRepository, VoucherRepository voucherRepository){
        return new CustomerService(customerRepository, voucherRepository);
    }

    @Bean
    public VoucherService voucherService(VoucherRepository voucherRepository, CustomerRepository customerRepository){
        return new VoucherService(voucherRepository, customerRepository);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
