package com.todo.app.service;

import com.todo.app.data.Todo;

public interface TodoService {
	
	public Todo[] retriveTasks();
	public Todo createNew(Todo todo);
	public Todo updateTask(Todo todo);
	public void deleteAll();
	public Todo retrive(String id);

}
