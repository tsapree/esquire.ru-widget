package pro.oneredpixel.esquireruwidget;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


//<span class="today-notd-number">147000</span>
//<div class="today-notd-units">рублей</div>
//<div class="today-notd-description">Такова цена смартфона iPhone 5s&nbsp;Supremo Putin с&nbsp;позолоченной задней крышкой и&nbsp;гравировкой портрета Владимира Путина, выпущенного итальянским ювелирным брендом Caviar.</div>

//<div class="today-quote">“Никто не&nbsp;собирался эти деньги возвращать, потому что эти деньги пошли на&nbsp;Крым, на&nbsp;принятие антикризисных мер”</div>
//<div class="today-quote-author-photo">
//<img src="/files/cache/images/6f/cc/b401774c.crop355x355x131x0-fit40xNone-mask_circle.a4a930.expert_789_006-1.png" width="40" height="40"    >
//</div>
//<div class="today-quote-author-name">Антон Силуанов</div>
//<div class="today-quote-author-description">Глава Минфина об&nbsp;отсутствии средств для возврата замороженных пенсионных накоплений россиян за&nbsp;2014 год негосударственным пенсионным фондам (НПФ)</div>

//<div class="today-science">
//<a href="/discoveries/">Люди, не&nbsp;слишком уверенные в&nbsp;существовании свободы воли, склонны требовать для преступников меньшего наказания.</a>
//</div>

//<div class="index-wilpost-big">
//<div class="index-date">
//    <time class="timeago" datetime="2014-06-24T12:47:41+0400"></time>
//</div>
//<div class="index-wilpost-head">
//    <header class="index-wilpost-title">
//        <a href="/wil/lazareva">Татьяна Лазарева</a>
//    </header>
//    <div class="index-wilpost-rating">
//        <div class="rating-value" data-rating-values="{&quot;twitter&quot;: &quot;68&quot;, &quot;vk&quot;: &quot;788&quot;, &quot;facebook&quot;: &quot;3591&quot;}">4.4K</div>
//    </div>
//    <div class="clear"></div>
//</div>
//<div class="index-wilpost-big-photo">
//    <a href="/wil/lazareva">
//        <img src="/files/cache/images/ca/8c/9f2f7bf8.bw-crop2275x2275x0x421-fit524xNone.9b6b5e.lazareva-bw.jpg" width="524" height="524"  style="background-image: url(data:image/gif;base64,R0lGODdhBAAEAIIAAJmZmczMzP//zMzM/8z//////wAAAAAAACwAAAAABAAEAAAIFQALBAhQIACA%0AAAQEAAhAYECAAAUCAgA7%0A)"  >
//    </a>
//</div>
//<div class="index-wilpost-excerpt">“Женщинам сложно в&nbsp;юмористическом жанре. Они не&nbsp;любят, когда над ними смеются. Они созданы, чтобы ими восхищались”
//        <br />
//        <a class="index-wilpost-link" href="/wil/lazareva#comments"><nobr>8 комментариев</nobr></a>
//    
//</div>
//</div>

