/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;

/**
 * @author pengfei.xu
 * @version 2013-2-17 下午1:46:16
 * @description 专辑
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 2744669712255342902L;

	private final int albumId;
	private String albumName;
	private String artistName;
	private String image;
	private String albumPinyin;

	public Album(int albumId, String albumName) {
		this.albumId = albumId;
		this.albumName = albumName;
	}

	public Album(int albumId, String albumName, String artistName) {
		this.albumId = albumId;
		this.artistName = artistName;
		this.albumName = albumName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public String getArtistName() {
		return artistName;
	}

	public int getAlbumId() {
		return albumId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAlbumPinyin() {
		return this.albumPinyin;
	}

	public void setAlbumPinyin(String albumPinyin) {
		this.albumPinyin = albumPinyin;
	}

	@Override
	public String toString() {
		return "[" + albumId + "," + albumName + "," + image + "]";
	}

}
