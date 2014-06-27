package pro.oneredpixel.esquireruwidget;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.bFill).setOnClickListener(this);
		findViewById(R.id.bClear).setOnClickListener(this);
		findViewById(R.id.bDecode).setOnClickListener(this);
		findViewById(R.id.bEsquire).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
	    SharedPreferences sp = getSharedPreferences("widget_data", MODE_PRIVATE);
	    Editor editor = sp.edit();
		switch (v.getId()) {
		case R.id.bFill:
		    editor.putString("NumberValue", "65 000");
		    editor.putString("NumberUnits", "рублей");
		    editor.putString("NumberDesc", "Такова стоимость терминала записи на прием к врачу, похищенного из больницы в городе Ленинске-Кузнецком. По одной из версий следствия, похитители могли принять терминал за мультикассу, в которой хранились деньги.");
		    
		    editor.putString("QuoteText", "“Для туристов лакшери и премиум-классов Крым может предложить только климат”");
		    editor.putString("QuoteAuthorName", "Алла Манилова");
		    editor.putString("QuoteAuthorDesc", "Заместитель министра культуры РФ, курирующая сферу туризма");

		    editor.commit();
			break;
		case R.id.bDecode:
			WebSqueezer ws=new WebSqueezer();
			ws.updateStorage(this,false);
			break;
		case R.id.bEsquire:
			WebSqueezer ws1=new WebSqueezer();
			ws1.updateStorage(this,true);
			break;
		case R.id.bClear:
		    editor.clear();
		    editor.commit();
			break;
		}		
	}
	
	
}
