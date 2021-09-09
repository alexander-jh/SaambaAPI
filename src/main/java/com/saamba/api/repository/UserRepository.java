package com.saamba.api.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.saamba.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper mapper;

    public User addUser(User user) {
        mapper.save(user);
        return user;
    }

    public User findUserByAccount(String account) {
        return mapper.load(User.class, account);
    }

    public String deleteUser(User user) {
        mapper.delete(user);
        return "user removed !!";
    }

    public String editUser(User user) {
        mapper.save(user, buildExpression(user));
        return "record updated ...";
    }

    private DynamoDBSaveExpression buildExpression(User user) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedMap = new HashMap<>();
        expectedMap.put("accountName", new ExpectedAttributeValue(new AttributeValue().withS(user.getAccountName())));
        dynamoDBSaveExpression.setExpected(expectedMap);
        return dynamoDBSaveExpression;
    }


}
