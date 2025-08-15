package com.main.user.services.services.impl;

import com.main.user.services.entities.Hotel;
import com.main.user.services.entities.Rating;
import com.main.user.services.entities.User;
import com.main.user.services.exceptions.ResourceNotFoundException;
import com.main.user.services.external.services.HotelService;
import com.main.user.services.repositories.UserRepository;
import com.main.user.services.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        // Get user from database with the help of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on server !! " + userId));
        // Fetch reating of the above user from RATING SERVICE
        // url : http://localhost:8083/ratings/users/d703facf-92a6-4494-8954-b148b601aa41
        Rating[] ratingOfUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        logger.info("{} ", ratingOfUser);

        //ArrayList<Rating> ratings = (ArrayList<Rating>) Arrays.stream(ratingOfUser).toList();
        ArrayList<Rating> ratings = new ArrayList<>(List.of(ratingOfUser));
        List<Rating> ratingList = ratings.stream().map(rating -> {

            // API call to hotel service to get the hotel
            // http://localhost:8082/hotels


            //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            //Hotel hotel = forEntity.getBody();
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            //logger.info("Response status code", forEntity.getStatusCode());

            // Set the hotel to rating
            rating.setHotel(hotel);
            // Return the Rating
            return rating;

        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }

    @Override
    public boolean deleteUser(String userId) {
        return false;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
