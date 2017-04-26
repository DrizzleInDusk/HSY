package com.zm.hsy.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.zm.hsy.entity.MyMessage;

public class MyMessageService {
	public static ArrayList<MyMessage> getStation(InputStream xml) throws Exception {
		ArrayList<MyMessage> mymessages = new ArrayList<MyMessage>();	
		MyMessage mymessage = null;
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据		
		
		int event = pullParser.getEventType();
		
		while (event != XmlPullParser.END_DOCUMENT){
			
			switch (event) {
			
			case XmlPullParser.START_DOCUMENT:
				mymessages = new ArrayList<MyMessage>();				
				break;	
			case XmlPullParser.START_TAG:	
				if ("mymessage".equals(pullParser.getName())){
					mymessage = new MyMessage();
				}
				if ("audioname".equals(pullParser.getName())){
					String audioname = pullParser.nextText();
					mymessage.setAudioname(audioname);
				}
				if ("head".equals(pullParser.getName())){
					String head = pullParser.nextText();
					mymessage.setHead(head);
				}								
				if ("headStatus".equals(pullParser.getName())){
					String headStatus = pullParser.nextText();
					mymessage.setHeadStatus(headStatus);
				}
				if ("nickname".equals(pullParser.getName())){
					String nickname = pullParser.nextText();
					mymessage.setNickname(nickname);
				}
				if ("addTime".equals(pullParser.getName())){
					String addTime = pullParser.nextText();
					mymessage.setAddTime(addTime);
				}
				if ("content".equals(pullParser.getName())){
					String content = pullParser.nextText();
					mymessage.setContent(content);
				}
				if ("id".equals(pullParser.getName())){
					String id = pullParser.nextText();
					mymessage.setId(id);
				}
				if ("picture".equals(pullParser.getName())){
					String picture = pullParser.nextText();
					mymessage.setPicture(picture);
				}
				if ("pushFor".equals(pullParser.getName())){
					String pushFor = pullParser.nextText();
					mymessage.setPushFor(pushFor);
				}
				if ("pushFrom".equals(pullParser.getName())){
					String pushFrom = pullParser.nextText();
					mymessage.setPushFrom(pushFrom);
				}
				if ("pushFromType".equals(pullParser.getName())){
					String pushFromType = pullParser.nextText();
					mymessage.setPushFromType(pushFromType);
				}
				if ("pushTo".equals(pullParser.getName())){
					String pushTo = pullParser.nextText();
					mymessage.setPushTo(pushTo);
				}
				if ("status".equals(pullParser.getName())){
					String status = pullParser.nextText();
					mymessage.setStatus(status);
				}
				
				break;
				
			case XmlPullParser.END_TAG:
				if ("mymessage".equals(pullParser.getName())){
					mymessages.add(mymessage);
				}
				break;
				
			}
			 //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#  
			event = pullParser.next();
		}
		
		
		return mymessages;
	}
	
	/**
	 * 保存数据到xml文件中
	 * @param mymessages
	 * @param out
	 * @throws Exception
	 */
	public static void save(ArrayList<MyMessage> mymessages, OutputStream out) throws Exception {
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(out, "UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "mymessage");		
		for (MyMessage mymessage : mymessages) {
			serializer.startTag(null, "mymessage");	
			
			serializer.startTag(null, "audioname");			
			serializer.text(mymessage.getAudioname().toString());
			serializer.endTag(null, "audioname");	
			
			serializer.startTag(null, "head");			
			serializer.text(mymessage.getHead().toString());
			serializer.endTag(null, "head");	
			
			serializer.startTag(null, "headStatus");			
			serializer.text(mymessage.getHeadStatus().toString());
			serializer.endTag(null, "headStatus");	
			
			serializer.startTag(null, "nickname");			
			serializer.text(mymessage.getNickname().toString());
			serializer.endTag(null, "nickname");
			
			serializer.startTag(null, "addTime");			
			serializer.text(mymessage.getAddTime().toString());
			serializer.endTag(null, "addTime");
			
			serializer.startTag(null, "content");			
			serializer.text(mymessage.getContent().toString());
			serializer.endTag(null, "content");
			
			serializer.startTag(null, "id");			
			serializer.text(mymessage.getId().toString());
			serializer.endTag(null, "id");
			
			serializer.startTag(null, "picture");			
			serializer.text(mymessage.getPicture().toString());
			serializer.endTag(null, "picture");
			
			serializer.startTag(null, "pushFor");			
			serializer.text(mymessage.getPushFor().toString());
			serializer.endTag(null, "pushFor");
			
			serializer.startTag(null, "pushFrom");			
			serializer.text(mymessage.getPushFrom().toString());
			serializer.endTag(null, "pushFrom");
			
			serializer.startTag(null, "pushFromType");			
			serializer.text(mymessage.getPushFromType().toString());
			serializer.endTag(null, "pushFromType");
			
			serializer.startTag(null, "pushTo");			
			serializer.text(mymessage.getPushTo().toString());
			serializer.endTag(null, "pushTo");
			
			serializer.startTag(null, "status");			
			serializer.text(mymessage.getStatus().toString());
			serializer.endTag(null, "status");
			
			serializer.endTag(null, "mymessage");
		}		
		serializer.endTag(null, "mymessage");
		serializer.endDocument();
		out.flush();
		out.close();
	}
}
