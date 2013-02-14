package com.example.foodlog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String TAG = "GCMIntentService";
    public GCMIntentService() {
    	super("SENDER_ID");
    	}
	@Override
	protected void onError(Context context, String errorId) {
		// error ���b�Z�[�W
	       Log.e(TAG, "onError errorId:" + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent data) {
		// TODO ���b�Z�[�W��M

	}

	@Override
	protected void onRegistered(Context context, String registrationId) { 
		// �o�^����
		Log.i(TAG, "onRegistered registrationId:" + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		// �o�^����     
		Log.i(TAG, "onUnregistered registrationId:" + registrationId);
	}

}
