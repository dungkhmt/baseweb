package com.hust.baseweb.applications.geo.service;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import org.springframework.stereotype.Service;

@Service
public interface PostalAddressService {
    PostalAddress save(String locationCode, String address, double latitude, double longitude);

}
