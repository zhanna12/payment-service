package kz.iitu.pharm.paymentservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;
}