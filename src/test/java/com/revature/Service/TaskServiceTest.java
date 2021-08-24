package com.revature.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.Model.Task;
import com.revature.utils.ConnectionSource;
import com.revature.utils.Dao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class TaskServiceTest {

    TaskService service;
    Dao<Task> taskDaoMock;
    ObjectMapper mapperMock;
    ObjectWriter writerMock;
    HttpServletRequest reqMock;
    HttpServletResponse resMock;
    ServletOutputStream outputStreamMock;
    ArrayList<Task> taskList;
    String json;
    ConnectionSource connectionSource;
    Task task;

    @Before
    public void setup() {
        taskDaoMock = Mockito.mock(Dao.class);
        mapperMock = Mockito.mock(ObjectMapper.class);
        writerMock = Mockito.mock(ObjectWriter.class);
        reqMock = Mockito.mock(HttpServletRequest.class);
        resMock = Mockito.mock(HttpServletResponse.class);
        outputStreamMock = Mockito.mock(ServletOutputStream.class);
        connectionSource = Mockito.mock(ConnectionSource.class);
        task = Mockito.mock(Task.class);

        service = new TaskService(taskDaoMock, mapperMock);

        taskList = new ArrayList<>();
        json = "json";
    }

    @Test
    public void insertTask() throws IOException {
        Task mockTask = new Task();
        BufferedReader mockBuff = Mockito.mock(BufferedReader.class);
        String s1 = "String one";
        Stream<String> stringStream = Arrays.stream(new String[]{s1});
        when(reqMock.getReader()).thenReturn(mockBuff);
        when(reqMock.getReader().lines()).thenReturn(stringStream);
        when(mapperMock.readValue(json, Task.class)).thenReturn(mockTask);
        when(mapperMock.readValue(s1, Task.class)).thenReturn(mockTask);
        when(service.insertTask(mockTask)).thenReturn(task);

        service.insertTask(reqMock, resMock);

        verify(resMock).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testUpdateTask() throws IOException {
        Task mockTask = new Task();
        mockTask.setId(5);
        BufferedReader mockBuff = Mockito.mock(BufferedReader.class);
        String s1 = "String one";
        Stream<String> stringStream = Arrays.stream(new String[]{s1});
        when(reqMock.getReader()).thenReturn(mockBuff);
        when(reqMock.getReader().lines()).thenReturn(stringStream);
        when(mapperMock.readValue(s1, Task.class)).thenReturn(mockTask);
        when(service.updateTask(mockTask)).thenReturn(task);


        service.updateTask(reqMock, resMock);

        verify(resMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testUpdateTaskWithIdOfZero() throws IOException {
        Task mockTask = new Task();
        BufferedReader mockBuff = Mockito.mock(BufferedReader.class);
        String s1 = "String one";
        Stream<String> stringStream = Arrays.stream(new String[]{s1});
        when(reqMock.getReader()).thenReturn(mockBuff);
        when(reqMock.getReader().lines()).thenReturn(stringStream);
        when(mapperMock.readValue(s1, Task.class)).thenReturn(mockTask);
        when(service.updateTask(mockTask)).thenReturn(task);


        service.updateTask(reqMock, resMock);

        verify(resMock).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    public void testUpdateTaskWithNullTask() throws IOException {
        Task mockTask = new Task();
        mockTask.setId(5);
        BufferedReader mockBuff = Mockito.mock(BufferedReader.class);
        String s1 = "String one";
        Stream<String> stringStream = Arrays.stream(new String[]{s1});
        when(reqMock.getReader()).thenReturn(mockBuff);
        when(reqMock.getReader().lines()).thenReturn(stringStream);
        when(mapperMock.readValue(s1, Task.class)).thenReturn(mockTask);
        when(service.updateTask(mockTask)).thenReturn(null);


        service.updateTask(reqMock, resMock);

        verify(resMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    @Test
    public void testGetTasksWithNoParameters() throws Exception {
        taskList.add(new Task());
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(taskList)).thenReturn(json);
        when(resMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getTasks(reqMock, resMock);

        verify(resMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testGetTasksWithEmptyStringParameter() {
        when(reqMock.getParameter("taskId")).thenReturn("");
        service.getTasks(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testGetTasksWithParameters() throws IOException {
        TaskService mockService = Mockito.mock(TaskService.class);
        when(reqMock.getParameter("taskId")).thenReturn("1");
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(service.getTask(1)).thenReturn(task);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(task)).thenReturn("Test");
        when(resMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getTasks(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testGetTasksWithParametersNullReturn() throws IOException {
        TaskService mockService = Mockito.mock(TaskService.class);
        when(reqMock.getParameter("taskId")).thenReturn("1");
        when(mapperMock.writerWithDefaultPrettyPrinter()).thenReturn(writerMock);
        when(service.getTask(1)).thenReturn(task);
        when(mapperMock.writerWithDefaultPrettyPrinter().writeValueAsString(task)).thenReturn("null");
        when(resMock.getOutputStream()).thenReturn(outputStreamMock);

        service.getTasks(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }


    @Test
    public void testDeleteTaskWithParameters() {
        TaskService mockService = Mockito.mock(TaskService.class);
        when(reqMock.getParameter("taskId")).thenReturn("1");
        when(service.deleteTask(1)).thenReturn(true);
        service.deleteTask(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteTasksWithNoParameters() {
        when(reqMock.getParameter("taskId")).thenReturn(null);
        service.deleteTask(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDeleteTasksWithEmptyStringParameters() {
        when(reqMock.getParameter("taskId")).thenReturn("");
        service.deleteTask(reqMock, resMock);
        verify(resMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }


    @Test
    public void testInsertTaskFromProtectedMethod() {
        Task task = new Task("Clean house", "Vacuum floors", 3, false);
        when(service.insertTask(task)).thenReturn(task);
        Task t = service.insertTask(task);
        Assert.assertEquals("Clean house", t.getTask());
        Assert.assertEquals("Vacuum floors", t.getDescription());
        Assert.assertFalse(t.getCompleted());
        Assert.assertEquals(3, t.getPriority());
    }

    @Test
    public void getTasks(){
        Task task = new Task("Clean house", "Vacuum floors", 3, false);
        task.setId(6);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        when(req.getParameter("userId")).thenReturn("6");



    }

    @Test
    public void testGetTask() {
        Task task = new Task("Clean house", "Wash toilets", 4, false);
        when(service.getTask(0)).thenReturn(task);
        Task t = service.getTask(0);
        Assert.assertEquals("Clean house", t.getTask());
        Assert.assertEquals("Wash toilets", t.getDescription());
        Assert.assertEquals(4, t.getPriority());
        Assert.assertFalse(t.getCompleted());
    }

    @Test
    public void testGetAll() {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Clean house", "Wash toilets", 4, false);
        Task task2 = new Task("Clean house", "Vacuum floors", 3, false);
        tasks.add(task1);
        tasks.add(task2);
        System.out.println(tasks.size());
        when(taskDaoMock.getAll(connectionSource)).thenReturn(tasks);
        List<Task> taskList = service.getAllTasks();
        Assert.assertEquals(2, tasks.size());
    }

    @Test
    public void testDeleteTask() {
        service.deleteTask(5);
    }
}