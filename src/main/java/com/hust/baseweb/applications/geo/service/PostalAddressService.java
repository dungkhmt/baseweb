package com.hust.baseweb.applications.geo.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.geo.entity.PostalAddress;

@Service
public interface PostalAddressService {
	public PostalAddress save(String address, String latitude, String longitude);
}
