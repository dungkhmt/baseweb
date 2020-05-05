package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.repo.ProductPriceRepo;
import com.hust.baseweb.applications.order.document.aggregation.CustomerRevenue;
import com.hust.baseweb.applications.order.document.aggregation.ProductRevenue;
import com.hust.baseweb.applications.order.document.aggregation.TotalRevenue;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.repo.mongodb.CustomerRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.ProductRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.TotalRevenueRepo;
import com.hust.baseweb.entity.Party;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class RevenueServiceImpl implements RevenueService {

    private ProductRevenueRepo productRevenueRepo;
    private CustomerRevenueRepo customerRevenueRepo;
    private TotalRevenueRepo totalRevenueRepo;
    private ProductPriceRepo productPriceRepo;
    /*
    public void updateRevenue(List<OrderItem> orderItems,
                              Function<OrderItem, PartyCustomer> orderItemToCustomerFunction,
                              Function<OrderItem, LocalDate> orderItemToDateFunction) {

        Map<LocalDate, TotalRevenue> totalRevenueMap = getTotalRevenueMap(orderItems, orderItemToDateFunction);

        Map<ProductRevenue.Id, ProductRevenue> productRevenueMap = getProductRevenueMap(orderItems,
                orderItemToDateFunction);

        Map<CustomerRevenue.Id, CustomerRevenue> customerRevenueMap = getCustomerRevenueMap(orderItems,
                orderItemToCustomerFunction,
                orderItemToDateFunction);

        List<Product> products = orderItems.stream().map(OrderItem::getProduct).distinct().collect(Collectors.toList());

        Map<String, ProductPrice> productPriceMap = getProductPriceMap(products);

        for (OrderItem orderItem : orderItems) {
            int quantity = orderItem.getQuantity();
            Product product = orderItem.getProduct();
            PartyCustomer customer = orderItemToCustomerFunction.apply(orderItem);
            ProductPrice productPrice = productPriceMap.get(product.getProductId());
            double revenue = quantity * productPrice.getPrice();
            LocalDate date = orderItemToDateFunction.apply(orderItem);

            totalRevenueMap.get(date).increase(revenue);

            customerRevenueMap.computeIfAbsent(new CustomerRevenue.Id(customer.getPartyId(), date),
                    id -> new CustomerRevenue(id, 0.0))
                    .increase(revenue);

            productRevenueMap.computeIfAbsent(new ProductRevenue.Id(product.getProductId(), date),
                    id -> new ProductRevenue(id, 0.0))
                    .increase(revenue);
        }

        totalRevenueRepo.saveAll(totalRevenueMap.values());
        customerRevenueRepo.saveAll(customerRevenueMap.values());
        productRevenueRepo.saveAll(productRevenueMap.values());
    }
    */
    public void updateRevenue(List<OrderItem> orderItems,
                              Function<OrderItem, Party> orderItemToCustomerFunction,
                              Function<OrderItem, LocalDate> orderItemToDateFunction) {

        Map<LocalDate, TotalRevenue> totalRevenueMap = getTotalRevenueMap(orderItems, orderItemToDateFunction);

        Map<ProductRevenue.Id, ProductRevenue> productRevenueMap = getProductRevenueMap(orderItems,
                orderItemToDateFunction);

        Map<CustomerRevenue.Id, CustomerRevenue> customerRevenueMap = getCustomerRevenueMap(orderItems,
                orderItemToCustomerFunction,
                orderItemToDateFunction);

        List<Product> products = orderItems.stream().map(OrderItem::getProduct).distinct().collect(Collectors.toList());

        Map<String, ProductPrice> productPriceMap = getProductPriceMap(products);

        for (OrderItem orderItem : orderItems) {
            int quantity = orderItem.getQuantity();
            Product product = orderItem.getProduct();
            //PartyCustomer customer = orderItemToCustomerFunction.apply(orderItem);
            Party customer = orderItemToCustomerFunction.apply(orderItem);
            ProductPrice productPrice = productPriceMap.get(product.getProductId());
            double revenue = 0;
            if(productPrice != null)
                revenue = quantity * productPrice.getPrice();
            LocalDate date = orderItemToDateFunction.apply(orderItem);

            totalRevenueMap.get(date).increase(revenue);

            customerRevenueMap.computeIfAbsent(new CustomerRevenue.Id(customer.getPartyId(), date),
                    id -> new CustomerRevenue(id, 0.0))
                    .increase(revenue);

            productRevenueMap.computeIfAbsent(new ProductRevenue.Id(product.getProductId(), date),
                    id -> new ProductRevenue(id, 0.0))
                    .increase(revenue);
        }

        totalRevenueRepo.saveAll(totalRevenueMap.values());
        customerRevenueRepo.saveAll(customerRevenueMap.values());
        productRevenueRepo.saveAll(productRevenueMap.values());
    }

    /*
    @NotNull
    private Map<CustomerRevenue.Id, CustomerRevenue> getCustomerRevenueMap(List<OrderItem> orderItems,
                                                                           Function<OrderItem, PartyCustomer> orderItemToCustomerFunction,
                                                                           Function<OrderItem, LocalDate> orderItemToDateFunction) {
        return customerRevenueRepo.findAllByIdIn(
                orderItems.stream()
                        .map(orderItem -> new CustomerRevenue.Id(orderItemToCustomerFunction.apply(
                                orderItem).getPartyId(), orderItemToDateFunction.apply(orderItem)))
                        .distinct()
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(CustomerRevenue::getId, customerRevenue -> customerRevenue));
    }
    */
    @NotNull
    private Map<CustomerRevenue.Id, CustomerRevenue> getCustomerRevenueMap(List<OrderItem> orderItems,
                                                                           Function<OrderItem, Party> orderItemToCustomerFunction,
                                                                           Function<OrderItem, LocalDate> orderItemToDateFunction) {
        return customerRevenueRepo.findAllByIdIn(
                orderItems.stream()
                        .map(orderItem -> new CustomerRevenue.Id(orderItemToCustomerFunction.apply(
                                orderItem).getPartyId(), orderItemToDateFunction.apply(orderItem)))
                        .distinct()
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(CustomerRevenue::getId, customerRevenue -> customerRevenue));
    }

    @NotNull
    private Map<ProductRevenue.Id, ProductRevenue> getProductRevenueMap(List<OrderItem> orderItems,
                                                                        Function<OrderItem, LocalDate> orderItemToDateFunction) {
        return productRevenueRepo.findAllByIdIn(
                orderItems.stream()
                        .map(orderItem -> new ProductRevenue.Id(orderItem.getProduct().getProductId(),
                                orderItemToDateFunction.apply(orderItem)))
                        .distinct()
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(ProductRevenue::getId, productRevenue -> productRevenue));
    }

    @NotNull
    private Map<LocalDate, TotalRevenue> getTotalRevenueMap(List<OrderItem> orderItems,
                                                            Function<OrderItem, LocalDate> orderItemToDateFunction) {
        List<LocalDate> localDates = orderItems.stream()
                .map(orderItemToDateFunction)
                .distinct()
                .collect(Collectors.toList());

        Map<LocalDate, TotalRevenue> totalRevenueMap = totalRevenueRepo.findAllByIdIn(localDates)
                .stream()
                .collect(Collectors.toMap(TotalRevenue::getId, totalRevenue -> totalRevenue));
        for (LocalDate localDate : localDates) {
            totalRevenueMap.computeIfAbsent(localDate, k -> new TotalRevenue(localDate, 0));
        }
        return totalRevenueMap;
    }

    @NotNull
    private Map<String, ProductPrice> getProductPriceMap(List<Product> products) {
        return productPriceRepo.findAllByProductInAndThruDateNull(products)
                .stream()
                .collect(Collectors.toMap(
                        productPrice -> productPrice.getProduct().getProductId(),
                        productPrice -> productPrice));
    }

}
