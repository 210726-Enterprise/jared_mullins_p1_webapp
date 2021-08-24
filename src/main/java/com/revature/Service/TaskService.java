package com.revature.Service;

import com.revature.Model.Task;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import com.revature.utils.DaoManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskService {
    /**
     * Logger
     */
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    /**
     * Dao object from the ORM
     */
    private Dao<Task> dao;
    /**
     * Object mapper for parsing JSON
     */
    private ObjectMapper mapper;
    /**
     * DB connection source
     */
    private ConnectionSource conn = new ConnectionSource();

    /**
     * Constructor
     * @param dao ORM dao
     * @param mapper Object mapper
     */
    public TaskService(Dao<Task> dao, ObjectMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
        DaoManager.addDao(dao);
    }

    /**
     * Entry point for doGet() in servlet class
     * Gets either all or a single task
     * @param req HttpRequestObject
     * @param resp HttpResponseObject
     */
    public void getTasks(HttpServletRequest req, HttpServletResponse resp) {
        if(req.getParameter("taskId") != null) {
            if(req.getParameter("taskId").equals("")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                Task task = getTask(Integer.parseInt(req.getParameter("taskId")));
                System.out.println(task);
                String json = mapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(task);
                if(json.equals("null")) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                resp.getOutputStream().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (IOException e) {
                logger.warn("Could not find tasks", e);
            }
        } else {
            try {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getAllTasks());
                resp.getOutputStream().print(json);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Entry point for doPost servlet method
     * @param req Request object
     * @param resp Response object
     * @return Task that was inserted
     */
    public Task insertTask(HttpServletRequest req, HttpServletResponse resp) {
        Task result = null;
        try {
            StringBuilder builder = new StringBuilder();
            req.getReader()
                    .lines()
                    .collect(Collectors.toList())
                    .forEach(builder::append);
            Task task = mapper.readValue(builder.toString(), Task.class);
            result = insertTask(task);
        } catch (IOException e) {
            logger.warn("Could not insert task", e);
        }
        if(result != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        return result;
    }

    /**
     * Entry point for doPut servlet method
     * @param req Request object
     * @param resp response object
     * @return Task object with the updates
     */
    public Task updateTask(HttpServletRequest req, HttpServletResponse resp) {
        Task result = null;
        StringBuilder builder = new StringBuilder();
        try {
            req.getReader()
                    .lines()
                    .collect(Collectors.toList())
                    .forEach(builder::append);
            Task task = mapper.readValue(builder.toString(), Task.class);
            if(task.getId() != 0) {
                result = updateTask(task);
                if(result != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } catch (IOException e) {
            logger.warn("Could not update task", e);
        }
        return result;
    }

    /**
     * Entry point for doDelete servlet method
     * @param req request object
     * @param resp response object
     */
    public void deleteTask(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("taskId") != null) {
            if (req.getParameter("taskId").equals("")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            boolean success = deleteTask(Integer.parseInt(req.getParameter("taskId")));
            if(success) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Calls ORM to get all tasks
     * @return List of all tasks
     */
    protected List<Task> getAllTasks() {
        return dao.getAll(conn);
    }

    /**
     * Calls ORM to get a single task
     * @param id id of task being retrieved
     * @return retrieved task object
     */
    protected Task getTask(int id) {
        return dao.getById(conn, id);
    }

    /**
     * Calls ORM to create a new task
     * @param task task object to be inserted into table
     * @return newly created task object
     */
    protected Task insertTask(Task task) {
        return dao.insert(conn, task);
    }

    /**
     * Calls ORM to update task
     * @param task task object to be updated
     * @return newly updated task object
     */
    protected Task updateTask(Task task) {
        return dao.updateById(conn, task.getId(), task);
    }

    /**
     * Calls ORM to delete object with given id
     * @param id of object to be deleted
     * @return true if deleted; false otherwise
     */
    protected boolean deleteTask(int id) {
        return dao.deleteById(conn, id);
    }



}
