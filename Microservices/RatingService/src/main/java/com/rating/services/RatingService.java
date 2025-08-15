package com.rating.services;

import com.rating.entities.Rating;

import java.util.List;

public interface RatingService {

    // Create
    Rating create(Rating rating);

    // Get All reting
    List<Rating> getRatings();

    // Get All by UserId
    List<Rating> getRatingsByUserId(String userId);

    // get All by Hotel
    List<Rating> getRatingsByHotelId(String hotelId);
}
