package org.bank.managementsystem.repository;

import java.math.BigDecimal;

public interface CurrencyTotalView {

    String getCurrency();

    BigDecimal getTotal();
}