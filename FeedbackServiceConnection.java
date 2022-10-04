  class FeedbackServiceConnection {
    private static int MAX_WIDTH = 800;
    private static int MAX_HEIGHT = 1700;
    protected final Window mWindow;

    public FeedbackServiceConnection(Window window) {
        this.mWindow = window;
    }
    public void bindFeedback(Context context){
        Intent intent = new Intent(Intent.ACTION_BUG_REPORT);
        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.feedback.LegacyBugReportService"));
        ServiceConnection service = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    Parcel parcel = Parcel.obtain();
                    Bitmap bitmap = getScreenshot();
                    if (bitmap != null) {
                        bitmap.writeToParcel(parcel, 0);
                    }
                    iBinder.transact(IBinder.FIRST_CALL_TRANSACTION, parcel, null, 0);
                    parcel.recycle();
                } catch (RemoteException e) {
                    Log.e("ServiceConn", e.getMessage(), e);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {}
        };

        context.bindService(intent, service, Context.BIND_AUTO_CREATE);
    }
     

     public Bitmap getScreenshot() {
          try {
              View rootView = mWindow.getDecorView().getRootView();
              rootView.setDrawingCacheEnabled(true);
              Bitmap bitmap = rootView.getDrawingCache();
              if (bitmap != null)
              {
                  double height = bitmap.getHeight();
                  double width = bitmap.getWidth();
                  double ratio = Math.min(MAX_WIDTH / width, MAX_HEIGHT / height);
                  return Bitmap.createScaledBitmap(bitmap, (int)Math.round(width * ratio), (int)Math.round(height * ratio), true);
              }
          } catch (Exception e) {
              Log.e("Screenshoter", "Error getting current screenshot: ", e);
          }
          return null;
      }


}





//  Us this in feedback button - new FeedbackServiceConnection(MainActivity.this.getWindow()).bindFeedback(getApplicationContext());
