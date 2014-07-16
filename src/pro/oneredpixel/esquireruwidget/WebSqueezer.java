package pro.oneredpixel.esquireruwidget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;


public class WebSqueezer {

	static final int READING_BUFFER_SIZE=8192;
	static final int TAIL_BUFFER_SIZE=3000;
	private static final String STR_DIV="div";
	private static final String STR_CLASS="class";
	private static final String STR_TODAY_NOTD_UNITS="today-notd-units";
	private static final String STR_TODAY_NOTD_DESCRIPTION="today-notd-description";
	private static final String STR_QUOTE="today-quote";
	
	
	public void updateStorageNew(Context context, boolean fromWeb) {
		String numberValue=null;
		String numberUnits=null;
		String numberDesc=null;
		
		String quoteText = null;
	    String quoteAuthorName = null;
	    String quoteAuthorDesc = null;
	    
	    String rulesAuthorName = null;
	    String rulesDesc = null;
	    String rulesAuthorPic = null;
	    String rulesAuthorPicLink = null;
	    
	    String discoveriesText = null;
	    
	    String issueDesc = null;
	    String issuePic = null;
	    String issuePicLink = null;
		
		TaggedStream ts=new TaggedStream();
		
		if (fromWeb) ts.openConnection("http://esquire.ru");
		else ts.openResourceConnection(context, R.raw.esquireru);

		String className;
		
		while (ts.nextTag()) {
			if (ts.getTagName().equalsIgnoreCase(STR_DIV)) {
				className=ts.findKeyValueByName(STR_CLASS);
				if (className!=null) {
					if (className.equals(STR_TODAY_NOTD_UNITS))
						numberUnits=ts.getTagValue();
					else if (className.equals(STR_TODAY_NOTD_DESCRIPTION))
						numberDesc=ts.getTagValue();
					
					else if ((quoteText==null) && (className.equals(STR_QUOTE)))
						quoteText=ts.getTagValue();
					else if ((quoteAuthorName==null) && (className.equals("today-quote-author-name")))
						quoteAuthorName=ts.getTagValue();
					else if ((quoteAuthorDesc==null) && (className.equals("today-quote-author-description")))
						quoteAuthorDesc=ts.getTagValue();
					
					else if (className.equals("index-wilpost-big-photo")) {
						ts.nextTag();
						ts.nextTag();
						rulesAuthorPicLink=ts.findKeyValueByName("src");
					}
					else if (rulesDesc==null && (className.equals("index-wilpost-excerpt"))) 
						rulesDesc = ts.getTagValue();
					
					else if ((discoveriesText==null) && (className.equals("today-science"))) {
						ts.nextTag();
						discoveriesText = ts.getTagValue();
					}
					
				}
			} else if (ts.getTagName().equalsIgnoreCase("span")) {
				className=ts.findKeyValueByName("class");
				if (className!=null) {
					if (className.equals("today-notd-number"))
						numberValue=ts.getTagValue();
				}
			} else if (ts.getTagName().equalsIgnoreCase("a")) {
				className=ts.findKeyValueByName("class");
				if (className!=null) {
					if (className.equals("cover_link")) {
						ts.nextTag();
						if (ts.getTagName().equalsIgnoreCase("img")) 
							issuePicLink=ts.findKeyValueByName("src");
					} else if (className.equals("current-issue-description")) {
						issueDesc=ts.getTagValue();
					}
				}
			}

			else if ((rulesAuthorName==null) && (ts.getTagName().equalsIgnoreCase("header"))) {
				className=ts.findKeyValueByName("class");
				if (className!=null) {
					if (className.equals("index-wilpost-title")) {
						ts.nextTag();
						rulesAuthorName=ts.getTagValue();
					}
				}
			}
			
			
			
		}

		SharedPreferences sp = context.getSharedPreferences("widget_data", Context.MODE_PRIVATE);

		if ((issuePicLink!=null) && (!issuePicLink.equals(sp.getString("IssuePicLink", ""))))
			issuePic = downloadFileToCache(context, getFileNameWithDomen(issuePicLink,"http://esquire.ru"), "issue.jpg");
		
		if ((rulesAuthorPicLink!=null) && (!rulesAuthorPicLink.equals(sp.getString("RulesAuthorPicLink", ""))))
			rulesAuthorPic = downloadFileToCache(context, getFileNameWithDomen(rulesAuthorPicLink,"http://esquire.ru"), "rules.jpg");
		
	    Editor editor = sp.edit();
	    if (!((numberValue==null) || (numberDesc==null))) {
		    editor.putString("NumberValue", numberValue);
		    editor.putString("NumberUnits", numberUnits);
		    editor.putString("NumberDesc", numberDesc);
	    }
	    
	    if (quoteText!=null) {
		    editor.putString("QuoteText", quoteText);
		    editor.putString("QuoteAuthorName", quoteAuthorName);
		    editor.putString("QuoteAuthorDesc", quoteAuthorDesc);
	    }
	    
	    editor.putString("RulesAuthorName", rulesAuthorName);
	    if (rulesAuthorPic!=null) editor.putString("RulesAuthorPic", rulesAuthorPic);
	    editor.putString("RulesAuthorPicLink", rulesAuthorPicLink);
	    editor.putString("RulesDesc", rulesDesc);
	    
	    editor.putString("DiscoveriesText", discoveriesText);
	    
	    if ((issueDesc!=null) && (issuePicLink!=null)) {
		    editor.putString("IssueDesc", issueDesc);
		    editor.putString("IssuePicLink", issueDesc);
		    if (issuePic!=null) editor.putString("IssuePic", issuePic);
	    }
	    
	    editor.commit();
		
	}
	
