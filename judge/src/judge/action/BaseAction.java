package judge.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import judge.spider.HDUSpider;
import judge.spider.HUSTSpider;
import judge.spider.HYSBZSpider;
import judge.spider.POJSpider;
import judge.spider.SGUSpider;
import judge.spider.SPOJSpider;
import judge.spider.Spider;
import judge.spider.URALSpider;
import judge.spider.UVALiveSpider;
import judge.spider.ZOJSpider;
import judge.submitter.HDUSubmitter;
import judge.submitter.HUSTSubmitter;
import judge.submitter.HYSBZSubmitter;
import judge.submitter.POJSubmitter;
import judge.submitter.SGUSubmitter;
import judge.submitter.SPOJSubmitter;
import judge.submitter.Submitter;
import judge.submitter.URALSubmitter;
import judge.submitter.UVALiveSubmitter;
import judge.submitter.ZOJSubmitter;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 用于公共用途
 * @author Isun
 *
 */
@SuppressWarnings("unchecked")
public class BaseAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	protected Integer iDisplayStart = 0;
	protected Integer iDisplayLength = 25;
	protected Integer iColumns;
	protected String sSearch;
	protected Boolean bEscapeRegex;
	protected Integer iSortingCols;
	protected Integer iSortCol_0;
	protected String sSortDir_0;
	protected String sEcho;

	static public List<String> OJList = new ArrayList<String>();
	static {
		OJList.add("POJ");
		OJList.add("ZOJ");
		OJList.add("UVALive");
		OJList.add("SGU");
		OJList.add("URAL");
		OJList.add("HUST");
		OJList.add("SPOJ");
		OJList.add("HDU");
		OJList.add("HYSBZ");
	}
	
	static private List<String> OJListAll = new ArrayList<String>();
	static {
		OJListAll.add("All");
		OJListAll.addAll(OJList);
	}
	
	static public Map<String, Spider> spiderMap = new HashMap<String, Spider>();
	static {
		spiderMap.put("POJ", new POJSpider());
		spiderMap.put("ZOJ", new ZOJSpider());
		spiderMap.put("UVALive", new UVALiveSpider());
		spiderMap.put("SGU", new SGUSpider());
		spiderMap.put("URAL", new URALSpider());
		spiderMap.put("HUST", new HUSTSpider());
		spiderMap.put("SPOJ", new SPOJSpider());
		spiderMap.put("HDU", new HDUSpider());
		spiderMap.put("HYSBZ", new HYSBZSpider());
	}
	
	static public Map<String, Submitter> submitterMap = new HashMap<String, Submitter>();
	static {
		submitterMap.put("POJ", new POJSubmitter());
		submitterMap.put("ZOJ", new ZOJSubmitter());
		submitterMap.put("UVALive", new UVALiveSubmitter());
		submitterMap.put("SGU", new SGUSubmitter());
		submitterMap.put("URAL", new URALSubmitter());
		submitterMap.put("HUST", new HUSTSubmitter());
		submitterMap.put("SPOJ", new SPOJSubmitter());
		submitterMap.put("HDU", new HDUSubmitter());
		submitterMap.put("HYSBZ", new HYSBZSubmitter());
	}
	
	static public Map<String, String> lf = new HashMap<String, String>();
	static {
		lf.put("POJ", "%I64d & %I64u");
		lf.put("ZOJ", "%lld & %llu");
		lf.put("UVALive", "%lld & %llu");
		lf.put("SGU", "%I64d & %I64u");
		lf.put("URAL", "%I64d & %I64u");
		lf.put("HUST", "%lld & %llu");
		lf.put("SPOJ", "%lld & %llu");
		lf.put("HDU", "%I64d & %I64u");
		lf.put("HYSBZ", "%I64d & %I64u");
	}
	

	public Integer getIDisplayStart() {
		return iDisplayStart;
	}
	public void setIDisplayStart(Integer displayStart) {
		iDisplayStart = displayStart;
	}
	public Integer getIDisplayLength() {
		return iDisplayLength;
	}
	public void setIDisplayLength(Integer displayLength) {
		iDisplayLength = displayLength;
	}
	public Integer getIColumns() {
		return iColumns;
	}
	public void setIColumns(Integer columns) {
		iColumns = columns;
	}
	public String getSSearch() {
		return sSearch;
	}
	public void setSSearch(String search) {
		sSearch = search;
	}
	public Boolean getBEscapeRegex() {
		return bEscapeRegex;
	}
	public void setBEscapeRegex(Boolean escapeRegex) {
		bEscapeRegex = escapeRegex;
	}
	public Integer getISortingCols() {
		return iSortingCols;
	}
	public void setISortingCols(Integer sortingCols) {
		iSortingCols = sortingCols;
	}
	public String getSEcho() {
		return sEcho;
	}
	public void setSEcho(String echo) {
		sEcho = echo;
	}
	public Integer getISortCol_0() {
		return iSortCol_0;
	}
	public void setISortCol_0(Integer sortCol_0) {
		iSortCol_0 = sortCol_0;
	}
	public String getSSortDir_0() {
		return sSortDir_0;
	}
	public void setSSortDir_0(String sortDir_0) {
		sSortDir_0 = sortDir_0;
	}
	public List getOJList() {
		return OJList;
	}
	public List getOJListAll() {
		return OJListAll;
	}
	
}
