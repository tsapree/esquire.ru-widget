package pro.oneredpixel.esquireruwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;



public class MyWidget extends AppWidgetProvider {
	
	static int layouts[]={
			R.layout.widget_about,
			R.layout.widget_number,
			R.layout.widget_quote,
			R.layout.widget_discoveries,
			R.layout.widget_rules,
			R.layout.widget_issue,
	};
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		      int[] appWidgetIds) {
		    super.onUpdate(context, appWidgetManager, appWidgetIds);
		    
		            for (int id : appWidgetIds) {
		          updateWidget(context, appWidgetManager, id);
		        }
	}
	
	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
		       int widgetID) {
		/*
		int layout=layouts[(int)(Math.random()*layouts.length)];
		
	    SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
	    String numberValue=sp.getString("NumberValue", null);
	    String quoteText=sp.getString("QuoteText", null);
	    
	    //TODO: kill this lame please
	    if ((layout==R.layout.widget_number) && (numberValue==null)) layout=R.layout.widget_about;
	    if ((layout==R.layout.widget_quote) && (quoteText==null)) layout=R.layout.widget_about;
	    
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), layout);
		switch (layout) {
		case R.layout.widget_number:
			
			//int numberFontSize=16;
			//int otherFontSize=8;
			int numberFontSize=30;
			int otherFontSize=12;

			fillText(widgetView, R.id.tvValue, "NumberValue", sp);
			fillText(widgetView, R.id.tvDesc, "NumberDesc", sp);
			fillText(widgetView, R.id.tvUnits, "NumberUnits", sp);
			
			widgetView.setFloat(R.id.tvValue, "setTextSize", numberFontSize);
			widgetView.setFloat(R.id.tvDesc, "setTextSize", otherFontSize);
			widgetView.setFloat(R.id.tvAbout, "setTextSize", otherFontSize);
			//widgetView.setf
			break;
		case R.layout.widget_quote:
			fillText(widgetView, R.id.tvText, "QuoteText", sp);
			fillText(widgetView, R.id.tvAuthorName, "QuoteAuthorName", sp);
			fillText(widgetView, R.id.tvAuthorDesc, "QuoteAuthorDesc", sp);
			break;
		case R.layout.widget_rules:
			fillText(widgetView, R.id.tvAuthorName, "RulesAuthorName", sp);
			fillText(widgetView, R.id.tvDesc, "RulesDesc", sp);
			break;
		case R.layout.widget_discoveries:
			fillText(widgetView, R.id.tvText, "DiscoveriesText", sp);
			break;
		case R.layout.widget_issue:
			fillText(widgetView, R.id.tvDesc, "IssueDesc", sp);
			break;
		}
		
		
		// Обновление виджета (вторая зона)
	    Intent updateIntent = new Intent(context, MyWidget.class);
	    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
	        new int[] { widgetID });
	    PendingIntent pIntent = PendingIntent.getBroadcast(context, widgetID, updateIntent, 0);
	    widgetView.setOnClickPendingIntent(R.id.rlWidget, pIntent);
		
		appWidgetManager.updateAppWidget(widgetID, widgetView);
			
		    //widgetView.setTextViewText(R.id.tv, widgetText);
		    //widgetView.setInt(R.id.tv, "setBackgroundColor", widgetColor);
		    
		    // Обновляем виджет
		*/
		
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
		Intent updateIntent = new Intent(context, MyWidget.class);
	    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
	        new int[] { widgetID });
	    PendingIntent pIntent = PendingIntent.getBroadcast(context, widgetID, updateIntent, 0);
	    widgetView.setOnClickPendingIntent(R.id.rlWidget, pIntent);

	    //widgetView.setViewVisibility(R.id.widgetAbout, View.INVISIBLE);
	    
	    SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
	    fillText(widgetView, R.id.tvDiscoveriesText, "DiscoveriesText", sp);
	    
	    fillText(widgetView, R.id.tvIssueDesc, "IssueDesc", sp);
	    
	    fillText(widgetView, R.id.tvNumberValue, "NumberValue", sp);
		fillText(widgetView, R.id.tvNumberDesc, "NumberDesc", sp);
		fillText(widgetView, R.id.tvNumberUnits, "NumberUnits", sp);
		
		fillText(widgetView, R.id.tvQuoteText, "QuoteText", sp);
		fillText(widgetView, R.id.tvQuoteAuthorName, "QuoteAuthorName", sp);
		fillText(widgetView, R.id.tvQuoteAuthorDesc, "QuoteAuthorDesc", sp);
		
		fillText(widgetView, R.id.tvRulesAuthorName, "RulesAuthorName", sp);
		fillText(widgetView, R.id.tvRulesDesc, "RulesDesc", sp);

		appWidgetManager.updateAppWidget(widgetID, widgetView);
		
		
		//ViewFlipper vf = (ViewFlipper)findViewById(R.id.viewFlipper1);
		
		//LayoutInflater inflater = getLayoutInflater();
		
		//View view1 = (View)inflater.inflate(R.layout.view1, null);
		//View view2 = (View)inflater.inflate(R.layout.view2, null);
	}
	
	static void fillText(RemoteViews rv, int id, String key, SharedPreferences sp) {
		String txt=sp.getString(key, null);
		if (txt!=null) rv.setTextViewText(id, Html.fromHtml(txt).toString());
		
		else rv.setViewVisibility(id, View.GONE);		
	}
}
