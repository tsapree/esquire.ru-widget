package pro.oneredpixel.esquireruwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;



public class MyWidget extends AppWidgetProvider {
	
	static int layouts[]={
			R.layout.widget_about,
			R.layout.widget_number,
			R.layout.widget_quote,
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
		
		int layout=layouts[(int)(Math.random()*layouts.length)];
		RemoteViews widgetView = new RemoteViews(context.getPackageName(),
		        layout);
		switch (layout) {
		case R.layout.widget_number:
			
			//int numberFontSize=16;
			//int otherFontSize=8;
			int numberFontSize=30;
			int otherFontSize=12;

			widgetView.setTextViewText(R.id.tvValue, "22%");
			widgetView.setTextViewText(R.id.tvDesc, "Столько жителей Рио де Жанейро настолько недовольны фактом проведения Чемпионата Мира по футболу в Бразилии, что желают проигрыша собственной сборной.");
			widgetView.setViewVisibility(R.id.tvUnits, View.GONE);
			widgetView.setFloat(R.id.tvValue, "setTextSize", numberFontSize);
			widgetView.setFloat(R.id.tvDesc, "setTextSize", otherFontSize);
			widgetView.setFloat(R.id.tvAbout, "setTextSize", otherFontSize);
			//widgetView.setf
			break;
		case R.layout.widget_quote:
			widgetView.setTextViewText(R.id.tvText,"«Слово Сталинград уже живет жизнью, независимой от имени Сталина»");
			widgetView.setTextViewText(R.id.tvAuthorName,"Всеволод Чаплин");
			widgetView.setTextViewText(R.id.tvAuthorDesc,"Протоиерей");
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
		    
	}
}
