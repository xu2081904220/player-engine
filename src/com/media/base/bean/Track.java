/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;

/**
 * @author pengfei.xu
 * @version 2013-2-17 下午1:45:35
 * @description 歌曲信息
 */
public class Track implements Serializable {

	private static final long serialVersionUID = -4747231997996809038L;

	private int id;
	private String name;
	private String pinyin;
	private int duration;
	private String path;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取文件名,不包括后缀
	 * 
	 * @return
	 */
	public String getFileName() {
		String pathName = null;
		if (path.startsWith("/")) {
			try {
				String[] s = path.split("/");
				pathName = s[s.length - 1];
				pathName = pathName.substring(0, pathName.lastIndexOf("."));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pathName;
	}

	/**
	 * 获取文件名，包括后缀
	 * 
	 * @return
	 */
	public String getFileNameWithSuffix() {
		String pathName = null;
		if (path.startsWith("/")) {
			try {
				String[] s = path.split("/");
				pathName = s[s.length - 1];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pathName;
	}

	@Override
	public String toString() {
		return name;
	}

}
