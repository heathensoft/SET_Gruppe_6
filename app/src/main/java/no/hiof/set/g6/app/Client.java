package no.hiof.set.g6.app;

import io.github.heathensoft.jlib.lwjgl.window.Engine;
import io.github.heathensoft.jlib.lwjgl.window.Resolution;
import no.hiof.set.g6.net.core.AppInterface;
import no.hiof.set.g6.net.core.ClientInstance;

public class Client extends G6App {

    public static void main(String[] args) { Engine.get().run(new Client(),args); }

    private ClientInstance client_instance;


    @Override
    protected void on_start(Resolution resolution) throws Exception {

    }

    @Override
    protected void on_update(float delta) {

    }

    @Override
    protected void on_exit() {

    }

    @Override
    protected AppInterface appInterface() {
        return client_instance;
    }
}