//<ul class="static-pages cover">
//<li>
//   <div class="ipad-footer">
//        <a class="cover_link" href="/current" data-href="/issue/101">
//            <img src="/files/cache/images/1b/1f/72c15e37.fit111xNone.7df8e8.cover-101-ipad.jpg" width="111" height="148"  style="background-image: url(data:image/gif;base64,R0lGODdhBAAEAIIAADMzM2YzZjNmZmZmZmZmmWaZmQAAAAAAACwAAAAABAAEAAAIEgAFCCQwoGAB%0AAAEAABgwAACAgAA7%0A)"  >
//        </a>
//        <a class="appstore" href="https://itunes.apple.com/ru/app/esquire-rossia/id722784882" data-href="/issue/101">
//            <img src="/static/images/appstore.png" width="100" />
//        </a>
//    </div>
//</li>
//</ul>
//<ul class="static-pages" style="width:443px;margin-right:100px;">
//<li>
//    <span id="adults-only"><a href="/issue/101" data-href="/issue/101" id="footer-issue-link" style="text-decoration:none">Свежий номер</a></span>
//    <a class="current-issue-description" style="margin-top:11px;" data-ipad-excerpt="Как больной шизофренией стал профессором права, как глава крымских татар не&nbsp;договорился с&nbsp;Владимиром Путиным, как выглядит Ибица после дискотеки. Кроме того: сборные самых выдающихся игроков чемпионата мира по&nbsp;футболу, война, любовь и&nbsp;другие подцензурные темы, а&nbsp;также правила жизни Райана Гослинга, Алана Мура и&nbsp;Татьяны Лазаревой." href="/current" data-href="/issue/101">
//        
//            Как больной шизофренией стал профессором права, как глава крымских татар не&nbsp;договорился с&nbsp;Владимиром Путиным, как выглядит Ибица после дискотеки. Кроме того: сборные самых выдающихся игроков чемпионата мира по&nbsp;футболу, война, любовь и&nbsp;другие подцензурные темы, а&nbsp;также правила жизни Райана Гослинга, Алана Мура и&nbsp;Татьяны Лазаревой.
//        
//    </a>
//</li>
//</ul>

public class WebSqueezer {

	static final int READING_BUFFER_SIZE=1024;
	static final int CHECKING_BUFFER_SIZE=512;
	
