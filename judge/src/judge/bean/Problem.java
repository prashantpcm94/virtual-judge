package judge.bean;

import java.util.Date;
import java.util.Set;

/**
 * 题目
 * @author Isun
 *
 */
public class Problem {
	private int id;			//Hibernate统编ID
	private String title;		//标题
	private String description;	//题面描述	Del
	private String input;		//输入介绍	Del
	private String output;		//输出介绍	Del
	private String sampleInput;	//样例输入	Del
	private String sampleOutput;//样例输出	Del
	private String hint;		//提示		Del
	private String source;		//出处
	private String url;			//题面原始url
	private String originOJ;	//原始OJ
	private String originProb;	//原始OJ题号
	private Date addTime;		//加入时间	Del
	private int hidden;			//被隐藏		Del
	private int memoryLimit;	//内存限制(KB)
	private int timeLimit;		//时间限制(ms)
	private int creatorId;		//创建人ID		Del
	
	private Date triggerTime;	//上次激活(有新版本)时间	Add
	
	private Set<Description> descriptions;
	
	public Problem(int id){
		this.id = id;
	}
	
	public Problem(){}

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
		if (source != null){
			source = source.trim();
			if (source.matches("(<[^<>]*>\\s*)*")){
				source = null;
			}
		}
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
	public Date getTriggerTime() {
		return triggerTime;
	}
	public void setTriggerTime(Date triggerTime) {
		this.triggerTime = triggerTime;
	}
	public Set<Description> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Set<Description> descriptions) {
		this.descriptions = descriptions;
	}
	
}
