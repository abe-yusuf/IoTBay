package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;
    
    public Cart() {
        this.items = new ArrayList<>();
    }
    
    public List<CartItem> getItems() {
        return items;
    }
    
    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    public void addItem(CartItem item) {
        items.add(item);
    }
    
    public void removeItem(CartItem item) {
        items.remove(item);
    }
    
    public double getTotalAmount() {
        return items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
    
    public void clear() {
        items.clear();
    }
} 