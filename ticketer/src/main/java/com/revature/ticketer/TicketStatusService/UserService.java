package com.revature.ticketer.TicketStatusService;

import java.util.List;
import java.util.UUID;

import com.revature.ticketer.Exceptions.InvalidUserException;
import com.revature.ticketer.daos.UserDAO;
import com.revature.ticketer.dtos.requests.NewUserRequest;
import com.revature.ticketer.models.User;

/*
 * Used to validate and retrieve data from the DAO
 * Service class is essentially an API
 * Service class is an example of Dependency Injection
 * since it relies on UserDAO to function
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /*
     * A DTO is sent instead of a POJO since there are some things you don't want
     * the user to send. Helps limit user control
     */
    public void saveUser(NewUserRequest request){
        List<String> usernames = userDAO.findAllUsernames();

        if(!isValidUsername(request.getUsername())) throw new InvalidUserException("ERROR: Username must be 8-20 characters long");
        if(usernames.contains(request.getUsername())) throw new InvalidUserException("ERROR: Username already exists");
        if(!isValidPassword(request.getPassword1())) throw new InvalidUserException("ERROR: Passwords must be a minimum of 8 " + 
        "characters, with at least one letter, one number, and one special character");
        if(!request.getPassword1().equals(request.getPassword2())) throw new InvalidUserException("ERROR: Passwords do not match");
        if(!isValidEmail(request.getEmail())) throw new InvalidUserException("ERROR: Invalid email");

        //Creates a new user using the DTO request UUID.randomUUID generates a random id for user
        //User will be given the Default role of Employee, and isActive
        User createdUser = new User(UUID.randomUUID().toString(), request.getUsername(), request.getEmail(),
            request.getPassword1(), request.getGivenName(), request.getSurname(), true , "DEFAULT");
        userDAO.save(createdUser);
    }

    //Checks to see if the username is valid
    private boolean isValidUsername(String username) {
        //Checks if the username is 8-20 are removes a lot of special characters
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    //Checks to see if the password is valid
    private boolean isValidPassword(String password) {
        //Checks to see if password is valid. Minimum of 8 characters, 1 letter, 1 num, 1 special char
        //added an extra \ before the d's because of a compiler issue
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    //Checks to see if the email is valid
    private boolean isValidEmail(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
