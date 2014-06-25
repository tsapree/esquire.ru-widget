package pro.oneredpixel.esquireruwidget;


public class WebSqueezer {

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
	*/
}
