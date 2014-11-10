package com.geekydreams.devicetester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.text.format.Time;
import android.util.Log;

public class Kernal {
	private String TAG = "Kernal";
	private Time timeNode[];
	public Kernal(){
		timeNode = new Time[9];
		for(int i=0;i<8;i++){
			timeNode[i] = new Time();
			timeNode[i].hour = 3*i;
		}
		timeNode[8]= new Time();
		timeNode[8].set(59,59,23,1,1,1);
	}
	public Date today_thisweek_month(int require){
		/*
		 * require:
		 * 1->today
		 * 2->this week
		 * 3->this month
		 */
		Calendar cal;
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd",Locale.CHINA);
		Date target_date;
		switch (require){
		
		case 1:
			target_date = new Date();
			break;
		case 2:
			cal = Calendar.getInstance();
			int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
			cal.add(Calendar.DATE, -day_of_week);
			target_date= cal.getTime();
			break;
		case 3:
			cal = Calendar.getInstance();
			int day_of_month = cal.get(Calendar.DAY_OF_MONTH)-1;
			cal.add(Calendar.DATE,-day_of_month);
			target_date= cal.getTime();
			break;
		default:
			target_date = new Date(0);
			break;
		}
		// zero_time_date means date without detail time;
		String str_day = sdf.format(target_date);
		Date zero_time_date;
		try {
			zero_time_date = sdf.parse(str_day);
			return zero_time_date;
		} catch (ParseException e) {
			// TODO �Զ���ɵ� catch ��
			e.printStackTrace();
		}
		return new Date(0);
	}
	public Date today_usage(int require,ArrayList<Date> lastScreenon, ArrayList<Date> lastScreenoff){
		//get the arrange date
		Date start_date = today_thisweek_month(require);
		
		int before_on=-1;
		int before_off=-1;
		List<Date> screenon;
		List<Date> screenoff;
		
		
		for(int i=0;i<lastScreenon.size();i++){
			if (!lastScreenon.get(i).before(start_date)){
				before_on = i;
				break;
			}
		}
		if (before_on!=-1){
		screenon = lastScreenon.subList(before_on, lastScreenon.size());}
		else{
			long todayUsage = new Date().getTime() - start_date.getTime();
			return new Date(todayUsage);
		}
		for(int i=0;i<lastScreenoff.size();i++){
			if(!lastScreenoff.get(i).before(start_date)){
				before_off =i;
				break;
			}
		}
		if (before_off!=-1){
		screenoff = lastScreenoff.subList(before_off, lastScreenoff.size());} 
		else{
			long todayUsage = new Date().getTime() - screenon.get(0).getTime();
			return new Date(todayUsage);
		}
				
		long diff=0;
		if(screenon.get(0).before(screenoff.get(0))){
			//Normal one, screen_off  when 00:00 o'clock
			for(int i=0;i<screenon.size()-1;i++){
				
				diff += screenoff.get(i).getTime() - screenon.get(i).getTime();
			}
			diff += new Date().getTime() - screenon.get(screenon.size()-1).getTime();
		}
		else{
			//Abnormal one, screen_on when 00:00 o'clock
			diff += screenon.get(0).getTime() - start_date.getTime();
			for(int i=0;i<screenon.size()-1;i++){
				diff += screenoff.get(i+1).getTime() - screenon.get(i).getTime();
			}
			diff += new Date().getTime() - screenon.get(screenon.size()-1).getTime();
		}
		//now it's screen_on, so the size of screenon should be 1 larger than the size of screenoff
		
		
		
		return new Date(diff);
	}
	public int[] occupancy_rate(ArrayList<Date> lastScreenon, ArrayList<Date> lastScreenoff){
		//only return todays occupancy rate in this version
		Time today = new Time();
		today.setToNow();
		for(int i=0;i<=8;i++){
			timeNode[i].set(today.monthDay,today.month,today.year);
		}
		
		int[] rate = new int[8];
		//check whether Screenoff exist.
		if(lastScreenoff.size()==0){
			Time the_begin = new Time();
			Time the_end   = new Time();
			the_begin.set(lastScreenon.get(0).getTime());
			the_end.setToNow();
			Time now_least = the_begin;//��ǰ��С������  4:50 ->  6:00 ->  9:00 -> 10:50 -> 12
			int first_field = the_begin.hour / 3;
			for(int i = first_field+1;i<=8;i++){
				System.out.println(i);
				if (the_end.hour>=i*3){
					rate[i-1]+= (i*3 - now_least.hour)*3600 -now_least.minute*60-now_least.second;
					now_least.hour = i*3;
					now_least.minute = 0;
					now_least.second = 0;
				}
				else{
					//no problem in today
					Log.i(TAG,"--->begin---"+the_begin.toString());
					Log.i(TAG,"--->end-----"+the_end.toString());
					rate[i-1]+=(the_end.hour - now_least.hour)*3600+(the_end.minute-now_least.minute)*60+(the_end.second-now_least.second);
					break;
				}
			}
			
		}
		else{
			Time the_begin = new Time();
			Time the_end   = new Time();
			Time now_least;
			int first_field;
			for(int i=0;i<lastScreenon.size();i++){
				if(lastScreenoff.get(0).before(lastScreenon.get(0))){
					if(i==0){
						the_begin.set(0,0,0,1,1,1);
						the_end.set(lastScreenoff.get(0).getTime());
						now_least = the_begin;
						first_field = the_begin.hour / 3;
						for(int j = first_field+1;j<=8;j++){
							System.out.println(j);
							if (the_end.hour>=j*3){
								rate[j-1]+= (j*3 - now_least.hour)*3600 -now_least.minute*60-now_least.second;
								now_least.hour = j*3;
								now_least.minute = 0;
								now_least.second = 0;
							}
							else{
								rate[j-1]+=(the_end.hour - now_least.hour)*3600+(the_end.minute-now_least.minute)*60+(the_end.second-now_least.second);
								break;
							}
						}
						the_begin.set(lastScreenon.get(i).getTime());
						if(lastScreenoff.size()==1){
							the_end.setToNow();
						}
						else{
						the_end.set(lastScreenoff.get(i+1).getTime());
						}
					}
					else{
						the_begin.set(lastScreenon.get(i).getTime());
						if(i==lastScreenoff.size()-1){
							the_end.setToNow();
						}
						else{						
						the_end.set(lastScreenoff.get(i+1).getTime());
						}
					}
				}
				else{
					the_begin.set(lastScreenon.get(i).getTime());
					if(i==lastScreenoff.size()){	
						the_end.setToNow();
					}
					else{
					the_end.set(lastScreenoff.get(i).getTime());
					}
				}
				now_least = the_begin;
				first_field = the_begin.hour / 3;
				for(int j = first_field+1;j<=8;j++){
					System.out.println(j);
					if (the_end.hour>=j*3){
						rate[j-1]+= (j*3 - now_least.hour)*3600 -now_least.minute*60-now_least.second;
						now_least.hour = j*3;
						now_least.minute = 0;
						now_least.second = 0;
					}
					else{
						rate[j-1]+=(the_end.hour - now_least.hour)*3600+(the_end.minute-now_least.minute)*60+(the_end.second-now_least.second);
						break;
					}
				}
			
			}
			
			
		}
		return rate;
	}
}
