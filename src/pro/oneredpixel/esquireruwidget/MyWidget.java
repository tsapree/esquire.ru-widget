package pro.oneredpixel.esquireruwidget;

import java.util.concurrent.TimeUnit;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;

//TODO: избавиться от String+=String
//TODO: использовать StringBuffer

public class MyWidget extends AppWidgetProvider {
	
	final static String ACTION_REFRESH = "pro.oneredpixel.esquireruwidget.refresh";
	final static String ACTION_REFRESH_DONE = "pro.oneredpixel.esquireruwidget.refreshdone";
	
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
	
	public void onReceive(Context context, Intent intent) {
	    super.onReceive(context, intent);
	    if (intent.getAction().equalsIgnoreCase(ACTION_REFRESH)) {
	    	SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
		    Editor editor = sp.edit();
		    editor.putLong("RefreshTime",0);
		    editor.commit();
		    //TODO: у меня два класса, MyWidget & MyWidgetLarge - но обновляется пока только MyWidget
	        ComponentName thisAppWidget = new ComponentName(
	            context.getPackageName(), getClass().getName());
	        AppWidgetManager appWidgetManager = AppWidgetManager
	            .getInstance(context);
	        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
	        for (int appWidgetID : ids) {
	          //updateWidget(context, appWidgetManager, appWidgetID);
	        	RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
	        	//TODO: временно
	        	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
	        		//widgetView.showNext(R.id.viewFlipper);
	        	}	        	
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
	    if (intent.getAction().equalsIgnoreCase(ACTION_REFRESH_DONE)) {
	    	SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
		    Editor editor = sp.edit();
		    editor.putLong("RefreshTime",0);
		    editor.commit();
		    //TODO: у меня два класса, MyWidget & MyWidgetLarge - но обновляется пока только MyWidget
	        ComponentName thisAppWidget = new ComponentName(
	            context.getPackageName(), getClass().getName());
	        AppWidgetManager appWidgetManager = AppWidgetManager
	            .getInstance(context);
	        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
	        for (int appWidgetID : ids) {
	          updateWidget(context, appWidgetManager, appWidgetID);
	        	//RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
	        	//widgetView.setViewVisibility(R.id.ibRefresh, View.VISIBLE);
	    		//appWidgetManager.updateAppWidget(appWidgetID, widgetView);
	        }
	      }
	    
	  }
	
	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
		       int widgetID) {
		
		RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_holder);
		
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
	    //widgetView.setImageViewUri(R.id.ivIssueImage, Uri.parse(sp.getString("IssuePic", "")));
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
		
		
		//ViewFlipper vf = (ViewFlipper)findViewById(R.id.viewFlipper1);
		
		//LayoutInflater inflater = getLayoutInflater();
		
		//View view1 = (View)inflater.inflate(R.layout.view1, null);
		//View view2 = (View)inflater.inflate(R.layout.view2, null);
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
	    protected void onPreExecute() {
	      super.onPreExecute();
	      //tvInfo.setText("Begin");
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
	      //tvInfo.setText("End");
	      if (pIntent!=null)
			try {
				pIntent.send();
			} catch (CanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	  }
}
