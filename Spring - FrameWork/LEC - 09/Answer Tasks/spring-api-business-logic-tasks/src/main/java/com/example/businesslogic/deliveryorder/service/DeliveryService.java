package com.example.businesslogic.deliveryorder.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.deliveryorder.dto.AssignDriverRequest;
import com.example.businesslogic.deliveryorder.dto.CreateDeliveryRequest;
import com.example.businesslogic.deliveryorder.dto.DeliveryResponse;
import com.example.businesslogic.deliveryorder.dto.UpdateStatusRequest;
import com.example.businesslogic.deliveryorder.entity.DeliveryCustomer;
import com.example.businesslogic.deliveryorder.entity.DeliveryOrder;
import com.example.businesslogic.deliveryorder.entity.Driver;
import com.example.businesslogic.deliveryorder.enums.DeliveryStatus;
import com.example.businesslogic.deliveryorder.enums.DeliveryVehicleType;
import com.example.businesslogic.deliveryorder.repository.DeliveryCustomerRepository;
import com.example.businesslogic.deliveryorder.repository.DeliveryOrderRepository;
import com.example.businesslogic.deliveryorder.repository.DriverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    // Rule 1: the delivery fee depends on the distance and the package weight
    private static final BigDecimal BASE_FEE = new BigDecimal("10");
    private static final BigDecimal FEE_PER_KM = new BigDecimal("2");
    private static final BigDecimal FEE_PER_KG = new BigDecimal("1.5");

    // Rule 3: orders above this distance require a car or a van
    private static final BigDecimal LONG_DISTANCE_KM = new BigDecimal("20");

    // Rule 5: the statuses that make a driver busy
    private static final List<DeliveryStatus> DRIVER_BUSY_STATUSES =
            List.of(DeliveryStatus.ASSIGNED, DeliveryStatus.PICKED_UP, DeliveryStatus.IN_TRANSIT);

    private final DeliveryOrderRepository orderRepository;
    private final DeliveryCustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public DeliveryService(DeliveryOrderRepository orderRepository,
                           DeliveryCustomerRepository customerRepository,
                           DriverRepository driverRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public DeliveryResponse create(CreateDeliveryRequest request) {

        DeliveryCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // The package must fit at least the biggest vehicle
        BigDecimal biggestLimit = DeliveryVehicleType.VAN.getMaxWeightKg();
        if (request.getWeightKg().compareTo(biggestLimit) > 0) {
            throw new BusinessException("Package is too heavy for any vehicle. Maximum: "
                    + biggestLimit + " kg");
        }

        DeliveryOrder order = new DeliveryOrder();
        order.setCustomer(customer);
        order.setDistanceKm(request.getDistanceKm());
        order.setWeightKg(request.getWeightKg());
        order.setDeliveryFee(calculateFee(request.getDistanceKm(), request.getWeightKg()));
        order.setStatus(DeliveryStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        DeliveryOrder saved = orderRepository.save(order);
        return toResponse(saved, "Delivery order created");
    }

    @Transactional
    public DeliveryResponse assignDriver(Long deliveryId, AssignDriverRequest request) {

        DeliveryOrder order = findOrder(deliveryId);
        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        // Rule 6: only a CREATED order can move to ASSIGNED
        if (!order.getStatus().canTransitionTo(DeliveryStatus.ASSIGNED)) {
            throw new BusinessException(
                    "Only a created order can be assigned to a driver", HttpStatus.CONFLICT);
        }

        // Rule 5: a driver cannot accept two active deliveries at the same time
        if (orderRepository.existsByDriverIdAndStatusIn(driver.getId(), DRIVER_BUSY_STATUSES)) {
            throw new BusinessException(
                    "Driver already has an active delivery", HttpStatus.CONFLICT);
        }

        // Rule 3: orders above a certain distance require a specific vehicle
        if (order.getDistanceKm().compareTo(LONG_DISTANCE_KM) > 0
                && !driver.getVehicleType().isLongDistanceCapable()) {
            throw new BusinessException("Orders above " + LONG_DISTANCE_KM
                    + " km require a car or a van", HttpStatus.CONFLICT);
        }

        // Rule 2: each vehicle type has a maximum weight limit
        BigDecimal maxWeight = driver.getVehicleType().getMaxWeightKg();
        if (order.getWeightKg().compareTo(maxWeight) > 0) {
            throw new BusinessException("Package weight exceeds the limit of the vehicle ("
                    + driver.getVehicleType() + " max " + maxWeight + " kg)", HttpStatus.CONFLICT);
        }

        order.setDriver(driver);
        order.setStatus(DeliveryStatus.ASSIGNED);
        return toResponse(orderRepository.save(order), "Driver assigned");
    }

    @Transactional
    public DeliveryResponse updateStatus(Long deliveryId, UpdateStatusRequest request) {

        DeliveryOrder order = findOrder(deliveryId);
        DeliveryStatus next = request.getStatus();

        if (next == DeliveryStatus.ASSIGNED) {
            throw new BusinessException("Use the assign endpoint to assign a driver");
        }
        if (next == DeliveryStatus.CANCELED) {
            throw new BusinessException("Use the cancel endpoint to cancel an order");
        }

        // Rule 6: the status must follow a valid sequence
        if (!order.getStatus().canTransitionTo(next)) {
            throw new BusinessException("Invalid status transition: "
                    + order.getStatus() + " -> " + next, HttpStatus.CONFLICT);
        }

        order.setStatus(next);
        return toResponse(orderRepository.save(order), "Status updated to " + next);
    }

    // Rule 4: a customer can cancel only before the driver picks up the order
    @Transactional
    public DeliveryResponse cancel(Long deliveryId) {

        DeliveryOrder order = findOrder(deliveryId);

        if (!order.getStatus().canTransitionTo(DeliveryStatus.CANCELED)) {
            throw new BusinessException(
                    "An order can be canceled only before the driver picks it up",
                    HttpStatus.CONFLICT);
        }

        order.setStatus(DeliveryStatus.CANCELED);
        return toResponse(orderRepository.save(order), "Delivery order canceled");
    }

    private BigDecimal calculateFee(BigDecimal distanceKm, BigDecimal weightKg) {
        return BASE_FEE
                .add(distanceKm.multiply(FEE_PER_KM))
                .add(weightKg.multiply(FEE_PER_KG))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private DeliveryOrder findOrder(Long deliveryId) {
        return orderRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery order not found"));
    }

    private DeliveryResponse toResponse(DeliveryOrder order, String message) {
        DeliveryResponse response = new DeliveryResponse();
        response.setDeliveryId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        if (order.getDriver() != null) {
            response.setDriverId(order.getDriver().getId());
            response.setDriverName(order.getDriver().getName());
        }
        response.setDistanceKm(order.getDistanceKm());
        response.setWeightKg(order.getWeightKg());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setMessage(message);
        return response;
    }
}
