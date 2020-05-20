package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.entity.Uom;
import com.hust.baseweb.applications.logistics.model.product.SaleReportModel;
import com.hust.baseweb.applications.logistics.repo.ProductPriceJpaRepo;
import com.hust.baseweb.applications.logistics.repo.ProductPriceRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.UomRepo;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.order.repo.OrderItemRepo;
import com.hust.baseweb.applications.order.repo.OrderRoleRepo;
import com.hust.baseweb.applications.order.repo.mongodb.CustomerRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.ProductRevenueRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductPriceServiceImpl implements ProductPriceService {

    private ProductPriceRepo productPriceRepo;
    private ProductPriceJpaRepo productPriceJpaRepo;
    private ProductRepo productRepo;
    private UomRepo uomRepo;

    private OrderHeaderRepo orderHeaderRepo;
    private OrderItemRepo orderItemRepo;

    private OrderRoleRepo orderRoleRepo;

    private CustomerRevenueRepo customerRevenueRepo;
    private ProductRevenueRepo productRevenueRepo;

    @Override
    @Transactional
    public ProductPrice setProductPrice(UserLogin createdByUserLogin,
                                        String productId,
                                        Double price,
                                        String currencyUomId,
                                        String taxInPrice,
                                        String fromDate,
                                        String thruDate) {
        Product product = productRepo.findById(productId).orElseThrow(NoSuchElementException::new);
//        if (product != null) {
//            log.info("setProductPrice, find product " + product.getProductId());
//        }

        Uom uom = uomRepo.findByUomId(Optional.ofNullable(currencyUomId).orElse("CUR_vnd"));

        Date now = new Date();
        ProductPrice productPrice = productPriceRepo.findByProductAndThruDateNullOrThruDateAfter(product, now);

        if (productPrice != null) {
            log.info("setProductPrice, find productPrice " + productPrice.getProductPriceId());

            productPrice.setThruDate(now);
            productPriceRepo.save(productPrice);
        }
        // set thru_date of the current product_price by now() to disable this information

        productPrice = new ProductPrice();
        productPrice.setCurrencyUom(uom);
        productPrice.setProduct(product);
        productPrice.setPrice(price);
        productPrice.setTaxInPrice(taxInPrice);

        if (fromDate == null || thruDate == null) {
            productPrice.setFromDate(now);
        } else {
            try {
                productPrice.setFromDate(Constant.DATE_FORMAT.parse(fromDate));
                productPrice.setThruDate(Constant.DATE_FORMAT.parse(thruDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //pp.setCreatedByUserLogin(createdByUserLogin);
        productPrice.setCreatedByUserLoginId(createdByUserLogin.getUserLoginId());
        productPrice = productPriceRepo.save(productPrice);

        log.info("setProductPrice update DONE");
        return productPrice;
    }

    @Override
    @Transactional
    public ProductPrice getProductPrice(String productId) {
        //log.info("getProductPrice, productId = " + productId);

        Product product = productRepo.findByProductId(productId);
        //log.info("getProductPrice, product = " + product.getProductId());

        //ProductPrice pp = productPriceRepo.findByProductAndThruDate(product, null);
        //Iterable<ProductPrice> lst = productPriceRepo.findAll();
        Date now = new Date();
        List<ProductPrice> lst = productPriceJpaRepo.findByProductAndThruDateNullOrThruDateAfter(product, now);
        if (lst == null || lst.size() == 0) {
            return null;
        } else {
            return lst.get(0);
        }
    }

    @Override
    public List<ProductPrice.Model> getProductPriceHistory(String productId) {
        Product product = productRepo.findById(productId).orElseThrow(NoSuchElementException::new);
        return productPriceJpaRepo.findByProduct(product)
            .stream()
            .map(ProductPrice::toModel)
            .collect(Collectors.toList());
    }

    /*
    @Override
    public SaleReportModel.Output getSaleReports(SaleReportModel.Input input) {
        if (input.getProductId() != null) {
            List<SaleReportModel.DatePrice> datePrices = productRevenueRepo.findAllById_ProductIdAndId_DateBetween(
                    input.getProductId(),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_TIME_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_TIME_FORMAT))
                    .stream()
                    .map(productRevenue -> new SaleReportModel.DatePrice(productRevenue.getId()
                            .getDate()
                            .format(Constant.LOCAL_DATE_FORMAT), productRevenue.getRevenue()))
                    .collect(Collectors.toList());
            return new SaleReportModel.Output(datePrices);

        } else if (input.getPartyCustomerId() != null) {
            List<SaleReportModel.DatePrice> datePrices = customerRevenueRepo.findAllById_CustomerIdAndId_DateBetween(
                    UUID.fromString(input.getPartyCustomerId()),
                    LocalDate.parse(input.getFromDate(), Constant.LOCAL_DATE_TIME_FORMAT),
                    LocalDate.parse(input.getThruDate(), Constant.LOCAL_DATE_TIME_FORMAT))
                    .stream()
                    .map(customerRevenue -> new SaleReportModel.DatePrice(customerRevenue.getId()
                            .getDate()
                            .format(Constant.LOCAL_DATE_FORMAT), customerRevenue.getRevenue()))
                    .collect(Collectors.toList());
            return new SaleReportModel.Output(datePrices);
        }
        return null;
    }
    */

    @NotNull
    private SaleReportModel.Output calcSaleReportByPartyCustomer(SaleReportModel.Input input,
                                                                 Date fromDate,
                                                                 Date thruDate) {
        List<OrderRole> orderRoles = orderRoleRepo.findAllByPartyIdAndRoleTypeId(
            UUID.fromString(input.getPartyCustomerId()),
            "BILL_TO_CUSTOMER");
        List<String> orderIds = orderRoles.stream()
            .map(OrderRole::getOrderId) // TODO: use party customer in order header
            .distinct()
            .collect(Collectors.toList());
        List<OrderHeader> orderHeaders = orderHeaderRepo.findAllByOrderIdInAndOrderDateBetween(
            orderIds,
            fromDate,
            thruDate);
        List<OrderItem> orderItems = orderItemRepo.findAllByOrderIdIn(orderIds);
        List<Product> products = orderItems.stream()
            .map(OrderItem::getProduct)
            .distinct()
            .collect(Collectors.toList());
        Date now = new Date();
        List<ProductPrice> productPrices = productPriceJpaRepo.findAllByProductInAndThruDateNullOrThruDateAfter(
            products, now);
        return calcSaleReport(orderHeaders, orderItems, productPrices);
    }

    @NotNull
    private SaleReportModel.Output calcSaleReportByProduct(SaleReportModel.Input input, Date fromDate, Date thruDate) {
        Product product = productRepo.findById(input.getProductId()).orElseThrow(NoSuchElementException::new);

        List<OrderHeader> orderHeaders = orderHeaderRepo.findAllByOrderDateBetween(fromDate, thruDate);
        List<OrderItem> orderItems = orderItemRepo.findAllByProductAndOrderIdIn(product,
            orderHeaders.stream().map(OrderHeader::getOrderId).collect(Collectors.toList()));
        Date now = new Date();
        List<ProductPrice> productPrices = productPriceJpaRepo.findByProductAndThruDateNullOrThruDateAfter(product,
            now);

        return calcSaleReport(orderHeaders, orderItems, productPrices);
    }

    @NotNull
    private SaleReportModel.Output calcSaleReport(List<OrderHeader> orderHeaders,
                                                  List<OrderItem> orderItems,
                                                  List<ProductPrice> productPrices) {
        Map<LocalDate, List<OrderHeader>> dateToOrders = new HashMap<>();
        for (OrderHeader orderHeader : orderHeaders) {
            dateToOrders.computeIfAbsent(orderHeader.getOrderDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate(), k -> new ArrayList<>()).add(orderHeader);
        }

        Map<String, List<OrderItem>> orderIdToOrderItems = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            orderIdToOrderItems.computeIfAbsent(orderItem.getOrderId(), k -> new ArrayList<>()).add(orderItem);
        }

        Map<String, ProductPrice> productPriceMap = new HashMap<>();
        for (ProductPrice productPrice : productPrices) {
            productPriceMap.put(productPrice.getProduct().getProductId(), productPrice);
        }

        SaleReportModel.Output saleReportOutput = new SaleReportModel.Output(new ArrayList<>());
        for (Map.Entry<LocalDate, List<OrderHeader>> dateOrderEntry : dateToOrders.entrySet()) {
            double totalPrice = 0.0;
            for (OrderHeader orderHeader : dateOrderEntry.getValue()) {
                for (OrderItem orderItem : orderIdToOrderItems.get(orderHeader.getOrderId())) {
                    ProductPrice productPrice = productPriceMap.get(orderItem.getProduct().getProductId());
                    totalPrice += productPrice.getPrice() * orderItem.getQuantity();
                }
            }
            saleReportOutput.getDatePrices()
                .add(new SaleReportModel.DatePrice(dateOrderEntry.getKey().toString(), totalPrice));
        }
        return saleReportOutput;
    }

}
