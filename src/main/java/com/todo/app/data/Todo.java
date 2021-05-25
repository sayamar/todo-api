package com.todo.app.data;

public class Todo {
	
	private String id;
    private String title;
    private boolean complete;
    private String category;
    private String deadline;
    private String assignedTo;
    
    public Todo() {
    	
    }
    
	public Todo(String id, String title, boolean complete, String category, String deadline,String assignedTo) {
		super();
		this.id = id;
		this.title = title;
		this.complete = complete;
		this.category = category;
		this.deadline = deadline;
		this.assignedTo = assignedTo;
	}
	
	
	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
    
    

}
