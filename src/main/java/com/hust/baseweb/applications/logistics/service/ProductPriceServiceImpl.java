package com.hust.baseweb.applications.logistics.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.repo.ProductPriceRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.entity.UserLogin;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {

	@Autowired
	private ProductPriceRepo productPriceRepo;
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private UomRepo uomRepo;
	
	@Override
	@Transactional
	public ProductPrice setProductPrice(UserLogin createdByUserLogin, String productId, BigDecimal price, String currencyUomId,
			String taxInPrice) {
		// TODO Auto-generated method stub
		
		Product product = productRepo.findByProductId(productId);
		Uom uom = uomRepo.findByUomId(currencyUomId);
		
		Date d = new Date();
		ProductPrice pp = productPriceRepo.findByProductAndThruDate(product, null);
		if(pp == null){
			pp = new ProductPrice();
			pp.setCurrencyUom(uom);
			pp.setProduct(product);
			pp.setPrice(price);
			pp.setFromDate(d);
			pp.setTaxInPrice(taxInPrice);
			pp.setCreatedByUserLogin(createdByUserLogin);
			pp = productPriceRepo.save(pp);
			return pp;
		}else{
			// set thru_date of the current product_price by now() to disable this information
			pp.setThruDate(d);
			productPriceRepo.save(pp);
			
			pp = new ProductPrice();
			pp.setCurrencyUom(uom);
			pp.setProduct(product);
			pp.setPrice(price);
			pp.setFromDate(d);
			pp.setTaxInPrice(taxInPrice);
			pp.setCreatedByUserLogin(createdByUserLogin);
			pp = productPriceRepo.save(pp);
			return pp;
		}
		
	}
	
	@Override
	@Transactional
	public ProductPrice getProductPrice(String productId){
		Product product = productRepo.findByProductId(productId);
		ProductPrice pp = productPriceRepo.findByProductAndThruDate(product, null);
		return pp;
	}

}
