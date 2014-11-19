/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;

/**
 * @author pengfei.xu
 * @date 2014-11-19 下午1:21:00
 * @Description: 播放列表实体类
 * 
 */
public class PlaylistEntry implements Serializable {

	private static final long serialVersionUID = -7420498454919505604L;

	private Track track;//歌曲信息
	private Album album;//专辑信息
	private Artist artist;//歌手信息
	private Lyric lyric;//歌词信息

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Lyric getLyric() {
		return lyric;
	}

	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}

}