	public String getFileNameWithDomen(String src, String domen) {
		if (src.substring(0, 3).equalsIgnoreCase("http")) return src;
		String fullFilename=domen;
		if (src.charAt(0)!='/') fullFilename+='/';
		fullFilename+=src;
		return fullFilename;
		
	}
	
	public String downloadFileToCache(Context context, String src, String dstFilename) {
		String dst=null;
		File fdst=null;
		
		try {
			
			context.deleteFile(dstFilename);
			FileOutputStream out=context.openFileOutput(dstFilename, Context.MODE_WORLD_READABLE);
			
			InputStream in = openUrlConnection(src);
			if (in!=null) {
			
		    byte[] buf = new byte[8192];
			    int len;
			    while ((len = in.read(buf)) > 0) {
			        out.write(buf, 0, len);
			    }
			    in.close();
			    out.close();
			    dst=Uri.fromFile(context.getFileStreamPath(dstFilename)).toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dst=null;
			//return null;
		}
		if (dst==null) { //если произошла отмена или ошибка
			if ((fdst!=null) && (fdst.exists())) fdst.delete();
			dst=null;
		};
		return dst;
		
	}
	
	InputStream openUrlConnection(String src) {
		InputStream in=null;
		try {
			URL url = new URL(src);
			URLConnection connection;
			
			
			//TODO: методы получени€ адреса и порта прокси deprecated, поэтому
			//      надо перевести на определение стандартным java-способом
			//      либо вынести настройки прокси в настройки и не паритьс€.
			String proxyServer = android.net.Proxy.getDefaultHost();
			int proxyPort = android.net.Proxy.getDefaultPort();
			if (proxyServer!=null && proxyPort>0) {
				Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress(proxyServer,proxyPort));
				connection = url.openConnection(proxy);
			} else {
				connection = url.openConnection();
			};
			connection.setConnectTimeout(3000);
			in = connection.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		return in;
	}
	

	
	class TaggedStream {
		
		private char[] chars = new char[READING_BUFFER_SIZE];
		private int bufferIndex=0;
		private int bufferDownloaded=0;
		
		private static final int MAX_KEYS=16;
		private static final int SYM_TAB=8; //символ табул€ции
		
		private InputStream is;
		private InputStreamReader isr;
		private int keys;
		private String keyNames[];
		private String keyValues[];
		
		private String tag;
		private String tagName;
		
		TaggedStream() {
			keys=0;
			keyNames=new String[MAX_KEYS];
			keyValues=new String[MAX_KEYS];
		}
		
		public boolean openConnection(String src) {
			try {
				URL url = new URL(src);
				URLConnection connection;
				
				//TODO: методы получени€ адреса и порта прокси deprecated, поэтому
				//      надо перевести на определение стандартным java-способом
				//      либо вынести настройки прокси в настройки и не паритьс€.
				String proxyServer = android.net.Proxy.getDefaultHost();
				int proxyPort = android.net.Proxy.getDefaultPort();
				if (proxyServer!=null && proxyPort>0) {
					Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress(proxyServer,proxyPort));
					connection = url.openConnection(proxy);
				} else {
					connection = url.openConnection();
				};
				connection.setConnectTimeout(3000);
				is = connection.getInputStream();
				isr = new InputStreamReader(is,"UTF-8");
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		public boolean openResourceConnection(Context c, int id) {
			try {
				is = c.getResources().openRawResource(id);
				isr = new InputStreamReader(is,"UTF-8");
			} catch (NotFoundException e) {
				return false;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			};
			return true;
		}
		
		public boolean nextTag() {
			char c=' ';
			char endSym=' ';
			tagName="";
			keys=0;
			
			while (c!='<') {
				if (bufferIndex>(bufferDownloaded-TAIL_BUFFER_SIZE)) {
					fillBuffer();
					if (bufferIndex>=bufferDownloaded) 
						return false;
				}
				while (!((chars[bufferIndex]=='<') && (chars[bufferIndex+1]!='\\')) && (bufferIndex<(bufferDownloaded-1))) {
					bufferIndex++;
				};
				c=chars[bufferIndex];
				if (c!='<') continue;
				bufferIndex++;
			}

			while (true) {
				c=chars[bufferIndex++];
				if ((c==' ') || (c=='>')) break;
				else tagName+=c;
			}
			
			//поиск ключей
			while (c!='>') {
				//пропустить пробелы и табул€ции
				while ((c==' ') || (c==SYM_TAB)) c=chars[bufferIndex++];
				keys++;
				keyNames[keys-1]="";
				keyValues[keys-1]="";
				//get key name
				do {
					keyNames[keys-1]+=c;
					c=chars[bufferIndex++];
				} while ((c!='=') && (c!=' ') && (c!='>'));
				//пропустить пробелы и табул€ции
				while ((c==' ') || (c==SYM_TAB)) c=chars[bufferIndex++];
				if (c!='=') continue;
				
				//получаю значение ключа, если начинаетс€ с кавычки - ищу до кавычки
				c=chars[bufferIndex++];
				if ((c=='"') || (c=='\'')) {
					endSym=c;
					c=chars[bufferIndex++];
				} else endSym=' ';
				
				while (c!=endSym) {
					keyValues[keys-1]+=c;
					c=chars[bufferIndex++];
				};
				c=chars[bufferIndex++];
			};
			
			return true;
		}
		
		//получить им€ тэга
		public String getTagName() {
			return tagName;
		}
		
		//получить значение тэга
		public String getTagValue() {
			int beginValue=bufferIndex;
			int endTagValue=bufferIndex+1;
			String endTag="/"+tagName+">";
			int c;
			while (bufferIndex<bufferDownloaded) {
				c=chars[bufferIndex++];
				if (c=='<') {
					endTagValue=bufferIndex-1;
					String testTag=String.copyValueOf(chars, bufferIndex, endTag.length());
					if (testTag.equalsIgnoreCase(endTag)) {
						bufferIndex+=endTag.length();
						break;
					};
				};
			};
			return String.copyValueOf(chars, beginValue, endTagValue-beginValue);
		}
		
		public String findKeyValueByName(String name) {
			for (int i=0;i<keys;i++) {
				if (keyNames[i].equalsIgnoreCase(name)) return keyValues[i];
			};
			return null;
			
		}


		private boolean fillBuffer() {
			try {
				//сохранить остаток
				int t=0;
				if (isr==null) return false;
				while (bufferIndex<bufferDownloaded) {
					chars[t]=chars[bufferIndex];
					t++;
					bufferIndex++;
				};
				bufferIndex=0;
				
				bufferDownloaded = isr.read(chars,t,READING_BUFFER_SIZE-t);
				if (bufferDownloaded==-1) {
					isr.close();
					isr=null;
				};
				bufferDownloaded+=t;				
			} catch (IOException e) {
				return false;
			}
			return true;
		}
	}

	
}