package com.saamba.api.entity.employee;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Employee")
public class Employee {

    @DynamoDBHashKey(attributeName = "employeeId")
    private String employeeId;

    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "harassmentLikelihood")
    private double harassmentLikelihood;

    @DynamoDBAttribute(attributeName = "theftLikelihood")
    private double theftLikelihood;

    @DynamoDBAttribute(attributeName = "disgruntledLikelihood")
    private double disgruntledLikelihood;

    @DynamoDBAttribute(attributeName = "documentList")
    private List<Document> documentList;

    @DynamoDBAttribute(attributeName = "toneList")
    private List<Document> toneList;
}
