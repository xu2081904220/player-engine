/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengfei.xu
 * @date 2014-11-19 下午1:20:46
 * @Description: 播放列表
 * 
 */
public class Playlist implements Serializable {

	private static final long serialVersionUID = -5023075762447674820L;

	private int mSelection = -1;
	private List<PlaylistEntry> mPlaylist = new ArrayList<PlaylistEntry>();

	public PlaylistEntry getSelectedTrack() {
		if (mSelection > -1 && mSelection < mPlaylist.size()) {
			return mPlaylist.get(mSelection);
		}
		return null;
	}

	public boolean isEmpty() {
		return mPlaylist.size() == 0;
	}

	public PlaylistEntry selectNext() {
		if (!isEmpty()) {
			mSelection++;
			mSelection %= mPlaylist.size();
			return mPlaylist.get(mSelection);
		}
		return null;
	}

	public PlaylistEntry selectPrevious() {
		if (!isEmpty()) {
			mSelection--;
			if (mSelection < 0)
				mSelection = mPlaylist.size() - 1;
		}
		return null;
	}

	public void select(PlaylistEntry entry) {
		for (int i = 0; i < mPlaylist.size(); i++) {
			if (mPlaylist.get(i).getTrack().getPath()
					.equals(entry.getTrack().getPath())) {
				mSelection = i;
				return;
			}
		}
	}

}
