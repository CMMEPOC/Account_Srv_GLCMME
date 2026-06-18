package com.example.poc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountStatementResponse {

    @JsonProperty("currentBalance")
    private BigDecimal currentBalance;

    @JsonProperty("sortedByTimeTransactions")
    private List<TransactionResponse> sortedByTimeTransactions;
}
