class GoogleFeedbackUtils {
    private static final String TAG = GoogleFeedbackUtils.class.getSimpleName();
    public static void bindFeedback(Context context) {
        Intent intent = new Intent(Intent.ACTION_BUG_REPORT);
        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.feedback.LegacyBugReportService"));
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    service.transact(Binder.FIRST_CALL_TRANSACTION, Parcel.obtain(), null, 0);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException", e);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}
