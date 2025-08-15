package com.main.user.services.controlers;

import com.main.user.services.config.MyConfig;
import com.main.user.services.entities.User;
import com.main.user.services.services.UserService;
import com.main.user.services.services.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    // create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("hey i'm here");
        User user1 = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount = 1;
    // Single user get
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallBack") // is for service dependencies userService->ratingService->hotelService
    @Retry(name = "ratingHotelService",fallbackMethod = "retringHotelFallBake")
    public ResponseEntity<User> getSingleUser(@PathVariable("userId") String userId) {
        logger.info("Getting single user Handler: UserController");
        logger.info("Retry coutn : {}", retryCount);
        retryCount++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }


    public ResponseEntity<User> retringHotelFallBake(String userId, Exception ex) {
        //logger.info("FallBack is executed because of service is down", ex.getMessage());

        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("Dummy")
                .about("Dummy about informations dummy because service is down")
                .userId("3409583")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /* Creating fall back mathod for circuitbreaker
    public ResponseEntity<User> ratingHotelFallBack(String userId, Exception ex) {
        logger.info("FallBack is executed because of service is down", ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("Dummy")
                .about("Dummy about informations dummy because service is down")
                .userId("3409583")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/

    // Get all user
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }


    public void deleteUser(@PathVariable("userId") String userId) {

    }

}
