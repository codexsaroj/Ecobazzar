package com.ecobazzar.service.interf;

import com.ecobazzar.dto.AddressDto;
import com.ecobazzar.dto.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}
