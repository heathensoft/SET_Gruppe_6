package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.common.utils.Rand;
import io.github.heathensoft.jlib.lwjgl.window.Application;
import io.github.heathensoft.jlib.lwjgl.window.BootConfiguration;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import no.hiof.set.g6.dt.LocalUser;
import no.hiof.set.g6.net.core.AppInterface;
import no.hiof.set.g6.net.core.LogEntry;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This is simply an application. It starts, runs and exits. (Hopefully)
 * How it works is not important. Any type of application could run our
 * network module.
 */
abstract class G6App extends Application {

    /** This is you */
    public static final LocalUser CLIENT_USER;
    static {
        // If you change your role you'll lose access to some functionality
        // Do not change your account ID
        CLIENT_USER = new LocalUser();
        CLIENT_USER.role = LocalUser.Role.OWNER;
        CLIENT_USER.userName = "Eddie";
        CLIENT_USER.accountID = 35345235;
    }
    protected static final LogEntry.Type NETTY_LOG_FILTER = LogEntry.Type.DEBUG;
    protected static final String DB_DIRECTORY = "app/db";
    protected static final String HOST_ADDRESS = "localhost";
    protected static final int PORT = 8080;
    protected static final int SCREEN_WIDTH_PIXELS = 400;
    protected static final int SCREEN_HEIGHT_PIXELS = 400;
    private List<LogEntry> internal_logs;

    @Override
    protected void engine_init(List<Resolution> supported, BootConfiguration config, String[] strings) {
        supported.add(new Resolution(SCREEN_WIDTH_PIXELS,SCREEN_HEIGHT_PIXELS));
        config.settings_width = SCREEN_WIDTH_PIXELS;
        config.settings_height = SCREEN_HEIGHT_PIXELS;
        config.auto_resolution = false;
        config.windowed_mode = true;
        config.resizable_window = false;
        config.vsync_enabled = true;
        config.limit_fps = false;
        config.target_ups = 60;
        this.internal_logs = new ArrayList<>();
        if (this instanceof Server ) config.window_title = "Server Application";
        else if (this instanceof Client) config.window_title = "Client Application";
    }

    protected abstract AppInterface appInterface();

    protected void flushInternalLogsToLogger() {
        AppInterface appInterface = appInterface();
        if (appInterface != null) {
            appInterface.eventLog().readAll(internal_logs);
            for (LogEntry entry : internal_logs) {
                if (entry.severity() >= NETTY_LOG_FILTER.ordinal())
                    switch (entry.type) {
                        case DEBUG -> Logger.debug(entry.message);
                        case INFO -> Logger.info(entry.message);
                        case WARN -> Logger.warn(entry.message);
                        case ERROR -> Logger.error(entry.message);
                    }
            } internal_logs.clear();
        }
    }

    protected void loadDefaultDB(Database database) {

        LocalUser user = CLIENT_USER;
        database.addUser(user);

        user = new LocalUser();
        user.accountID = Rand.nextInt();
        user.userName = "Rose";
        user.role = LocalUser.Role.RESIDENT;
        database.addUser(user);

        user = new LocalUser();
        user.accountID = Rand.nextInt();
        user.userName = "Elizabeth";
        user.role = LocalUser.Role.RESIDENT;
        database.addUser(user);

        user = new LocalUser();
        user.accountID = Rand.nextInt();
        user.userName = "Donnie";
        user.role = LocalUser.Role.RESIDENT;
        database.addUser(user);

        user = new LocalUser();
        user.accountID = Rand.nextInt();
        user.userName = "Frank";
        user.role = LocalUser.Role.GUEST;
        database.addUser(user);

        user = new LocalUser();
        user.accountID = Rand.nextInt();
        user.userName = "Gretchen";
        user.role = LocalUser.Role.GUEST;
        database.addUser(user);

        database.addLock(434444214,"Garage");
        database.addLock(434785851,"Upstairs");
        database.addLock(434785211,"Front Door");
        database.addLock(434784432,"Cellar Door");
    }

    protected abstract void on_start(Resolution resolution) throws Exception;
    protected abstract void on_update(float delta);
    protected void on_render(float frame_time, float alpha) { }
    protected void resolution_request(Resolution resolution) throws Exception { }
}
