package judge.bean;

import java.util.Date;

public class Description {
	
	private int id;				//Hibernate统编ID
	private String description;	//题面描述
	private String input;		//输入介绍
	private String output;		//输出介绍
	private String sampleInput;	//样例输入
	private String sampleOutput;//样例输出
	private String hint;		//提示

	private Date updateTime;	//更新时间
	private String author;		//作者	
	private String remarks;		//备注
	private int vote;			//like票数
	
	private Problem problem;

	
	public Problem getProblem() {
		return problem;
	}
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if (description != null){
			description = description.trim();
			if (description.matches("(<[^<>]*>\\s*)*")){
				description = null;
			}
		}
		this.description = description;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		if (input != null){
			input = input.trim();
			if (input.matches("(<[^<>]*>\\s*)*")){
				input = null;
			}
		}
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		if (output != null){
			output = output.trim();
			if (output.matches("(<[^<>]*>\\s*)*")){
				output = null;
			}
		}
		this.output = output;
	}
	public String getSampleInput() {
		return sampleInput;
	}
	public void setSampleInput(String sampleInput) {
		if (sampleInput != null){
			sampleInput = sampleInput.trim();
			if (sampleInput.matches("(<[^<>]*>\\s*)*")){
				sampleInput = null;
			}
		}
		this.sampleInput = sampleInput;
	}
	public String getSampleOutput() {
		return sampleOutput;
	}
	public void setSampleOutput(String sampleOutput) {
		if (sampleOutput != null){
			sampleOutput = sampleOutput.trim();
			if (sampleOutput.matches("(<[^<>]*>\\s*)*")){
				sampleOutput = null;
			}
		}
		this.sampleOutput = sampleOutput;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		if (hint != null){
			hint = hint.trim();
			if (hint.matches("(<[^<>]*>\\s*)*")){
				hint = null;
			}
		}
		this.hint = hint;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}


}
