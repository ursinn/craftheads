package me.deejayarroba.craftheads.util;

import me.deejayarroba.craftheads.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    private int id;
    private Main plugin;
    private boolean update;
    public Thread checkUpdates = new Thread() {
        public void run() {
            try {
                URLConnection conn = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + id).openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String oldVersion = plugin.getDescription().getVersion();
                String newVersion = br.readLine();
                if (!newVersion.equals(oldVersion)) {
                    update = true;
                    plugin.getLogger().info("An update for CraftHeads is available");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public UpdateChecker(int id, Main plugin) {
        this.id = id;
        this.plugin = plugin;
    }

    public boolean isUpdate() {
        return update;
    }
}