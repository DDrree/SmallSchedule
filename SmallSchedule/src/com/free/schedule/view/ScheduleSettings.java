package com.free.schedule.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.FrontiaUser.FrontiaUserDetail;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.free.schedule.All;
import com.free.schedule.Main;
import com.free.schedule.R;
import com.free.schedule.databases.ClassManager;
import com.free.schedule.service.EncodingHandler;
import com.free.schedule.service.ScheduleService;
import com.free.schedule.service.SyncData;
import com.google.zxing.WriterException;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;


public class ScheduleSettings extends PopupWindow {
	private String[] sectionNumber;
	private TextView sectionText;//��ʾÿ�տγ���
	private RelativeLayout sectionNumberSet;//����ÿ�տγ����ĶԻ���
	private Dialog sectionDialog;//����
	private SharedPreferences sp;
	private SharedPreferences.Editor spe;
	private CheckBox isAutoMute;//�Ͽ��Զ�����
	private CheckBox everydayRemind;//ÿ������
	private TextView timeStting;//ʱ������
	private TextView feedback;//����
	private TextView share;//����
	private TextView clearClasses,clearNotes;//��տγ̺ͱʼ�
	private RelativeLayout tb;//ͬ���γ̱�
	private TextView userName;//�༶��
	private FrontiaAuthorization authorization;
	private Context cx;
	private com.free.schedule.service.SyncData syncData ;
	private View settingView;
	private UMSocialService mController;
	public ScheduleSettings(Context context){
		super(context);
		cx = context;
		sp = context.getSharedPreferences(All.sharedName, 0);
		spe = sp.edit();
		sectionNumber = context.getResources().getStringArray(R.array.sectionNumber);
		LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		settingView = li.inflate(R.layout.schedule_setting, null);
		setAnimationStyle(R.style.popupStyle);
		setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_full_bright));
		setWindowLayoutMode(-1, LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		
		sectionNumberSet = (RelativeLayout)settingView.findViewById(R.id.sectionNumberSet);
		sectionText = (TextView)settingView.findViewById(R.id.sectionNumberText);
		isAutoMute = (CheckBox)settingView.findViewById(R.id.isAutoMute);
		everydayRemind = (CheckBox)settingView.findViewById(R.id.everydayRemind);
		clearClasses = (TextView)settingView.findViewById(R.id.clearClasses);
		clearNotes = (TextView)settingView.findViewById(R.id.clearNotes);
		feedback = (TextView)settingView.findViewById(R.id.feedback);
		tb = (RelativeLayout)settingView.findViewById(R.id.synchronization);
		userName =(TextView)settingView.findViewById(R.id.userName);
		timeStting =(TextView)settingView.findViewById(R.id.timeStting);
		share =(TextView)settingView.findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mController = UMServiceFactory.getUMSocialService("com.free.schedule",
		                RequestType.SOCIAL);
				//Ϊ�˱�֤���˷���ɹ����ܹ���PC��������ʾ��������upside                                      
				mController.setAppWebSite(SHARE_MEDIA.RENREN, "http://www.umeng.com/social");
//				String contentUrl = "http://zhushou.360.cn/detail/index/soft_id/1652822";
				mController.getConfig().supportQQPlatform((Activity)cx, "http://zhushou.360.cn/detail/index/soft_id/1652822");
//				String appID = "wx31a75af706a375c7";
//				// ΢��ͼ�ķ����������һ��url 
//				// ���΢��ƽ̨������1Ϊ��ǰActivity, ����2Ϊ�û������AppID, ����3Ϊ�������������ת����Ŀ��url
//				UMWXHandler wxHandler = mController.getConfig().supportWXPlatform((Activity)cx,appID, contentUrl);
//				wxHandler.setWXTitle("����");
//				// ֧��΢������Ȧ
//				UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform((Activity)cx,appID, contentUrl) ;
//				circleHandler.setCircleTitle("����");
//				
				if(authorization.isAuthorizationReady(MediaType.QZONE.toString())||authorization.isAuthorizationReady(MediaType.BAIDU.toString())){
					if(authorization.isAuthorizationReady(MediaType.QZONE.toString())){
						creatQrandShare( MediaType.QZONE.toString(),mController);
					}else{
						creatQrandShare( MediaType.BAIDU.toString(),mController);
					}
				}else{
					new AlertDialog.Builder(cx).setTitle(R.string.loginCase2).setItems(R.array.platforms2, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case 0:
								authorization.authorize((Activity)cx, MediaType.QZONE.toString(), new AuthorizationListener() {
									@Override
									public void onSuccess(FrontiaUser result) {
										creatQrandShare( MediaType.QZONE.toString(),mController);
									}
									@Override
									public void onFailure(int errCode, String errMsg) {
										
									}
									@Override
									public void onCancel() {
										Log.d("log","cancel");
									}
								});
								
								break;

							case 1:
								authorization.authorize((Activity)cx, MediaType.BAIDU.toString(), new AuthorizationListener() {
									@Override
									public void onSuccess(FrontiaUser result) {
										creatQrandShare( MediaType.BAIDU.toString(),mController);
									}
									@Override
									public void onFailure(int errCode, String errMsg) {
									}
									@Override
									public void onCancel() {
										Log.d("log","cancel");
									}
								});
								break;
							}
						}
					}).show();
				}
				ScheduleSettings.this.dismiss();
			}
		});
		
		sectionNumberSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					sectionDialog.show();
			}
		});
		
		isAutoMute.setOnCheckedChangeListener(new CheckChange());
		everydayRemind.setOnCheckedChangeListener(new CheckChange());
		
		clearClasses.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(cx).setTitle(R.string.isClearClasses).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ClassManager classManager = new ClassManager(cx);
						classManager.clearClasses();
						ScheduleView.refreshSchedule();
					}
				}).setNegativeButton(R.string.no, null).show();
			}
		});
		
		clearNotes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(cx).setTitle(R.string.isClearNotes).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SharedPreferences sp = cx.getSharedPreferences(All.noteShared, 0);
						SharedPreferences.Editor spe = sp.edit();
						spe.clear().commit();
						ScheduleView.refreshSchedule();
					}
				}).setNegativeButton(R.string.no, null).show();
			}
		});
		
		timeStting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeSetting timeSetting = new TimeSetting(cx, sp.getInt(All.everyDaySections, 8));
				timeSetting.showAsDropDown(ScheduleView.titleBar);
			}
		});
		
		feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 FeedbackAgent agent = new FeedbackAgent(cx);
				    agent.startFeedbackActivity();
			}
		});
		
	}
	
	/**
	 * ��ʼ��������ͼ
	 */
	public void initView(){
		setContentView(settingView);
		everydayRemind.setChecked(sp.getBoolean(All.everyDayRemind, true));
		isAutoMute.setChecked(sp.getBoolean(All.isAutoMute, true));
		sectionText.setText(sp.getInt(All.everyDaySections, 8) + cx.getString(R.string.section));
		boolean isInit = Frontia.init(cx,SyncData.accessKey);
		if(isInit){ 
			 authorization = Frontia.getAuthorization();
			 syncData = new com.free.schedule.service.SyncData(cx, authorization);
			 tb.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(authorization.isAuthorizationReady(MediaType.QZONE.toString())||authorization.isAuthorizationReady(MediaType.BAIDU.toString())){
							showOperationsDialog();
						}else{
							showCaseDialog();
						}
					}
				});
		}
		if(authorization.isAuthorizationReady(MediaType.QZONE.toString())) {
			authorization.getUserInfo(MediaType.QZONE.toString(), new UserInfoListener() {
				
				@Override
				public void onSuccess(FrontiaUserDetail arg0) {
					// TODO Auto-generated method stub
					userName.setText(arg0.getName());
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}else if(authorization.isAuthorizationReady(MediaType.BAIDU.toString())){
			authorization.getUserInfo(MediaType.BAIDU.toString(), new UserInfoListener() {
				
				@Override
				public void onSuccess(FrontiaUserDetail arg0) {
					// TODO Auto-generated method stub
					userName.setText(arg0.getName());
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		sectionDialog = new AlertDialog.Builder(cx).setTitle(cx.getString(R.string.pleaseSelect)).setSingleChoiceItems(sectionNumber, sp.getInt(All.everyDaySections, 8) - 8,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sectionText.setText(sectionNumber[which] + cx.getString(R.string.section));
				spe.putInt(All.everyDaySections, which + 8).commit();
				sectionDialog.dismiss();
				ScheduleView.refreshSchedule();
			}
		}).create();
	}
	
	private class CheckChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			Intent scheduleService = new Intent(cx,ScheduleService.class);
			switch(buttonView.getId()){
			case R.id.isAutoMute:
				spe.putBoolean(All.isAutoMute, isChecked).commit();
				break;
			case R.id.everydayRemind:
				spe.putBoolean(All.everyDayRemind, isChecked).commit();
				break;
			}
			if(isAutoMute.isChecked() || isAutoMute.isChecked()){
				cx.startService(scheduleService);
			}else{
				cx.stopService(scheduleService);
			}
		}

	}
	
	private void showCaseDialog(){
		new AlertDialog.Builder(cx).setTitle(R.string.loginCase).setItems(R.array.platforms, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					authorization.authorize((Activity)cx, MediaType.QZONE.toString(), new AuthorizationListener() {
						@Override
						public void onSuccess(FrontiaUser result) {
							authorization.getUserInfo(MediaType.QZONE.toString(), new UserInfoListener() {

								@Override
								public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
									authorization.getUserInfo(MediaType.QZONE.toString(), new UserInfoListener() {
										
										@Override
										public void onSuccess(FrontiaUserDetail arg0) {
											// TODO Auto-generated method stub
											userName.setText(arg0.getName());
											showOperationsDialog();
										}
										
										@Override
										public void onFailure(int arg0, String arg1) {
											// TODO Auto-generated method stub
											
										}
									});
								}

								@Override
								public void onFailure(int errCode, String errMsg) {
										System.out.println("errCode:" + errCode + ", errMsg:" + errMsg);
								}
								
							});
						}
						@Override
						public void onFailure(int errCode, String errMsg) {
						}
						@Override
						public void onCancel() {
							Log.d("log","cancel");
						}
					});
					
					break;

				case 1:
					authorization.authorize((Activity)cx, MediaType.BAIDU.toString(), new AuthorizationListener() {
						@Override
						public void onSuccess(FrontiaUser result) {
							authorization.getUserInfo(MediaType.BAIDU.toString(), new UserInfoListener() {

								@Override
								public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
									authorization.getUserInfo(MediaType.BAIDU.toString(), new UserInfoListener() {
										
										@Override
										public void onSuccess(FrontiaUserDetail arg0) {
											// TODO Auto-generated method stub
											userName.setText(arg0.getName());
											showOperationsDialog();
										}
										
										@Override
										public void onFailure(int arg0, String arg1) {
											// TODO Auto-generated method stub
											
										}
									});
								}

								@Override
								public void onFailure(int errCode, String errMsg) {
										System.out.println("errCode:" + errCode + ", errMsg:" + errMsg);
								}
								
							});
						}
						@Override
						public void onFailure(int errCode, String errMsg) {
						}
						@Override
						public void onCancel() {
							Log.d("log","cancel");
						}
					});
					break;
				case 2:
					Main.handler.sendEmptyMessage(2);
					break;
				case 3:
					Main.handler.sendEmptyMessage(3);
					break;
				}
			}
		}).show();
	}
	
	private void showOperationsDialog(){
		new AlertDialog.Builder(cx).setTitle(R.string.operate).setItems(R.array.operations, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
				case 0:
					syncData.backupDate();
					break;
				case 1:
					syncData.syncData();
					break;
				case 2:
					Main.handler.sendEmptyMessage(2);
					break;
				case 3:
					Main.handler.sendEmptyMessage(3);
					break;
				case 4:
					if(authorization.clearAllAuthorizationInfos()){
						Toast.makeText(cx, R.string.exitSuccess, Toast.LENGTH_SHORT).show();
						userName.setText(R.string.space);
					}
					break;
				}
			}
			
		}).show();
	}
	
	private ImageView imageView;
	private File qrFile;
	
	/**�����������ά��
	 * @param media
	 * @param mController
	 */
	private void creatQrandShare(final String media,final UMSocialService mController){
		authorization.getUserInfo(media, new UserInfoListener() {

			@Override
			public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
				authorization.getUserInfo(media, new UserInfoListener() {
					
					@Override
					public void onSuccess(FrontiaUserDetail arg0) {
						// TODO Auto-generated method stub
						mController.setShareContent("����������ʹ�õĿγ̱�,ͬ���С����ǿ���ɨһɨ,�ҵ�ID��:"+arg0.getId()+";С�α����ص�ַ��http://zhushou.360.cn/detail/index/soft_id/1652822");
						qrFile = new File(cx.getFilesDir().getParent()+"/files/qrcode.png");
							File p = new File(cx.getFilesDir().getParent()+"/files");
							if(!p.exists()) p.mkdirs();
							LayoutInflater li  = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							View view = li.inflate(R.layout.qrcode, null);
							imageView = (ImageView)view.findViewById(R.id.qrcode);
							TextView textView = (TextView)view.findViewById(R.id.id);
							textView.setText("ID:"+arg0.getId());
							try {
								qrFile.createNewFile();
								Log.i("userId", arg0.getId());
								imageView.setImageBitmap(EncodingHandler.createQRCode(arg0.getId(),350));
								new AlertDialog.Builder(cx).setView(view).show();
								syncData.uploadData(true);
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											Thread.sleep(1500);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										FileOutputStream fos;
										try {
											fos = new FileOutputStream(qrFile);
											imageView.setDrawingCacheEnabled(true);
											Bitmap obmp = Bitmap.createBitmap(imageView.getDrawingCache());
											imageView.setDrawingCacheEnabled(false);
											obmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
											fos.flush();
											fos.close();
											mController.setShareMedia(new UMImage(cx, 
													qrFile.toString()));
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
									}
								}).start();
								mController.openShare((Activity)cx, false);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (WriterException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
					System.out.println("errCode:" + errCode + ", errMsg:" + errMsg);
			}
			
		});
	}
	
}
