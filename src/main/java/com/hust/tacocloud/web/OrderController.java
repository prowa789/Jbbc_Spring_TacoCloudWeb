package com.hust.tacocloud.web;

import javax.validation.Valid;

import com.hust.tacocloud.Order;
import com.hust.tacocloud.data.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderRepository orderRepo;

    @Autowired
    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm(Model model) {
        return "orderForm";
    }
    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            log.info(order.toString());
            return "orderForm";
        }
        // lưu order
        orderRepo.save(order);
        // xóa session thôi
        sessionStatus.setComplete();

        log.info("Order submitted: " + order);
        return "redirect:/design";
    }
}