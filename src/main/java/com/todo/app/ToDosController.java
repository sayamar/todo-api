package com.todo.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import com.todo.app.data.Todo;
import com.todo.app.service.TodoService;

@RestController
public class ToDosController {

	private static Logger LOG = LoggerFactory.getLogger(ToDosController.class);

	@Value("${todos.api.limit}")
	long _limit;

	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	private static String DEFAULT_GROUP = "Default group";

	private final TodoService todoService;

	public ToDosController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping("/")
	public List<Todo> retrieve() {
		return Arrays.asList(this.todoService.retriveTasks());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/")
	public Todo create(@RequestBody Todo todo) {
		// check if cache size is not over the limit
		throwIfOverLimit();

		if (todo.getTitle() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "todos.title cannot be null on put");
		}

		LOG.debug("Creating TODO: " + todo);
		Todo obj = new Todo();
		if (ObjectUtils.isEmpty(todo.getId())) {
			obj.setId(UUID.randomUUID().toString());
		} else {
			obj.setId(todo.getId());
		}
		if (!ObjectUtils.isEmpty(todo.getTitle())) {
			obj.setTitle(todo.getTitle());
		}
		if (!ObjectUtils.isEmpty(todo.isComplete())) {
			obj.setComplete(todo.isComplete());
		}
		if (ObjectUtils.isEmpty(todo.getCategory())) {
			obj.setCategory(DEFAULT_GROUP);
		} else {
			obj.setCategory(todo.getCategory());
		}
		if (ObjectUtils.isEmpty(todo.getDeadline())) {
			obj.setDeadline(dtf.format(LocalDateTime.now()));
		} else {
			obj.setDeadline(todo.getDeadline());
		}

		Todo saved = this.todoService.createNew(todo);
		return saved;
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/update/{id}")
	public Todo put(@PathVariable String id, @RequestBody Todo todo) {
		if (todo.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "todos.id cannot be null on put");
		}
		if (!todo.getId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"todos.id ${todo.id} and id $id are inconsistent");
		}

		Todo obj = new Todo();
		obj.setId(todo.getId());

		if (!ObjectUtils.isEmpty(todo.isComplete())) {
			obj.setComplete(todo.isComplete());
		}
		if (ObjectUtils.isEmpty(todo.getCategory())) {
			obj.setCategory(DEFAULT_GROUP);
		} else {
			obj.setCategory(todo.getCategory());
		}
		if (ObjectUtils.isEmpty(todo.getDeadline())) {
			obj.setDeadline(dtf.format(LocalDateTime.now()));
		} else {
			obj.setDeadline(todo.getDeadline());
		}
		Todo updatedTask = this.todoService.updateTask(todo);
		return updatedTask;
	}
	
	@GetMapping("/get/{id}")
	public Todo retrieve(@PathVariable("id") String id) {
		LOG.debug("Retrieving Todo: " + id);
		Todo todo = null;
		// Check cache + DB
		Todo cached = null;
		try {
			todo = this.todoService.retrive(id);
		} catch (HttpStatusCodeException ex) {
			if (ex.getRawStatusCode() != 404) {
				LOG.error("Caching service error downstream", ex);
				throw ex;
			}
		}
		return todo;
	}

	@DeleteMapping("/delete")
	public void deleteAll() {
		LOG.debug("Removing all Todos");
		this.todoService.deleteAll();
	}

	private void throwIfOverLimit() {
		Todo[] cached = this.todoService.retriveTasks();
		if (cached.length > _limit) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "todos.api.limit=$limit, todos.size=$count");
		} else {
			return;
		}

	}

}
