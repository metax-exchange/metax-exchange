package org.metax.exchange.core.webapi;

import org.metax.exchange.core.currency.CurrencyPair;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public interface ApiService {

    Object ping(Object... args) throws IOException;

    Object time(Object... args) throws IOException;

    List<CurrencyPair> getCurrencyPairList(Object... args) throws IOException;

    <T> T execute(Call<?> call) throws IOException;


}
