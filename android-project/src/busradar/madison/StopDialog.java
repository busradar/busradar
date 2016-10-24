// Copyright (C) 2010 Aleksandr Dobkin, Michael Choi, and Christopher Mills.
// 
// This file is part of BusRadar <https://github.com/orgs/busradar/>.
// 
// BusRadar is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// BusRadar is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

package busradar.madison;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import static busradar.madison.G.dp2px;
import static busradar.madison.ProblemReporter.*;

public final class StopDialog extends Dialog {
	
//	static javax.net.ssl.SSLSocketFactory sslsockfactory;
//	
//	static {
//		// Imports: javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager
//		try {
//		    // Create a trust manager that does not validate certificate chains
//		    final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//		        public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
//		        }
//		        public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
//		        }
//		        public X509Certificate[] getAcceptedIssuers() {
//		            return null;
//		        }
//		    } };
//		    
//		    // Install the all-trusting trust manager
//		    final SSLContext sslContext = SSLContext.getInstance( "TLS" );
//		    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
//		    // Create an ssl socket factory with our all-trusting manager
//		    sslsockfactory = sslContext.getSocketFactory();
//		    
//		    
//		    
//		} catch ( final Exception e ) {
//		    e.printStackTrace();
//		}
//	
//	}
	
	final static Pattern num_vehicles_re = Pattern.compile("Next (\\d) Vehicles Arrive at:");
	final static Pattern time_re = Pattern.compile("(\\d\\d?:\\d\\d [AP]\\.M\\.).*TO (.*)<");
	final static Pattern time_re_backup = Pattern.compile("(\\d\\d?:\\d\\d [AP]\\.M\\.)");
	
	//final static Pattern no_busses_re = Pattern.compile("No stops with upcoming crossings times found\\.");
	//final static Pattern no_timepoints_re = Pattern.compile("No stop information is found with this time point\\.");
	final static int route_list_id = 1;
	final static int time_list_id = 2;
	final static int stop_num_id = 3;

	ListView list_view;
	TextView status_text;
	TextView cur_loading_text;
	BaseAdapter times_adapter;
	final static String TRANSITTRACKER_URL = "http://webwatch.cityofmadison.com/tmwebwatch/LiveADAArrivalTimes?";

	static class RouteURL {
		static final int LOADING = 0;
		static final int DONE = 1;
		static final int NO_MORE_TODAY = 2;
		static final int NO_TIMEPOINTS = 3;
		static final int NO_STOPS_UNKONWN = 4;
		static final int ERROR = 5;
		
		int route;
		String url;
		//String text;
		int status = 0;
	}

	static final class RouteTime implements Comparable<RouteTime> {
		int route;
		String dir;
		String time;
		Date date;

		public int compareTo(RouteTime another) {
			return date.compareTo(another.date);

		}
	}

	final class CellView extends RelativeLayout {
		TextView route_textview;
		TextView dir_textview;
		TextView time_textview;

