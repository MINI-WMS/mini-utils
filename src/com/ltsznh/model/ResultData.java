package com.ltsznh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4335213485634395783L;

	protected boolean status = true;
	protected String message = "";
	protected int level = 0;

	protected long rows = 0;// 增删改行数或分页内容总行数
	protected int cols = 0;

	protected String time;

	protected List<String[]> list;

	protected String[] colsNames;
	protected String[] footValues;
	protected int[] colsWidth;

	public void setColNames(String[] colsNames) {
		this.colsNames = colsNames;
		cols = this.colsNames.length;
	}

	public String[] getColNames() {
		return colsNames;
	}

	public int getColIndex(String colName) {
		int j = colsNames.length;
		for (int i = 0; i < j; i++) {
			if (colsNames[i].equals(colName))
				return i;
		}
		return 0;
	}

	public void setFootValues(String[] footValues) {
		this.footValues = footValues;
	}

	public String[] getFootValues() {
		return footValues;
	}

	public void setColsWidth(int[] colsWidth) {
		this.colsWidth = colsWidth;
	}

	public int[] getColsWidth() {
		return colsWidth;
	}

	public ResultData() {
		list = new ArrayList<String[]>();
	}

	public void add(String[] data) {
		list.add(data);
	}

	public List<String[]> getList() {
		return list;
	}

	public void setList(List<String[]> list) {
		this.list = list;
	}

	public int getCols() {
		return cols;
	}

	public void setRows(int r) {
		rows = r;
	}

	/**
	 * 增删改结果行数
	 * 
	 * @return
	 */
	public long getRows() {
		return rows;
	}

	/**
	 * 结果行数;
	 * 
	 * @return
	 */
	public int size() {
		return (int) (rows > list.size() ? rows : list.size());
	}

	/**
	 * 结果LIST行数，list.size();
	 * 
	 * @return
	 */
	public int listSize() {
		return list.size();
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExpired() {
		setLevel(-1);
	}

	public int getLevel() {
		return level;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatusAndMessage(Boolean status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTime(Long time) {
		this.time = time + "";
	}

	public String getTime() {
		return time;
	}

	public String getText(int i, int j) {
		return list.get(i)[j] == null ? "" : list.get(i)[j];
	}

	public String getText(int i, String j) {
		return getText(i, getColIndex(j));
	}
}
