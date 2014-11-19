/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.engine;

import com.media.base.bean.PlaylistEntry;

/**
 * @author pengfei.xu
 * @date 2014-11-19 上午11:45:07
 * @Description: 多媒体播放对外接口
 * 
 */
public interface IPlayerEngine {

	// 音乐播放
	public void play();

	// 播放制定的音乐
	public void play(PlaylistEntry entry);
	
	//音乐恢复播放
	public void resume();

	// 音乐暂停
	public void pause();

	// 下一首音乐
	public void next();

	// 上一首音乐
	public void prev();

	// 音乐停止
	public void stop();
	
	// 拖动进度条
	public void seekTo(int msec);

}
