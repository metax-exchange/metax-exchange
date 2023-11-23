package org.metax.exchange.okx.dto;

import lombok.Data;
import lombok.ToString;
import org.metax.exchange.core.dto.marketdata.ServerTime;

@Data
@ToString(callSuper = true)
public class OkxServerTime extends ServerTime {

    private long ts;

    public void setTs(long ts) {
        this.ts = ts;
        setServerTime(ts);
    }
}
