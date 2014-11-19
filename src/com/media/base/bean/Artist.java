/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;

/**
 * @author pengfei.xu
 * @version 2013-2-17 下午1:52:03
 * @description 艺术家
 */
public class Artist implements Serializable {

	private static final long serialVersionUID = -2097047931609621623L;

	private final int artistId;
	private final String artistName; // 歌手名称
	private String artistPinyin;

	public Artist(int artistId, String artistName) {
		this.artistId = artistId;
		this.artistName = artistName;
	}

	public String getArtistName() {
		return artistName;
	}

	public int getArtistId() {
		return artistId;
	}

	public String getArtistPinyin() {
		return artistPinyin;
	}

	public void setArtistPinyin(String artistPinyin) {
		this.artistPinyin = artistPinyin;
	}

}
