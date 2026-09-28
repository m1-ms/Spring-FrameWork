package com.example.businesslogic.ecommerce.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.ecommerce.dto.CreateOrderRequest;
import com.example.businesslogic.ecommerce.dto.OrderItemRequest;
import com.example.businesslogic.ecommerce.dto.OrderItemResponse;
import com.example.businesslogic.ecommerce.dto.OrderResponse;
import com.example.businesslogic.ecommerce.dto.UpdateOrderRequest;
import com.example.businesslogic.ecommerce.entity.EcomCustomer;
import com.example.businesslogic.ecommerce.entity.EcomOrder;
import com.example.businesslogic.ecommerce.entity.EcomOrderItem;
import com.example.businesslogic.ecommerce.entity.EcomProduct;
import com.example.businesslogic.ecommerce.enums.OrderStatus;
import com.example.businesslogic.ecommerce.enums.PaymentStatus;
import com.example.businesslogic.ecommerce.repository.EcomCustomerRepository;
import com.example.businesslogic.ecommerce.repository.EcomOrderRepository;
import com.example.businesslogic.ecommerce.repository.EcomProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EcomOrderService {

    // Orders above this subtotal get free shipping
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal SHIPPING_FEE = new BigDecimal("50");

    private final EcomProductRepository productRepository;
    private final EcomCustomerRepository customerRepository;
    private final EcomOrderRepository orderRepository;

    public EcomOrderService(EcomProductRepository productRepository,
                            EcomCustomerRepository customerRepository,
                            EcomOrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        EcomCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Rule 5: a customer with an unpaid order cannot create a new one
        if (orderRepository.existsByCustomerIdAndPaymentStatus(customer.getId(), PaymentStatus.UNPAID)) {
            throw new BusinessException(
                    "Customer has unpaid orders and cannot create a new order", HttpStatus.CONFLICT);
        }

        EcomOrder order = new EcomOrder();
        order.setCustomer(customer);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;
        // Tracks the total quantity per product in case the same product appears twice
        Map<Long, Integer> requestedQuantities = new HashMap<>();

        for (OrderItemRequest itemRequest : request.getItems()) {

            EcomProduct product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + itemRequest.getProductId()));

            // Rule 1: only active products can be ordered
            if (!product.isActive()) {
                throw new BusinessException("Product is not active: " + product.getName());
            }

            // Rule 2: requested quantity cannot exceed the available stock
            int totalRequested = requestedQuantities.merge(
                    product.getId(), itemRequest.getQuantity(), Integer::sum);
            if (totalRequested > product.getStock()) {
                throw new BusinessException(
                        "Requested quantity exceeds available stock for product: " + product.getName(),
                        HttpStatus.CONFLICT);
            }

            // Rule 3: the price always comes from the current product price
            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            EcomOrderItem item = new EcomOrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);
            order.getItems().add(item);

            subtotal = subtotal.add(lineTotal);
        }

        // Rule 4: free shipping above the threshold
        BigDecimal shippingFee = subtotal.compareTo(FREE_SHIPPING_THRESHOLD) > 0
                ? BigDecimal.ZERO
                : SHIPPING_FEE;

        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setTotalAmount(subtotal.add(shippingFee));

        EcomOrder saved = orderRepository.save(order);

        // Rule 6: stock is reduced only after the order was created successfully
        for (EcomOrderItem item : saved.getItems()) {
            EcomProduct product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        return toResponse(saved, "Order created successfully");
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        EcomOrder order = findOrder(orderId);

        // Rule 7: a confirmed order cannot be modified
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException("Confirmed order cannot be modified", HttpStatus.CONFLICT);
        }

        order.setShippingAddress(request.getShippingAddress());
        return toResponse(orderRepository.save(order), "Order updated successfully");
    }

    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        EcomOrder order = findOrder(orderId);

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException("Order is already confirmed", HttpStatus.CONFLICT);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        return toResponse(orderRepository.save(order), "Order confirmed");
    }

    @Transactional
    public OrderResponse payOrder(Long orderId) {
        EcomOrder order = findOrder(orderId);

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Order is already paid", HttpStatus.CONFLICT);
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        return toResponse(orderRepository.save(order), "Order paid successfully");
    }

    private EcomOrder findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private OrderResponse toResponse(EcomOrder order, String message) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (EcomOrderItem item : order.getItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());
            itemResponse.setLineTotal(item.getLineTotal());
            itemResponses.add(itemResponse);
        }

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setShippingAddress(order.getShippingAddress());
        response.setItems(itemResponses);
        response.setSubtotal(order.getSubtotal());
        response.setShippingFee(order.getShippingFee());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setMessage(message);
        return response;
    }
}