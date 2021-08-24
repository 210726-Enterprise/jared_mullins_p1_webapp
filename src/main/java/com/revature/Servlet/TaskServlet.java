package com.revature.Servlet;

import com.revature.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Model.Task;
import com.revature.utils.Dao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/tasks")
public class TaskServlet extends HttpServlet {

    /**tas
     * com.revature.Service layer object
     */
    private TaskService service;

    /**
     * Constructor
     */
    public TaskServlet() {
        this.service = new TaskService(new Dao<>(Task.class), new ObjectMapper());
    }

    /**
     * Calls service layer to perform GET logic
     * @param req request object
     * @param resp response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.getTasks(req, resp);
    }

    /**
     * Calls service layer to perform POST logic
     * @param req request object
     * @param resp response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.insertTask(req, resp);
    }

    /**
     * Calls service layer to perform PUT logic
     * @param req request object
     * @param resp response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.updateTask(req, resp);
    }

    /**
     * Calls service layer to perform DELETE logic
     * @param req request object
     * @param resp response object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.deleteTask(req, resp);
    }
}
