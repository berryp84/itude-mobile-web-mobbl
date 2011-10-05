package com.itude.mobile.template.util;

import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.model.MBElementContainer;

public class MCDSUtilities
{
	public static void setPositions(MBDocument document, String type, int startFrom)
	{
		int count = document.getElementsWithName(type).size();
		for (int i = 0; i < count; i++)
		{
			String value = ""+(i+1+startFrom);
			String path = "/"+type+"["+i+"]/@position";
			document.setValue(value, path);
		}
	}
	
	public static void setPositionsForSearchResults(MBDocument document)
	{
		int realtoneCount = ((MBElementContainer) document.getValueForPath("/realtones[0]")).getElementsWithName("realtone").size();
		document.setValue(""+realtoneCount, "/realtones[0]/@count");
		for (int i = 0; i < realtoneCount; i++)
		{
			String value = ""+(i+1);
			String path = "/realtones[0]/realtone["+i+"]/@position";
			document.setValue(value, path);
		}
		int truetoneCount = ((MBElementContainer) document.getValueForPath("/truetones[0]")).getElementsWithName("truetone").size();
		document.setValue(""+truetoneCount, "/truetones[0]/@count");
		for (int i = 0; i < truetoneCount; i++)
		{
			String value = ""+(i+1);
			String path = "/truetones[0]/truetone["+i+"]/@position";
			document.setValue(value, path);
		}
		int polytoneCount = ((MBElementContainer) document.getValueForPath("/polytones[0]")).getElementsWithName("polytone").size();
		document.setValue(""+polytoneCount, "/polytones[0]/@count");
		for (int i = 0; i < polytoneCount; i++)
		{
			String value = ""+(i+1);
			String path = "/polytones[0]/polytone["+i+"]/@position";
			document.setValue(value, path);
		}
		int javagameCount = ((MBElementContainer) document.getValueForPath("/javagames[0]")).getElementsWithName("javagame").size();
		document.setValue(""+javagameCount, "/javagames[0]/@count");
		for (int i = 0; i < javagameCount; i++)
		{
			String value = ""+(i+1);
			String path = "/javagames[0]/javagame["+i+"]/@position";
			document.setValue(value, path);
		}
		int wallpaperCount = ((MBElementContainer) document.getValueForPath("/wallpapers[0]")).getElementsWithName("wallpaper").size();
		document.setValue(""+wallpaperCount, "/wallpapers[0]/@count");
		for (int i = 0; i < wallpaperCount; i++)
		{
			String value = ""+(i+1);
			String path = "/wallpapers[0]/wallpaper["+i+"]/@position";
			document.setValue(value, path);
		}
		int minimovieCount = ((MBElementContainer) document.getValueForPath("/minimovies[0]")).getElementsWithName("minimovie").size();
		document.setValue(""+minimovieCount, "/minimovies[0]/@count");
		for (int i = 0; i < minimovieCount; i++)
		{
			String value = ""+(i+1);
			String path = "/minimovies[0]/minimovie["+i+"]/@position";
			document.setValue(value, path);
		}
	}
	
	public static void resizeImages(MBDocument document, String type, int width, int height)
	{
		int count = document.getElementsWithName(type).size();
		for (int i = 0; i < count; i++)
		{
			String contendId = document.getValueForPath("/"+type+"["+i+"]/id[0]/@text()");
			String value = "http://dl.mob24-7.com/system/cached_image/"+type+"/"+contendId+"/"+width+"x"+height+"q60.gif";
			String path = "/"+type+"["+i+"]/thumbnail[0]/@text()";
			document.setValue(value, path);
		}
	}

}
