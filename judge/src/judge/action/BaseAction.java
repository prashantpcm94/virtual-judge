package judge.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 用于公共用途
 * @author Isun
 *
 */
public class BaseAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	protected Integer iDisplayStart;
	protected Integer iDisplayLength;
	protected Integer iColumns;
	protected String sSearch;
	protected Boolean bEscapeRegex;
	protected Integer iSortingCols;
	protected String sEcho;
	
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

	
}
