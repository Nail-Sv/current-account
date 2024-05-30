package com.currentaccount.currentaccount.mapper;

import com.currentaccount.currentaccount.dto.CustomerDTO;
import com.currentaccount.currentaccount.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerDTO customerDTO);

    CustomerDTO toDto(Customer customer);

    List<CustomerDTO> toDtos(List<Customer> customers);

}
