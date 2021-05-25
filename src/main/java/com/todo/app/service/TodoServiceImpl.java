/**
 * 
 */
package com.todo.app.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.todo.app.data.Todo;

/**
 * @author Maruthi Sayampu
 *
 */
@Service
public class TodoServiceImpl implements TodoService {

	static Map<String, Todo> taskList = new HashMap<>();
	static {
		taskList.put("JIRA-01", new Todo("JIRA-01", "Add component to list", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));
		taskList.put("JIRA-02", new Todo("JIRA-02", "update component to list", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));
		taskList.put("JIRA-03", new Todo("JIRA-03", "delete component to list", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));
		taskList.put("JIRA-04", new Todo("JIRA-04", "cache component to list", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));
		taskList.put("JIRA-05", new Todo("JIRA-05", "modify list of list", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));
		taskList.put("JIRA-06", new Todo("JIRA-06", "Deplou task", false, "development", "2021-27-05","maruthi.singapore@gmail.com"));

	}

	@Override
	public Todo[] retriveTasks() {
		Collection<Todo> tasks = taskList.values();
		return tasks.toArray(new Todo[0]);
	}
	
	@Override
	public Todo retrive(String id) {
		return taskList.get(id);
	}
	
	@Override
	public Todo createNew(Todo todo) {
		taskList.put(todo.getId(), todo);
		return taskList.get(todo.getId());
	}
	@Override
	public Todo updateTask(Todo todo) {
		taskList.put(todo.getId(), todo);
		return taskList.get(todo.getId());
	}
	@Override
	public void deleteAll() {
		taskList.clear();
	}
	
	

}
