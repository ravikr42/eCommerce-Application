package com.example.demo.controller;

import com.example.demo.DummyTestData;
import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demo.DummyTestData.getDummyItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_item_by_id() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(getDummyItem()));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Item foundItem = response.getBody();
        assertNotNull(foundItem);
        assertEquals("DummyItem", foundItem.getName());
        assertEquals("This is a Dummy Item", foundItem.getDescription());
        assertEquals(BigDecimal.valueOf(2.59), foundItem.getPrice());
    }

    @Test
    public void get_item_by_invalidId() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void get_all_items() {
        when(itemRepository.findAll()).thenReturn(DummyTestData.getDummyItems());
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void get_item_by_name() {
        when(itemRepository.findByName("DummyItem")).thenReturn(DummyTestData.getDummyItems());
        ResponseEntity<List<Item>> response = itemController.getItemsByName("DummyItem");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("DummyItem", response.getBody().get(0).getName());
        assertEquals(DummyTestData.getDummyItems().get(0), response.getBody().get(0));
    }

    @Test
    public void get_item_by_invalid_name() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("DummyItem");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }


}
