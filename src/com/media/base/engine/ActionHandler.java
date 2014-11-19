/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.engine;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.HandlerThread;

import com.media.base.utils.Wlog;

/**
 * @author pengfei.xu
 * @date 2014-11-19 下午2:41:53
 * @Description: 处理音乐播放的所有事件，具体参考对应的枚举定义
 * 
 */
public class ActionHandler {

	private static final String TAG = ActionHandler.class.getSimpleName();

	private static PlayerEngineImpl mPlayerEngine;
	private static Handler mHandler;
	private static List<ActionOperation> mList = new ArrayList<ActionOperation>();

	private static Object syncObj = new Object();

	static {
		Wlog.d(TAG, "init");
		HandlerThread thread = new HandlerThread("ActionHandler");
		mHandler = new Handler(thread.getLooper());
		thread.start();

		mPlayerEngine = PlayerEngineImpl.getInstance();
	}

	private static Runnable mTask = new Runnable() {

		@Override
		public void run() {
			ActionOperation operation = null;
			synchronized (syncObj) {
				operation = mList.remove(0);
			}
			Wlog.d(TAG, "execute:", operation);
			if (operation == null) {
				return;
			}
			switch (operation) {
			case PLAY:
				mPlayerEngine.play();
				break;
			case RESUME:
				mPlayerEngine.resume();
				break;
			case PAUSE:
				mPlayerEngine.pause();
				break;
			case NEXT:
				mPlayerEngine.next();
				break;
			case PREV:
				mPlayerEngine.prev();
				break;
			case STOP:
				mPlayerEngine.stop();
				break;
			}
		}
	};

	public static void execute(ActionOperation operation) {
		synchronized (syncObj) {
			mList.add(operation);
		}
		mHandler.post(mTask);
	}

}