		CellView(Context ctx) {
			super(ctx);

			setPadding(dp2px(5), 0, dp2px(5), 0);
			addView(route_textview = new TextView(ctx) {
				{
					setId(1);
					setTextColor(0xffffffff);
					setTypeface(Typeface.DEFAULT_BOLD);
					setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * 2f);
				}
			}, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT) {
				{
					addRule(RelativeLayout.ALIGN_PARENT_TOP);
				}
			});
			addView(dir_textview = new TextView(ctx) {
				{
					setTextColor(0xffffffff);
				}
			}, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT) {
				{
					addRule(RelativeLayout.BELOW, 1);
				}
			});
			addView(time_textview = new TextView(ctx) {
				{
					setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * 2f);
					setTextColor(0xffffffff);
				}
			}, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT) {
				{
					addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				}
			});
		}
	}

	int stopid;
	RouteURL[] routes;
	ArrayList<RouteTime> times = new ArrayList<RouteTime>();
	ArrayList<RouteTime> curr_times = times;
	RouteURL selected_route;

	StopDialog(final Context ctx, final int stopid, final int lat, final int lon) {
		super(ctx);
		this.stopid = stopid;

		// getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);


		final String name = DB.getStopName(stopid);
		setTitle(name);

		routes = get_time_urls(StopDialog.this.stopid);
		
		// getWindow().setLayout(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT);

		setContentView(new RelativeLayout(ctx) {{
			addView(new TextView(ctx) {{
					setId(stop_num_id);
					setText(Html.fromHtml(String.format("[<a href='http://www.cityofmadison.com/metro/BusStopDepartures/StopID/%04d.pdf'>%04d</a>]", stopid, stopid)));
					setPadding(0, 0, dp2px(5), 0);
					this.setMovementMethod(LinkMovementMethod.getInstance());
			}}, new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
				addRule(ALIGN_PARENT_RIGHT);
			}});
			
			addView(new ImageView(ctx) {
				boolean enabled;
				@Override public void setEnabled(boolean e) {
					enabled = e;
					
					setImageResource(e ? R.drawable.love_enabled : R.drawable.love_disabled);
					if (e) {
						G.favorites.add_favorite_stop(stopid, name, lat, lon);
						Toast.makeText(ctx, "Added stop to Favorites", Toast.LENGTH_SHORT).show();
					}
					else {
						G.favorites.remove_favorite_stop(stopid);
						Toast.makeText(ctx, "Removed stop from Favorites", Toast.LENGTH_SHORT).show();
					}
				}
				
				{
					enabled = G.favorites.is_stop_favorite(stopid);
					setImageResource(enabled ?
							R.drawable.love_enabled : R.drawable.love_disabled);
					
					setPadding(0, 0, dp2px(10), 0);
					setOnClickListener(new OnClickListener() {
						public void onClick(View v) {							
							setEnabled(!enabled);
						}
				});
			}}, new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
				addRule(LEFT_OF, stop_num_id);
				setMargins(0, dp2px(-3), 0, 0);
			}});
			
			addView(cur_loading_text = new TextView(ctx) {{
				setText("Loading...");
				setPadding(dp2px(5), 0, 0, 0);
			}});
			addView(new HorizontalScrollView(ctx) {
				{
					setId(route_list_id);
					setHorizontalScrollBarEnabled(false);

					addView(new LinearLayout(ctx) {
						float text_size;
						Button cur_button;
						{
                                                         setBaselineAligned(false);
							int last_route = -1;
							for (int i = 0; i < routes.length; i++) {
									
								final RouteURL route = routes[i];
								
								if (route.route == last_route)
									continue;
								last_route = route.route;
								
								addView(new Button(ctx) {
									public void setEnabled(boolean e) {
										if (e) {
											setBackgroundColor(0xff000000 | G.route_points[route.route].color);
											setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size * 1.5f);
										} else {
											setBackgroundColor(0x90000000 | G.route_points[route.route].color);
											setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
										}
									}

									{
										setText(G.route_points[route.route].name);
										setTextColor(0xffffffff);
										setTypeface(Typeface.DEFAULT_BOLD);
										text_size = getTextSize();

										if (G.active_route == route.route) {
											setEnabled(true);
											cur_button = this;
										} else
											setEnabled(false);

										final Button b = this;

										setOnClickListener(new OnClickListener() {
											public void onClick(View v) {
												if (cur_button != null) {
													cur_button.setEnabled(false);
												}

												if (cur_button == b) {
													cur_button.setEnabled(false);
													cur_button = null;

													selected_route = null;
													update_time_display();
												} else {
													cur_button = b;
													cur_button.setEnabled(true);

													selected_route = route;
													update_time_display();
												}
											}
										});

									}
								}, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
                                                                    gravity = Gravity.BOTTOM;
                                                                 }});
							}
						}
					});
				}
			}, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT) {
				{
					addRule(RelativeLayout.BELOW, stop_num_id);
					addRule(RelativeLayout.CENTER_HORIZONTAL);
				}
			});
			
			addView(status_text = new TextView(ctx) {{
				setText("");
			}}, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT) {{
					addRule(RelativeLayout.BELOW, route_list_id);
					addRule(RelativeLayout.CENTER_HORIZONTAL);
			}});
			addView(list_view = new ListView(ctx) {
				{
					setId(time_list_id);
					setVerticalScrollBarEnabled(false);
					setAdapter(times_adapter = new BaseAdapter() {

						public View getView(final int position,
								View convertView, ViewGroup parent) {
							CellView v;

							if (convertView == null)
								v = new CellView(ctx);
							else
								v = (CellView) convertView;
							
							RouteTime rt = curr_times.get(position);
							v.setBackgroundColor(G.route_points[rt.route].color | 0xff000000);
							v.route_textview.setText(G.route_points[rt.route].name);
							if (rt.dir != null) v.dir_textview.setText("to "+ rt.dir);
							v.time_textview.setText(rt.time);

							return v;

						}

						public int getCount() {
							return curr_times.size();
						}

						public Object getItem(int position) {
							return null;
						}

						public long getItemId(int position) {
							return 0;
						}

					});
				}
			}, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) {{
					addRule(RelativeLayout.BELOW, route_list_id);
			}});
		}});

		TextView title = (TextView) findViewById(android.R.id.title);
		title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		title.setSelected(true);
		title.setTextColor(0xffffffff);
		title.setMarqueeRepeatLimit(-1);

		// getWindow().set, value)
		// getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
		// android.R.drawable.ic_dialog_info);

		// title.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		
		if (G.active_route >= 0)
			for (int i = 0; i < routes.length; i++)
				if (routes[i].route == G.active_route) {
					selected_route = routes[i];
					
					
					RouteURL[] rnew = new RouteURL[routes.length];
					rnew[0] = selected_route;
						
					for (int j = 0, k = 1; j < routes.length; j++)
						if (j != i)
							rnew[k++] = routes[j];
						
					routes = rnew;
					break;
				}
		update_time_display();
	}

	@Override
	public void show() {
		new Thread() {
			@Override
			public void run() {

				for (final RouteURL r : routes) {
					G.activity.runOnUiThread(new Runnable() {
						public void run() {
							cur_loading_text.setText(String.format("Loading route %s...", G.route_points[r.route].name));
						}
					});
					
					final ArrayList<RouteTime> curtimes = new ArrayList<RouteTime>();
					try {
						System.err.printf("BusRadar URL %s\n", TRANSITTRACKER_URL+r.url);
						URL url = new URL(TRANSITTRACKER_URL+r.url);
					    URLConnection url_conn = url.openConnection();
						if (url_conn instanceof HttpsURLConnection)
						{
							((HttpsURLConnection)url_conn).setHostnameVerifier(new HostnameVerifier() {
								
								public boolean verify(String hostname, SSLSession session) {
									return true;
								}
							});
						}
						InputStream is = url_conn.getInputStream();
						Scanner scan = new Scanner(is, "UTF-8");

						//String outstr_cur = "Route " + r.route + "\n";
						
						if (scan.findWithinHorizon(num_vehicles_re, 0) != null) {

							while (scan.findWithinHorizon(time_re, 0) != null) {
								RouteTime time = new RouteTime();
								time.route = r.route;
								time.time = scan.match().group(1).replace(".", "");
								time.dir = scan.match().group(2);
								//time.date = DateFormat.getTimeInstance(DateFormat.SHORT).parse(time.time);
								
								SimpleDateFormat f = new SimpleDateFormat("h:mm aa", Locale.US);
								time.date = f.parse(time.time);
								r.status = RouteURL.DONE;
								
								//outstr_cur += String.format("%s to %s\n", time.time, time.dir);
								curtimes.add(time);
							}
							
							while (scan.findWithinHorizon(time_re_backup, 0) != null) {
								RouteTime time = new RouteTime();
								time.route = r.route;
								time.time = scan.match().group(1).replace(".", "");
								//time.dir = scan.match().group(2);
								//time.date = DateFormat.getTimeInstance(DateFormat.SHORT).parse(time.time);
								
								SimpleDateFormat f = new SimpleDateFormat("h:mm aa", Locale.US);
								time.date = f.parse(time.time);
								r.status = RouteURL.DONE;
								
								//outstr_cur += String.format("%s to %s\n", time.time, time.dir);
								curtimes.add(time);
							}

						}

//						else if (scan.findWithinHorizon(no_busses_re, 0) != null) {
//							r.status = RouteURL.NO_MORE_TODAY; 
//						} 
//						else if (scan.findWithinHorizon(no_timepoints_re, 0) != null) {
//							r.status = RouteURL.NO_TIMEPOINTS;
//						}
//						else {
//							r.status = RouteURL.ERROR;
//							System.out.printf("BusRadar: Could not get stop info for %s\n", r.url);
//							
//							throw new Exception("Error parsing TransitTracker webpage.");
//						}
						else {
							r.status = RouteURL.NO_STOPS_UNKONWN;
						}

						//r.text = outstr_cur;

						G.activity.runOnUiThread(new Runnable() {
							public void run() {
								times.addAll(curtimes);
								StopDialog.this.update_times();
							}
						});

					} 
//					catch  (final IOException ioe) {
//						log_problem(ioe);
//						G.activity.runOnUiThread(new Runnable() {
//							public void run() {
//								final Context ctx = StopDialog.this.getContext();
//								
//								StopDialog.this.setContentView(new RelativeLayout(ctx) {{
//									addView(new TextView(ctx) {{
//										setText(Html.fromHtml("Error downloading data. Is the data connection enabled?"+
//												              "<p>Report problems to <a href='mailto:support@busradarapp.com'>support@busradarapp.com</a><p>"+ioe));
//										setPadding(5, 5, 5, 5);
//										this.setMovementMethod(LinkMovementMethod.getInstance());
//									}}, new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//								}});
//							}
//						});						
//						return;
//					}
					catch (Exception e) {
						log_problem(e);
						
						String custom_msg = "";
						final String turl = TRANSITTRACKER_URL+r.url;
						if ((e instanceof SocketException) ||
						    (e instanceof UnknownHostException)) 
						{
							// data connection doesn't work
							custom_msg = "Error downloading data. Is the data connection enabled?"+
							              "<p>Report problems to <a href='mailto:support@busradarapp.com'>support@busradarapp.com</a><p>"+TextUtils.htmlEncode(e.toString());
						}
						else 
						{
							
							String rurl = String.format("http://www.cityofmadison.com/metro/BusStopDepartures/StopID/%04d.pdf", stopid);
							custom_msg = "Trouble retrieving real-time arrival estimates from <a href='"+turl+"'>this</a> TransitTracker webpage, which is displayed below. "+
							             "Meanwhile, try PDF timetable <a href='"+rurl+"'>here</a>. "+
							             "Contact us at <a href='mailto:support@busradarapp.com'>support@busradarapp.com</a> to report the problem.<p>"+ TextUtils.htmlEncode(e.toString());
						}
				
						final String msg = custom_msg;
						
						G.activity.runOnUiThread(new Runnable() {
							public void run() {
								final Context ctx = StopDialog.this.getContext();
								
								StopDialog.this.setContentView(new RelativeLayout(ctx) {{
									addView(new TextView(ctx) {{
										setId(1);
										setText(Html.fromHtml(msg));
										setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
										this.setMovementMethod(LinkMovementMethod.getInstance());
									}}, new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
									
									addView(new WebView(ctx) {{
										setWebViewClient(new WebViewClient());
										loadUrl(turl);
									}}, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) {{
										addRule(RelativeLayout.BELOW, 1);
									}});
								}});
							}
						});
						return;
					}

				}
				
				G.activity.runOnUiThread(new Runnable() {
					public void run() {
						cur_loading_text.setText("");
					}
				});
			}
		}.start();

		super.show();

	}

	void update_times() {
		Collections.sort(times);
		update_time_display();
	}

	void update_time_display() {
		if (selected_route != null) {
			switch (selected_route.status) {
				case RouteURL.DONE:
					status_text.setText("");
					break;
				case RouteURL.LOADING:
					status_text.setText("Loading...");
					break;
				case RouteURL.NO_MORE_TODAY:
					status_text.setText("No more stops today.");
					break;
				case RouteURL.NO_TIMEPOINTS:
					status_text.setText("Does not run today.");
					break;
				case RouteURL.NO_STOPS_UNKONWN:
					status_text.setText("No stops to show.");
					break;
				case RouteURL.ERROR:
					status_text.setText("Unkown error.");
					break;
				default:
					throw new Error();
			}
			
			ArrayList<RouteTime> list = new ArrayList<RouteTime>();
			for (RouteTime t : times) {
				if (t.route == selected_route.route)
					list.add(t);
			}
			curr_times = list;
		} else {
			curr_times = times;
			status_text.setText("");
		}

		times_adapter.notifyDataSetChanged();

	}

	static RouteURL[] get_time_urls(int stopid) {
		Cursor c = G.db.rawQuery("SELECT url, route " + 
				                 "FROM routestops " + 
				                 "WHERE stopid = ? " +
				                 "ORDER BY route" , new String[] { stopid + "" });

		RouteURL[] list = new RouteURL[c.getCount()];
		//ArrayList<RouteURL> list = new ArrayLst<RouteURL>();
		//int last = -1;
		int i = 0;
		while (c.moveToNext()) {
			RouteURL r = new RouteURL();
			r.url = c.getString(0);
			r.route = c.getInt(1);
			list[i++] = r;
		
		}

		c.close();
		return list;
	}
}
