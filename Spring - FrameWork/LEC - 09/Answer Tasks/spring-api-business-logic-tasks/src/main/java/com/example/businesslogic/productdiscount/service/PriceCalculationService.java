package com.example.businesslogic.productdiscount.service;

import com.example.businesslogic.common.exception.BusinessException;
import com.example.businesslogic.common.exception.ResourceNotFoundException;
import com.example.businesslogic.productdiscount.dto.PriceItemRequest;
import com.example.businesslogic.productdiscount.dto.PriceItemResponse;
import com.example.businesslogic.productdiscount.dto.PriceRequest;
import com.example.businesslogic.productdiscount.dto.PriceResponse;
import com.example.businesslogic.productdiscount.entity.DiscountCode;
import com.example.businesslogic.productdiscount.entity.DiscountCustomer;
import com.example.businesslogic.productdiscount.entity.DiscountProduct;
import com.example.businesslogic.productdiscount.enums.CustomerType;
import com.example.businesslogic.productdiscount.repository.DiscountCodeRepository;
import com.example.businesslogic.productdiscount.repository.DiscountCustomerRepository;
import com.example.businesslogic.productdiscount.repository.DiscountProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PriceCalculationService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    // Rule 4: the total discount cannot exceed 30%
    private static final BigDecimal MAX_DISCOUNT_PERCENT = new BigDecimal("30");

    // Rule 3: the additional discount for VIP customers
    private static final BigDecimal VIP_EXTRA_DISCOUNT_PERCENT = new BigDecimal("5");

    // Rule 6: free shipping above this amount (after discounts)
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal SHIPPING_FEE = new BigDecimal("50");

    private final DiscountProductRepository productRepository;
    private final DiscountCustomerRepository customerRepository;
    private final DiscountCodeRepository codeRepository;

    public PriceCalculationService(DiscountProductRepository productRepository,
                                   DiscountCustomerRepository customerRepository,
                                   DiscountCodeRepository codeRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.codeRepository = codeRepository;
    }

    @Transactional(readOnly = true)
    public PriceResponse calculate(PriceRequest request) {

        DiscountCustomer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Rule 7: incompatible discount codes cannot be combined
        List<DiscountCode> codes = loadAndValidateCodes(request.getDiscountCodes());

        BigDecimal codesPercent = BigDecimal.ZERO;
        List<String> appliedCodes = new ArrayList<>();
        for (DiscountCode code : codes) {
            codesPercent = codesPercent.add(code.getDiscountPercent());
            appliedCodes.add(code.getCode());
        }

        BigDecimal vipPercent = customer.getType() == CustomerType.VIP
                ? VIP_EXTRA_DISCOUNT_PERCENT
                : BigDecimal.ZERO;

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<PriceItemResponse> itemResponses = new ArrayList<>();

        for (PriceItemRequest item : request.getItems()) {

            DiscountProduct product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + item.getProductId()));

            BigDecimal lineSubtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            BigDecimal percent = BigDecimal.ZERO;
            boolean capped = false;

            // Rule 5: some products are not eligible for discounts
            if (product.isDiscountEligible()) {
                // Rule 1 and 2: the category discount + VIP + discount codes
                BigDecimal rawPercent = product.getCategory().getDiscountPercent()
                        .add(vipPercent)
                        .add(codesPercent);

                // Rule 4: the total discount cannot exceed 30%
                if (rawPercent.compareTo(MAX_DISCOUNT_PERCENT) > 0) {
                    percent = MAX_DISCOUNT_PERCENT;
                    capped = true;
                } else {
                    percent = rawPercent;
                }
            }

            BigDecimal discountAmount = lineSubtotal
                    .multiply(percent)
                    .divide(HUNDRED, 2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = lineSubtotal.subtract(discountAmount);

            PriceItemResponse itemResponse = new PriceItemResponse();
            itemResponse.setProductId(product.getId());
            itemResponse.setProductName(product.getName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(product.getPrice());
            itemResponse.setLineSubtotal(lineSubtotal);
            itemResponse.setDiscountPercent(percent);
            itemResponse.setDiscountCapped(capped);
            itemResponse.setDiscountAmount(discountAmount);
            itemResponse.setLineTotal(lineTotal);
            itemResponses.add(itemResponse);

            subtotal = subtotal.add(lineSubtotal);
            totalDiscount = totalDiscount.add(discountAmount);
        }

        BigDecimal amountAfterDiscount = subtotal.subtract(totalDiscount);

        // Rule 6: free shipping when the order total exceeds the threshold
        boolean freeShipping = amountAfterDiscount.compareTo(FREE_SHIPPING_THRESHOLD) > 0;
        BigDecimal shippingFee = freeShipping ? BigDecimal.ZERO : SHIPPING_FEE;

        PriceResponse response = new PriceResponse();
        response.setCustomerId(customer.getId());
        response.setCustomerType(customer.getType());
        response.setAppliedCodes(appliedCodes);
        response.setItems(itemResponses);
        response.setSubtotal(subtotal);
        response.setTotalDiscount(totalDiscount);
        response.setAmountAfterDiscount(amountAfterDiscount);
        response.setShippingFee(shippingFee);
        response.setFreeShipping(freeShipping);
        response.setFinalTotal(amountAfterDiscount.add(shippingFee));
        return response;
    }

    private List<DiscountCode> loadAndValidateCodes(List<String> requestedCodes) {
        List<DiscountCode> codes = new ArrayList<>();
        if (requestedCodes == null) {
            return codes;
        }

        Set<String> seen = new HashSet<>();
        for (String raw : requestedCodes) {
            String value = raw.trim().toUpperCase();
            if (!seen.add(value)) {
                throw new BusinessException("Discount code is repeated: " + value);
            }
            DiscountCode code = codeRepository.findByCode(value)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Discount code not found: " + value));
            codes.add(code);
        }

        // Two codes in the same exclusive group are incompatible
        for (int i = 0; i < codes.size(); i++) {
            for (int j = i + 1; j < codes.size(); j++) {
                DiscountCode first = codes.get(i);
                DiscountCode second = codes.get(j);
                if (first.getExclusiveGroup() != null
                        && first.getExclusiveGroup().equals(second.getExclusiveGroup())) {
                    throw new BusinessException("Discount codes " + first.getCode()
                            + " and " + second.getCode() + " cannot be combined",
                            HttpStatus.CONFLICT);
                }
            }
        }
        return codes;
    }
}
