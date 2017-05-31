package com.connoisseur.services.service;

import com.connoisseur.services.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant getRestaurantById(String restaurantId);

    List<Restaurant> randomFiveRestaurants(String city);

    List<Restaurant> randomFiveRestaurants(String city, String label);

    Restaurant updateRestaurantById(String restaurantId);

}
