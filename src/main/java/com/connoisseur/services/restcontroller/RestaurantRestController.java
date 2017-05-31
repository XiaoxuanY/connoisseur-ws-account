package com.connoisseur.services.restcontroller;

import com.connoisseur.services.implementation.RestaurantServiceImplementation;
import com.connoisseur.services.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantRestController {

    private RestaurantServiceImplementation restaurantService;
//    private YelpServiceImplementation yelpService;

    @Autowired
//    public RestaurantRestController(RestaurantServiceImplementation restaurantService, YelpServiceImplementation yelpService) {
    public RestaurantRestController(RestaurantServiceImplementation restaurantService) {
        this.restaurantService = restaurantService;
//        this.yelpService = yelpService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public Restaurant getRestaurantById(@PathVariable("restaurantId") String restaurantId) throws Exception {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @GetMapping("/restaurant/city/{city}")
    public List<Restaurant> getRestaurantsByCity(@PathVariable("city") String city) throws Exception {
        return restaurantService.randomFiveRestaurants(city);
    }

    @GetMapping("/restaurant/city/{city}/label/{label}")
    public List<Restaurant> getRestaurantsByCityLabel(@PathVariable("city") String city, @PathVariable("label") String label)
            throws Exception {
        return restaurantService.randomFiveRestaurants(city, label);
    }

//    @GetMapping("/yelp")
//    public HttpStatus getAccessToken() throws IOException {
//        return yelpService.generateAccessToken();
//    }

}

