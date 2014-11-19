/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.engine;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;

import com.media.base.bean.Playlist;
import com.media.base.bean.PlaylistEntry;
import com.media.base.utils.Wlog;

/**
 * @author pengfei.xu
 * @date 2014-11-19 上午11:50:31
 * @Description: 多媒体播放功能实现
 * 
 */
public class PlayerEngineImpl implements IPlayerEngine {

	private static final String TAG = PlayerEngineImpl.class.getSimpleName();

	private static PlayerEngineImpl instance;

	private Handler mProgressMonitorHandler;
	private InternalMediaPlayer mCurrentMediaPlayer;
	private Playlist mCurrentPlaylist;

	private PlayerEngineImpl() {
		HandlerThread thread = new HandlerThread("ProgressMonitorThread");
		mProgressMonitorHandler = new Handler(thread.getLooper());
		thread.start();
	}

	/**
	 * 第一次需要在ui主线程中调用
	 * 
	 * @return
	 */
	protected static synchronized PlayerEngineImpl getInstance() {
		if (instance == null) {
			instance = new PlayerEngineImpl();
		}
		return instance;
	}

	private Runnable mProgressMonitorTask = new Runnable() {

		@Override
		public void run() {
			if (mCurrentMediaPlayer != null) {

			}
		}
	};

	/**
	 * 
	 * @author pengfei.xu
	 * @date 2014-11-19 下午1:01:01
	 * @Description: 封装了MediaPlayer，并处理了相关逻辑
	 * 
	 */
	private class InternalMediaPlayer extends MediaPlayer implements
			MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
			MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

		public boolean prepared = false; // MediaPlayer是否解码完毕
		public String path;// 当前歌曲路径

		public InternalMediaPlayer() {
			setOnPreparedListener(this);
			setOnBufferingUpdateListener(this);
			setOnErrorListener(this);
			setOnCompletionListener(this);
		}

		/**
		 * 音乐是否已经发生变化
		 */
		public boolean isMusicChanged() {
			if (this.path == null) {
				return true;
			}
			String currentPath = (mCurrentPlaylist != null && !mCurrentPlaylist
					.isEmpty()) ? mCurrentPlaylist.getSelectedTrack()
					.getTrack().getPath() : null;
			return !this.path.equals(currentPath);
		}

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			Wlog.d(TAG, "onBufferingUpdate:", percent);
		}

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Wlog.d(TAG, "onError:", what, extra);
			// TODO 此处需要根据需求决定是否默认播放下一首
			next();
			return false;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			Wlog.d(TAG, "onPrepared:" + path);
			prepared = true;
			if (!isMusicChanged()) {
				resume();
			}
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			Wlog.d(TAG, "onCompletion:" + path);
			// TODO 此处根据需求决定是循环播放，还是播放下一首，还是直接停止播放
			next();
		}
	}

	private InternalMediaPlayer create(PlaylistEntry entry) {
		if (entry == null) {
			return null;
		}
		final String path = entry.getTrack().getPath();
		Wlog.d(TAG, "create:", entry.getTrack().getPath());
		final InternalMediaPlayer mp = new InternalMediaPlayer();
		try {
			mp.path = path;
			mp.prepared = false;
			mp.setDataSource(path);
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
			cleanUp();
		}
		return mp;
	}

	private void cleanUp() {
		Wlog.d(TAG, "cleanUp");
		if (mCurrentMediaPlayer != null) {
			try {
				mProgressMonitorHandler.removeCallbacks(mProgressMonitorTask);
				mCurrentMediaPlayer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mCurrentMediaPlayer.release();
				mCurrentMediaPlayer.path = null;
				mCurrentMediaPlayer = null;
			}
		}
	}

	@Override
	public void play() {

		if (mCurrentPlaylist == null || mCurrentPlaylist.isEmpty()) {
			Wlog.d(TAG,
					"The playlist has not prepared , so will ignore this play operation!");
			return;
		}

		Wlog.d(TAG, "play start:",
				String.valueOf(Thread.currentThread().getId()));

		if (mCurrentMediaPlayer != null) {
			// 若有歌曲在播放，且播放的歌曲和当前播放列表选中的歌曲不一样，则停止当前播放的歌曲
			// 若有歌曲在播放，且播放的歌曲和当前播放列表选中的歌曲一样，则忽略本次play操作
			if (mCurrentMediaPlayer.isMusicChanged()) {
				cleanUp();
			} else {
				if (mCurrentMediaPlayer.isPlaying()) {
					Wlog.d(TAG,
							"This music is playing now , so will ignore this play operation!");
					return;
				}
			}
		}

		mCurrentMediaPlayer = create(mCurrentPlaylist.getSelectedTrack());

		Wlog.d(TAG, "play end:", String.valueOf(Thread.currentThread().getId()));
	}

	@Override
	public void play(PlaylistEntry entry) {

		if (entry == null) {
			Wlog.d(TAG,
					"The playlist entry was null , so will ignore this play operation!");
			return;
		}

		mCurrentPlaylist.select(entry);
		play();

	}

	@Override
	public void resume() {
		try {
			if (mCurrentMediaPlayer != null && mCurrentMediaPlayer.prepared) {
				mCurrentMediaPlayer.start();
				mProgressMonitorHandler.removeCallbacks(mProgressMonitorTask);
				mProgressMonitorHandler.post(mProgressMonitorTask);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void pause() {
		try {
			if (mCurrentMediaPlayer != null && mCurrentMediaPlayer.prepared) {
				mProgressMonitorHandler.removeCallbacks(mProgressMonitorTask);
				mCurrentMediaPlayer.pause();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void seekTo(int msec) {
		try {
			if (mCurrentMediaPlayer != null && mCurrentMediaPlayer.prepared) {
				mCurrentMediaPlayer.seekTo(msec);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void next() {
		if (mCurrentPlaylist == null || mCurrentPlaylist.isEmpty()) {
			Wlog.d(TAG,
					"The playlist has not prepared , so will ignore this next operation!");
			return;
		}
		mCurrentPlaylist.selectNext();
		play();
	}

	@Override
	public void prev() {
		if (mCurrentPlaylist == null || mCurrentPlaylist.isEmpty()) {
			Wlog.d(TAG,
					"The playlist has not prepared , so will ignore this prev operation!");
			return;
		}
		mCurrentPlaylist.selectPrevious();
		play();
	}

	@Override
	public void stop() {
		cleanUp();
		// TODO 根据需求，执行其他释放资源操作
	}

}
