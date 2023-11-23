package org.metax.exchange.okx.webapi;

import lombok.Data;

import java.util.List;

@Data
public class OkxApiResponse<T> {

    private int code;

    private String msg;

    private List<T> data;
}
