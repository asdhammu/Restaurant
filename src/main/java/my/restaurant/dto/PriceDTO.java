package my.restaurant.dto;

import my.restaurant.utils.RestaurantUtils;

import java.text.DecimalFormat;

public record PriceDTO(float price, CurrencyDTO currency) {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public PriceDTO(float price) {
        this(Float.parseFloat(decimalFormat.format(price)), RestaurantUtils.getDefaultCurrency());
    }

    @Override
    public String toString() {
        return String.format("%s%s", currency.code(), price);
    }
}
