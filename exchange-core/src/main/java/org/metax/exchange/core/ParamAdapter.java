package org.metax.exchange.core;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

public class ParamAdapter {

    public static Map toMap(ExchangeSpecification exchangeSpecification) {
        Map map = Maps.newHashMap();
        if (Objects.nonNull(exchangeSpecification.getAccount())) {
            map.putAll(BeanMap.create(exchangeSpecification.getAccount()));
        }
        if (Objects.nonNull(exchangeSpecification.getApiSecret())) {
            map.putAll(BeanMap.create(exchangeSpecification.getApiSecret()));
        }
        if (!CollectionUtils.isEmpty(exchangeSpecification.getParameterMap())) {
            map.putAll(exchangeSpecification.getParameterMap());
        }
        return map;
    }

    public static String[] splitInstId(String instId) {
        String[] res = null;
        if (instId.contains("/")) {
            res = instId.split("/");
        } else if (instId.contains("-")) {
            res = instId.split("-");
        } else if (instId.contains("_")) {
            res = instId.split("_");
        }
        Assert.isTrue(Objects.nonNull(res) && res.length >= 2, "Invalid instId.");
        return res;
    }

}
