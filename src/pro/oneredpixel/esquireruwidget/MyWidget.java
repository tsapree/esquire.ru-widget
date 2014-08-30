package pro.oneredpixel.esquireruwidget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;

//TODO: избавиться от String+=String
//TODO: использовать StringBuffer

public class MyWidget extends AppWidgetProvider {
	
	final static String ACTION_REFRESH = "pro.oneredpixel.esquireruwidget.refresh";
	final static String ACTION_REFRESH_DONE = "pro.oneredpixel.esquireruwidget.refreshdone";
	final static int AUTO_REFRESH_DELAY = 6*60*60*1000; //каждые 6 часов разрешать обновление виджета
	
	/*
	static int layouts[]={
			R.layout.widget_about,
			R.layout.widget_number,
			R.layout.widget_quote,
			R.layout.widget_discoveries,
			R.layout.widget_rules,
			R.layout.widget_issue,
	};
	*/
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		      int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int id : appWidgetIds) {
			updateWidget(context, appWidgetManager, id);
		}
		SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
		if ((sp.getLong("RefreshTime", 0)+AUTO_REFRESH_DELAY)<System.currentTimeMillis())
			startRefresh(context);
		

	}
	
	public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    if (intent.getAction().equalsIgnoreCase(ACTION_REFRESH)) {
	        startRefresh(context);
	    } else if (intent.getAction().equalsIgnoreCase(ACTION_REFRESH_DONE)) {
	        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
	        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
	        for (int appWidgetID : ids) {
	        	updateWidget(context, appWidgetManager, appWidgetID);
	        }
	    }
	}
	
	public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
		updateWidget(context, appWidgetManager, appWidgetId);
	}
	
	
	public void startRefresh(Context context) {
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        for (int appWidgetID : ids) {
        	RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
        	widgetView.setViewVisibility(R.id.ibRefresh, View.GONE);
    		appWidgetManager.updateAppWidget(appWidgetID, widgetView);
        }
        Intent refreshDoneIntent = new Intent(context, MyWidget.class);
	    refreshDoneIntent.setAction(ACTION_REFRESH_DONE);
	    PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, refreshDoneIntent, 0);
	    MyTask mt = new MyTask();
	    mt.attachIntent(pIntent , context);
        mt.execute();
	}
	
	@SuppressLint("NewApi")
	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
		       int widgetID) {
		boolean landscape = true;
		RemoteViews widgetView;
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			Bundle opt = appWidgetManager.getAppWidgetOptions(widgetID);
			int w = (Integer)opt.get(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH );
			int h = (Integer)opt.get(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT );
			if ((h>0) && ((w*100/h)<120)) landscape = false; 
		}
		
		if (landscape) widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder_land);
		else widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
		
		//обновление виджета, вызов метода onUpdate 
		Intent updateIntent = new Intent(context, MyWidget.class);
	    updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
	        new int[] { widgetID });
	    PendingIntent pIntent = PendingIntent.getBroadcast(context, widgetID, updateIntent, 0);
	    widgetView.setOnClickPendingIntent(R.id.rlWidget, pIntent);
	    
	    //отправка запроса на обновление данных
	    Intent refreshIntent = new Intent(context, MyWidget.class);
	    refreshIntent.setAction(ACTION_REFRESH);
	    refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { widgetID });
	    pIntent = PendingIntent.getBroadcast(context, widgetID, refreshIntent, 0);
	    widgetView.setOnClickPendingIntent(R.id.ibRefresh, pIntent);
	    
	    SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
	    fillText(widgetView, R.id.tvDiscoveriesText, "DiscoveriesText", sp);
	    
	    fillText(widgetView, R.id.tvIssueDesc, "IssueDesc", sp);
	    String path=sp.getString("IssuePic", "");
		widgetView.setImageViewResource(R.id.ivIssueImage, R.drawable.esquire);
	    widgetView.setImageViewUri(R.id.ivIssueImage, Uri.parse(path));	    	    
	    
	    fillText(widgetView, R.id.tvNumberValue, "NumberValue", sp);
		fillText(widgetView, R.id.tvNumberDesc, "NumberDesc", sp);
		fillText(widgetView, R.id.tvNumberUnits, "NumberUnits", sp);
		
		fillText(widgetView, R.id.tvQuoteText, "QuoteText", sp);
		fillText(widgetView, R.id.tvQuoteAuthorName, "QuoteAuthorName", sp);
		fillText(widgetView, R.id.tvQuoteAuthorDesc, "QuoteAuthorDesc", sp);
		
		fillText(widgetView, R.id.tvRulesAuthorName, "RulesAuthorName", sp);
		fillText(widgetView, R.id.tvRulesDesc, "RulesDesc", sp);
		String rulesPicPath=sp.getString("RulesAuthorPic", "");

		widgetView.setImageViewResource(R.id.ivRulesPhoto, R.drawable.esquire);
		widgetView.setImageViewUri(R.id.ivRulesPhoto, Uri.parse(rulesPicPath));

		widgetView.setViewVisibility(R.id.ibRefresh, View.VISIBLE);
		appWidgetManager.updateAppWidget(widgetID, widgetView);
		
	}
	
	static void fillText(RemoteViews rv, int id, String key, SharedPreferences sp) {
		String txt=sp.getString(key, null);
		if (txt!=null) {
			rv.setTextViewText(id, Html.fromHtml(txt).toString());
			rv.setViewVisibility(id,View.VISIBLE);
		}
		
		else rv.setViewVisibility(id, View.GONE);
	}
	
	class MyTask extends AsyncTask<Void, Void, Void> {
		PendingIntent pIntent;
		Context context;
		
		void attachIntent(PendingIntent pi, Context c) {
			pIntent = pi;
			context = c;
		}
		
	    @Override
	    protected Void doInBackground(Void... params) {
	    	WebSqueezer ws = new WebSqueezer();
	    	ws.updateStorageNew(context, true);
	      return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      if (pIntent!=null)
			try {
				pIntent.send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}
	    }
	  }
}
