package com.example.student.training;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Student on 2018/1/10.
 */

public class MyHandler extends DefaultHandler { //自己創一個MyHandler Class，要繼承DefaultHandler(org.xml.sax.helpers)，
    boolean isTitle = false;
    boolean isItem = false;     //抓出來的第一行Mobile01 本站新聞不是頭條，所以要避開他
    boolean isLink = false;
    boolean isDescription = false;
    StringBuilder linkSB = new StringBuilder();

    StringBuilder descSB = new StringBuilder();     //修復抓兩次description而閃退的bug

    public ArrayList<EttodayNewsItem> newsItems = new ArrayList<>();
    EttodayNewsItem item;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        switch(qName)       //JAVA7之後，switch可放字串
        {
            case "h3":
                isTitle = true;
                break;
            case "data-original=\"":
                isItem = true;
                item = new EttodayNewsItem();
                break;
            case "<a href=\"":
                isLink = true;
                break;
            case "<p class=\"summary\">":
                isDescription = true;

                descSB = new StringBuilder();       //修復抓兩次description而閃退的bug

                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch(qName)
        {
            case "h3":
                isTitle = false;
                break;
            case "data-original=\"":
                isItem = false;
                newsItems.add(item);
                break;
            case "class=\"pic\">":
                isLink = false;
                if (isItem)
                {
                    item.link = linkSB.toString();
                    linkSB = new StringBuilder();
                }
                break;
            case "</p>\n":
//                發生了重抓兩次description，導致圖抓也了兩次，第二次為空白，所以閃退的bug
//                isDescription = false;

                if (isItem)
                {
                    String str = descSB.toString();
                    Log.d("NET", "end Element str:" + str);
                    Pattern pattern = Pattern.compile("https.*jpg");
                    Matcher m = pattern.matcher(str);
                    String imgurl = "";
                    if (m.find())
                    {
                        imgurl = m.group(0);
                    }
                    str = str.replaceAll("<img.*/>", "");
                    item.description = str;
                    item.imgurl = imgurl;
                    Log.d("NET", "In Handler: Item.desc:" + item.description);
                    Log.d("NET", "In Handler: Item.imgurl:" + item.imgurl);
                }

                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(isTitle && isItem)
        {
            Log.d("NET1",new String(ch,start,length));
//            titles.add(new String(ch,start,length));
            item.title = new String(ch,start,length);
        }
        if(isLink && isItem)
        {
            Log.d("NET2",new String(ch,start,length));
            linkSB.append(new String(ch,start,length));
        }
        if (isDescription && isItem)
        {
            descSB.append(new String(ch, start, length));
        }
    }
}
