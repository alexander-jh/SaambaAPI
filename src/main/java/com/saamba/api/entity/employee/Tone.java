package com.saamba.api.entity.employee;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class Tone {

    @DynamoDBAttribute(attributeName = "tone")
    private String tone;

    @DynamoDBAttribute(attributeName = "weight")
    private double weight;

}
