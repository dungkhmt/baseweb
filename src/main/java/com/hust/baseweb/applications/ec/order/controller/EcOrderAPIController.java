package com.hust.baseweb.applications.ec.order.controller;

import com.hust.baseweb.applications.ec.order.model.CreateOrderModel;
import com.hust.baseweb.applications.ec.order.model.OrderResult;
import com.hust.baseweb.applications.ec.order.model.UpdateOrderInput;
import com.hust.baseweb.applications.ec.order.service.EcOrderService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EcOrderAPIController {
    private EcOrderService ecOrderService;
    private UserService userService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(
        Principal principal,
        @RequestBody List<CreateOrderModel> input
    ) {
        UserLogin u = userService.findById(principal.getName());
        ecOrderService.save(u, input);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderResult>> getAllOrders(
        Principal principal
    ) {
        UserLogin u = userService.findById(principal.getName());
        List<OrderResult> orderResultList = ecOrderService.getAllOrders(u);

        return ResponseEntity.ok().body(orderResultList);
    }

    @PutMapping("/update-order/{orderId}")
    public ResponseEntity<String> updateOrder(
        @PathVariable String orderId,
        @RequestBody UpdateOrderInput input
    ) {
        ecOrderService.update(orderId, input);

        return ResponseEntity.ok().build();
    }
}