	public void updateStorage(Context context) {
		InputStream is = context.getResources().openRawResource(R.raw.esquireru);
		
		String numberValue=null;
		String numberUnits=null;
		String numberDesc=null;
		
		char[] chars = new char[READING_BUFFER_SIZE];
		int readedSize;
		int i=0;
		int t=0;
		int t1=0;
		
		try {
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			do {
				readedSize=isr.read(chars,i,READING_BUFFER_SIZE-i);
				if (readedSize<0) readedSize=i;
				else readedSize+=i;
				if (readedSize<=0) break;
				i=0;
				
				while (i<CHECKING_BUFFER_SIZE) {
					if (chars[i++]!='<') continue;
					t = findTagValue("span ","class=\"today-notd-number\"", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</span>",chars,t,readedSize-1);
						if (t1<0) continue;
						numberValue=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};
					t = findTagValue("div ","class=\"today-notd-units\"", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						numberUnits=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};
					t = findTagValue("div ","class=\"today-notd-description\"", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						numberDesc=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};
					
				};
				
				t=0;
				while (i<readedSize) {
					chars[t]=chars[i];
					t++;
					i++;
				};
				i=t;
				
			} while (true);

		} catch (UnsupportedEncodingException e) {
			//
		} catch (IOException e) {
			//
		} finally {
		    SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);
		    Editor editor = sp.edit();
		    editor.putString("NumberValue", numberValue);
		    editor.putString("NumberUnits", numberUnits);
		    editor.putString("NumberDesc", numberDesc);
		    editor.commit();

		}

	
	};
	
	
	/*
	static final int READING_BUFFER_SIZE=1024;
	static final int CHECKING_BUFFER_SIZE=512;
	static final int COUNT_OF_UPDATE_RECENT_ENTRIES=10;
	
	public void readNumbers(InputStream is, Boolean onlyRecent) {
		char[] chars = new char[READING_BUFFER_SIZE];
		int readedSize;
		int i=0;
		int t=0;
		int t1=0;
		int recentDate=0;
		int currentDate=0;
		
		Number nmb=new Number();
		
		try {
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			try {recentDate=Integer.valueOf(getDateOfRecentNumber());}	catch (NumberFormatException e) {	}

			i=0;
			do {
				readedSize=isr.read(chars,i,READING_BUFFER_SIZE-i);
				if (readedSize<0) readedSize=i;
				else readedSize+=i;
				if (readedSize<=0) break;
				i=0;
				
				while (i<CHECKING_BUFFER_SIZE) {
					if (chars[i++]!='<') continue;					
					t = compareToKey("span class=\"number-value\">", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</span>",chars,t,readedSize-1);
						if (t1<0) continue;
						nmb=new Number();
						nmb.value=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};
					//some optimisation
					t = compareToKey("div class=\"number-", chars, i, readedSize-1);
					if (t<0) continue;
					i=t;
					
					t = compareToKey("units\">", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						nmb.units=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};
					
					t = compareToKey("desc\">", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						nmb.desc=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};

					t = compareToKey("source\">", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						nmb.source=String.copyValueOf(chars, t, t1-t);
						i=t1+1;
						continue;
					};

					t = compareToKey("date\">", chars, i, readedSize-1);
					if (t>0) {
						t1=findKey("</div>",chars,t,readedSize-1);
						if (t1<0) continue;
						nmb.date=convertDateFromHtml(String.copyValueOf(chars, t, t1-t));
						if (nmb.date!=null) insertOrUpdateNumber(nmb.value, nmb.units, nmb.desc, nmb.source, nmb.date);
						i=t1+1;
						try {
							currentDate=Integer.valueOf(getDateOfRecentNumber());
							if (currentDate<recentDate-COUNT_OF_UPDATE_RECENT_ENTRIES) break;
						} catch (NumberFormatException e) {
						}
						continue;
					};
				};
				if (currentDate<recentDate-COUNT_OF_UPDATE_RECENT_ENTRIES) break;
				
				t=0;
				while (i<readedSize) {
					chars[t]=chars[i];
					t++;
					i++;
				};
				i=t;
				
			} while (true);

		} catch (UnsupportedEncodingException e) {
			//
		} catch (IOException e) {
			//
		} finally {
			
		}
	
	}
	
	*/
	
	
	//Сравниваю массив с ключом. при этом один пробел в ключе может быть равен любому количеству пробелов и табуляций в проверяемом массиве.
	//В будущем можно сравнивать независимо от регистра... и еще чего добавить.
	//возвращаю индекс следующего символа после подтвержденной последовательности, либо -1 если нет совпадения.
	int compareToKey(String key, char[] c, int index, int maximum) {
		int k=0;
		while (k<key.length()) {
			switch (key.charAt(k)) {
			case ' ':
				boolean spacesFound=false;
				if ((c[index]!=' ') && (c[index]!=8)) {
					if (spacesFound==false) return -1;
				} else {
					spacesFound=true;
					index++;
					if (index>maximum) return -1;
				}
				k++;
				break;
			default:
				if (c[index]!=key.charAt(k)) return -1;
				k++;
				index++;
				if (index>maximum) return -1;
			}
		};
		return index;
	}

	//ищет последовательность key до конца массива. если встретился - возвращает индекс, где. иначе -1
	int findKey(String key, char[] c, int index, int maximum) {
		while (index<maximum) {
			if (compareToKey(key,c,index,maximum)>0) return index;
			index++;
		}
		return -1;
	}
	
	//convert date from DD/MM/YYYY format to YYYYMMDD
	String convertDateFromHtml(String htmldate) {
		if ((htmldate.length()==10) && (htmldate.charAt(2)=='/') && (htmldate.charAt(5)=='/'))
			return htmldate.substring(6, 10)+htmldate.substring(3,5)+htmldate.substring(0, 2);
		else return null;
	}
	
	//проверка в массиве начиная с index наличия слова tag, если да - ищу наличие key до встречи символа >, если да - возвращаю позицию после > 
	int findTagValue(String tag, String key, char[] c, int index, int maximum) {
		//проверяю начало тэга
		int i=findKey(tag, c, index, maximum);
		if (i<0) return i;
		
		//проверяю, что тэг содержит ключевое слово
		if (key!=null) {
			int m;
			while (true) {
				if (c[i]=='>') return -1;
				m=compareToKey(key,c,i,maximum);
				i++;
				if (m>0) {
					i=m;
					break;
				};
				if (i==maximum) return -1;
			}
		};
		
		//ищу окончание тэга
		while (i<maximum) {
			if (c[i++]=='>') return i;
		}
		return -1;
	}
}