/**
 * 我心中有猛虎，细嗅蔷薇...
 */
package com.media.base.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.media.base.utils.EncoderUtils;
import com.media.base.utils.Wlog;

/**
 * @author pengfei.xu
 * @version 2013-2-17 下午1:49:43
 * @description 歌词实体类
 */
public class Lyric implements Serializable {

	private static final long serialVersionUID = -1893104630344070944L;

	private static final String CLASSNAME = "Lyric";
	private static final Pattern pattern = Pattern
			.compile("(?<=\\[).*?(?=\\])");// 用于缓存的一个正则表达式对象

	private List<Sentence> list = new ArrayList<Sentence>(); // 歌词所有句子集合
	private Sentence currentSentence;
	private Sentence firstSentence;
	private int selected = -1;
	private int offset;// 整首歌的偏移量

	public boolean isUnlawful; // 是否是非法的歌词文件
	public String path;// 歌词文件目录

	public Lyric() {
		Sentence sentence = new Sentence("");
		list.add(sentence);
	}

	public Lyric(String content) {
		init(content);
	}

	public String getPath() {
		return path;
	}

	/**
	 * 根据时间获取该时间所属句子在列表中的position
	 * 
	 * @param currentTime
	 * @return
	 */
	public int getPositionByTime(long currentTime) {
		int position = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFromTime() <= currentTime
					&& list.get(i).getToTime() > currentTime) {
				position = i;
				break;
			}
		}
		return position;
	}

	public boolean isCurrentPosition(int position) {
		if (selected == position) {
			return true;
		}
		selected = position;
		return false;
	}

	/**
	 * 根据当前时间获取当前正在播放的句子，通过比较当前时间和所有句子的起始时间，甄别当前正在播放的句子
	 * 
	 * @param currentTime
	 * @return
	 */
	public Sentence getSentenceByTime(long currentTime) {
		if (currentTime == 0) {
			currentSentence = null;
		}
		// 若当前时间和当前句子相匹配，则无需遍历，直接返回当前句子即可
		if (currentSentence != null
				&& currentTime <= currentSentence.getToTime()
				&& currentTime >= currentSentence.getFromTime()) {
			return currentSentence;
		}
		// 遍历
		for (Sentence sentence : list) {
			if (currentTime <= sentence.getToTime()
					&& currentTime >= sentence.getFromTime()) {
				currentSentence = sentence;
				break;
			}
		}
		return currentSentence;
	}

	public Sentence getNextSentence() {
		selected++;
		return currentSentence = list.get(selected);
	}

	public List<Sentence> getSentences() {
		return list;
	}

	/**
	 * 最重要的一个方法，它根据读到的歌词内容 进行初始化，比如把歌词一句一句分开并计算好时间
	 * 
	 * @param content
	 *            歌词内容|歌词文件路径
	 */
	private void init(String content) {
		try {
			if (content.startsWith("/")) {
				Wlog.d(CLASSNAME, "init " + content);
				File f = new File(content);
				InputStream ins = new FileInputStream(f);
				String encoder = EncoderUtils.getEncoder(content);
				Wlog.d(CLASSNAME, "Encoder is : " + encoder);
				if (encoder == null) {
					encoder = "UTF-8";
					// return;
				}
				InputStreamReader inr = null;
				try {
					inr = new InputStreamReader(ins, encoder);
				} catch (Exception ue) {
					ue.printStackTrace();
					Wlog.d(CLASSNAME,
							"UnSupportedEncoding , so will use gbk to encode");
					inr = new InputStreamReader(ins, "gbk");
				}
				BufferedReader br = new BufferedReader(inr);
				String temp = null;
				// list.add(new Sentence("", 0, 0)); // 第一行默认“”
				while ((temp = br.readLine()) != null) {
					parseLine(temp.trim());
				}
				br.close();
			} else {
				String[] lrcs = content.trim().split("\\\\n");
				for (int i = 0; i < lrcs.length; i++) {
					if (lrcs[i].trim().length() != 0)
						parseLine(lrcs[i].trim());
				}
			}
			// 读进来以后就排序了
			Collections.sort(list, new Comparator<Sentence>() {

				public int compare(Sentence o1, Sentence o2) {
					return (int) (o1.getFromTime() - o2.getFromTime());
				}
			});
			// 处理第一句歌词的起始情况,无论怎么样,加上歌名做为第一句歌词,并把它的
			// 结尾为真正第一句歌词的开始
			if (list.size() == 0) {
				// list.add(new Sentence(info.getFormattedName(), 0,
				// Integer.MAX_VALUE));
				return;
			}

			// 设置ToTime
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Sentence next = null;
				if (i + 1 < size) {
					next = list.get(i + 1);
				}
				Sentence now = list.get(i);
				if (next != null) {
					now.setToTime(next.getFromTime() - 1);
				}
			}
			// 如果就是没有怎么办,那就只显示一句歌名了
			if (list.size() == 1) {
				list.get(0).setToTime(Integer.MAX_VALUE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (list.size() > 0) {
				// list.add(0, new Sentence("", 0, 0));
				selected = 0;
				currentSentence = list.get(selected);
				firstSentence = currentSentence;
				Wlog.d(CLASSNAME, "firstSentence:" + firstSentence);
			}
		}
	}

	/**
	 * 分析这一行的内容，根据这内容 以及标签的数量生成若干个Sentence对象 当此行中的时间标签分布不在一起时，也要能分析出来
	 * 
	 * 
	 * @param line
	 *            这一行
	 */
	private void parseLine(String line) {
		System.out.println(line);
		if (line.equals("")) {
			return;
		}
		if (line.contains("[ti:")) {
			int length = line.length();
			int index = line.indexOf("[ti:") + 4;
			if (index < length - 1)
				list.add(new Sentence(line.substring(index, length - 1), 0));
		}
		Matcher matcher = pattern.matcher(line);
		List<String> temp = new ArrayList<String>();
		int lastIndex = -1;// 最后一个时间标签的下标
		int lastLength = -1;// 最后一个时间标签的长度
		while (matcher.find()) {
			String s = matcher.group();
			int index = line.indexOf("[" + s + "]");
			if (lastIndex != -1 && index - lastIndex > lastLength + 2) {
				// 如果大于上次的大小，则中间夹了别的内容在里面
				// 这个时候就要分段了
				String content = line.substring(lastIndex + lastLength + 2,
						index);
				for (String str : temp) {
					long t = parseTime(str);
					if (t != -1) {
						list.add(new Sentence(content, t));
					}
				}
				temp.clear();
			}
			temp.add(s);
			lastIndex = index;
			lastLength = s.length();
		}
		// 如果列表为空，则表示本行没有分析出任何标签
		if (temp.isEmpty()) {
			return;
		}
		try {
			int length = lastLength + 2 + lastIndex;
			String content = line.substring(length > line.length() ? line
					.length() : length);
			// if (Config.getConfig().isCutBlankChars()) {
			content = content.trim();
			// }
			// 当已经有了偏移量的时候，就不再分析了
			if (content.equals("") && offset == 0) {
				for (String s : temp) {
					int of = parseOffset(s);
					if (of != Integer.MAX_VALUE) {
						offset = of;
						// info.setOffset(offset);
						break;// 只分析一次
					}
				}
				return;
			}
			for (String s : temp) {
				long t = parseTime(s);
				if (t != -1) {
					list.add(new Sentence(content, t));
				}
			}
		} catch (Exception exe) {
		}
	}

	/**
	 * 分析出整体的偏移量
	 * 
	 * @param str
	 *            包含内容的字符串
	 * @return 偏移量，当分析不出来，则返回最大的正数
	 */
	private int parseOffset(String str) {
		String[] ss = str.split("\\:");
		if (ss.length == 2) {
			if (ss[0].equalsIgnoreCase("offset")) {
				int os = Integer.parseInt(ss[1]);
				System.err.println("整体的偏移量：" + os);
				return os;
			} else {
				return Integer.MAX_VALUE;
			}
		} else {
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * 把如00:00.00这样的字符串转化成 毫秒数的时间，比如 01:10.34就是一分钟加上10秒再加上340毫秒 也就是返回70340毫秒
	 * 
	 * @param time
	 *            字符串的时间
	 * @return 此时间表示的毫秒
	 */
	private long parseTime(String time) {
		String[] ss = time.split("\\:|\\.");
		// 如果 是两位以后，就非法了
		if (ss.length < 2) {
			return -1;
		} else if (ss.length == 2) {// 如果正好两位，就算分秒
			try {
				// 先看有没有一个是记录了整体偏移量的
				if (offset == 0 && ss[0].equalsIgnoreCase("offset")) {
					offset = Integer.parseInt(ss[1]);
					// info.setOffset(offset);
					System.err.println("整体的偏移量：" + offset);
					return -1;
				}
				int min = Integer.parseInt(ss[0]);
				int sec = Integer.parseInt(ss[1]);
				if (min < 0 || sec < 0 || sec >= 60) {
					throw new RuntimeException("数字不合法!");
				}
				return (min * 60 + sec) * 1000L;
			} catch (Exception exe) {
				return -1;
			}
		} else if (ss.length == 3) {// 如果正好三位，就算分秒，十毫秒
			try {
				int min = Integer.parseInt(ss[0]);
				int sec = Integer.parseInt(ss[1]);
				int mm = Integer.parseInt(ss[2]);
				if (min < 0 || sec < 0 || sec >= 60 || mm < 0 || mm > 99) {
					throw new RuntimeException("数字不合法!");
				}
				return (min * 60 + sec) * 1000L + mm * 10;
			} catch (Exception exe) {
				return -1;
			}
		} else {// 否则也非法
			return -1;
		}
	}

}
