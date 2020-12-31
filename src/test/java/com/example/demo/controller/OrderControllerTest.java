package com.example.demo.controller;

import com.example.demo.DummyTestData;
import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.DummyTestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_order() {
        User user = getDummyUser();
        Cart cart = user.getCart();
        cart.setItems(getDummyItems());
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("ravikr42")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("ravikr42");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals("ravikr42", order.getUser().getUsername());
    }

    @Test
    public void submit_order_with_nonexistent_user() {
        ResponseEntity<UserOrder> response = orderController.submit("ravikr42");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }


    @Test
    public void order_history() {
        User user = getDummyUser();
        Cart cart = user.getCart();
        cart.setItems(getDummyItems());
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("ravikr42")).thenReturn(user);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ravikr42");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(0, orders.size());
    }

    @Test
    public void order_history_with_nonexistent_user() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ravikr42");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }


}
