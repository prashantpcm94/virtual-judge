package judge.bean;

import java.util.Date;

/**
 * 题目
 * @author Isun
 *
 */
public class Problem {
	private int id;			//Hibernate统编ID
	private String title;		//标题
	private String description;	//题面描述
	private String input;		//输入介绍
	private String output;		//输出介绍
	private String sampleInput;	//样例输入
	private String sampleOutput;//样例输出
	private String hint;		//提示
	private String source;		//出处
	private String url;			//题面原始url
	private String originOJ;	//原始OJ
	private String originProb;	//原始OJ题号
	private Date addTime;		//加入时间
	private int hidden;			//被隐藏
	private int memoryLimit;	//内存限制(KB)
	private int timeLimit;		//时间限制(ms)
	private int creatorId;		//创建人ID
	

	public int getHidden() {
		return hidden;
	}
	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getSampleInput() {
		return sampleInput;
	}
	public void setSampleInput(String sampleInput) {
		this.sampleInput = sampleInput;
	}
	public String getSampleOutput() {
		return sampleOutput;
	}
	public void setSampleOutput(String sampleOutput) {
		this.sampleOutput = sampleOutput;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOriginOJ() {
		return originOJ;
	}
	public void setOriginOJ(String originOJ) {
		this.originOJ = originOJ;
	}
	public String getOriginProb() {
		return originProb;
	}
	public void setOriginProb(String originProb) {
		this.originProb = originProb;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getMemoryLimit() {
		return memoryLimit;
	}
	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
}
