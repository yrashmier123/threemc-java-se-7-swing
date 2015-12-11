package com.threemc.data;

public class OutputsUpdate {
	
	private int id;
	private int output_id;
	private int employee_id;
	private String ouDesc;
	private String ouDate;
	private Output output;
	private Employee employee;

	public OutputsUpdate(int output_id, int employee_id, String ouDesc,
			String ouDate) {
		this.output_id = output_id;
		this.employee_id = employee_id;
		this.ouDesc = ouDesc;
		this.ouDate = ouDate;
	}
	
	public OutputsUpdate(int id, int output_id, int employee_id, String ouDesc,
			String ouDate) {
		this(output_id, employee_id, ouDesc, ouDate);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOutput_id() {
		return output_id;
	}

	public void setOutput_id(int output_id) {
		this.output_id = output_id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getOuDesc() {
		return ouDesc;
	}

	public void setOuDesc(String ouDesc) {
		this.ouDesc = ouDesc;
	}

	public String getOuDate() {
		return ouDate;
	}

	public void setOuDate(String ouDate) {
		this.ouDate = ouDate;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
