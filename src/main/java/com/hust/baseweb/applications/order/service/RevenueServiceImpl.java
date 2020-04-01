package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RevenueServiceImpl implements RevenueService {

    private ProductRevenueRepo productRevenueRepo;
    private CustomerRevenueRepo customerRevenueRepo;
    private TotalRevenueRepo totalRevenueRepo;
    private ProductPriceRepo productPriceRepo;

    public void updateRevenue(Collection<PartyCustomer> partyCustomers,
                              Map<String, Product> productMap,
                              List<OrderItem> orderItems,
                              Function<OrderItem, PartyCustomer> orderItemToCustomerFunction) {
        LocalDate today = LocalDate.now();
        TotalRevenue totalRevenueToday = totalRevenueRepo.findById(today).orElse(new TotalRevenue(today, 0));

        Map<String, ProductRevenue> productRevenueMap = productRevenueRepo.findAllByIdIn(productMap.keySet()
                .stream()
                .map(productId -> new ProductRevenue.ProductRevenueId(productId, today))
                .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(productRevenue -> productRevenue.getId().getProductId(),
                        productRevenue -> productRevenue));
        Map<UUID, CustomerRevenue> customerRevenueMap = customerRevenueRepo.findAllByIdIn(partyCustomers
                .stream()
                .map(customer -> new CustomerRevenue.CustomerRevenueId(customer.getPartyId(), today))
                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(customerRevenue -> customerRevenue.getId().getCustomerId(),
                        customerRevenue -> customerRevenue));

        Map<String, ProductPrice> productPriceMap = productPriceRepo.findAllByProductInAndThruDateNull(productMap.values())
                .stream()
                .collect(Collectors.toMap(productPrice -> productPrice.getProduct().getProductId(),
                        productPrice -> productPrice));

        for (OrderItem orderItem : orderItems) {
            int quantity = orderItem.getQuantity();
            Product product = orderItem.getProduct();
            PartyCustomer customer = orderItemToCustomerFunction.apply(orderItem);
            ProductPrice productPrice = productPriceMap.get(product.getProductId());
            double revenue = quantity * productPrice.getPrice();

            totalRevenueToday.increase(revenue);
            customerRevenueMap.computeIfAbsent(customer.getPartyId(),
                    k -> new CustomerRevenue(new CustomerRevenue.CustomerRevenueId(customer.getPartyId(), today), 0))
                    .increase(revenue);
            productRevenueMap.computeIfAbsent(product.getProductId(),
                    k -> new ProductRevenue(new ProductRevenue.ProductRevenueId(product.getProductId(), today), 0))
                    .increase(revenue);
        }

        totalRevenueRepo.save(totalRevenueToday);
        customerRevenueRepo.saveAll(customerRevenueMap.values());
        productRevenueRepo.saveAll(productRevenueMap.values());
    }
}
