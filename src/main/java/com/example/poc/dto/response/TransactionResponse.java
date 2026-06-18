package com.example.poc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @JsonProperty("transactionId")
    private UUID transactionId;

    @JsonProperty("srcAccNo")
    private String srcAccNo;

    @JsonProperty("destAccNo")
    private String destAccNo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("netBalance")
    private BigDecimal netBalance;

    // Epoch seconds (with millis as fraction) so transactions can be ordered by time.
    @JsonProperty("timeStamp")
    private Double timeStamp;
}
