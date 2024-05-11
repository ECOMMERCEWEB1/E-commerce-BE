package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.OrderItemDTO;
import com.webproject.ecommerce.entities.OrderItem;
import com.webproject.ecommerce.repositories.OrderItemRepository;
import com.webproject.ecommerce.services.OrderItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.webproject.ecommerce.entities.OrderItem}.
 */
@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final Logger log = LoggerFactory.getLogger(OrderItemController.class);

    private final OrderItemService orderItemService;

    private final OrderItemRepository orderItemRepository;

    public OrderItemController(OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

}

