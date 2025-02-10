package az.edu.eshopcustomer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Currency {
    AZN(0), USD(1), EURO(2);

    private Integer value;

    public static boolean isValidCurrency(Integer value) {
        for (Currency currency : Currency.values()) {
            if (currency.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
