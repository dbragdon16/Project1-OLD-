package com.revature.ticketer.utils;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ticketer.TicketStatusService.TicketService;
import com.revature.ticketer.TicketStatusService.UserService;
import com.revature.ticketer.daos.TicketDAO;
import com.revature.ticketer.daos.UserDAO;
import com.revature.ticketer.handlers.TicketHandler;
import com.revature.ticketer.handlers.UserHandler;

import io.javalin.Javalin;

/*
 * The purpose of the Router is to map endpoints.
 */
public class Router {

    public static void router(Javalin app){
        ObjectMapper mapper = new ObjectMapper();

        /*
         * Hierarchy of dependency injection
         * UserDAO > UserService > UserHandler
         */
        //Users
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);
        UserHandler userHandler = new UserHandler(userService, mapper);

        //Tickets
        TicketDAO ticketDAO = new TicketDAO();
        TicketService ticketService = new TicketService(ticketDAO);
        TicketHandler ticketHandler = new TicketHandler(ticketService);

        //User Handler Group
        //Goes Routes, userHandler, userService, userDAO
        app.routes(() -> {
            //User
            path("/users", () -> {
                //Post takes in a context which points to a function body
                //Turns a function into a variable
                post(c -> userHandler.signup(c));
            });

            //Auth
        });
    }
}
