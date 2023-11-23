package org.metax.exchange.core.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Currency {

    private String code;

    @Override
    public String toString() {
        return code;
    }
}
