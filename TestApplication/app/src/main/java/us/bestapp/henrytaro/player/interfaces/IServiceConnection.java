package us.bestapp.henrytaro.player.interfaces;

/**
 * Created by xuhaolin on 15/11/3.
 */
public interface IServiceConnection {
    public void onServiceConnected(ITrackHandleBinder binder);
    public void onServiceDisconnected();
}
