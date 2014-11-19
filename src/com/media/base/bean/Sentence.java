/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;

/**
 * @author pengfei.xu
 * @version 2013-2-17 下午2:04:50
 * @description 歌词句子
 */
public class Sentence implements Serializable {

	private static final long serialVersionUID = 2816437615717804011L;

	private long fromTime;// 这句的起始时间,时间是以毫秒为单位
	private long toTime;// 这一句的结束时间
	private String content;// 这一句的内容

	public static final String DEFAULT_CONTENT = "~~~~~~~~~~~~~~~~";

	public Sentence(String content, long fromTime, long toTime) {
		this.content = content.trim();
		this.fromTime = fromTime;
		this.toTime = toTime;
		if (this.content.equals("")) {
			this.content = DEFAULT_CONTENT;
		}
	}

	public Sentence(String content, long fromTime) {
		this(content, fromTime, 0);
	}

	public Sentence(String content) {
		this(content, 0, 0);
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return "[" + fromTime + ":" + toTime + "][" + content + "]";
	}

}
